import user.*;
import utilities.Scan;
import database.DatabaseSupervisor;
import database.DatabaseStudent;
import database.DatabaseRequest;
import database.DatabaseProject;

public class FYPMS {
    Scan sc = Scan.getInstance();
    public void start() {
        DatabaseStudent.readStudentCSV("../data/student_list.csv");
        DatabaseSupervisor.readSupervisorCSV("../data/supervisor_list.csv");
        DatabaseProject.readProjectCSV("../data/project_list.csv");
        DatabaseRequest.readRequestCSV("../data/request_list.csv");
        
        Boolean end = false;
        int choice;
        Scan sc = Scan.getInstance();
        while (!end) {
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println("|     Welcome to the FYPMS system    |");
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println("|          Login to system:          |");
            System.out.println("|  Please select from these choices: |");
            System.out.println("|            1. Student              |");
            System.out.println("|           2. Supervisor            |");
            System.out.println("|         3. FYPCoordinator          |");
            System.out.println("|             4. Exit                |");
            System.out.println("++++++++++++++++++++++++++++++++++++++");

            choice = sc.nextInt();
            sc.nextLine();
            switch(choice) {
                case 1:
                    System.out.println("Enter username:");
                    String userid = sc.next();
                    while (DatabaseStudent.getStudent(userid) == null) {
                        System.out.println("Username not found. Enter the correct username.");
                        userid = sc.next();                        
                    } 
                    Student student = DatabaseStudent.getStudent(userid);
                    
                    System.out.println("Enter password:");
                    String password = sc.next();
                    if (student.login(password)) {
                        System.out.println("Login Successful\n");
                        //enter student page
                        student.StudentPage();
                    }
                    else {
                        System.out.println("Login Unsuccessful");
                        System.out.println("Try again");
                    }
                    break;
                case 2:
                    System.out.println("Enter username:");
                    userid = sc.next();
                    while(DatabaseSupervisor.getSupervisor(userid) == null){
                        System.out.println("Username not found. Enter the correct username.");
                        userid = sc.next();
                    }
                    
                    Supervisor supervisor = DatabaseSupervisor.getSupervisor(userid);

                    System.out.println("Enter password:");
                    password = sc.next();
                    if (supervisor.login(password)) {
                        System.out.println("Login successful\n");
                        //enter supervisor page
                        supervisor.SupervisorPage();
                    }
                    else {
                        System.out.println("Login unsuccessful");
                        System.out.println("Try again");
                    }
                    break;
                case 3:
                    System.out.println("Enter password:");
                    password = sc.next();
                    FYPCoordinator coordinator = DatabaseSupervisor.getFYPCoordinator();
                    if (coordinator.login(password)) {
                        System.out.println("Login successful\n");
                        //enter coordinator page
                        coordinator.FYPCoordinatorPage();
                    }
                    else {
                        System.out.println("Login unsuccessful");
                        System.out.println("Try again");
                    }
                    break;
                case 4:
                    System.out.println("Exiting");
                    DatabaseProject.updateProjectCSV("../data/project_list.csv");
                    DatabaseStudent.updateStudentCSV("../data/student_list.csv");
                    DatabaseRequest.updateRequestCSV("../data/request_list.csv");
                    DatabaseSupervisor.updateSupervisorCSV("../data/supervisor_list.csv");
                    end = true;
                    break;
                default:
                    System.out.println("choice not found, try again");
            }
        }
        sc.close();
    }
}
