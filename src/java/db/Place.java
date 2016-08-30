/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Comparator;


public class Place implements Serializable{
    
    private String state = null;
    private String region = null;
    private String city = null;
    public double d;

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
     * Crea la stringa nel formato utilizzato anche per le query nel database.
     * È consigliato utilizzare questo metodo per nelle query.
     * @return 
     */
    @Override
    public String toString(){
        String r = "";
        if(state != null){
            r += state + " ";
        }
        if(region != null){
            r += region + " ";
        }
        if(city != null){
            r += city;
        }
        return r.trim();
    }
    
    
    /**
     * Classe che implementa l'interfaccia Comparator per poter ordinare gli oggetti di tipo Place all'interno della priority queue
     */
    public static class PlaceComparator implements Comparator<Place> {
        @Override
        public int compare(Place x, Place y){
            return (int)Math.signum(y.d - x.d);
        }
    }
    
}
