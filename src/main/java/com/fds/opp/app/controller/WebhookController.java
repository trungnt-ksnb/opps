package com.fds.opp.app.controller;

import com.fds.opp.app.daoImpl.memberInProjectImpl;
import com.fds.opp.app.daoImpl.projectImpl;
import com.fds.opp.app.daoImpl.workPackageImpl;
import com.fds.opp.app.model.MemberInProject;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v3/telegram/notify")
public class WebhookController {
    @PostMapping("/create")
    public static List<com.fds.opp.app.model.Message> newRequest(@RequestBody String JsonString) throws Exception
    {
        System.out.println(JsonString);
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
                    MessageContent += "Tên Dự Án được thay đổi: " + oldProject.getNameProject() + " -> " + newProject.getNameProject() + "\n";
                } else if(oldProject.getDescriptionProject().equals(newProject.getDescriptionProject())){
                    MessageContent += "Mô Tả Dự Án được thay đổi: " + oldProject.getDescriptionProject() + " -> " + newProject.getDescriptionProject() + "\n";
                } else if(oldProject.getStatus().equals(newProject.getStatus())){
                    MessageContent += "Trạng Thái Dự Án được thay đổi: " + oldProject.getStatus() + " -> " + newProject.getStatus() + "\n";
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
                MessageContent += "Có Lỗi! \n";
            }
        } else if(action.equals("work_package:created") || action.equals("work_package:updated")){
            WorkPackage newWorkPackage = new WorkPackage();
            JSONObject work_package = new JSONObject(request.get("work_package").toString());
            newWorkPackage.setIdWorkPackage(work_package.getInt("id"));
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
            System.out.println(assignee);
            String assigneeString = "";
            if(assignee.equals("{\"href\":null}")){
                assigneeString = "null";
            } else {
                assigneeString = assignJS.get("title").toString();
            }
            newWorkPackage.setNameUser(assigneeString);
            System.out.println(assigneeString);
            JSONObject author = new JSONObject(_embedded.get("author").toString());
            newWorkPackage.setAuthor(author.get("name").toString());
            JSONObject type = new JSONObject(_embedded.get("type").toString());
            newWorkPackage.setTypeWorkPackage(type.get("name").toString());
            System.out.println(newWorkPackage);
            if(action.equals("work_package:created")){
                System.out.println("Đang thêm WorkPackage...");
                workPackageImpl.addWorkPackage(session, newWorkPackage);
                session.close();
            }else if(action.equals("work_package:updated")){
                WorkPackage oldWorkPackage = session.get(WorkPackage.class, newWorkPackage.getIdWorkPackage());
                if(!oldWorkPackage.getNameWorkPackage().equals(newWorkPackage.getNameWorkPackage())){
                    MessageContent += "Tên CV được cập nhật : " + oldWorkPackage.getNameWorkPackage() + " -> " +  newWorkPackage.getNameWorkPackage() + "\n";
                } else if(!oldWorkPackage.getDescriptionWorkPackage().equals(newWorkPackage.getDescriptionWorkPackage())){
                    MessageContent += "Mô tả CV được cập nhật : " + oldWorkPackage.getDescriptionWorkPackage() + " -> " +  newWorkPackage.getDescriptionWorkPackage() + "\n";
                } else if(oldWorkPackage.getStartDate() != newWorkPackage.getStartDate() || oldWorkPackage.getDueDate() != newWorkPackage.getDueDate() || oldWorkPackage.getDeadlineDate() != newWorkPackage.getDeadlineDate()){
                    MessageContent += "Thời gian CV được cập nhật : " + newWorkPackage.getStartDate() + " - " + newWorkPackage.getDueDate() + "\n Deadline : " + newWorkPackage.getDeadlineDate();
                } else if(!oldWorkPackage.getPriorityWorkPackage().equals(newWorkPackage.getPriorityWorkPackage())){
                    MessageContent += "Mức độ ưu tiên CV được cập nhật: " + oldWorkPackage.getPriorityWorkPackage() + " -> " + newWorkPackage.getPriorityWorkPackage();
                } else if(!oldWorkPackage.getStatusWorkPackage().equals(newWorkPackage.getStatusWorkPackage())){
                    MessageContent += "Trạng Thái CV được cập nhật: " + oldWorkPackage.getStatusWorkPackage() + " -> " + newWorkPackage.getStatusWorkPackage();
                } else if(!oldWorkPackage.getTypeWorkPackage().equals(newWorkPackage.getTypeWorkPackage())){
                    MessageContent += "Loại CV được cập nhật: " + oldWorkPackage.getTypeWorkPackage() + " -> " + newWorkPackage.getTypeWorkPackage();
                } else if(!oldWorkPackage.getNameUser().equals(newWorkPackage.getNameUser())){
                    MessageContent += "Người thực hiện CV được cập nhật: " + oldWorkPackage.getNameUser() + " -> " + newWorkPackage.getNameUser();
                }
                workPackageImpl.update(session, newWorkPackage);
                session.close();
            } else{
                System.out.println("Lỗi Workpackage!");
                    MessageContent+="Lỗi Package";
            }
            memberInProjectImpl.syncMemberInProject(session);
            List<MemberInProject> memberInProjects = memberInProjectImpl.read(session, newWorkPackage.getNameProject());
            for (MemberInProject eachMember: memberInProjects) {
                com.fds.opp.app.model.Message MessageObj = new com.fds.opp.app.model.Message();
                MessageObj.setNameUser(eachMember.getNameUser());
                MessageObj.setRole(eachMember.getRoles());
                MessageObj.setMessage(MessageContent);
                listMessage.add(MessageObj);
            }
        }
        return listMessage;
    }
}
