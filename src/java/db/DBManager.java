/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author gabriele
 */
public class DBManager implements Serializable{
    
    private transient Connection con;
    
    public DBManager(String dburl) throws SQLException {

        try {

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());

        } catch(Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
        
        Connection con = DriverManager.getConnection(dburl, "db_manager", "tripadvisor");
        //System.out.println(dburl);
        this.con = con;

    }


    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).info(ex.getMessage());
        }
    }
    
    /**
     * Autentica un utente in base a un nome utente e a una password
     * 
     * @param username il nome utente
     * @param password la password
     * @return null se l'utente non è autenticato, un oggetto User se l'utente esiste ed è autenticato
     */

    public User authenticate(String username, String password) throws SQLException {
         // usare SEMPRE i PreparedStatement, anche per query banali. 
        // *** MAI E POI MAI COSTRUIRE LE QUERY CONCATENANDO STRINGHE !!!! ***
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.users WHERE username = ? AND password = ?");
        try {
            stm.setString(1, username);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(username);
                    user.setFullname(rs.getString("fullname"));
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public void changePassword(String username, String new_password) throws SQLException{
        PreparedStatement stm = con.prepareStatement("UPDATE APP.users SET password = ? WHERE username = ?");
        try {
            stm.setString(2, username);
            stm.setString(1, new_password);
            
            int rs = stm.executeUpdate();
            //TODO -fare qualcosa??
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
    }
    
    public void insertToken(int id, String token, int expirationTime) throws SQLException{
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.tokens VALUES(?, ?, ?)");
        try {
            stm.setInt(1, id);
            stm.setString(2, token);
            long expTime = new Date().getTime() + expirationTime*60*60*1000;
            stm.setLong(3, expTime);
            
            int rs = stm.executeUpdate();
            //TODO -fare qualcosa??
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public User getUserByToken(String token) throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT id, username, fullname, email " + 
                                                     "FROM APP.tokens JOIN APP.users USING (id) " +
                                                     "WHERE token = ? AND expirationTime > ?");
        try {
            stm.setString(1, token);
            stm.setLong(2, new Date().getTime());
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setMail(rs.getString("email"));
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public User getUserByMail(String mail) throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT id, username, fullname FROM APP.users WHERE email = ?");
        try {
            stm.setString(1, mail);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setMail(mail);
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public User registerUser(String username, String password)throws SQLException{
        
        return null;
    }
    
    public User registerResturant(String name, String password) throws SQLException {
         
        return null;
    }
    
    public Restaurant getRestaurant(int key) throws SQLException {
        
        return null;
    }
    
    public List<Restaurant> getRestaurantByName(String name) throws SQLException {
        
        return null;
    }
    
    public List<Restaurant> getRestaurant(String name, String place) throws SQLException {
        
        return null;
    }
    
    public List<Restaurant> getRestaurantByPlace(String place) throws SQLException {
        
        return null;
    }
    
    
    
}
