package user;

import project_request.Project;
import project_request.Request;
import utilities.Scan;

import java.util.ArrayList;

import database.DatabaseProject;
import database.DatabaseSupervisor;
public class Student extends User{
    public enum StudentStatus {REGISTERED,NOTREGISTERED,DEREGISTERED};
    private int projectID;
    private StudentStatus studentStatus;
    private  ArrayList<Request> studentRequests = new ArrayList<>();

    public Student(String userId, String email, String name, String password, StudentStatus status, Integer projectid){
        super(userId, email, name, password);
        this.studentStatus = status;
        this.projectID = projectid;
    }

    public void addRequest(Request request){
        this.studentRequests.add(request);
    }

    public int getProjectID() {
        return this.projectID;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setProjectID(int projectid){
        this.projectID = projectid;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }

    public void viewAvailableProjects() {
        switch(this.studentStatus) {
            case NOTREGISTERED:
                System.out.println("You have not been registered to any project");
                System.out.println("All available projects:");
                DatabaseProject.printProjectsByStatus(Project.projectStatus.AVAILABLE);
                return;
            case REGISTERED:
                System.out.println("You have been registered a project. You cannot view available projects.");
                return;
            case DEREGISTERED:
                System.out.println("You are not allowed to make selection again as you deregistered your FYP.");
                return;
        }
    }

    public void requestProjectAllocation(int projectID ) {
        Project project = DatabaseProject.getProject(projectID);

        if(this.studentStatus == StudentStatus.DEREGISTERED){
            System.out.println("You have been deregistered. You cannot request for project allocation anymore.");
            return;
        }
        else if (this.studentStatus == StudentStatus.REGISTERED) {
            System.out.println("You have been registered a project.");
            return;
        }
        else if (project == null) {
            System.out.println("Project does not exist, try again.");
            return;
        }
        else if(project.getStatus() == Project.projectStatus.AVAILABLE ){
            Request request = new Request(Request.RequestType.STUDENT_TO_FYPCOORDINATOR, Request.RequestDetails.REGISTRATION, project);
            studentRequests.add(0, request);
            FYPCoordinator fypCoordinator = DatabaseSupervisor.getFYPCoordinator();
            fypCoordinator.coordinatorRequests.add(request);
            project.reserve(this);
            System.out.println("Your request is being processed by FYP Coordinator");
            return;
        }
        else if(project.getStatus() == Project.projectStatus.RESERVED){
            System.out.println("Project currently reserved.");
            return;
        }
        else if(project.getStatus() == Project.projectStatus.ALLOCATED){
            System.out.println("Project has already been allocated.");
        }
        else if(project.getStatus() == Project.projectStatus.UNAVAILABLE){
            System.out.println("Project is unavailable.");
        }
        else{
            System.out.println("Enter valid projectID.");
        }
    }

    public void viewRegisteredProject() {
        if(this.studentStatus == StudentStatus.REGISTERED){
            System.out.println("Your registered project:");
            Project project = DatabaseProject.getProject(projectID);
            project.printProjectInfo(true);
            return;
        }
        else{
            System.out.println("You have not been registered to any project.");
        }
    }

    public void requestTitleChange(String newTitle) {
        if(this.studentStatus == StudentStatus.REGISTERED){
            Project project = DatabaseProject.getProject(projectID);
            Request request = new Request(Request.RequestType.STUDENT_TO_SUPERVISOR, Request.RequestDetails.CHANGE_TITLE, project);
            studentRequests.add(0,request);
            request.setNewTitle(newTitle);
            Supervisor supervisor = project.getSupervisor();
            supervisor.supervisorRequests.add(request);
            System.out.println("Your request is being processed by your supervisor");
            return;
        }

        System.out.println("You have not been registered to any project.");
    }

    public void requestDeregistration() {
        if(this.studentStatus == Student.StudentStatus.NOTREGISTERED){
            System.out.println("You have not been registered to any project.");
            return;
        }
        Project project = DatabaseProject.getProject(this.projectID);
        studentRequests.add(0,new Request(Request.RequestType.STUDENT_TO_FYPCOORDINATOR, Request.RequestDetails.DEREGISTER, project));
        FYPCoordinator fypCoordinator = DatabaseSupervisor.getFYPCoordinator();
        fypCoordinator.coordinatorRequests.add(studentRequests.get(0));
        System.out.println("Your request is being processed by the FYP Coordinator");
    }

    public void viewRequestHistory() {
        Request r;
        if (studentRequests.size() == 0) {
            System.out.println("You have not made any requests.");
            return;
        }
        for(int i=0; i<studentRequests.size(); i++){
            r = studentRequests.get(i);
            r.printRequest();
        }
    }

    public void StudentPage() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("|                   WELCOME TO STUDENT PAGE!                    |");
        Scan sc = Scan.getInstance();
        int choice;

        Boolean logout = false;
        while (!logout) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|                  What would you like to do?                   |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("|               1. View all available projects                  |");
            System.out.println("|                2. Request project allocation                  |");
            System.out.println("|                 3. View registered project                    |");
            System.out.println("|               4. Request project title change                 |");
            System.out.println("|              5. Request project deregistration                |");
            System.out.println("|             6. View request history and status                |");
            System.out.println("|                      7. Change password                       |");
            System.out.println("|                         8. Log out                            |");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            choice = sc.nextInt();
            sc.nextLine();
            switch(choice) {
                case 1:
                    this.viewAvailableProjects();
                    break;
                case 2: 
                    int projectID;
                    if (this.getStudentStatus() == StudentStatus.REGISTERED) {
                        System.out.println("You have been registered a project.");
                        break;
                    }
                    System.out.println("Enter projectID to request allocation for project.");
                    projectID = sc.nextInt();
                    sc.nextLine();
                    this.requestProjectAllocation(projectID);
                    break;
                case 3:
                    this.viewRegisteredProject();
                    break;
                case 4:
                    if(this.getStudentStatus() != Student.StudentStatus.REGISTERED){
                        System.out.println("You have not been registered to any project.");
                        break;
                    }
                    int projectid = this.getProjectID();
                    Project project = DatabaseProject.getProject(projectid);
                    project.printProjectInfo(true);
                    System.out.println("Enter new title name: ");
                    String title = sc.nextLine();
                    this.requestTitleChange(title);
                    break;
                case 5:
                    if (this.getStudentStatus() == Student.StudentStatus.DEREGISTERED) {
                        System.out.println("You have already been deregistered.");
                        break;
                    }
                    this.requestDeregistration();
                    break;
                case 6:
                    this.viewRequestHistory();
                    break;
                case 7:
                    
                    this.changePassword();
                    break;
                case 8:
                    logout = true;    
                    System.out.println("Logged out succesfully");
                    break;
                default:
                    System.out.println("choice not found, try again");
            }
        }
    }
}    
