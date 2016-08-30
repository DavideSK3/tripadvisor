/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;


public class NotificaReclami {
    
    
    private String restaurant_name;
    private Integer id_restaurant;
    private Integer id_user;
    private String name;
    private String surname;
    private String email;
    
    
    public NotificaReclami(String restaurant_name, Integer id_restaurant, Integer id_user, String name, String surname, String email) { 
        this.restaurant_name = restaurant_name;
        this.id_restaurant = id_restaurant;
        this.id_user = id_user;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    /**
     * @return the restaurant_name
     */
    public String getRestaurant_name() {
        return restaurant_name;
    }

    /**
     * @param restaurant_name the restaurant_name to set
     */
    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
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
     * @return the id_user
     */
    public Integer getId_user() {
        return id_user;
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
}
