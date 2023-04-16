package project_request;
import user.*;
public class Project {
    public enum projectStatus {AVAILABLE, RESERVED, UNAVAILABLE, ALLOCATED};
    private Supervisor supervisor;
    private Student student;
    private int projectID;
    private String title;
    private projectStatus status;
    public static int projectIdCount = 0;

    public Project(String projecttitle, Supervisor supervisor) {
        this.supervisor = supervisor;
        this.student = null;
        this.projectID = ++projectIdCount;
        this.title = projecttitle;
        this.status = projectStatus.AVAILABLE;
    }

    public Project(String projecttitle, Supervisor supervisor, Student student, Integer projectID, projectStatus status) {
        this.supervisor = supervisor;
        this.student = student;
        this.projectID = projectID;
        this.title = projecttitle;
        this.status = status;
        projectIdCount++;
    }

    public void printProjectInfo(Boolean isStudent) {
        if (this.status == projectStatus.AVAILABLE) {
            System.out.println("Project title: " + this.title);
            System.out.println("Project ID: " + this.projectID);
            System.out.println("Project status: " + this.status);
            System.out.println("Supervisor name: " + this.supervisor.getName());
            System.out.println("Supervisor name: " + this.supervisor.getEmail());
            System.out.println("");
        }
        else if(this.status == projectStatus.ALLOCATED || this.status == projectStatus.RESERVED) {
            System.out.println("Project title: " + this.title);
            System.out.println("Project ID: " + this.projectID);
            System.out.println("Project status: " + this.status);
            System.out.println("Supervisor name: " + this.supervisor.getName());
            System.out.println("Supervisor name: " + this.supervisor.getEmail());
            System.out.println("Student ID: " + this.student.getUserID());
            System.out.println("Student Email: " + this.student.getEmail());
            System.out.println("");
        }
        else if (!isStudent) {
            System.out.println("Project title: " + this.title);
            System.out.println("Project ID: " + this.projectID);
            System.out.println("Project status: " + this.status);
            System.out.println("Supervisor name: " + this.supervisor.getName());
            System.out.println("Supervisor name: " + this.supervisor.getEmail());
            System.out.println("");
        }
        else
            System.out.println("Project Unavailable");
        System.out.println("");

    }

    public void reserve(Student student) {
        this.student = student;
        this.status = projectStatus.RESERVED;
    }

    public void allocate() {
        if (this.status == projectStatus.RESERVED) {
            this.status = projectStatus.ALLOCATED;
            student.setStudentStatus(Student.StudentStatus.REGISTERED);
            student.setProjectID(projectID);
        }
    }

    public void deregister() {
        if (this.status == projectStatus.ALLOCATED) {
            this.status = projectStatus.AVAILABLE;
            student.setStudentStatus(Student.StudentStatus.DEREGISTERED);
            student.setProjectID(-1);
            this.student = null;
        }
    }

    public projectStatus getStatus() {
        return status;
    }

    public Student getStudent() {
        return this.student;
    }

    public Supervisor getSupervisor() {
        return this.supervisor;
    }

    public int getProjectId() {
        return this.projectID;
    }

    public String getTitle(){
        return this.title;
    }

    public void setStatus(projectStatus status) {
        this.status = status;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
