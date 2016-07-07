/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;

/**
 *
 * @author gabriele
 */
public class Restaurant implements Serializable{
    
    private Integer id;
    private String name;
    private String description;
    private String url;
    private Integer review_count;
    private Double global_review;
    private Double atmposhpere_review;
    private Double food_review;
    private Double service_review;
    private Double money_review;
    private Integer id_owner;
    private Integer id_creator;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer min_price;
    private Integer max_price;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the global_review
     */
    public Double getGlobal_review() {
        return global_review;
    }

    /**
     * @param global_review the global_review to set
     */
    public void setGlobal_review(Double global_review) {
        this.global_review = global_review;
    }

    /**
     * @return the atmposhpere_review
     */
    public Double getAtmposhpere_review() {
        return atmposhpere_review;
    }

    /**
     * @param atmposhpere_review the atmposhpere_review to set
     */
    public void setAtmposhpere_review(Double atmposhpere_review) {
        this.atmposhpere_review = atmposhpere_review;
    }

    /**
     * @return the food_review
     */
    public Double getFood_review() {
        return food_review;
    }

    /**
     * @param food_review the food_review to set
     */
    public void setFood_review(Double food_review) {
        this.food_review = food_review;
    }

    /**
     * @return the service_review
     */
    public Double getService_review() {
        return service_review;
    }

    /**
     * @param service_review the service_review to set
     */
    public void setService_review(Double service_review) {
        this.service_review = service_review;
    }

    /**
     * @return the money_review
     */
    public Double getMoney_review() {
        return money_review;
    }

    /**
     * @param money_review the money_review to set
     */
    public void setMoney_review(Double money_review) {
        this.money_review = money_review;
    }

    /**
     * @return the id_owner
     */
    public Integer getId_owner() {
        return id_owner;
    }

    /**
     * @param id_owner the id_owner to set
     */
    public void setId_owner(Integer id_owner) {
        this.id_owner = id_owner;
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the min_price
     */
    public Integer getMin_price() {
        return min_price;
    }

    /**
     * @param min_price the min_price to set
     */
    public void setMin_price(Integer min_price) {
        this.min_price = min_price;
    }

    /**
     * @return the max_price
     */
    public Integer getMax_price() {
        return max_price;
    }

    /**
     * @param max_price the max_price to set
     */
    public void setMax_price(Integer max_price) {
        this.max_price = max_price;
    }

    /**
     * @return the review_count
     */
    public Integer getReview_count() {
        return review_count;
    }

    /**
     * @param review_count the review_count to set
     */
    public void setReview_count(Integer review_count) {
        this.review_count = review_count;
    }

    
}
