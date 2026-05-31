package application;

public class ProviderClient {
	private int id;
    private int provider_id;
    private int client_id;
    private double price;
    private String service;
    private String description;
    private String start_date;
    private String end_date;
    
    public ProviderClient(int id, int id1, int id2, String service, String description, double price, String start_date, String end_date) {
        this.id = id;
        provider_id = id1;
        client_id = id2;
        this.service = service;
        this.description = description;
        this.price = price;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setServices(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_at) {
        this.start_date = start_at;
    }
    
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_at) {
        this.end_date = end_at;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getProvider_Id() {
        return provider_id;
    }
    public void setProvider_Id(int id) {
        this.provider_id = id;
    }
    
    public int getClient_Id() {
        return client_id;
    }
    public void setClient_Id(int id) {
        this.client_id = id;
    }
    
}
