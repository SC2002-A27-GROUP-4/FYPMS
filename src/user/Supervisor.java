package user;

import project_request.Project;
import project_request.Request;
import project_request.Project.projectStatus;
import utilities.Scan;

import java.util.ArrayList;
import java.util.Queue;

import database.DatabaseProject;
import database.DatabaseSupervisor;

import java.util.LinkedList;
public class Supervisor extends User{
    public ArrayList<Integer> projectIDs = new ArrayList<>();
    public Queue<Request> supervisorRequests = new LinkedList<>();
    public static final int MAX_PROJECT=2;
    private int projectCount=0;

    public Supervisor (String userID, String email, String name, String password) {
        super(userID, email, name, password);
    }

    public void setProjects(int projectid){
        if(projectIDs.size()>=2){
            System.out.println("You have reached the maximum number of projects allocated.");
        }
        else{
            projectIDs.add(projectid);
            System.out.println("Project successfully allocated to supervisor.");
        }
    }

    public void addRequest(Request request){
        this.supervisorRequests.add(request);
    }

    public void createProject(String projecttitle){
        Project project = new Project(projecttitle, this);
        DatabaseProject.addProject(project);
        System.out.println("Projects created successfully.");
    }

    public void viewMyProjects(){

        for (Project project : DatabaseProject.getProjectData()){
            if (project.getSupervisor() == this) {
                project.printProjectInfo(false);
            }
        }
    }

    public void modifyTitle(Project project, String newTitle){
        if(project.getSupervisor() != this){
            System.out.println("You cannot modify this project's title.");
        }
        else{
            project.setTitle(newTitle);
            System.out.println("Project title modified successfully.");
        }
    }

    public int viewRequests() {
        if (supervisorRequests.size() == 0) {
            System.out.println("You have no pending requests.");
            return 0;
        } else {
            System.out.println("Here are your pending requests:");
            for (Request request : supervisorRequests) {
                if (request.getStatus() == Request.RequestStatus.PENDING) {
                    request.printRequest();
                }
            }
            return 1;
        }
    }

    public boolean checkRequests() {
        if (supervisorRequests.size() == 0) {
            return false;
        } for (Request request : supervisorRequests) {
            if (request.getStatus() == Request.RequestStatus.PENDING) {
               return true;
            }
        }
        return false;
    }

    public Request findRequest(int requestid) {
        for (Request request : supervisorRequests) {
            if (request.getRequestID() == requestid){
                return request;
            }
        }
        return null;
    }

    public void approveRequest(int requestID) {
        Request request = findRequest(requestID);

        if (request.getRequestType() == Request.RequestType.STUDENT_TO_SUPERVISOR &&
                request.getProject().getSupervisor() == this && request.getStatus() == Request.RequestStatus.PENDING) {

            request.approve();
            System.out.println("Request approved successfully.");
            return;
        }

        System.out.println("Request not found or cannot be approved.");
    }

    public void rejectRequest(int requestID) {
        Request request = findRequest(requestID);

        if (request.getRequestType() == Request.RequestType.STUDENT_TO_SUPERVISOR &&
                request.getProject().getSupervisor() == this && request.getStatus() == Request.RequestStatus.PENDING) {

            request.reject();
            System.out.println("Request rejected successfully.");
            return;
        }

        System.out.println("Request not found or cannot be rejected.");
    }

    public void viewRequestHistory() {
        if (supervisorRequests.size() == 0) {
            System.out.println("You have no request history.");
        } else {
            System.out.println("Here is your request history:");
            for (Request request : supervisorRequests) {
                System.out.println("Request ID: " + request.getRequestID());
                System.out.println("Request type: " + request.getRequestType().toString());
                System.out.println("Request details: " + request.getDetails().toString());
                System.out.println("Request status: " + request.getStatus().toString());
            }
        }
    }

    public void requestTransfer(int projectID, String replacementSupervisorID) {
        Project project = DatabaseProject.getProject(projectID);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        Supervisor replacementSupervisor = DatabaseSupervisor.getSupervisor(replacementSupervisorID);

        if (replacementSupervisor == null) {
            System.out.println("Replacement supervisor not found.");
            return;
        }
        if (project.getStatus() != Project.projectStatus.ALLOCATED || project.getStudent() == null) {
            System.out.println("Project is not allocated or does not have a student assigned.");
            return;
        }
        Request request = new Request(Request.RequestType.SUPERVISOR_TO_FYPCOORDINATOR, Request.RequestDetails.TRANSFER_STUDENT, project);
        request.setReplacementSupervisorID(replacementSupervisorID);
        FYPCoordinator fypCoordinator = DatabaseSupervisor.getFYPCoordinator();
        fypCoordinator.coordinatorRequests.add(request);
        System.out.println("Request sent successfully.");
    }

    public int checkProjectCount(){
        return this.projectCount;
    }

    public void allocatedProject(){
        projectCount++;
        if(projectCount == MAX_PROJECT){
            for (Project project : DatabaseProject.getProjectData()){
                if (project.getSupervisor() == this) {
                    if(project.getStatus() != Project.projectStatus.ALLOCATED)
                        project.setStatus(projectStatus.UNAVAILABLE);
                }
            }
        }
    }

    public void deallocatedProject() {
        projectCount--;
        if (projectCount == 1) {
            for (Project project : DatabaseProject.getProjectData()){
                if (project.getSupervisor() == this) {
                    if(project.getStatus() == Project.projectStatus.UNAVAILABLE && project.getStudent() == null)
                        project.setStatus(projectStatus.AVAILABLE);
                }
            }
        }
    }

    public void SupervisorPage() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("|                 WELCOME TO SUPERVISOR PAGE!                   |");
        Scan sc = Scan.getInstance();
        int choice;

        Boolean logout = false;
        while (!logout) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                  What would you like to do?                   |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                      1. Create project                        |");
            System.out.println("|                     2. View my projects                       |");
            System.out.println("|                  3. Request transfer student                  |");
            if(this.checkRequests())
                System.out.println("|          4. View and approve/reject student requests \033[0;32m NEW\033[0m     |");
                else
            System.out.println("|          4. View and approve/reject student requests          |");
            System.out.println("|   5. View incoming and outgoing request history and status    |");
            System.out.println("|                    6. Update project title                    |");
            System.out.println("|                      7. Change password                       |");
            System.out.println("|                         8. Log out                            |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            choice = sc.nextInt();
            sc.nextLine();
            switch(choice) {
                case 1:
                    System.out.println("Enter project title");
                    String projecttitle = sc.nextLine();
                    this.createProject(projecttitle);
                    break;
                case 2:
                    this.viewMyProjects();
                    break;
                case 3:
                    System.out.println("Enter projectid: ");
                    int projectID = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter replacement supervisor id: ");
                    String replacementid = sc.nextLine();
                    this.requestTransfer(projectID, replacementid);
                    break;
                case 4:
                    if (this.viewRequests() == 0) break;
                    System.out.println("Do you want to resolve a request? Type 'Yes' if you do");
                    String selection = sc.next();
                    while(selection.equals("Yes") || selection.equals("yes"))
                    {
                        System.out.println("Select the id of a request you want to resolve");
                        int requestid = sc.nextInt();
                        Request request = this.findRequest(requestid);
                        if(request == null)
                        {
                            System.out.println("Invalid requestID");
                            break;
                        }

                        System.out.println("Type 1 to approve this request\n" +  "Type 2 to reject this request\n" + "Type anything else to not resolve this request");
                        int select = sc.nextInt();
                        sc.nextLine();
                        if(select == 1)
                        {
                            String newtitle = request.getNewTitle();
                            Project project = request.getProject();
                            this.modifyTitle(project, newtitle);
                            this.approveRequest(requestid);
                        }
                        else if(select == 2)
                        {
                            this.rejectRequest(requestid);
                        }
                        else
                        {
                            System.out.println("The request status remains pending");
                        }
                        System.out.println("Do you want to resolve another request? Type 'Yes' if you do");
                        selection = sc.next();
                    }

                    break;
                case 5:
                    this.viewRequestHistory();
                    break;
                case 6:
                    System.out.println("Please input project id");
                    int projectid = sc.nextInt();
                    sc.nextLine();
                    Project project = DatabaseProject.getProject(projectid);
                    if (project == null) {
                        System.out.println("invalid project id, try again");
                        break;
                    }
                    System.out.println("Input new project title");
                    String newtitle = sc.next();

                    this.modifyTitle(project, newtitle);
                    break;
                case 7:
                    this.changePassword();
                    break;
                case 8:
                    logout = true;
                    System.out.println("Logged out succesfully\n");
                    break;
                default:
                    System.out.println("choice not found, try again");
            }
        }
    }

}
