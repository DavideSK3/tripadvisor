/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author gabriele
 */
public class NotificaFoto {
    
    

    public NotificaFoto(String name, Integer id_restaurant, Integer id_photo, String path, String photo_name) {
        this.name = name;
        this.id_restaurant = id_restaurant;
        this.id_photo = id_photo;
        this.path = path;
        this.photo_name = photo_name;
    }
    
    private String name;
    private Integer id_restaurant;
    private Integer id_photo;
    private String path;
    private String photo_name;
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
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the photo_name
     */
    public String getPhoto_name() {
        return photo_name;
    }

    /**
     * @param photo_name the photo_name to set
     */
    public void setPhoto_name(String photo_name) {
        this.photo_name = photo_name;
    }
    
}
