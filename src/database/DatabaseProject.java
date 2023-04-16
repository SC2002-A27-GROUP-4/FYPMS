package database;
import java.util.Collection;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import user.Student;
import user.Supervisor;
import project_request.Project;
import project_request.Project.projectStatus;
public class DatabaseProject {
    private static HashMap<Integer, Project> projectData = new HashMap<>();

    public static Collection<Project> getProjectData() {
        return DatabaseProject.projectData.values();
    }

    public static Project getProject(int projectid ) {
        return projectData.get(projectid);
    }   

    public static void addProject(Project project) {
        projectData.put(project.getProjectId(), project);
    }

    public static void printProjectsByStatus(projectStatus status){
        for (Project p : projectData.values()) {
            if (p.getStatus() == status)
                p.printProjectInfo(false);
        }
    }

    public static void printProjectsBySupervisorID(String SupervisorId){
        Boolean noneprint = true;
        for (Project p: projectData.values()){
            Supervisor s = p.getSupervisor();
            if (s.getUserID().compareTo(SupervisorId) == 0){
                p.printProjectInfo(false);
                noneprint = false;
            }
        }

        if (noneprint) {
            System.out.println("Supervisor not found.");
        }
    }

    public static Project getProjectByID(Integer ID) {
        for (Project p : projectData.values()) {
            if (p.getProjectId() == ID) {
                return p;
            }
        }
        return null;
    }

    public static void printAllProjects(Boolean isStudent){
        for (Project p : projectData.values()) {
            p.printProjectInfo(isStudent);
        }
    }

    public static void readProjectCSV(String loc) {
        String line = "";
        String splitBy = ",";
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            DatabaseStudent.class.getResourceAsStream(loc)))) {
            while ((line = br.readLine()) != null)
            {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] readLine = line.split(splitBy,-1);
                Integer projectID = Integer.parseInt(readLine[0].trim()) ;
                String projectTitle = readLine[1].trim();
                String supervisorName = readLine[2].trim();
                String studentname = readLine[3].trim();
                String status = readLine[4].trim();
                Supervisor supervisor = DatabaseSupervisor.getSupervisorByName(supervisorName);
                Student student = DatabaseStudent.getStudentByName(studentname);
                Project project = new Project(projectTitle, supervisor, student, projectID, projectStatus.valueOf(status));
                addProject(project);
            }
        } catch (IOException e) {
            System.out.println("Could not read file");
            e.printStackTrace();
        }
    }
    
    public static void updateProjectCSV(String loc){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseStudent.class.getResource(loc).getFile()));
            writer.write("ProjectID,Title,Supervisor,Student,Status");
            writer.newLine();
            for (Project p : projectData.values()) {
                String line = Integer.toString(p.getProjectId());
                line += "," + p.getTitle();
                line += "," + p.getSupervisor().getName();
                if(p.getStudent() == null)
                    line += ",";
                else
                    line += "," + p.getStudent().getName();
                line += "," + p.getStatus();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write file");
            e.printStackTrace();
        }
    }

}
