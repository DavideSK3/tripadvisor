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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
     * @param email la mail dell'utente
     * @param password la password
     * @return null se l'utente non è autenticato, un oggetto User se l'utente esiste ed è autenticato
     */

    public User authenticate(String email, String password) throws SQLException {
         // usare SEMPRE i PreparedStatement, anche per query banali. 
        // *** MAI E POI MAI COSTRUIRE LE QUERY CONCATENANDO STRINGHE !!!! ***
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.users WHERE email = ? AND password = ?");
        try {
            stm.setString(1, email);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(rs.getString("email"));
                    user.setType(User.toType(rs.getString("type").charAt(0)));
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
    
    public void changePassword(int id, String new_password) throws SQLException{
        PreparedStatement stm = con.prepareStatement("UPDATE APP.users SET password = ? WHERE id = ?");
        try {
            stm.setInt(2, id);
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
            stm.setTimestamp(3, new Timestamp(expTime));
            
            int rs = stm.executeUpdate();
            //TODO -fare qualcosa??
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public User getUserByToken(String token) throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT id, name, surname, email, type " + 
                                                     "FROM APP.tokens JOIN APP.users USING (id) " +
                                                     "WHERE token = ? AND expiration_time > ?");
        try {
            stm.setString(1, token);
            stm.setTimestamp(2, new Timestamp(new Date().getTime()));
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(rs.getString("email"));
                    user.setType(User.toType(rs.getString("type").charAt(0)));
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
        PreparedStatement stm = con.prepareStatement("SELECT id, name, surname, type FROM APP.users WHERE email = ?");
        try {
            stm.setString(1, mail);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(mail);
                    user.setType(User.toType(rs.getString("type").charAt(0)));
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    return null;
                }
            } finally {
                // ricordarsuser.setType(rs.getString("type").charAt(0));i SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public void registerUser(String name, String surname, String email, String password)throws SQLException{
        //TODO - pasare come argomento un oggetto USER, non i vari attributi
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.users VALUES(default,?,?,?,?,'u')");
        try {
            stm.setString(1, name);
            stm.setString(2, surname);
            stm.setString(3, password);
            stm.setString(4, email);
            
            int rs = stm.executeUpdate();
            //TODO -fare qualcosa??
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    
    public void registerRestaurant(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.resturants VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        
        PreparedStatement stm2 = con.prepareStatement("INSERT INTO APP.names_term VALUES(?, ?)");
        
        try {
            stm.setString(1, r.getName());
            stm.setString(2, r.getDescription());
            stm.setString(3, r.getUrl());
            if(r.getGlobal_review()!= null){
                stm.setDouble(4, r.getGlobal_review());
            }else{
                stm.setNull(4, Types.DOUBLE);
            }
            if(r.getFood_review() != null){
                stm.setDouble(5, r.getFood_review());
            }else{
                stm.setNull(5, Types.DOUBLE);
            }
            if(r.getAtmposhpere_review()!=null){
                stm.setDouble(6, r.getAtmposhpere_review());
            }else{
                stm.setNull(6, Types.DOUBLE);
            }
            if(r.getService_review() != null){
                stm.setDouble(7, r.getService_review());
            }else{
                stm.setNull(7, Types.DOUBLE);
            }
            if(r.getMoney_review() != null){
                stm.setDouble(8, r.getMoney_review());
            }else{
                stm.setNull(8, Types.DOUBLE);
            }
            if(r.getId_owner() != null){
                stm.setInt(9, r.getId_owner());
            }else{
                stm.setNull(9, Types.INTEGER);
            }
            if(r.getId_creator() != null){
                stm.setInt(10, r.getId_creator());
            }else{
                stm.setNull(10, Types.INTEGER);
            }
            stm.setString(11, r.getAddress());
            
            if(r.getLatitude() != null){
                stm.setDouble(12, r.getLatitude());
            }else{
                stm.setNull(12, Types.DOUBLE);
            }
            
            if(r.getLongitude() != null){
                stm.setDouble(13, r.getLongitude());
            }else{
                stm.setNull(13, Types.DOUBLE);
            }
            
            if(r.getMin_price() != null){
                stm.setDouble(14, r.getMin_price());
            }else{
                stm.setNull(14, Types.DOUBLE);
            }
            if(r.getMax_price() != null){
                stm.setDouble(15, r.getMax_price());
            }else{
                stm.setNull(15, Types.DOUBLE);
            }
            int rs = stm.executeUpdate();
            
            
            if(rs > 0){
                Set<String> terms = StringDistanceUtil.generateNearTerms(r.getName());
                for(String t : terms){
                    
                    stm2.setInt(1, r.getId());
                    stm2.setString(2, t);
                    stm2.executeUpdate();
                    
                }
            }
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public Restaurant getRestaurant(int key) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants WHERE id = ?");
        try {
            stm.setInt(1, key);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    Restaurant r = new Restaurant();
                    
                    
                    double count = rs.getInt("review_counter");
                    
                    r.setId(key);
                    r.setName(rs.getString("name"));
                    r.setDescription(rs.getString("description"));
                    r.setUrl(rs.getString("web_site_url"));
                    if(count > 0){
                        r.setGlobal_review(rs.getDouble("global_review")/count);
                        r.setFood_review(rs.getDouble("food_review")/count);
                        r.setAtmposhpere_review(rs.getDouble("atmosphere_review")/count);
                        r.setMoney_review(rs.getDouble("value_for_money_review")/count);
                    }else{
                        r.setGlobal_review(.0);
                        r.setFood_review(.0);
                        r.setAtmposhpere_review(.0);
                        r.setMoney_review(.0);
                    }
                    r.setId_owner(rs.getInt("id_owner") > 0 ? rs.getInt("id_owner") : null);
                    r.setId_creator(rs.getInt("id_creator") >  0 ? rs.getInt("id_creator") : null);
                    r.setAddress(rs.getString("address"));
                    r.setLatitude(rs.getDouble("latitude") > 0 ? rs.getDouble("latitude") : null);
                    r.setLongitude(rs.getDouble("longitude") > 0 ? rs.getDouble("longitude") : null);
                    r.setMin_price(rs.getInt("min_price") > 0 ? rs.getInt("max_price") : null);
                    r.setMax_price(rs.getInt("max_price") > 0 ? rs.getInt("max_price") : null);
                    
                    
                    return r;
                } else {
                    return null;
                }
            } finally {
                // ricordarsuser.setType(rs.getString("type").charAt(0));i SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public List<Restaurant> getRestaurantsByName(String name) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants WHERE name = ?");
        try {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            try {
                ArrayList<Restaurant> r_l = new ArrayList<>();
                while(rs.next()) {
                    Restaurant r = new Restaurant();
                    
                    
                    double count = rs.getInt("review_counter");
                    
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    r.setDescription(rs.getString("description"));
                    r.setUrl(rs.getString("web_site_url"));
                    if(count > 0){
                        r.setGlobal_review(rs.getDouble("global_review")/count);
                        r.setFood_review(rs.getDouble("food_review")/count);
                        r.setAtmposhpere_review(rs.getDouble("atmosphere_review")/count);
                        r.setMoney_review(rs.getDouble("value_for_money_review")/count);
                    }else{
                        r.setGlobal_review(.0);
                        r.setFood_review(.0);
                        r.setAtmposhpere_review(.0);
                        r.setMoney_review(.0);
                    }
                    r.setId_owner(rs.getInt("id_owner") > 0 ? rs.getInt("id_owner") : null);
                    r.setId_creator(rs.getInt("id_creator") >  0 ? rs.getInt("id_creator") : null);
                    r.setAddress(rs.getString("address"));
                    r.setLatitude(rs.getDouble("latitude") > 0 ? rs.getDouble("latitude") : null);
                    r.setLongitude(rs.getDouble("longitude") > 0 ? rs.getDouble("longitude") : null);
                    r.setMin_price(rs.getInt("min_price") > 0 ? rs.getInt("max_price") : null);
                    r.setMax_price(rs.getInt("max_price") > 0 ? rs.getInt("max_price") : null);
                    
                    
                    r_l.add(r);
                }
                return r_l;
            } finally {
                // ricordarsuser.setType(rs.getString("type").charAt(0));i SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    public List<Restaurant> getRestaurants(String name, String place) throws SQLException {
        
        return null;
    }
    
    public List<Restaurant> getRestaurantsByPlace(String place) throws SQLException {
        
        return null;
    }
    
    public List<String> getRestaurantsNamesByTerm(String term, int limit) throws SQLException {
        
        List<String> result = new ArrayList<String>();
        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.restaurants WHERE name LIKE '%"+term+"%'");
        try {
                //stm.setString(1, term);
                ResultSet rs = stm.executeQuery();
                try{
                    int count = 0;
                    while(rs.next() && count++ < limit) {
                        result.add(rs.getString("name"));
                    }
                }finally {
                    rs.close();
                }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return result;
        
    }
    
    public List<Restaurant> getRestaurantsByNameSimilarity(String name) throws SQLException {
        Set<String> terms = StringDistanceUtil.generateNearTerms(name);
        Set<Integer> ids = new HashSet<Integer>();
        PreparedStatement stm;
        for(String t : terms){
            stm = con.prepareStatement("SELECT id FROM APP.names_term WHERE term = ?");
            try {
                    stm.setString(1, t);
                    ResultSet rs = stm.executeQuery();
                    try{
                        while(rs.next()) {
                            ids.add(rs.getInt("id"));
                        }
                    }finally {
                        rs.close();
                    }
            } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
                stm.close();
            }
        }
        ArrayList<Restaurant> restaurants = new ArrayList<>(ids.size());
        for(Integer id : ids){
            restaurants.add(getRestaurant(id));
        }
        return restaurants;
        
    }
    
    public List<Restaurant> getRestaurantsByNameSimilarity2(String name) throws SQLException {
        ArrayList<Restaurant> r_l = new ArrayList<>();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants");
        try {
                ResultSet rs = stm.executeQuery();
                try{
                    while(rs.next()) {
                        String r_name = rs.getString("name");
                        if(StringDistanceUtil.editDistanceLimited(name, r_name, 4) < 4){
                            Restaurant r = new Restaurant();


                            double count = rs.getInt("review_counter");

                            r.setId(rs.getInt("id"));
                            r.setName(r_name);
                            r.setDescription(rs.getString("description"));
                            r.setUrl(rs.getString("web_site_url"));
                            if(count > 0){
                                r.setGlobal_review(rs.getDouble("global_review")/count);
                                r.setFood_review(rs.getDouble("food_review")/count);
                                r.setAtmposhpere_review(rs.getDouble("atmosphere_review")/count);
                                r.setMoney_review(rs.getDouble("value_for_money_review")/count);
                            }else{
                                r.setGlobal_review(.0);
                                r.setFood_review(.0);
                                r.setAtmposhpere_review(.0);
                                r.setMoney_review(.0);
                            }
                            r.setId_owner(rs.getInt("id_owner") > 0 ? rs.getInt("id_owner") : null);
                            r.setId_creator(rs.getInt("id_creator") >  0 ? rs.getInt("id_creator") : null);
                            r.setAddress(rs.getString("address"));
                            r.setLatitude(rs.getDouble("latitude") > 0 ? rs.getDouble("latitude") : null);
                            r.setLongitude(rs.getDouble("longitude") > 0 ? rs.getDouble("longitude") : null);
                            r.setMin_price(rs.getInt("min_price") > 0 ? rs.getInt("max_price") : null);
                            r.setMax_price(rs.getInt("max_price") > 0 ? rs.getInt("max_price") : null);


                            r_l.add(r);
                        }
                    }
                }finally {
                    rs.close();
                }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return r_l;
        
    }
    
    public void genereteNearNameTerms() throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT id, name FROM APP.restaurants");
        ArrayList<Restaurant> restaurants = new ArrayList();
        try{
            ResultSet rs = stm.executeQuery();
            try{
                while(rs.next()){
                    Restaurant r = new Restaurant();
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    restaurants.add(r);
                }
            } finally {
                rs.close();
            }
            
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
        for(Restaurant r : restaurants){
            Set<String> terms = StringDistanceUtil.generateNearTerms(r.getName());
            for(String t : terms){
                stm = con.prepareStatement("INSERT INTO APP.names_term VALUES(?, ?)");
                try{
                    stm.setInt(1, r.getId());
                    stm.setString(2, t);
                    stm.executeUpdate();
                }finally{
                    stm.close();
                }
            }
        }
    }
    
    
}
                       