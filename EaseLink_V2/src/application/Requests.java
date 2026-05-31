package application;

public class Requests {
    private int requestId;         
    private int clientId;           
    private String serviceRequested; 
    private String requestedDate;    
    private String completionDate;   
    private String address; 
    private double estimatedPrice;   

    // Constructor
    public Requests(int requestId, int clientId, String serviceRequested, String requestedDate, String completionDate, String location, double estimatedPrice) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.serviceRequested = serviceRequested;
        this.requestedDate = requestedDate;
        this.completionDate = completionDate;
        this.address = location;
        this.estimatedPrice = estimatedPrice;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getServiceRequested() {
        return serviceRequested;
    }

    public void setServiceRequested(String serviceRequested) {
        this.serviceRequested = serviceRequested;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAdress(String location) {
        this.address = location;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
}