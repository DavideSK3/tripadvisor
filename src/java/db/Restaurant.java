/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;


public class Restaurant implements Serializable{
    
    private Integer id;
    private String name;
    private String description;
    private String url;
    private Integer review_count;
    private Double global_review;
    private Double atmosphere_review;
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
    
    private String city;
    private String region;
    private String state;
    
    private String qr_path;

    private ArrayList<String> cuisines;
    
    private Photo firstPhoto = null;
    private ArrayList<Photo> photos;
    
    private ArrayList<Orario> orari;
    
    private ArrayList<Review> recensioni;
    
    
    private Integer posizione = null;
    //private Integer numeroTotaleRistoranti = null;
    
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
     * @return the atmosphere_review
     */
    public Double getAtmosphere_review() {
        return atmosphere_review;
    }

    /**
     * @param atmosphere_review the atmposhpere_review to set
     */
    public void setAtmosphere_review(Double atmosphere_review) {
        this.atmosphere_review = atmosphere_review;
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

    /**
     * @return the cuisines
     */
    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    /**
     * @param cuisines the cuisines to set
     */
    public void setCuisines(ArrayList<String> cuisines) {
        this.cuisines = cuisines;
    }

    /**
     * @return the photos
     */
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    /**
     * @param photos the photos to set
     */
    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
    
    public void addPhoto(Photo photo){
        if(photos == null){
            photos = new ArrayList<>();
        }
        photos.add(photo);
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the orari
     */
    public ArrayList<Orario> getOrari() {
        return orari;
    }

    /**
     * @param orari the orari to set
     */
    public void setOrari(ArrayList<Orario> orari) {
        this.orari = orari;
    }
    
    public void addOrario(Orario o){
        if(orari == null){
            orari = new ArrayList<>();
        }
        orari.add(o);
    }

    /**
     * @return the posizione
     */
    public Integer getPosizione() {
        return posizione;
    }

    /**
     * @param posizione the posizione to set
     */
    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    

    /**
     * @return the recensioni
     */
    public ArrayList<Review> getRecensioni() {
        return recensioni;
    }

    /**
     * @param recensioni the recensioni to set
     */
    public void setRecensioni(ArrayList<Review> recensioni) {
        this.recensioni = recensioni;
    }
    
    public void addRecensione(Review r){
        if(recensioni == null){
            recensioni = new ArrayList<>();
        }
        recensioni.add(r);
    }

    /**
     * @return the qr_path
     */
    public String getQr_path() {
        return qr_path;
    }

    /**
     * @param qr_path the qr_path to set
     */
    public void setQr_path(String qr_path) {
        this.qr_path = qr_path;
    }

    /**
     * @return the firstPhoto
     */
    public Photo getFirstPhoto() {
        return firstPhoto;
    }

    /**
     * @param firstPhoto the firstPhoto to set
     */
    public void setFirstPhoto(Photo firstPhoto) {
        this.firstPhoto = firstPhoto;
    }
    
    public Integer getPriceSum(){
        if(getMin_price() == null || getMax_price() == null) return null;
        else return getMin_price()+getMax_price();
    }
    
    /**
     * Metodo che costruisce e salva l'immagine del QR del ristorante identificato da "id".
     * Questo metodo in particolare si occupa di costruire e salvare sul file system l'immagine
     * e poi aggiornare il database inserendone il percorso 
     * @param id
     * @param qrDir - il nome della directory cotenente il QR
     * @param contextPath - il percorso assoluto sul filesystem dell'applicazione
     * @param manager - il DBManager che interfaccia il database
     * @return il percorso per il file contenente il QR se tutte le operazioni sono andate a buon fine, null altrimenti
     */
    public static final String buildQR(int id, String qrDir, String contextPath, DBManager manager){
        
        Restaurant r;
        try {
            r = manager.getRestaurant(id);
            manager.getRestaurantTimes(r);
        } catch (SQLException ex) {
            Logger.getLogger(Restaurant.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        StringBuilder s = new StringBuilder();
        s.append(r.getName())
                .append(":\n")
                .append(r.getAddress())
                .append(", ")
                .append(r.getCity())
                .append(", ")
                .append(r.getRegion())
                .append(", ")
                .append(r.getState())
                .append("\nOrari: \n");
        
        if(r.getOrari() != null){
            for(Orario o : r.getOrari()){
                s.append(" ")
                    .append(o.getGiorno())
                    .append(": ")
                    .append(o.getAperturaString())
                    .append("-")
                    .append(o.getChiusuraString())
                    .append("\n");
            }
        }
        
        
        
        ByteArrayOutputStream out = QRCode.from(s.toString()).to(ImageType.JPG).withSize(150, 150).stream();
        
        File dir = new File(contextPath + qrDir); 

        String name = "r" + r.getId()+ new Date(System.currentTimeMillis()).toString().replaceAll(" ", "") +".qr";

        File file = new File(dir, name);

        try {
                FileOutputStream fout = new FileOutputStream(file);

                fout.write(out.toByteArray());

                fout.flush();
                fout.close();

        } catch (FileNotFoundException e) {
            Logger.getLogger(Restaurant.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } catch (IOException e) {
            Logger.getLogger(Restaurant.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        
        if(r.getQr_path() != null){
            //System.out.println("old (real) path = " + contextPath + r.getQr_path());
            File f = new File(contextPath + r.getQr_path());
            f.delete();
        }
        
        r.setQr_path(qrDir + "/" + name);
        
        /*System.out.println("new (real) path = " + contextPath + r.getQr_path());
        System.out.println("new (relative) path = " + r.getQr_path());
        System.out.println("new (absolute) path = " + file.getAbsolutePath());*/
        
        try {
            manager.setRestaurantQRPath(id, r.getQr_path());
        } catch (SQLException ex) {
            Logger.getLogger(Restaurant.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return r.getQr_path();
        
        
    }
    
    
    public static class ComparatorByValue implements Comparator<Restaurant>{

        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            if(Objects.equals(r2.getGlobal_review(), r1.getGlobal_review())){ return 0;}
            else if(r1.getGlobal_review() == null){
                return 1;
            }else if(r2.getGlobal_review() == null){
                return -1;
            }else{
                return (int)Math.signum(r2.getGlobal_review() - r1.getGlobal_review());
            }
        }
        
    }
    
    public static class ComparatorByPrice implements Comparator<Restaurant>{

        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            if(Objects.equals(r2.getPriceSum(), r1.getPriceSum())){ return 0;}
            else if(r1.getPriceSum() == null){
                return -1;
            }else if(r2.getPriceSum()== null){
                return 1;
            }else{
                return (r1.getPriceSum() - r2.getPriceSum());
            }
        }
        
    }
    
    public static class ComparatorByName implements Comparator<Restaurant>{

        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            return r1.getName().compareTo(r2.getName());
        }
        
    }
}
