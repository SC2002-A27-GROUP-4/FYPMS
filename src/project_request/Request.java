package project_request;

import database.DatabaseRequest;

public class Request {
    private static Integer requestCounter = 0;
    public enum RequestStatus {
        APPROVED, REJECTED, PENDING
    }

    public enum RequestDetails {
        CHANGE_TITLE, DEREGISTER, TRANSFER_STUDENT, REGISTRATION
    }

    public enum RequestType {
        STUDENT_TO_SUPERVISOR, STUDENT_TO_FYPCOORDINATOR, SUPERVISOR_TO_FYPCOORDINATOR
    }

    private Integer requestID;
    private RequestType requestType;
    private RequestStatus status;
    private RequestDetails details;
    private Project project;
    private String newTitle;
    private String replacementSupervisorID;

    public Request(RequestType requestType, RequestDetails details, Project project) {
        this.requestID = ++requestCounter;
        this.requestType = requestType;
        this.details = details;
        this.status = RequestStatus.PENDING;
        this.project = project;
        DatabaseRequest.addRequest(this);
    }

    public Request(RequestType requestType, RequestDetails details, Project project, RequestStatus status, Integer requestID){
        this.requestID = requestID;
        this.requestType = requestType;
        this.details = details;
        this.status = status;
        this.project = project;
        ++requestCounter;
        DatabaseRequest.addRequest(this);
    }

    public void printRequest() {
        System.out.println("Request ID: " + requestID);
        System.out.println("Request type: " + requestType.toString());
        System.out.println("Request details: " + details.toString());
        System.out.println("Request status: " + status.toString());
        System.out.println("");
    }

    public void approve() {
        this.status = RequestStatus.APPROVED;
    }

    public void reject() {
        this.status = RequestStatus.REJECTED;
    }

    public int getRequestID(){
        return this.requestID;
    }
    
    public RequestStatus getStatus() {
        return this.status;
    }

    public RequestDetails getDetails() {
        return this.details;
    } 
    
    public RequestType getRequestType() {
        return requestType;
    }

    public Project getProject() {
        return project;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public String getReplacementSupervisorID(){
        return this.replacementSupervisorID;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setDetails(RequestDetails details) {
        this.details = details;
    }

    public void setNewTitle(String newtitle) {
        this.newTitle = newtitle;
    }

    public void setReplacementSupervisorID(String id) {
        this.replacementSupervisorID = id;
    }
}
