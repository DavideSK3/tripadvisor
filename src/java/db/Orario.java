/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.sql.Time;

/**
 *
 * @author gabriele
 */
public class Orario implements Serializable{
    
    public static final String[] days = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};

    public Orario(String giorno, Time apertura, Time chiusura) {
        this.giorno = giorno;
        this.apertura = apertura;
        this.chiusura = chiusura;
    }
    
    public Orario(int giorno, Time apertura, Time chiusura) {
        this.giorno = days[giorno];
        this.apertura = apertura;
        this.chiusura = chiusura;
    }
    
    private String giorno;
    private Time apertura;
    private Time chiusura;

    /**
     * @return the giorno
     */
    public String getGiorno() {
        return giorno;
    }

    /**
     * @param giorno the giorno to set
     */
    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }
    
    public void setGiorno(int giorno) {
        this.giorno = days[giorno];
    }

    /**
     * @return the apertura
     */
    public Time getApertura() {
        return apertura;
    }

    /**
     * @param apertura the apertura to set
     */
    public void setApertura(Time apertura) {
        this.apertura = apertura;
    }

    /**
     * @return the chiusura
     */
    public Time getChiusura() {
        return chiusura;
    }

    /**
     * @param chiusura the chiusura to set
     */
    public void setChiusura(Time chiusura) {
        this.chiusura = chiusura;
    }
    
    public String getChiusuraString(){
        return chiusura.toString().substring(0, 5);
    }
    public String getAperturaString(){
        return apertura.toString().substring(0, 5);
    }
    
}
