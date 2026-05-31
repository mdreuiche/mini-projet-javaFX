package application;

public class Clients {
	 	private int id;
	    private String name;
	    private String email;
	    private String phone;
	    private String serviceRequested;
	    private String description;
	    private String address;
	    private String addedDate;

	    // Constructor
	    public Clients(int id, String name, String email, String phone, String serviceRequested, String description, String address, String addedDate) {
	        this.id = id;
	        this.name = name;
	        this.email = email;
	        this.phone = phone;
	        this.serviceRequested = serviceRequested;
	        this.description = description;
	        this.address = address;
	        this.addedDate = addedDate;
	    }
	    //Constructor 2
	    public Clients(int id, String name) {
	        this.id = id;
	        this.name = name;
	        this.email = null;
	        this.phone = null;
	        this.serviceRequested = null;
	        this.description = null;
	        this.address = null;
	        this.addedDate = null;
	    }
	    

	    // Getters and Setters
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getServiceRequested() {
	        return serviceRequested;
	    }

	    public void setServiceRequested(String serviceRequested) {
	        this.serviceRequested = serviceRequested;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public String getAddedDate() {
	        return addedDate;
	    }

	    public void setAddedDate(String addedDate) {
	        this.addedDate = addedDate;
	    }
}
