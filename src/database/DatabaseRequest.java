package database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import project_request.Project;
import project_request.Request;

import java.util.ArrayList;

public class DatabaseRequest {
    private static ArrayList<Request> requests = new ArrayList<>();

    public static void addRequest(Request request){
        requests.add(request);
    }

    public static void readRequestCSV(String loc) {
        String line = "";
        String splitBy = ",";
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                DatabaseStudent.class.getResourceAsStream(loc)))) {
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] readLine = line.split(splitBy, -1);
                Integer requestID = Integer.parseInt(readLine[0].trim());
                Request.RequestType requestType = Request.RequestType.valueOf(readLine[1].trim());
                Request.RequestDetails details = Request.RequestDetails.valueOf(readLine[2].trim());
                Request.RequestStatus status = Request.RequestStatus.valueOf(readLine[3].trim());
                Project project = DatabaseProject.getProjectByID(Integer.parseInt(readLine[4].trim()));
                String optional = readLine[5].trim();
                Request request = new Request(requestType, details, project, status, requestID);
                if(details == Request.RequestDetails.CHANGE_TITLE)
                    request.setNewTitle(optional);
                if(details == Request.RequestDetails.TRANSFER_STUDENT)
                    request.setReplacementSupervisorID(optional);
                if(requestType == Request.RequestType.STUDENT_TO_FYPCOORDINATOR){
                    DatabaseSupervisor.getFYPCoordinator().coordinatorRequests.add(request);
                    if(project.getStudent() != null)
                        project.getStudent().addRequest(request);
                }
                if(requestType == Request.RequestType.STUDENT_TO_SUPERVISOR){
                    project.getSupervisor().addRequest(request);
                    if(project.getStudent() != null)
                        project.getStudent().addRequest(request);
                }
                if(requestType == Request.RequestType.SUPERVISOR_TO_FYPCOORDINATOR){
                    DatabaseSupervisor.getFYPCoordinator().coordinatorRequests.add(request);
                    project.getSupervisor().addRequest(request);
                }

            }
        } catch (IOException e) {
            System.out.println("Could not read file");
            e.printStackTrace();
        }
    }

    public static void updateRequestCSV(String loc){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseRequest.class.getResource(loc).getFile()));
            writer.write("requestID,requestType,requestDetails,requestStatus,requestProjectID,optional");
            writer.newLine();
            for (Request request : requests) {
                String line = Integer.toString(request.getRequestID());
                line += "," + request.getRequestType();
                line += "," + request.getDetails();
                line += "," + request.getStatus();
                line += "," + request.getProject().getProjectId() + ",";
                if(request.getDetails() == Request.RequestDetails.CHANGE_TITLE)
                    line += request.getNewTitle();
                if(request.getDetails() == Request.RequestDetails.TRANSFER_STUDENT)
                    line += request.getReplacementSupervisorID();
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
