package com.example.toytrader;

public class Toy {

    public Toy(String id, String name, String description, Double cost, String image, String location, String userID, String category) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.image = image;
        this.location = location;
        this.userID = userID;
        this.toyID = id;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToyID() {
        return toyID;
    }

    public void setToyID(String id) {
        this.toyID = id;
    }

    private String toyID;
    private String name;
    private String description;
    private Double cost;
    private String image;
    private String location;
    private String userID;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;



    public Toy(){

    }

}
