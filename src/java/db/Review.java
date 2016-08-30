/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable{
    private Integer global_value;
    private Integer food;
    private Integer service;
    private Integer value_for_money;
    private Integer atmosphere;
    private String title;
    private String description;
    private Date creation;
    private Integer id_restaurant;
    private Integer id_creator;
    private Integer id_photo;
    private String author;
    
    private String photoPath;
    private String photoName;

    /**
     * @return the global_value
     */
    public Integer getGlobal_value() {
        return global_value;
    }

    /**
     * @param global_value the global_value to set
     */
    public void setGlobal_value(Integer global_value) {
        this.global_value = global_value;
    }

    /**
     * @return the food
     */
    public Integer getFood() {
        return food;
    }

    /**
     * @param food the food to set
     */
    public void setFood(Integer food) {
        this.food = food;
    }

    /**
     * @return the service
     */
    public Integer getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(Integer service) {
        this.service = service;
    }

    /**
     * @return the value_for_money
     */
    public Integer getValue_for_money() {
        return value_for_money;
    }

    /**
     * @param value_for_money the value_for_money to set
     */
    public void setValue_for_money(Integer value_for_money) {
        this.value_for_money = value_for_money;
    }

    /**
     * @return the atmosphere
     */
    public Integer getAtmosphere() {
        return atmosphere;
    }

    /**
     * @param atmosphere the atmosphere to set
     */
    public void setAtmosphere(Integer atmosphere) {
        this.atmosphere = atmosphere;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the creation
     */
    public Date getCreation() {
        return creation;
    }

    /**
     * @param creation the creation to set
     */
    public void setCreation(Date creation) {
        this.creation = creation;
    }

    /**
     * @return the id_restaurant
     */
    public Integer getId_restaurant() {
        return id_restaurant;
    }

    /**
     * @param id_restaurant the id_restaurant to set
     */
    public void setId_restaurant(Integer id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    /**
     * @return the id_creator
     */
    public Integer getId_creator() {
        return id_creator;
    }

    /**
     * @param id_creator the id_creator to set
     */
    public void setId_creator(Integer id_creator) {
        this.id_creator = id_creator;
    }

    /**
     * @return the id_photo
     */
    public Integer getId_photo() {
        return id_photo;
    }

    /**
     * @param id_photo the id_photo to set
     */
    public void setId_photo(Integer id_photo) {
        this.id_photo = id_photo;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the photo
     */
    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * @param photo the photo to set
     */
    public void setPhotoPath(String photo) {
        this.photoPath = photo;
    }

    /**
     * @return the photoName
     */
    public String getPhotoName() {
        return photoName;
    }

    /**
     * @param photoName the photoName to set
     */
    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
    
}
