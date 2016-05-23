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
public class User implements Serializable{
    
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private USER_TYPE type;

    
    
    public enum USER_TYPE {U, A, R};
    
    public User() {}

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
    
    
    /**
     * @return the type
     */
    public USER_TYPE getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(USER_TYPE type) {
        this.type = type;
    }
    
    public static USER_TYPE toType(char c){
        switch(c){
            case 'u':
                return USER_TYPE.U;
            case 'a':
                return USER_TYPE.A;
            case 'r':
                return USER_TYPE.R;
            default:
                return null;
        }
    }
    
    
    public static char toChar(USER_TYPE t){
        switch(t){
            case U:
                return 'u';
            case A:
                return 'a';
            case R:
                return 'r';
            default:
                return 'u';
        }
    }

}
