package com.fds.opp.app.controller;

import com.fds.opp.app.daoImpl.MessageImpl;
import com.fds.opp.app.daoImpl.memberInProjectImpl;
import com.fds.opp.app.daoImpl.projectImpl;
import com.fds.opp.app.daoImpl.workPackageImpl;
import com.fds.opp.app.model.MemberInProject;
import com.fds.opp.app.model.Message;
import com.fds.opp.app.model.Project;
import com.fds.opp.app.model.WorkPackage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v3/telegram/notify")
public class WebhookController {
    @PostMapping("/create")
    public List<com.fds.opp.app.model.Message> newRequest(@RequestBody String JsonString) throws Exception
    {
        List<com.fds.opp.app.model.Message> listMessage = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        JSONObject request = new JSONObject(JsonString);
        String action = request.get("action").toString();
        String MessageContent = "";
        if(action.equals("project:created") || action.equals("project:updated")){
            Project newProject = new Project();
            JSONObject project = new JSONObject(request.get("project").toString());
            newProject.setIdProject(project.getInt("id"));
            newProject.setNameProject(project.get("name").toString());
            JSONObject description = new JSONObject(project.get("description").toString());
            newProject.setDescriptionProject(description.get("raw").toString());
            newProject.setStatus(project.get("status").toString());
            if (action.equals("project:created")) {
                projectImpl.addProject(session, newProject);
                session.close();
                MessageContent += "Dự án " + newProject.getNameProject() + " được tạo!";
            } else if (action.equals("project:updated")){
                Project oldProject = (Project) session.get(Project.class, newProject.getIdProject());
                if(oldProject.getNameProject().equals(newProject.getNameProject()) ){
                    MessageContent += "Tên Dự Án được thay đổi: "
                            + oldProject.getNameProject() + " -> "
                            + newProject.getNameProject() + "\n";
                } else if(oldProject.getDescriptionProject().equals(newProject.getDescriptionProject())){
                    MessageContent += "Mô Tả Dự Án được thay đổi: "
                            + oldProject.getDescriptionProject() + " -> "
                            + newProject.getDescriptionProject() + "\n";
                } else if(oldProject.getStatus().equals(newProject.getStatus())){
                    MessageContent += "Trạng Thái Dự Án được thay đổi: "
                            + oldProject.getStatus() + " -> "
                            + newProject.getStatus() + "\n";
                }
                memberInProjectImpl.syncMemberInProject(session);
                List<MemberInProject> memberInProjects = memberInProjectImpl.read(session, oldProject.getNameProject());
                for (MemberInProject eachMember: memberInProjects) {
                    com.fds.opp.app.model.Message MessageObj = new com.fds.opp.app.model.Message();
                    MessageObj.setNameUser(eachMember.getNameUser());
                    MessageObj.setRole(eachMember.getRoles());
                    MessageObj.setMessage(MessageContent);
                    listMessage.add(MessageObj);
                }
            } else {
                System.out.println("Lỗi Package!! ");
            }
        } else if(action.equals("work_package:created") || action.equals("work_package:updated")){
            WorkPackage newWorkPackage = new WorkPackage();
            JSONObject work_package = new JSONObject(request.get("work_package").toString());
            newWorkPackage.setIdWorkPackage(work_package.getInt("id"));
            String ActivitesAuthor = ActivitiesGetAuthor.getAuthor(work_package.getInt("id"));
            newWorkPackage.setNameWorkPackage(work_package.get("subject").toString());
            JSONObject description = new JSONObject(work_package.get("description").toString());
            newWorkPackage.setDescriptionWorkPackage(description.get("raw").toString());
            String startDateString = work_package.get("startDate").toString();
            String dueDateString = work_package.get("dueDate").toString();
            String deadlineDateString = "2020-12-12";
            SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
            Date startDate, dueDate, deadlineDate;
            if(startDateString.equals("null") || startDateString.equals("")) {
                startDate = null;
            } else{
                startDate = formatter1.parse(startDateString);
            }
            newWorkPackage.setStartDate(startDate);
            if(dueDateString.equals("null") || dueDateString.equals("")) {
                dueDate = null;
            } else{
                dueDate = formatter1.parse(dueDateString);
            }
            newWorkPackage.setDueDate(dueDate);
            if(deadlineDateString.equals("null") || deadlineDateString.equals("")) {
                deadlineDate = null;
            } else{
                deadlineDate = formatter1.parse(deadlineDateString);
            }
            newWorkPackage.setDeadlineDate(deadlineDate);
            JSONObject _embedded = new JSONObject(work_package.get("_embedded").toString());
            JSONObject priority = new JSONObject(_embedded.get("priority").toString());
            newWorkPackage.setPriorityWorkPackage(priority.get("name").toString());
            JSONObject status = new JSONObject(_embedded.get("status").toString());
            newWorkPackage.setStatusWorkPackage(status.get("name").toString());
            JSONObject project = new JSONObject(_embedded.get("project").toString());
            newWorkPackage.setNameProject(project.get("name").toString());
            JSONObject _links = new JSONObject(work_package.get("_links").toString());
            JSONObject assignJS = new JSONObject(_links.get("assignee").toString());
            String assignee = _links.get("assignee").toString();
            String assigneeString = "";
            if(assignee.equals("{\"href\":null}")){
                assigneeString = "null";
            } else {
                assigneeString = assignJS.get("title").toString();
            }
            JSONObject responsibleJS = new JSONObject(_links.get("responsible").toString());
            String responsible = _links.get("responsible").toString();
            String responsibleString = "";
            if(responsible.equals("{\"href\":null}")){
                responsibleString = "null";
            } else {
                responsibleString = responsibleJS.get("title").toString();
            }
            newWorkPackage.setAccountable(responsibleString);
            newWorkPackage.setNameUser(assigneeString);
            JSONObject author = new JSONObject(_embedded.get("author").toString());
            newWorkPackage.setAuthor(author.get("name").toString());
            JSONObject type = new JSONObject(_embedded.get("type").toString());
            newWorkPackage.setTypeWorkPackage(type.get("name").toString());
            if(action.equals("work_package:created")){
                session = sessionFactory.openSession();
                workPackageImpl.addWorkPackage(session, newWorkPackage);
                session.close();
                if(!newWorkPackage.getNameWorkPackage().equals("")){
                    MessageContent += "Tên CV được tạo : " + newWorkPackage.getNameWorkPackage() + "\n";
                }
                if(!newWorkPackage.getDescriptionWorkPackage().equals("")){
                    MessageContent += "Mô tả CV : " + newWorkPackage.getDescriptionWorkPackage() + "\n";
                }
                if (newWorkPackage.getStartDate() != null ||
                        newWorkPackage.getDueDate() != null ||
                        newWorkPackage.getDeadlineDate() != null) {
                    MessageContent += "Thời gian CV : "
                            + newWorkPackage.getStartDate() + " - "
                            + newWorkPackage.getDueDate() + "\n Deadline : "
                            + newWorkPackage.getDeadlineDate() + "\n";
                }
                if(!newWorkPackage.getNameUser().equals("null") || !newWorkPackage.getAccountable().equals("null")){
                    session = sessionFactory.openSession();
                    memberInProjectImpl.syncMemberInProject(session);
                    List<MemberInProject> memberInProjects = memberInProjectImpl.read(session, newWorkPackage.getNameProject());
                    session.close();
                    for (MemberInProject mip: memberInProjects) {
                        if(mip.getNameUser().equals(newWorkPackage.getNameUser())){
                            MessageContent += "Bạn đã được assign cho công việc : "
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage();
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            session = sessionFactory.openSession();
                            MessageImpl.addNewMessage(session, newMessage);
                            session.close();
                        } else if(mip.getNameUser().equals(newWorkPackage.getAccountable())){
                            MessageContent += "Bạn đã được accountable cho công việc : "
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage();
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            session = sessionFactory.openSession();
                            MessageImpl.addNewMessage(session, newMessage);
                            session.close();
                        } else if(mip.getRoles().equals("Project admin")){
                            MessageContent += "Bạn đã tạo công việc : "
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage();
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            session = sessionFactory.openSession();
                            MessageImpl.addNewMessage(session, newMessage);
                            session.close();
                        }
                    }
                }
            }else if(action.equals("work_package:updated")){
                WorkPackage oldWorkPackage = session.get(WorkPackage.class, newWorkPackage.getIdWorkPackage());
                if(!oldWorkPackage.getNameWorkPackage().equals(newWorkPackage.getNameWorkPackage())){
                    MessageContent += "Tên CV được cập nhật : "
                            + oldWorkPackage.getNameWorkPackage() + " -> "
                            + newWorkPackage.getNameWorkPackage() + "\n";
                }
                if(!oldWorkPackage.getDescriptionWorkPackage().equals(newWorkPackage.getDescriptionWorkPackage())){
                    MessageContent += "Mô tả CV được cập nhật : "
                            + oldWorkPackage.getDescriptionWorkPackage() + " -> "
                            +  newWorkPackage.getDescriptionWorkPackage() + "\n";
                }
                if(oldWorkPackage.getStartDate() == null ||
                        oldWorkPackage.getDueDate() == null ||
                        oldWorkPackage.getDeadlineDate() == null){
                    if(newWorkPackage.getStartDate() == null ||
                            newWorkPackage.getDueDate() == null ||
                            newWorkPackage.getDeadlineDate() == null) {
                        System.out.println("Null - !=Null");
                    } else{
                        MessageContent += "Thời gian CV được cập nhật : "
                                + newWorkPackage.getStartDate() + " - "
                                + newWorkPackage.getDueDate() + "\n Deadline : "
                                + newWorkPackage.getDeadlineDate()+ "\n";
                        System.out.println("Thời gian CV được cập nhật : "
                                + newWorkPackage.getStartDate() + " - "
                                + newWorkPackage.getDueDate() + "\n Deadline : "
                                + newWorkPackage.getDeadlineDate() + "\n");
                    }
                } else if(oldWorkPackage.getStartDate().compareTo(newWorkPackage.getStartDate())!=0 ||
                        oldWorkPackage.getDueDate().compareTo(newWorkPackage.getDueDate())!=0  ||
                        oldWorkPackage.getDeadlineDate().compareTo(newWorkPackage.getDeadlineDate())!=0){
                    MessageContent += "Thời gian CV được cập nhật : "
                            + newWorkPackage.getStartDate() + " - "
                            + newWorkPackage.getDueDate() + "\n Deadline : "
                            + newWorkPackage.getDeadlineDate() + "\n";
                    System.out.println("123");
                }
                if(!oldWorkPackage.getPriorityWorkPackage().equals(newWorkPackage.getPriorityWorkPackage())){
                    MessageContent += "Mức độ ưu tiên CV được cập nhật: " + oldWorkPackage.getPriorityWorkPackage() + " -> " + newWorkPackage.getPriorityWorkPackage() + "\n";
                }
                if(!oldWorkPackage.getStatusWorkPackage().equals(newWorkPackage.getStatusWorkPackage())){
                    MessageContent += "Trạng Thái CV được cập nhật: " + oldWorkPackage.getStatusWorkPackage() + " -> " + newWorkPackage.getStatusWorkPackage() + "\n";
                }
                if(!oldWorkPackage.getTypeWorkPackage().equals(newWorkPackage.getTypeWorkPackage())){
                    MessageContent += "Loại CV được cập nhật: " + oldWorkPackage.getTypeWorkPackage() + " -> " + newWorkPackage.getTypeWorkPackage() + "\n";
                }
                if(!oldWorkPackage.getNameUser().equals(newWorkPackage.getNameUser())){
                    MessageContent += "Người thực hiện CV được cập nhật: " + oldWorkPackage.getNameUser() + " -> " + newWorkPackage.getNameUser() + "\n";
                }
                workPackageImpl.update(session, newWorkPackage);
                session.close();
                if (!MessageContent.equals("")){
                    session = sessionFactory.openSession();
                    memberInProjectImpl.syncMemberInProject(session);
                    List<MemberInProject> memberInProjects = memberInProjectImpl.read(session, newWorkPackage.getNameProject());
                    session.close();
                    for (MemberInProject eachMember: memberInProjects) {
                        if(!eachMember.getNameUser().equals(ActivitesAuthor) ||
                                eachMember.getRoles().equals("Project admin") ||
                                eachMember.getNameUser().equals(newWorkPackage.getAccountable()) ||
                                eachMember.getNameUser().equals(newWorkPackage.getNameUser()))
                        {
                            Message MessageObj = new Message();
                            MessageObj.setNameUser(eachMember.getNameUser());
                            MessageObj.setRole(eachMember.getRoles());
                            MessageObj.setMessage(MessageContent);
                            MessageObj.setStatus("Pending...");
                            listMessage.add(MessageObj);
                            session = sessionFactory.openSession();
                            MessageImpl.addNewMessage(session, MessageObj);
                            session.close();
                        } else {
                            System.out.println("Người k gửi : " + eachMember.getNameUser());
                        }

                    }
                } else {
                    System.out.println("Không có sự thay đổi!");
                }
            }
        }
        return listMessage;
    }
}
