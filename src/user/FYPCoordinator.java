package user;

import project_request.Project;
import project_request.Request;
import project_request.Project.projectStatus;
import utilities.Scan;

import java.util.Queue;

import database.DatabaseProject;
import database.DatabaseStudent;
import database.DatabaseSupervisor;

import java.util.LinkedList;
public class FYPCoordinator extends Supervisor{
    public Queue<Request> coordinatorRequests = new LinkedList<Request>();

    public FYPCoordinator(String userID, String email, String name, String password) {
        super(userID, email, name, password);
    }

    public int viewCoordinatorRequests() {
        if (coordinatorRequests.size() == 0) {
            System.out.println("You have no pending requests.");
            return 0;
        }
        for (Request request : coordinatorRequests) {
            if (request.getStatus() == Request.RequestStatus.PENDING) {
                System.out.println("Request ID: " + request.getRequestID());
                System.out.println("Request type: " + request.getRequestType());
                System.out.println("Request details: " + request.getDetails());
                System.out.println("Request status: " + request.getStatus());
                System.out.println("");
            }
        }
        return 1;
    }

    public boolean checkCoordinatorRequests() {
        if (coordinatorRequests.size() == 0) {
            return false;
        }
        for (Request request : coordinatorRequests) {
            if (request.getStatus() == Request.RequestStatus.PENDING) {
                return true;
            }
        }
        return false;
    }

    public void viewAllRequest(){
        DatabaseStudent.printStudentData();
        DatabaseSupervisor.printSupervisorData();
        System.out.println("Enter a user ID");
        Scan sc = Scan.getInstance();
        String userid = sc.next();
        if((DatabaseStudent.getStudent(userid)) != null){
            Student student = DatabaseStudent.getStudent(userid);
            student.viewRequestHistory();
            return;
        }
        if((DatabaseSupervisor.getSupervisor(userid)) != null){
            Supervisor supervisor = DatabaseSupervisor.getSupervisor(userid);
            supervisor.viewRequestHistory();
            return;
        }
        System.out.println("User not found");
        return;
    }

    public void viewCoordinatorRequestHistory(){
        if (coordinatorRequests.size() == 0) {
            System.out.println("You have no requests.");
            return;
        }
        for (Request request : coordinatorRequests){
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Request type: " + request.getRequestType());
            System.out.println("Request details: " + request.getDetails());
            System.out.println("Request status: " + request.getStatus());
            System.out.println("");
        }

    }

    public Request findCoordinatorRequest(int requestid) {
        for (Request request : coordinatorRequests) {
            if (request.getRequestID() == requestid){
                return request;
            }
        }
        return null;
    }

    public void rejectRequest(int requestID) {
        Request request = findCoordinatorRequest(requestID);
        if (request == null) {
            System.out.println("Request not found.");
            return;
        }
        if (request.getStatus() == Request.RequestStatus.PENDING) {
            request.reject();
            System.out.println("Request rejected successfully.");
            return;
        }
        System.out.println("Reject request unsuccessful");
    }

    public void allocateProject(Request request) {
        Project project = request.getProject();
        Supervisor supervisor = project.getSupervisor();
        if(project.getStatus() == Project.projectStatus.RESERVED) {
            project.allocate();
            System.out.println("Project allocated successfully.");
            supervisor.allocatedProject();
            request.approve();
            return;
        }

        if(supervisor.checkProjectCount() == 2){
            System.out.println("Projects are capped for the supervisor.");
            request.reject();
            return;
        }
        request.reject();
        System.out.println("Project not found or cannot be allocated.");
    }

    public void deregisterStudent(Request request) {
        Project project = request.getProject();

        if(project.getStatus() == Project.projectStatus.ALLOCATED) {
            Supervisor supervisor = project.getSupervisor();
            supervisor.deallocatedProject();
            project.deregister();
            request.approve();
            System.out.println("Project deregistered successfully.");
            return;
        }
        request.reject();
        System.out.println("Project not found or cannot be deregistered.");
    }

    public void changeSupervisor(Request request) {
        String newsupervisorid = request.getReplacementSupervisorID();
        Project project = request.getProject();
        Supervisor supervisor = DatabaseSupervisor.getSupervisor(newsupervisorid);

        if (newsupervisorid == null || supervisor == null) {
            System.out.println("Supervisor not found");
            request.reject();
            return;
        }

        if(supervisor.checkProjectCount() == 2){
            System.out.println("Projects are capped");
            request.reject();
            return;
        }
        Supervisor s = project.getSupervisor();
        s.deallocatedProject();
        request.approve();
        project.setSupervisor(supervisor);
        supervisor.allocatedProject();
    }

    public void viewProjects(){
        DatabaseProject.printAllProjects(false);
    }

    public void generateProjectDetailsReport(){
        Scan sc = Scan.getInstance();

        System.out.println("Select the filters");
        System.out.println("1: Status");
        System.out.println("2: Supervisor");
        int i = sc.nextInt();

        switch(i){
            case 1:
                System.out.println("Choose to filter by status.(Input an integer)");
                System.out.println("1: Project is Available");
                System.out.println("2: Project is Reserved");
                System.out.println("3: Project is Allocated");
                System.out.println("4: Project is Unavailable");

                int j = sc.nextInt();
                switch(j){
                    case 1:
                        DatabaseProject.printProjectsByStatus(projectStatus.AVAILABLE);
                        break;
                    case 2:
                        DatabaseProject.printProjectsByStatus(projectStatus.RESERVED);
                        break;
                    case 3:
                        DatabaseProject.printProjectsByStatus(projectStatus.ALLOCATED);
                        break;
                    case 4:
                        DatabaseProject.printProjectsByStatus(projectStatus.UNAVAILABLE);
                        break;
                }
                break;

            case 2: // put exception error
                System.out.println("Input SupervisorId");
                String s = sc.next();
                DatabaseProject.printProjectsBySupervisorID(s);
                break;

            default:
                System.out.println("Input 1 or 2");
        }
    }

    public void FYPCoordinatorPage() {
        //If Li Fang manages to login
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("|                 WELCOME COORDINATOR LI FANG!                  |");
        Scan sc = Scan.getInstance();
        int choice;

        Boolean logout = false;
        while (!logout) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                  What would you like to do?                   |");
            System.out.println("|                   FYP Coordinator Options                     |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                     1. View all projects                      |");
            System.out.println("|                  2. Generate project details                  |");
            if(this.checkCoordinatorRequests())
                System.out.println("|          3. View and approve/reject all pending requests \033[0;32m NEW\033[0m |");
            else
                System.out.println("|       3. View and approve/reject all pending requests         |");
            System.out.println("|            4. View all request history and status             |");
            System.out.println("|          5. View a users request history and status           |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                      Supervisor Options                       |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                      6. Create project                        |");
            System.out.println("|                     7. View my projects                       |");
            System.out.println("|                  8. Request transfer student                  |");
            if(this.checkRequests())
                System.out.println("|          9. View and approve/reject student requests \033[0;32m NEW\033[0m     |");
                else
            System.out.println("|          9. View and approve/reject student requests          |");
            System.out.println("|                    10. Update project title                   |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                     11. Change password                       |");
            System.out.println("|                        12. Log out                            |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            choice = sc.nextInt();
            String selection;
            sc.nextLine();
            switch(choice) {
                case 1:
                    this.viewProjects();
                    break;
                case 2:
                    this.generateProjectDetailsReport();
                    break;
                case 3: //deregister student, change supervisor, allocate project
                    if (this.viewCoordinatorRequests() == 0) break;

                    System.out.println("Do you want to resolve a request? Type 'Yes' if you do");
                    selection = sc.next();
                    while(selection.equals("Yes")|| selection.equals("yes"))
                    {
                        System.out.println("Select the id of a request you want to resolve");
                        int requestid = sc.nextInt();
                        sc.nextLine();
                        Request request = this.findCoordinatorRequest(requestid);
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
                            if (request.getDetails() == Request.RequestDetails.REGISTRATION) {
                                this.allocateProject(request);
                            }
                            else if (request.getDetails() == Request.RequestDetails.DEREGISTER) {
                                this.deregisterStudent(request);
                            }
                            else {
                                this.changeSupervisor(request);
                            }
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
                case 4:
                    System.out.println("As a supervisor:");
                    this.viewRequestHistory();
                    System.out.println("As a coordinator:");
                    this.viewCoordinatorRequestHistory();
                    break;
                case 5:
                    this.viewAllRequest();
                    break;
                case 6:
                    System.out.println("Enter project title");
                    String projecttitle = sc.nextLine();
                    this.createProject(projecttitle);
                    break;
                case 7:
                    this.viewMyProjects();
                    break;
                case 8:
                    System.out.println("Enter projectid: ");
                    int projectID = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter replacement supervisor id: ");
                    String replacementid = sc.nextLine();
                    this.requestTransfer(projectID, replacementid);
                    break;
                case 9:
                    if (this.viewRequests() == 0) break;
                    System.out.println("Do you want to resolve a request? Type 'Yes' if you do");
                    selection = sc.next();
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
                case 10:
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

                case 11:
                    this.changePassword();
                    break;
                case 12:
                    logout = true;
                    System.out.println("Logged out successfully\n");
                    break;
                default:
                    System.out.println("choice not found, try again");
            }
        }
    }
}
