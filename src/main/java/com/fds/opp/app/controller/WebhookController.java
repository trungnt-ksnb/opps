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

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api/v3/telegram/notify")
public class WebhookController {
    @PostMapping("/create")
    public List<com.fds.opp.app.model.Message> newRequest(@RequestBody String JsonString) throws Exception {
        List<com.fds.opp.app.model.Message> listMessage = new ArrayList<>();
        JSONObject request = new JSONObject(JsonString);
        String action = request.get("action").toString();
        String MessageContent = null;
        Locale localeEn = new Locale("vi");
        ResourceBundle labels = ResourceBundle.getBundle("messages", localeEn);
        if (action.equals("project:created") || action.equals("project:updated")) {
            Project newProject = new Project();
            JSONObject project = new JSONObject(request.get("project").toString());
            newProject.setIdProject(project.getInt("id"));
            newProject.setNameProject(project.get("name").toString());
            JSONObject description = new JSONObject(project.get("description").toString());
            newProject.setDescriptionProject(description.get("raw").toString());
            newProject.setStatus(project.get("status").toString());
            if (action.equals("project:created")) {
                projectImpl.addProject(newProject);
            } else if (action.equals("project:updated")) {
                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
                Session session = sessionFactory.openSession();
                Project oldProject = session.get(Project.class, newProject.getIdProject());
                session.close();
                projectImpl.update(newProject);
                if (!oldProject.getNameProject().equals(newProject.getNameProject())) {
                    MessageContent += labels.getString("projectNameUpdated")
                            + oldProject.getNameProject() + " -> "
                            + newProject.getNameProject() + "\n";
                }
                if (!oldProject.getDescriptionProject().equals(newProject.getDescriptionProject())) {
                    MessageContent += labels.getString("projectDescriptionupdated")
                            + oldProject.getDescriptionProject() + " -> "
                            + newProject.getDescriptionProject() + "\n";
                }
                if (oldProject.getStatus() == null){
                    oldProject.setStatus("");
                }
                if (newProject.getStatus() == null){
                    oldProject.setStatus("");
                }
                if (!oldProject.getStatus().equals(newProject.getStatus())) {
                    MessageContent += labels.getString("projectStatusUpdated")
                            + oldProject.getStatus() + " -> "
                            + newProject.getStatus() + "\n";
                }
                if(MessageContent!=null){
                    memberInProjectImpl.syncMemberInProject();
                    List<MemberInProject> memberInProjects = memberInProjectImpl.read(oldProject.getNameProject());
                    assert memberInProjects != null;
                    for (MemberInProject eachMember : memberInProjects) {
                        com.fds.opp.app.model.Message MessageObj = new com.fds.opp.app.model.Message();
                        MessageObj.setNameUser(eachMember.getNameUser());
                        MessageObj.setRole(eachMember.getRoles());
                        MessageObj.setMessage(MessageContent);
                        MessageObj.setStatus("Pending...");
                        listMessage.add(MessageObj);
                        MessageImpl.addNewMessage(MessageObj);
                    }
                }
            } else {
                System.out.println("Lỗi Package!! ");
            }
        } else if (action.equals("work_package:created") || action.equals("work_package:updated")) {
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
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate, dueDate, deadlineDate;
            if (startDateString.equals("null") || startDateString.equals("")) {
                startDate = null;
            } else {
                startDate = formatter1.parse(startDateString);
            }
            newWorkPackage.setStartDate(startDate);
            if (dueDateString.equals("null") || dueDateString.equals("")) {
                dueDate = null;
            } else {
                dueDate = formatter1.parse(dueDateString);
            }
            newWorkPackage.setDueDate(dueDate);
            if (deadlineDateString.equals("null") || deadlineDateString.equals("")) {
                deadlineDate = null;
            } else {
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
            String assigneeString;
            if (assignee.equals("{\"href\":null}")) {
                assigneeString = "null";
            } else {
                assigneeString = assignJS.get("title").toString();
            }
            JSONObject responsibleJS = new JSONObject(_links.get("responsible").toString());
            String responsible = _links.get("responsible").toString();
            String responsibleString;
            if (responsible.equals("{\"href\":null}")) {
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
            if (action.equals("work_package:created")) {
                workPackageImpl.addWorkPackage(newWorkPackage);
                if (!newWorkPackage.getNameWorkPackage().equals("")) {
                    MessageContent += labels.getString("workpackageNameCreated") + newWorkPackage.getNameWorkPackage() + "\n";
                }
                if (!newWorkPackage.getDescriptionWorkPackage().equals("")) {
                    MessageContent += labels.getString("workpackageDescriptionCreated") + newWorkPackage.getDescriptionWorkPackage() + "\n";
                }
                if (newWorkPackage.getStartDate() != null ||
                        newWorkPackage.getDueDate() != null ||
                        newWorkPackage.getDeadlineDate() != null) {
                    MessageContent += labels.getString("workpackageTimeCreated")
                            + workPackageImpl.DateString(newWorkPackage.getStartDate().toString()) + " - "
                            + workPackageImpl.DateString(newWorkPackage.getDueDate().toString()) + "\n Deadline : "
                            + workPackageImpl.DateString(newWorkPackage.getDeadlineDate().toString()) + "\n";
                }
                if (!newWorkPackage.getNameUser().equals("null") || !newWorkPackage.getAccountable().equals("null")) {
                    memberInProjectImpl.syncMemberInProject();
                    List<MemberInProject> memberInProjects = memberInProjectImpl.read(newWorkPackage.getNameProject());
                    assert memberInProjects != null;
                    for (MemberInProject mip : memberInProjects) {
                        if (mip.getNameUser().equals(newWorkPackage.getNameUser())) {
                            MessageContent += labels.getString("workpackageAssigneeCreated")
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage() + "\n";
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            MessageImpl.addNewMessage(newMessage);
                        } else if (mip.getNameUser().equals(newWorkPackage.getAccountable())) {
                            MessageContent += labels.getString("workpackageAccountableCreated")
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage() + "\n";
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            MessageImpl.addNewMessage(newMessage);
                        }
                        if (mip.getRoles().equals("Project admin")) {
                            MessageContent += labels.getString("workpackageMessageForAdmin")
                                    + newWorkPackage.getIdWorkPackage() + " : "
                                    + newWorkPackage.getNameWorkPackage();
                            Message newMessage = new Message();
                            newMessage.setNameUser(mip.getNameUser());
                            newMessage.setRole(mip.getRoles());
                            newMessage.setMessage(MessageContent);
                            newMessage.setStatus("Pending...");
                            listMessage.add(newMessage);
                            MessageImpl.addNewMessage(newMessage);
                        } else {
                            System.out.println("Không gửi : " + mip.getNameUser());
                        }
                    }
                }
            } else if (action.equals("work_package:updated")) {
                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
                Session session = sessionFactory.openSession();
                WorkPackage oldWorkPackage = session.get(WorkPackage.class, newWorkPackage.getIdWorkPackage());
                session.close();
                if (!oldWorkPackage.getNameWorkPackage().equals(newWorkPackage.getNameWorkPackage())) {
                    MessageContent += labels.getString("workpackageNameUpdated")
                            + oldWorkPackage.getNameWorkPackage() + " -> "
                            + newWorkPackage.getNameWorkPackage() + "\n";
                }
                if (!oldWorkPackage.getDescriptionWorkPackage().equals(newWorkPackage.getDescriptionWorkPackage())) {
                    MessageContent += labels.getString("workpackageDescriptionUpdated")
                            + oldWorkPackage.getDescriptionWorkPackage() + " -> "
                            + newWorkPackage.getDescriptionWorkPackage() + "\n";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (oldWorkPackage.getStartDate() == null ||
                        oldWorkPackage.getDueDate() == null ||
                        oldWorkPackage.getDeadlineDate() == null) {
                    if (newWorkPackage.getStartDate() == null ||
                            newWorkPackage.getDueDate() == null ||
                            newWorkPackage.getDeadlineDate() == null) {
                        System.out.println("Null - !=Null");
                    } else {
                        MessageContent += labels.getString("workpackageTimeUpdated1")
                                + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated")
                                + workPackageImpl.DateString(newWorkPackage.getStartDate().toString()) + " - "
                                + workPackageImpl.DateString(newWorkPackage.getDueDate().toString()) + "\n Deadline : "
                                + workPackageImpl.DateString(newWorkPackage.getDeadlineDate().toString()) + "\n";
                    }
                } else {
                    SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
                    String oldStartDate = out.format(in.parse(oldWorkPackage.getStartDate().toString()));
                    String oldDueDate = out.format(in.parse(oldWorkPackage.getDueDate().toString()));
                    String oldDeadlineDate = out.format(in.parse(oldWorkPackage.getDeadlineDate().toString()));
                    String newDeadlineDate = workPackageImpl.DateString(newWorkPackage.getDeadlineDate().toString());
                    String newStartDate = workPackageImpl.DateString(newWorkPackage.getStartDate().toString());
                    String newDueDate = workPackageImpl.DateString(newWorkPackage.getDueDate().toString());
                    if (!oldStartDate.equals(newStartDate) || !!oldDueDate.equals(newDueDate) || !oldDeadlineDate.equals(newDeadlineDate)) {
                        MessageContent += labels.getString("workpackageTimeUpdated1")
                                + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated")
                                + workPackageImpl.DateString(newWorkPackage.getStartDate().toString()) + " - "
                                + workPackageImpl.DateString(newWorkPackage.getDueDate().toString()) + "\n Deadline : "
                                + workPackageImpl.DateString(newWorkPackage.getDeadlineDate().toString()) + "\n";
                    }
                }
                if (!oldWorkPackage.getPriorityWorkPackage().equals(newWorkPackage.getPriorityWorkPackage())) {
                    MessageContent += labels.getString("worpackagePriorityUpdated") + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated") + oldWorkPackage.getPriorityWorkPackage() + " -> " + newWorkPackage.getPriorityWorkPackage() + "\n";
                }
                if (!oldWorkPackage.getStatusWorkPackage().equals(newWorkPackage.getStatusWorkPackage())) {
                    MessageContent += labels.getString("workpackageStatusUpdated") + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated") + oldWorkPackage.getStatusWorkPackage() + " -> " + newWorkPackage.getStatusWorkPackage() + "\n";
                }
                if (!oldWorkPackage.getTypeWorkPackage().equals(newWorkPackage.getTypeWorkPackage())) {
                    MessageContent += labels.getString("workpackageTypeUpdated") + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated") + oldWorkPackage.getTypeWorkPackage() + " -> " + newWorkPackage.getTypeWorkPackage() + "\n";
                }
                if (!oldWorkPackage.getNameUser().equals(newWorkPackage.getNameUser())) {
                    MessageContent += labels.getString("workpackageAsigneeUpdated")+ newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated") + oldWorkPackage.getNameUser() + " -> " + newWorkPackage.getNameUser() + "\n";
                }
                if (!oldWorkPackage.getAccountable().equals(newWorkPackage.getAccountable())) {
                    MessageContent += labels.getString("workpackageAccountableUpdated") + newWorkPackage.getNameWorkPackage() + labels.getString("workpackageBeingUpdated") + oldWorkPackage.getNameUser() + " -> " + newWorkPackage.getNameUser() + "\n";
                }

                workPackageImpl.update(newWorkPackage);
                if (!MessageContent.equals("")) {
                    memberInProjectImpl.syncMemberInProject();
                    List<MemberInProject> memberInProjects = memberInProjectImpl.read(newWorkPackage.getNameProject());
                    for (MemberInProject eachMember : memberInProjects) {
                        if (eachMember.getNameUser().equals(newWorkPackage.getAccountable())) {
                            Message MessageObj = new Message();
                            MessageObj.setNameUser(eachMember.getNameUser());
                            MessageObj.setRole(eachMember.getRoles());
                            MessageObj.setMessage(MessageContent);
                            MessageObj.setStatus("Pending...");
                            listMessage.add(MessageObj);
                            MessageImpl.addNewMessage(MessageObj);
                        }
                        if (eachMember.getNameUser().equals(newWorkPackage.getNameUser())) {
                            Message MessageObj = new Message();
                            MessageObj.setNameUser(eachMember.getNameUser());
                            MessageObj.setRole(eachMember.getRoles());
                            MessageObj.setMessage(MessageContent);
                            MessageObj.setStatus("Pending...");
                            listMessage.add(MessageObj);
                            MessageImpl.addNewMessage(MessageObj);
                        }
                        if (eachMember.getRoles().equals("Project admin")) {
                            Message MessageObj = new Message();
                            MessageObj.setNameUser(eachMember.getNameUser());
                            MessageObj.setRole(eachMember.getRoles());
                            MessageObj.setMessage(MessageContent);
                            MessageObj.setStatus("Pending...");
                            listMessage.add(MessageObj);
                            MessageImpl.addNewMessage(MessageObj);
                        } else {
                            System.out.println("Người k gửi : " + eachMember.getNameUser());
                        }

                    }

                } else {
                    System.out.println("Không có sự thay đổi!");
                }

            }
        }
        TelegramBotAPI.callExec();
//        System.gc();
        return listMessage;
    }

}

