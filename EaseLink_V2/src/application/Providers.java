package application;



public class Providers {
	private int id;
    private String name;
    private double price;
    private String services;
    private String description;
    private String added_date;
    private String phone;
    private String email;
 
 
    //Constructor
    public Providers(int id, String name, String email, String phone, String services, String description, double price, String added_date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.services = services;
        this.description = description;
        this.price = price;
        this.added_date = added_date;
    }
    
    //Constructor 2
    public Providers(int id, String name,String services, double price) {
        this.id = id;
        this.name = name;
        this.email = null;
        this.phone = null;
        this.services = services;
        this.description = null;
        this.price = price;
        this.added_date = null;
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
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getAdded_date() {
        return added_date;
    }
    public void setAdded_date(String added_at) {
        this.added_date = added_at;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}
