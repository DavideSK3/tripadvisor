/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author gabriele
 */
public class DBManager implements Serializable {
    
    private static final String duplicateKeyErrorCode = "23505";
    private transient Connection con;
    
    /**
     * Costruisce un'istanza di DBManager usando il database che si trova nel percorso indicato da 'dburl'
     * @param dburl
     * @throws SQLException 
     */
    public DBManager(String dburl) throws SQLException {

        try {
            //Class.forName("org.postgresql.Driver");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());

        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }

        Connection con = DriverManager.getConnection(dburl, "db_manager", "tripadvisor");
        

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
     * --------GESTIONE UTENTE-----------
     */
    
    
    /**
     * Autentica un utente in base a un nome utente e a una password
     *
     * @param email la mail dell'utente
     * @param password la password
     * @return null se l'utente non è autenticato, un oggetto User se l'utente
     * esiste ed è autenticato
     */
    public User authenticate(String email, String password) throws SQLException {
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
    
    /**
     * Cambia la password di un utente
     * @param id - id dell'utente di cui cambiare la password
     * @param new_password - nuova password dell'utente
     * @throws SQLException 
     */
    public void changePassword(int id, String new_password) throws SQLException {
        PreparedStatement stm = con.prepareStatement("UPDATE APP.users SET password = ? WHERE id = ?");
        try {
            stm.setInt(2, id);
            stm.setString(1, new_password);

            int rs = stm.executeUpdate();
        } finally {
            stm.close();
        }

    }

    /**
     * Modifica il profilo di un utente cambiando i valori dei campi della tablla USERS
     * 
     * @param id - l'id dell'utente di cui modificare i dati
     * @param newName - nuovo nome
     * @param newSurname - nuovo cognome
     * @param newEMail - nuova mail
     * @throws SQLException 
     */
    public void editProfile(int id, String newName, String newSurname, String newEMail) throws SQLException {
        StringBuilder builder = new StringBuilder("UPDATE APP.users ");
        
        List<String> sets = new ArrayList<>();
        if(newName != null){
            sets.add("name = '" + newName + "'" );
        }
        if(newSurname != null){
            sets.add("surname = '" + newSurname + "'" );
        }
        if(newEMail != null){
            sets.add("email = '" + newEMail + "'" );
        }
        
        if(sets.size() > 0){
            builder.append("SET ");
            for(int i=0; i<sets.size(); i++){
                if(i>0){
                    builder.append(",");
                }
                builder.append(sets.get(i));
                builder.append(" ");
            }
        }
        builder.append("WHERE ID = ?");
        
        PreparedStatement stm = con.prepareStatement(builder.toString());
        try {
            stm.setInt(1, id);
            int rs = stm.executeUpdate();

        } finally { 
            stm.close();
        }

    }
    
    
    /**
     * Controlla che la password sia corretta
     * 
     * @param id - id dell'utente
     * @param password - password da controllare
     * @return - true se la password è corretta, false altrimenti
     * @throws SQLException 
     */
    public boolean isPasswordCorrect(int id, String password) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.users WHERE id = ? AND password = ?");
        try {
            stm.setInt(1, id);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();
            try {
                return (rs.next());
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    /**
     * Inserisce un nuovo token nella tabella TOKENS per permettere il recupero dell'account all'utente che ha perso la password
     * @param id - id dell'utente che ha perso la password
     * @param token - stringa unica generata casualmente usata per identificare l'utente che ha perso la password
     * @param expirationTime
     * @throws SQLException 
     */
    public void insertToken(int id, String token, int expirationTime) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.tokens VALUES(?, ?, ?)");
        try {
            stm.setInt(1, id);
            stm.setString(2, token);
            long expTime = new Date().getTime() + expirationTime * 60 * 60 * 1000;
            stm.setTimestamp(3, new Timestamp(expTime));

            int rs = stm.executeUpdate();

        } finally { 
            stm.close();
        }
    }
    
    /**
     * rimuove dalla tabella un token
     * @param id
     * @param token
     * @throws SQLException 
     */
    public void removeToken(int id, String token) throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.tokens WHERE id = ? AND token = ?");
        try {
            stm.setInt(1, id);
            stm.setString(2, token);

            int rs = stm.executeUpdate();

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
    /**
     * Cerca nella tabella TOKENS la stringa passata come parametro e restituisce (se trovata) l'utente a cui è stata associata
     * @param token - la stringa unica usata per identificare temporaneamente l'utente
     * @return - l'utente corrispondente se è stata trovata, null altrimenti
     * @throws SQLException 
     */
    public User getUserByToken(String token) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT id, name, surname, email, type "
                + "FROM APP.tokens JOIN APP.users USING (id) "
                + "WHERE token = ? AND expiration_time > ?");
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

    /**
     * Ritorna l'utente identificato da questa mail se esiste (il campo mail nella tabella USERS è UNIQUE)
     * @param mail - indirizzo email dell'utente
     * @return - l'utente che possiede questa mail, se esite, null altrimenti
     * @throws SQLException 
     */
    public User getUserByMail(String mail) throws SQLException {
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
    
    /**
     * Crea un nuovo utente con questi valori e lo inserisce nel database
     * @param name
     * @param surname
     * @param email
     * @param password
     * @throws SQLException 
     */
    public void registerUser(String name, String surname, String email, String password) throws SQLException {
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

    
    /**
     * --------GESTIONE RISTORANTE-----------
     */
    
    /**
     * Inserisce nel database un nuovo ristorante usando i valori contenuti dell'oggetto di tipo Restaurant passato come parametro
     * @param r
     * @throws SQLException 
     */
    public void registerRestaurant(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.resturants VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?)");

        if((r.getLongitude() == null || r.getLatitude() == null) && r.getCity() != null){
            double[] coordinate = Util.getCoordinates(r.getAddress(), r.getCity(), r.getRegion(), r.getState());
            r.setLongitude(coordinate[0]);
            r.setLongitude(coordinate[1]);
        }
        try {
            stm.setString(1, r.getName());
            stm.setString(2, r.getDescription());
            stm.setString(3, r.getUrl());
            if (r.getGlobal_review() != null) {
                stm.setDouble(4, r.getGlobal_review());
            } else {
                stm.setNull(4, Types.DOUBLE);
            }
            
            
            if (r.getId_owner() != null) {
                stm.setInt(5, r.getId_owner());
            } else {
                stm.setNull(5, Types.INTEGER);
            }
            if (r.getId_creator() != null) {
                stm.setInt(6, r.getId_creator());
            } else {
                stm.setNull(6, Types.INTEGER);
            }
            stm.setString(7, r.getAddress());

            if (r.getLatitude() != null) {
                stm.setDouble(8, r.getLatitude());
            } else {
                stm.setNull(8, Types.DOUBLE);
            }

            if (r.getLongitude() != null) {
                stm.setDouble(9, r.getLongitude());
            } else {
                stm.setNull(9, Types.DOUBLE);
            }

            if (r.getMin_price() != null) {
                stm.setDouble(10, r.getMin_price());
            } else {
                stm.setNull(10, Types.DOUBLE);
            }
            if (r.getMax_price() != null) {
                stm.setDouble(11, r.getMax_price());
            } else {
                stm.setNull(11, Types.DOUBLE);
            }
            
            if (r.getQr_path()!= null) {
                stm.setString(12, r.getQr_path());
            } else {
                stm.setNull(12, Types.VARCHAR);
            }
            
            int rs = stm.executeUpdate();

        } finally {
            stm.close();
        }
    }

    /**
     * Ritorna la lista di ristoranti di cui l'utente è in possesso
     * @param id - id dell'utentes
     * @return
     * @throws SQLException 
     */
    public List<Restaurant> getRestaurantsByOwnerID(int id) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants WHERE ID_OWNER = ?");
        try {
            stm.setInt(1, id);

            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    public Restaurant getRestaurant(int key) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE id = ?");
        try {
            stm.setInt(1, key);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    return getRestaurant(rs);
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally { 
            stm.close();
        }
    }

    public List<Restaurant> getRestaurantsByName(String name) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE name = ?");
        try {
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            try {
                ArrayList<Restaurant> r_l = new ArrayList<>();
                while (rs.next()) {
                    r_l.add(getRestaurant(rs));
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
    
    public List<String> getRestaurantsNamesByTerm(String term, int limit) throws SQLException {

        List<String> result = new ArrayList<String>();
        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.restaurants R WHERE lcase(name) LIKE '" + term + "%'");
        try {
            ResultSet rs = stm.executeQuery();

            try {
                int count = 0;
                while (rs.next() && count++ < limit) {
                    result.add(rs.getString("name"));
                }

            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return result;

    }
    
    /**
     * Diversi metodi per ottenere una lista di ristoranti filtrati e ordinati
     * in modo predefinito per la ricerca
     */
    
    /*Metodi di base */
    
    /**
     * Metodo usato per ottenere una lista di ristoranti a parire dai parametri di ricerca
     * @param name
     * @param place
     * @return
     * @throws SQLException 
     */
    public List<Restaurant> getRestaurants(String name, String place) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    public List<Restaurant> getRestaurantsByNameSimilarity(String name) throws SQLException {
        ArrayList<Restaurant> r_l = new ArrayList<>();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R");
        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }
    
    public List<Restaurant> getRestaurantsByPlace(String place) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        try {
            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    
    
    /*Metodi usati per la ricerca normale */
    
    public List<Restaurant> getRestaurantsOrderedBy(String name, String place, String order) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '").append(place).append("%'");
        switch (order) {
            case "name":
                query.append(" ORDER BY name");
                break;
            case "price":
                query.append(" ORDER BY min_price + max_price");
                break;
            case "position":
                query.append(" ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) DESC");
                break;
            default:
                break;
        }
        PreparedStatement stm = con.prepareStatement(query.toString());
        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { 
            stm.close();
        }
    }
    
    public List<Restaurant> getRestaurantsByNameSimilarityOrderedBy(String name, String order) throws SQLException {
        ArrayList<Restaurant> r_l = new ArrayList<>();
        PreparedStatement stm;
        switch (order) {
            case "name":
                stm = con.prepareStatement("SELECT * FROM APP.restaurants R ORDER BY name");
                break;
            case "price":
                stm = con.prepareStatement("SELECT * FROM APP.restaurants R ORDER BY min_price + max_price");
                break;
            case "position":
                stm = con.prepareStatement("SELECT * FROM APP.restaurants R ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) DESC");
                break;
            default:
                stm = con.prepareStatement("SELECT * FROM APP.restaurants R");
                break;
        }
        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }
    
    
    public List<Restaurant> getRestaurantsByPlaceOrderedBy(String place, String order) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '").append(place).append("%'");
        switch (order) {
            case "name":
                query.append(" ORDER BY name");
                break;
            case "price":
                query.append(" ORDER BY min_price + max_price");
                break;
            case "position":
                query.append(" ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) DESC");
                break;
            default:
                break;
        }
        PreparedStatement stm = con.prepareStatement(query.toString());
        try {
            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    
    
    /* Metodi usati per la ricerca avanzata*/
    
    public List<Restaurant> getRestaurantsFilteredOrderedBy(String name, String place, String order,
            Integer minPrice, Integer maxPrice,
            String[] cuisines, ArrayList<Integer> valutazioni
    ) throws SQLException {
        
        
        
        StringBuilder query = new StringBuilder("SELECT DISTINCT R.* FROM APP.restaurants R");
        

        if (cuisines != null && cuisines.length > 0) {
            String cond = " JOIN APP.Restaurants_Cuisines RC ON R.id =  RC.id_restaurant JOIN (SELECT * FROM APP.Cuisines WHERE ";
            for (int i = 0; i < cuisines.length; i++) {
                if (i > 0) {
                    cond += " OR ";
                }
                cond += "name = '" + cuisines[i] + "'";
            }
            cond += ")";

            cond += " C ON RC.id_cuisine = C.id";

            query.append(cond);
        }

        ArrayList<String> conditions = new ArrayList<>();

        
        if(place != null && !place.isEmpty()){
            conditions.add("state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        }
        
        
        if (minPrice != null) {
            conditions.add("min_price >= " + minPrice);
        }
        if (maxPrice != null) {
            conditions.add("max_price <= " + maxPrice);
        }
        if (valutazioni.size() > 0) {
            StringBuilder cond = new StringBuilder();
            for (int i = 0; i < valutazioni.size(); i++) {
            
                if (i > 0) {
                    cond.append(" OR ");
                }
                cond.append("ABS(CAST((CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) as REAL) - ").append(valutazioni.get(i)).append(") <= 0.5");
            }
            
            conditions.add(cond.toString());
        }

        if (conditions.size() > 0) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append("(").append(conditions.get(i)).append(")");
            }
        }
        
        switch (order) {
            case "name":
                query.append(" ORDER BY name");
                break;
            case "price":
                query.append(" ORDER BY min_price + max_price");
                break;
            case "position":
                query.append(" ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) DESC");
                break;
            default:
                break;
        }
        
        PreparedStatement stm = con.prepareStatement(query.toString());
        System.out.println(query.toString());
        try {
            if(name != null && !name.isEmpty()){
                return getRestaurantsFilteredByNameSimilarity(stm, name);
            }else{
                return getRestaurantsUnfiltered(stm);
            }
        } finally { 
            stm.close();
        }

    }
    
    
    public List<Restaurant> getRestaurantsByPlaceFilteredOrderedBy(String place, String order,
            Integer minPrice, Integer maxPrice,
            String[] cuisines, ArrayList<Integer> valutazioni
    ) throws SQLException {
        
        StringBuilder query = new StringBuilder("SELECT DISTINCT R.* FROM APP.restaurants R");
        

        if (cuisines != null && cuisines.length > 0) {
            String cond = " JOIN APP.Restaurants_Cuisines RC ON R.id =  RC.id_restaurant JOIN (SELECT * FROM APP.Cuisines WHERE ";
            for (int i = 0; i < cuisines.length; i++) {
                if (i > 0) {
                    cond += " OR ";
                }
                cond += "name = '" + cuisines[i] + "'";
            }
            cond += ")";

            cond += " C ON RC.id_cuisine = C.id";

            query.append(cond);
        }

        ArrayList<String> conditions = new ArrayList<>();

        conditions.add("state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        if (minPrice != null) {
            conditions.add("min_price >= " + minPrice);
        }
        if (maxPrice != null) {
            conditions.add("max_price <= " + maxPrice);
        }
        if (valutazioni.size() > 0) {
            StringBuilder cond = new StringBuilder();
            for (int i = 0; i < valutazioni.size(); i++) {
                if (i > 0) {
                    cond.append(" OR ");
                }
                cond.append("ABS((CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) - ").append(valutazioni.get(i)).append(") <= 0.5");
            }
            conditions.add(cond.toString());
        }

        if (conditions.size() > 0) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append("(").append(conditions.get(i)).append(")");
            }
        }
        
        switch (order) {
            case "name":
                query.append(" ORDER BY name");
                break;
            case "price":
                query.append(" ORDER BY min_price + max_price");
                break;
            case "position":
                query.append(" ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) DESC");
                break;
            default:
                break;
        }
        
        PreparedStatement stm = con.prepareStatement(query.toString());
        try {
            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }
    
    public List<Restaurant> getRestaurantsByNameSimilarityFilteredOrderedBy(String name, String order,
            Integer minPrice, Integer maxPrice,
            String[] cuisines, ArrayList<Integer> valutazioni
    ) throws SQLException {

        StringBuilder query = new StringBuilder("SELECT DISTINCT R.* FROM APP.restaurants R");

        if (cuisines != null && cuisines.length > 0) {
            String cond = " JOIN APP.Restaurants_Cuisines RC ON R.id =  RC.id_restaurant JOIN (SELECT * FROM APP.Cuisines WHERE ";
            for (int i = 0; i < cuisines.length; i++) {
                if (i > 0) {
                    cond += " OR ";
                }
                cond += "name = '" + cuisines[i] + "'";
            }
            cond += ")";

            cond += " C ON RC.id_cuisine = C.id";

            query.append(cond);
        }

        ArrayList<String> conditions = new ArrayList<>();

        if (minPrice != null) {
            conditions.add("min_price >= " + minPrice);
        }
        if (maxPrice != null) {
            conditions.add("max_price <= " + maxPrice);
        }
        if (valutazioni.size() > 0) {
            StringBuilder cond = new StringBuilder();
            for (int i = 0; i < valutazioni.size(); i++) {
                if (i > 0) {
                    cond.append(" OR ");
                }
                cond.append("ABS((CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) - ").append(valutazioni.get(i)).append(") <= 0.5");
            }
            conditions.add(cond.toString());
        }

        if (conditions.size() > 0) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append("(").append(conditions.get(i)).append(")");
            }
        }

        switch (order) {
            case "name":
                query.append(" ORDER BY name");
                break;
            case "price":
                query.append(" ORDER BY min_price + max_price");
                break;
            case "position":
                query.append(" ORDER BY (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END), REVIEW_COUNT DESC");
                break;
            default:
                break;
        }
        PreparedStatement stm = con.prepareStatement(query.toString());

        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    
    /**
     * Costruisce e ritorna il prossimo oggetto restaurant letto dal database
     * N.B.: DEVE ESSERE ESEGUITO DOPO rs.next() E SOLO SE QUESTA HA RESTITUITO
     * true N.B.: FUNZIONA SOLO SE LA QUERY RITORNA UNA RIGA DELLA TABELLA
     * RISTORANTE, e la tabella ristorante è chiamata R
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Restaurant getRestaurant(ResultSet rs) throws SQLException {

        Restaurant r = new Restaurant();

        r.setName(rs.getString("name"));
        r.setId(rs.getInt("id"));
        int count = rs.getInt("review_counter");
        r.setReview_count(count);
        r.setDescription(rs.getString("description"));
        r.setUrl(rs.getString("web_site_url"));
        if (count > 0) {
            r.setGlobal_review(rs.getDouble("global_review") / count);
        } else {
            r.setGlobal_review(.0);
        }
        r.setId_owner((Integer)rs.getObject("id_owner"));
        r.setId_creator((Integer)rs.getObject("id_creator"));
        r.setAddress(rs.getString("address"));
        r.setLatitude((Double)rs.getObject("latitude"));
        r.setLongitude((Double)rs.getObject("longitude"));
        r.setMin_price((Integer)rs.getObject("min_price"));
        r.setMax_price((Integer)rs.getObject("max_price"));
        r.setCity(rs.getString("city"));
        r.setRegion(rs.getString("region"));
        r.setState(rs.getString("state"));
        r.setQr_path(rs.getString("qr"));
        
        
        getRestaurantCuisines(r);

        return r;
    }

    private List<Restaurant> getRestaurantsUnfiltered(PreparedStatement stm) throws SQLException {

        ArrayList<Restaurant> r_l = new ArrayList<>();
        ResultSet rs = stm.executeQuery();
        try {
            while (rs.next()) {
                r_l.add(getRestaurant(rs));
            }
        } finally {
            rs.close();
        }

        return r_l;
    }

    private List<Restaurant> getRestaurantsFilteredByNameSimilarity(PreparedStatement stm, String name) throws SQLException {

        ArrayList<Restaurant> r_l = new ArrayList<>();
        ResultSet rs = stm.executeQuery();
        try {
            while (rs.next()) {
                String r_name = rs.getString("name");
                if (Util.containingDistanceLimited(name, r_name, 3) < 3) {

                    r_l.add(getRestaurant(rs));
                }
            }
        } finally {
            rs.close();
        }

        return r_l;
    }

    
    
    
    /**
     * --------GESTIONE LUOGHI-----------
     */
    private static final int algorithm = 4;
    private static final boolean use_full_matching = false;

    private void valuta(Place p, String[] words) {
        p.d = 0;
        String r_name = p.toString().toLowerCase();
        String[] name_ws;
        
        switch(algorithm){
            case 0:
                for (String word : words) {
                    int k = 1 + Math.round(word.length() / 8.0f);
                    int d = Util.containingDistanceLimited(word, r_name, k);
                    if (d < k) {
                        //p.d += 1.0/(d+1.0);
                        p.d += 1.0 - d / ((double) k);
                    }
                }
                break;
            case 1:
                name_ws = r_name.split(" ");
                
                for (String w : name_ws) {
                    for (String word : words) {
                        int d = Util.editDistanceLimited(word, w, 3);
                        if (d < 3) {
                            p.d += 1.0 / (d + 1);
                        }
                    }
                }
                break;
            case 2:
                for (String word : words) {
                    if (r_name.contains(word)) {
                        p.d++;
                    }
                }
                break;
            default:
                name_ws = r_name.split(" ");
                for (String w : name_ws) {
                    for (String word : words) {
                        int k = 1 + Math.round(word.length() / 8.0f);
                        int d = Util.containingDistanceLimited(word, w, k);
                        if (d < k) {
                            //p.d += 1.0/(d+1.0);
                            p.d += 1.0 - d / ((double) k);
                        }
                    }
                }
                break;
        }
    }

    public List<String> getPlaces(String term) throws SQLException {
        String[] words = term.toLowerCase().trim().split(" ");
        ArrayList<String> r_l = new ArrayList<>();
        
        PreparedStatement stm;
        
        stm = con.prepareStatement("SELECT * FROM APP.cities");
        try {
            ResultSet rs = stm.executeQuery();
            PriorityQueue<Place> queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    if(use_full_matching){
                        p.setState(rs.getString("state"));
                        p.setRegion(rs.getString("region"));
                    }
                    p.setCity(rs.getString("name"));

                    valuta(p, words);

                    if (p.d > 0) {
                        queue.add(p);
                    }

                }
            } finally {
                rs.close();
            }
            //aggiungo solo 5 città
            for (int i = 0; i < 5; i++) {
                if (queue.peek() != null) {
                    Place p = queue.poll();
                    r_l.add(p.toString());
                    System.out.print(p.toString() + ": " + p.d + "; ");
                }
            }
            System.out.print("\n");

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
        stm = con.prepareStatement("SELECT * FROM APP.regions");
        try {
            ResultSet rs = stm.executeQuery();
            PriorityQueue<Place> queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    if(use_full_matching){
                        p.setState(rs.getString("state"));
                    }
                    p.setRegion(rs.getString("name"));
                    valuta(p, words);

                    if (p.d > 0) {
                        queue.add(p);
                    }

                }
            } finally {
                rs.close();
            }
            //aggiungo solo 3 regioni
            for (int i = 0; i < 3; i++) {
                if (queue.peek() != null) {
                    r_l.add(queue.poll().toString());
                }
            }

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

        
        stm = con.prepareStatement("SELECT * FROM APP.states");
        try {
            ResultSet rs = stm.executeQuery();
            PriorityQueue<Place> queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    p.setState(rs.getString("name"));
                    valuta(p, words);

                    if (p.d > 0) {
                        queue.add(p);
                    }

                }
            } finally {
                rs.close();
            }
            //aggiungo solo 2 stati
            for (int i = 0; i < 2; i++) {
                if (queue.peek() != null) {
                    r_l.add(queue.poll().toString());
                }
            }

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        

        return r_l;

    }

    public Place getPlaceBySimilarity(String query) throws SQLException {
        String[] words = query.toLowerCase().trim().split(" ");
        Place place = new Place();
        place.d = 0;
        place.setCity("");
        place.setRegion("");
        place.setState("");

        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.states");
        try {
            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    Place p = new Place();
                    p.setState(rs.getString("name"));
                    valuta(p, words);

                    if (p.d > place.d) {
                        place = p;
                    }

                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

        stm = con.prepareStatement("SELECT * FROM APP.regions");
        try {
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    Place p = new Place();
                    p.setState(rs.getString("state"));
                    p.setRegion(rs.getString("name"));
                    valuta(p, words);

                    if (p.d > place.d) {
                        place = p;
                    }
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

        stm = con.prepareStatement("SELECT * FROM APP.cities");
        try {
            ResultSet rs = stm.executeQuery();
            PriorityQueue<Place> queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    p.setState(rs.getString("state"));
                    p.setRegion(rs.getString("region"));
                    p.setCity(rs.getString("name"));

                    valuta(p, words);
                    if (p.d > place.d) {
                        place = p;
                    }
                }
            } finally {
                rs.close();
            }

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

        return place;

    }

    
    /**
     * -------- GESTIONE TABELLE LEGATE AI RISTORANTI -----------
     */
    public void calcolaPosizioneInClassifica(Restaurant r) throws SQLException {
        if (r.getCity() == null || r.getRegion() == null || r.getState() == null) {
            return;
        }

        PreparedStatement stm = 
                con.prepareStatement("SELECT COUNT(*)"
                                  + " FROM APP.RESTAURANTS "
                                  + "WHERE state = ? AND region = ? AND city = ?"
                                  +     " AND (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) > ?");
        try {
            
            stm.setString(1, r.getState());
            stm.setString(2, r.getRegion());
            stm.setString(3, r.getCity());
            stm.setDouble(4, r.getGlobal_review());
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    r.setPosizione(1 + rs.getInt(1));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    public void getRestaurantTimes(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT giorno, apertura, chiusura FROM APP.ORARIO WHERE id = ? ORDER BY giorno, apertura");
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    r.addOrario(new Orario(rs.getInt("giorno"), rs.getTime("apertura"), rs.getTime("chiusura")));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    
    
    public void getRestaurantReviewValues(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT AVG(service) as service, AVG(food) as food, AVG(atmosphere) as atmosphere, AVG(value_for_money) as value_for_money FROM APP.REVIEWS WHERE id_restaurant = ? GROUP BY id_restaurant");
        
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                if(rs.next()) {
                    if(rs.getObject("service")!=null){
                        r.setService_review(rs.getDouble("service"));
                    }else{
                        r.setService_review(.0);
                    }
                    if(rs.getObject("food")!=null){
                        r.setFood_review(rs.getDouble("food"));
                    }else{
                        r.setFood_review(.0);
                    }
                    if(rs.getObject("atmosphere")!=null){
                        r.setAtmosphere_review(rs.getDouble("atmosphere"));
                    }else{
                        r.setAtmosphere_review(.0);
                    }
                    if(rs.getObject("value_for_money")!=null){
                        r.setMoney_review(rs.getDouble("value_for_money"));
                    }else{
                        r.setMoney_review(.0);
                    }
                    
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    public List<String> getCuisines() throws SQLException {

        ArrayList<String> r_l = new ArrayList<>();

        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.cuisines ORDER BY ID ASC");
        try {
            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    r_l.add(rs.getString("name"));

                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

        return r_l;

    }

    public void getRestaurantCuisines(Restaurant r) throws SQLException {

        ArrayList<String> cuisines = new ArrayList<>();

        PreparedStatement stm = con.prepareStatement(
                "SELECT name\n"
                + "FROM APP.RESTAURANTS_CUISINES JOIN APP.CUISINES ON (ID_CUISINE = ID) \n"
                + "WHERE id_restaurant = ?");
        stm.setInt(1, r.getId());
        try {
            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    cuisines.add(rs.getString("name"));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        r.setCuisines(cuisines);

    }

    public void rimuoviOrario(Integer id, Integer day, String apertura, String chiusura)throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.ORARIO WHERE id = ? AND giorno = ? AND apertura = ? AND chiusura = ?");

        java.sql.Time time1 = java.sql.Time.valueOf(apertura);
        java.sql.Time time2 = java.sql.Time.valueOf(chiusura);
        try {
            stm.setInt(1, id);
            stm.setInt(2, day);
            stm.setTime(3, time1);
            stm.setTime(4, time2);

            stm.executeUpdate();

        }finally {
            stm.close();
        }
    }
        
    public boolean inserisciOrario(Integer id, Integer day, String ora_apertura, String minuti_apertura, String ora_chiusura, String minuti_chiusura)throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.ORARIO VALUES (?,?,?,?)");

        java.sql.Time time1 = java.sql.Time.valueOf(ora_apertura+":"+minuti_apertura+":00");
        java.sql.Time time2 = java.sql.Time.valueOf(ora_chiusura+":"+minuti_chiusura+":00");
        try {
            stm.setInt(1, id);
            stm.setInt(2, day);
            stm.setTime(3, time1);
            stm.setTime(4, time2);

            stm.executeUpdate();

        } catch(SQLException e){
            /*Se l'errore è causato da un inserimento di una chiave duplicata*/
            if(duplicateKeyErrorCode.equals(e.getSQLState())){
                return false;
            }else{
                throw e; 
            }
        } finally {
            stm.close();
        }
        return true;
    }
    
    public void rimuoviCucina(Integer restaurantID, String cuisine)throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.RESTAURANTS_CUISINES "
                                                   + "WHERE id_restaurant = ? "
                                                   + "AND id_cuisine = (SELECT id FROM APP.CUISINES WHERE name = ?)");

        try {
            stm.setInt(1, restaurantID);
            stm.setString(2, cuisine);
            
            stm.executeUpdate();

        }finally {
            stm.close();
        }
    }
    
    public boolean inserisciCucina(Integer restaurantID, Integer cuisineID)throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.RESTAURANTS_CUISINES VALUES (?,?)");

        try {
            stm.setInt(1, restaurantID);
            stm.setInt(2, cuisineID);
            stm.executeUpdate();

        } catch(SQLException e){
            /*Se l'errore è causato da un inserimento di una chiave duplicata*/
            if(duplicateKeyErrorCode.equals(e.getSQLState())){
                return false;
            }else{
                throw e; 
            }
        } finally {
            stm.close();
        }
        return true;
    }
    
    public String[] getRestaurantLocation(int restaurant) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT city, region, state FROM APP.RESTAURANTS WHERE id = ?");
        try {
            stm.setInt(1, restaurant);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()){
                    String[] location = new String[3];
                    location[0] = rs.getString(1);
                    location[1] = rs.getString(2);
                    location[2] = rs.getString(3);
                    return location;
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    
    public void computeRestaurantsCoordinate()throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT id, address, city, region, state FROM APP.RESTAURANTS WHERE latitude IS NULL OR longitude IS NULL");
        
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        
        
        try {
            ResultSet rs = stm.executeQuery();
            try{
                while(rs.next()){
                    Restaurant r = new Restaurant();
                    r.setId(rs.getInt("id"));
                    r.setAddress(rs.getString("address"));
                    r.setCity(rs.getString("city"));
                    r.setRegion(rs.getString("region"));
                    r.setState(rs.getString("state"));
                    restaurants.add(r);
                }
                
            } finally{
                rs.close();
            }
        } finally {
            stm.close();
        }
        
        
        for(Restaurant r : restaurants){
            stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET latitude = ?, longitude = ? WHERE id = ?");
            double[] coordinates = Util.getCoordinates(r.getAddress(), r.getCity(), r.getRegion(), r.getState());
            
            try{
                stm.setDouble(1, coordinates[0]);
                stm.setDouble(2, coordinates[1]);
                stm.setInt(3, r.getId());
                stm.executeUpdate();
            }finally {
                stm.close();
            }
        }
        
    }

    
    public void manageRestaurant(Integer id, String name, String description, String url, String address, Integer min_price, Integer max_price)throws SQLException {
        
        
        StringBuilder query=new StringBuilder("UPDATE App.Restaurants SET ");
        
        ArrayList<String> lista = new ArrayList<>();
        
        if(!name.isEmpty()){
            lista.add("name='"+name+"'");
        }
        if(!description.isEmpty()){
            lista.add("description='"+description+"'");
        }
        if(!url.isEmpty()){
            lista.add("web_site_url='"+url+"'");
        }
        if(!address.isEmpty()){
            String[] location = getRestaurantLocation(id);
            double[] coordinates = Util.getCoordinates(address, location[0], location[1], location[2]);
            lista.add("address='"+address+"'");
            lista.add("latitude = " + coordinates[0]);
            lista.add("longitude = " + coordinates[1]);
        }
        if(min_price!=null){
            lista.add("min_price="+min_price.toString());
        }
        if(max_price!=null){
            lista.add("max_price="+max_price.toString());
        }
        
        for (int i=0; i<lista.size(); i++) {
            if(i>0){
                query.append(", ");
            }
            query.append(lista.get(i));
        }
        
        query.append(" WHERE id=").append(id);
        System.out.println(query.toString());
        PreparedStatement stm = con.prepareStatement(query.toString());
        try {
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }


    public void setRestaurantQRPath(Integer id, String path)throws SQLException {
        
        
        PreparedStatement stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET qr = ? WHERE id = ?");
        try {
            stm.setString(1, path);
            stm.setInt(2, id);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    
    
    /**
     * ------- GESTIONE FOTO/RECENSIONI -----------------
     */
    
    public void getRestaurantFirstPhoto(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT name, path, type FROM APP.photos WHERE id_restaurant = ? ORDER BY type DESC FETCH FIRST 1 ROWS ONLY");
        
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                if(rs.next()) {
                    r.setFirstPhoto(new Photo(rs.getString("name"), rs.getString("path"), rs.getInt("type")));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }        
    }

    
    public void getRestaurantPhotos(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT name, path, type FROM APP.photos WHERE id_restaurant = ? ORDER BY type DESC");
        ArrayList<Photo> photos = new ArrayList<>();
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    photos.add(new Photo(rs.getString("name"), rs.getString("path"), rs.getInt("type")));
                    //System.out.println(rs.getString("path"));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        if(photos.size() > 0){
            r.setFirstPhoto(photos.get(0));
            if(photos.size()>1){
                r.setPhotos(new ArrayList<>(photos.subList(1, photos.size())));
            }    
        }
        
    }

    /**
     *
     * @param name
     * @param path
     * @param restaurant_id
     * @return
     * @throws SQLException
     */
    public Integer insertPhoto(String name, String path, int restaurant_id, int type) throws SQLException {

        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.photos VALUES(default,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        int k = -1;
        try {
            stm.setString(1, name);
            stm.setString(2, path);
            stm.setInt(3, restaurant_id);
            stm.setInt(4, type);

            int rs = stm.executeUpdate();
            ResultSet keys = stm.getGeneratedKeys();
            try {
                if (keys.next()) {
                    k = keys.getInt(1);
                }
            } finally {
                keys.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return k;
    }

    public boolean insertReview(Integer global_value, Integer food, Integer service, Integer value_for_money,
            Integer atmosphere, String title, String description,
            Integer id_restaurant, Integer id_creator, Integer id_photo) throws SQLException {

        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.reviews VALUES(?,?,?,?,?,?,?,default,?,?,?)");
        try {
            stm.setInt(1, global_value);
            if(food != null){
                stm.setInt(2, food);
            }else{
                stm.setNull(2, Types.INTEGER);
            }
            if(service != null){
                stm.setInt(3, service);
            }else{
                stm.setNull(3, Types.INTEGER);
            }
            if(value_for_money!= null){
                stm.setInt(4, value_for_money);
            }else{
                stm.setNull(4, Types.INTEGER);
            }
            if(atmosphere != null){
                stm.setInt(5, atmosphere);
            }else{
                stm.setNull(5, Types.INTEGER);
            }
            if(title!= null){
                stm.setString(6, title);
            }else{
                stm.setNull(6, Types.VARCHAR);
            }
            if(description != null){
                stm.setString(7, description);
            }else{
                stm.setNull(7, Types.INTEGER);
            }
            if(food != null){
                stm.setInt(2, food);
            }else{
                stm.setNull(2, Types.INTEGER);
            }
            
            
            
            
            
            
            stm.setInt(8, id_restaurant);
            stm.setInt(9, id_creator);
            if(id_photo >= 0){
                stm.setInt(10, id_photo);
            }else{
                stm.setNull(10, Types.INTEGER);
            }
            

            int rs = stm.executeUpdate();

        } catch(SQLException e){
            /*Se l'errore è causato da un inserimento di una chiave duplicata*/
            if(duplicateKeyErrorCode.equals(e.getSQLState())){
                return false;
            }else{
                throw e; 
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
        stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET REVIEW_COUNTER = REVIEW_COUNTER + 1, GLOBAL_REVIEW = GLOBAL_REVIEW + ?WHERE id = ?");
        try {
            stm.setInt(1, global_value);
            stm.setInt(2, id_restaurant);
            
            int rs = stm.executeUpdate();

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
        return true;
    }

    public void getReviews(Restaurant res) throws SQLException {

        PreparedStatement stm = con.prepareStatement("SELECT r.*, u.name as name, u.surname as surname FROM App.REVIEWS r JOIN App.USERS u ON u.id = r.id_creator WHERE ID_RESTAURANT = ?");

        try {
            stm.setInt(1, res.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    Review r = new Review();
                    r.setAtmosphere(rs.getInt("atmosphere"));
                    r.setCreation(rs.getDate("date_creation"));
                    r.setTitle(rs.getString("title"));
                    r.setDescription(rs.getString("description"));
                    r.setFood(rs.getInt("food"));
                    r.setGlobal_value(rs.getInt("global_value"));
                    r.setId_creator(rs.getInt("id_creator"));
                    
                    r.setId_photo((Integer)rs.getObject("id_photo"));
                    
                    r.setId_restaurant(rs.getInt("id_restaurant"));
                    r.setService(rs.getInt("service"));
                    r.setValue_for_money(rs.getInt("value_for_money"));
                    r.setAuthor(rs.getString("name") + " " + rs.getString("surname"));
                    
                    res.addRecensione(r);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }

    }
    
    public void getCompletedReviews(Restaurant res) throws SQLException {

        PreparedStatement stm = con.prepareStatement("SELECT r.*, u.name as name, u.surname as surname FROM App.REVIEWS r JOIN App.USERS u ON u.id = r.id_creator WHERE ID_RESTAURANT = ? AND r.title IS NOT NULL");

        try {
            stm.setInt(1, res.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    Review r = new Review();
                    r.setAtmosphere(rs.getInt("atmosphere"));
                    r.setCreation(rs.getDate("date_creation"));
                    r.setTitle(rs.getString("title"));
                    r.setDescription(rs.getString("description"));
                    r.setFood(rs.getInt("food"));
                    r.setGlobal_value(rs.getInt("global_value"));
                    r.setId_creator(rs.getInt("id_creator"));
                    
                    r.setId_photo((Integer)rs.getObject("id_photo"));
                    
                    r.setId_restaurant(rs.getInt("id_restaurant"));
                    r.setService(rs.getInt("service"));
                    r.setValue_for_money(rs.getInt("value_for_money"));
                    r.setAuthor(rs.getString("name") + " " + rs.getString("surname"));
                    
                    res.addRecensione(r);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }

    }
    
    
    public boolean isRestaurantOwner(int user, int restaurant) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT id_owner FROM APP.RESTAURANTS WHERE id = ? AND id_owner = ?");
        try {
            stm.setInt(1, restaurant);
            stm.setInt(2, user);
            ResultSet rs = stm.executeQuery();
            try {
                return (rs.next());
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        
    }
    
    /**
     * -------------- GESTIONE NOTIFICHE -----------------
     */
    
    /**
     * 
     * @param restaurant
     * @return id del proprietario se esiste, null se il ristorante non ha un proprietario o -1 se il ristorante non è stato trovato
     * @throws SQLException 
     */
    public Integer getRestaurantOwner(int restaurant) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT id_owner FROM APP.RESTAURANTS WHERE id = ?");
        try {
            stm.setInt(1, restaurant);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()){
                    if(rs.getObject(1) != null) {
                        return rs.getInt(1);
                    }else{
                        return null;
                    }
                } else {
                    return -1;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    /**
     * Se target impostato ad un valore < 0, viene inserito il valore null In
     * tal caso la notifica viene inviata a tutti gli admin @param photo @param
     * target @throws SQLException
     */
    public void newPhotoNotification(int photo, int target) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.NOTIFICATIONS_PHOTO VALUES (?, ?)");

        try {
            
            if (target < 0) {
                stm.setNull(1, java.sql.Types.INTEGER);
            } else {
                stm.setInt(1, target);
            }
            stm.setInt(2, photo);
            

            stm.executeUpdate();

        } finally {
            stm.close();
        }

    }

    public void richiestaReclamoRistorante(int user, int restaurant) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.NOTIFICATIONS_RESTAURANT VALUES (?, ?)");

        try {
            stm.setInt(1, restaurant);
            stm.setInt(2, user);

            stm.executeUpdate();

        } finally {
            stm.close();
        }

    }

    
    public int contaNotificheFoto(User u) throws SQLException{
        StringBuilder builder = new StringBuilder("SELECT COUNT(*) FROM APP.NOTIFICATIONS_PHOTO N ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ").append(u.getId());
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }

        PreparedStatement stm = con.prepareStatement(builder.toString());

        try {
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }else{
                    return 0;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }
    
    public NotificaFoto getUnaNotificaFoto(User u) throws SQLException {

        StringBuilder builder = new StringBuilder("SELECT R.name, R.id, P.id, P.path, P.name FROM APP.NOTIFICATIONS_PHOTO N JOIN APP.PHOTOS P "
                                                 + "ON N.id_photo = P.id JOIN App.RESTAURANTS R ON P.id_restaurant = R.id ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ").append(u.getId());
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }

        builder.append(" FETCH FIRST 1 ROWS ONLY");
        
        PreparedStatement stm = con.prepareStatement(builder.toString());

        try {
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    return new NotificaFoto(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                }else{
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    
    public List<NotificaFoto> getNotificheFoto(User u) throws SQLException {

        StringBuilder builder = new StringBuilder("SELECT R.name, R.id, P.id, P.path, P.name FROM APP.NOTIFICATIONS_PHOTO N JOIN APP.PHOTOS P "
                                                 + "ON N.id_photo = P.id JOIN App.RESTAURANTS R ON P.id_restaurant = R.id ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ").append(u.getId());
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }

        PreparedStatement stm = con.prepareStatement(builder.toString());

        ArrayList<NotificaFoto> list = new ArrayList<>();

        try {
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    NotificaFoto nf = new NotificaFoto(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                    list.add(nf);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }

        return list;

    }

    

     
    public int contaNotificheReclami() throws SQLException {

        String query = "SELECT COUNT(*) FROM APP.NOTIFICATIONS_RESTAURANT";

        PreparedStatement stm = con.prepareStatement(query);

        try {
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                   return rs.getInt(1);
                }else{
                    return 0;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

     public NotificaReclami getUnReclamoRistorante() throws SQLException {

        String query = "SELECT R.name, R.id, U.id, U.name, U.surname, U.email\n"
                        + "FROM APP.NOTIFICATIONS_RESTAURANT N JOIN APP.USERS U\n"
                        + "ON N.id_user = U.id \n"
                        + "JOIN APP.RESTAURANTS R ON N.id_restaurant = R.id "
                        + "FETCH FIRST 1 ROWS ONLY";

        PreparedStatement stm = con.prepareStatement(query);


        try {
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    return new NotificaReclami(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6));
                }else{
                    return null;
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
    }

    
    public List<NotificaReclami> getReclamiRistoranti() throws SQLException {

        String query = "SELECT R.name, R.id, U.id, U.name, U.surname, U.email\n"
                                                + "FROM APP.NOTIFICATIONS_RESTAURANT N JOIN APP.USERS U\n"
                                                + "ON N.id_user = U.id \n"
                                                + "JOIN APP.RESTAURANTS R ON N.id_restaurant = R.id ";

        PreparedStatement stm = con.prepareStatement(query);

        ArrayList<NotificaReclami> list = new ArrayList<>();

        try {
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    NotificaReclami nr = new NotificaReclami(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),rs.getString(6));
                    list.add(nr);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        System.out.println("reclami "+list.size());

        return list;

    }

    
    
    public void confermaFoto(int id_photo) throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.NOTIFICATIONS_PHOTO WHERE ID_PHOTO=?");
        
        try{
            stm.setInt(1, id_photo);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    public void segnalaAdminFoto(int id_photo) throws SQLException {
        PreparedStatement stm = con.prepareStatement("UPDATE APP.NOTIFICATIONS_PHOTO SET ID_TARGET=NULL WHERE ID_PHOTO=?");
        
        try{
            stm.setInt(1, id_photo);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    public void eliminaFoto(int id_photo) throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.NOTIFICATIONS_PHOTO WHERE ID_PHOTO=?");
        
        try{
            stm.setInt(1, id_photo);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
        
        PreparedStatement stm2 = con.prepareStatement("DELETE FROM APP.PHOTO WHERE ID=?");
        
        try{
            stm2.setInt(1, id_photo);
            stm2.executeUpdate();
        } finally {
            stm2.close();
        }
    }
    
    public void eliminaReclamo(int id_restaurant, int id_user) throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.NOTIFICATIONS_RESTAURANT WHERE ID_RESTAURANT=? AND ID_USER=?");
        
        try{
            stm.setInt(1, id_restaurant);
            stm.setInt(2, id_user);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    public void confermaReclamo(int id_restaurant, int id_user) throws SQLException {
        PreparedStatement stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET ID_OWNER=? WHERE ID=?");
        
        try{
            stm.setInt(1, id_user);
            stm.setInt(2, id_restaurant);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
        
        PreparedStatement stm2 = con.prepareStatement("UPDATE APP.USERS SET TYPE='r' WHERE ID=? and type ='u'");
        
        try{
            stm2.setInt(1, id_user);
            stm2.executeUpdate();
        } finally {
            stm2.close();
        }
        
        PreparedStatement stm3 = con.prepareStatement("DELETE FROM APP.NOTIFICATIONS_RESTAURANT WHERE ID_RESTAURANT=? AND ID_USER=?");
        
        try{
            stm3.setInt(1, id_restaurant);
            stm3.setInt(2, id_user);
            stm3.executeUpdate();
        } finally {
            stm3.close();
        }
    }
    
    /*
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
     */
}
