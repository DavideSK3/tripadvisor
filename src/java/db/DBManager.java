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

    private transient Connection con;

    public DBManager(String dburl) throws SQLException {

        try {
            //Class.forName("org.postgresql.Driver");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, getClass().getClassLoader());

        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }

        Connection con = DriverManager.getConnection(dburl, "db_manager", "tripadvisor");
        //Connection con = DriverManager.getConnection(dburl, "tripadvisor", "tripadvisor");

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

    public void changePassword(int id, String new_password) throws SQLException {
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

    public void insertToken(int id, String token, int expirationTime) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.tokens VALUES(?, ?, ?)");
        try {
            stm.setInt(1, id);
            stm.setString(2, token);
            long expTime = new Date().getTime() + expirationTime * 60 * 60 * 1000;
            stm.setTimestamp(3, new Timestamp(expTime));

            int rs = stm.executeUpdate();

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }
    
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
    
    
    public void registerRestaurant(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.resturants VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement stm2 = con.prepareStatement("INSERT INTO APP.names_term VALUES(?, ?)");

        try {
            stm.setString(1, r.getName());
            stm.setString(2, r.getDescription());
            stm.setString(3, r.getUrl());
            if (r.getGlobal_review() != null) {
                stm.setDouble(4, r.getGlobal_review());
            } else {
                stm.setNull(4, Types.DOUBLE);
            }
            if (r.getFood_review() != null) {
                stm.setDouble(5, r.getFood_review());
            } else {
                stm.setNull(5, Types.DOUBLE);
            }
            if (r.getAtmosphere_review() != null) {
                stm.setDouble(6, r.getAtmosphere_review());
            } else {
                stm.setNull(6, Types.DOUBLE);
            }
            if (r.getService_review() != null) {
                stm.setDouble(7, r.getService_review());
            } else {
                stm.setNull(7, Types.DOUBLE);
            }
            if (r.getMoney_review() != null) {
                stm.setDouble(8, r.getMoney_review());
            } else {
                stm.setNull(8, Types.DOUBLE);
            }
            if (r.getId_owner() != null) {
                stm.setInt(9, r.getId_owner());
            } else {
                stm.setNull(9, Types.INTEGER);
            }
            if (r.getId_creator() != null) {
                stm.setInt(10, r.getId_creator());
            } else {
                stm.setNull(10, Types.INTEGER);
            }
            stm.setString(11, r.getAddress());

            if (r.getLatitude() != null) {
                stm.setDouble(12, r.getLatitude());
            } else {
                stm.setNull(12, Types.DOUBLE);
            }

            if (r.getLongitude() != null) {
                stm.setDouble(13, r.getLongitude());
            } else {
                stm.setNull(13, Types.DOUBLE);
            }

            if (r.getMin_price() != null) {
                stm.setDouble(14, r.getMin_price());
            } else {
                stm.setNull(14, Types.DOUBLE);
            }
            if (r.getMax_price() != null) {
                stm.setDouble(15, r.getMax_price());
            } else {
                stm.setNull(15, Types.DOUBLE);
            }
            int rs = stm.executeUpdate();

            if (rs > 0) {
                Set<String> terms = Util.generateNearTerms(r.getName());
                for (String t : terms) {

                    stm2.setInt(1, r.getId());
                    stm2.setString(2, t);
                    stm2.executeUpdate();

                }
            }

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

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
                // ricordarsuser.setType(rs.getString("type").charAt(0));i SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    
    public List<Restaurant> getRestaurants(String name, String place) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        try {
            return getRestaurantsFilteredByNameSimilarity(stm, name);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
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
    
    public List<Restaurant> getRestaurantsByPlace(String place) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE '" + place + "%'");
        try {
            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    
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
                query.append(" AND review_counter > 0 ORDER BY (global_review/review_counter) DESC");
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
                query.append(" AND review_counter > 0 ORDER BY (global_review/review_counter) DESC");
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

    public List<Restaurant> getRestaurantsFilteredOrderedBy(String order,
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
            conditions.add(" max_price <= " + maxPrice);
        }
        if (valutazioni.size() > 0) {
            StringBuilder cond = new StringBuilder();
            for (int i = 0; i < valutazioni.size(); i++) {
                if (i > 0) {
                    cond.append(" OR ");
                }
                cond.append("REVIEW_COUNTER > 0 AND FLOOR(global_review/review_counter) = ").append(valutazioni.get(i));
            }
            conditions.add(cond.toString());
        }

        if (conditions.size() > 0) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append(conditions.get(i));
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
                query.append(" AND review_counter > 0 ORDER BY (global_review/review_counter) DESC");
                break;
            default:
                break;
        }
        System.out.println(query.toString());
        PreparedStatement stm = con.prepareStatement(query.toString());

        try {
            return getRestaurantsUnfiltered(stm);
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    public List<String> getRestaurantsNamesByTerm(String term, int limit) throws SQLException {

        List<String> result = new ArrayList<String>();
        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.restaurants R WHERE lcase(name) LIKE '%" + term + "%'");
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
     * in modo predefinito
     */
    
    
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
                stm = con.prepareStatement("SELECT * FROM APP.restaurants R WHERE REVIEW_COUNTER > 0 ORDER BY global_review/review_counter DESC");
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
                cond.append("FLOOR(global_review/review_counter) = ").append(valutazioni.get(i));
            }
            conditions.add(cond.toString());
        }

        if (conditions.size() > 0) {
            query.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append(conditions.get(i));
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
                if(conditions.size() > 0){
                    query.append("AND ");
                }else{
                    query.append("WHERE ");
                }
                query.append("REVIEW_COUNTER > 0 ORDER BY (global_review/review_counter) DESC");
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

    public List<Restaurant> getRestaurantsByNameSimilarity(String name) throws SQLException {
        ArrayList<Restaurant> r_l = new ArrayList<>();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.restaurants R");
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
            r.setService_review(rs.getDouble("service_review") / count);
            r.setFood_review(rs.getDouble("food_review") / count);
            r.setAtmosphere_review(rs.getDouble("atmosphere_review") / count);
            r.setMoney_review(rs.getDouble("value_for_money_review") / count);
        } else {
            r.setGlobal_review(.0);
            r.setService_review(.0);
            r.setFood_review(.0);
            r.setAtmosphere_review(.0);
            r.setMoney_review(.0);
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
                if (Util.editDistanceLimited(name, r_name, 4) < 4) {

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
    private static final boolean USE_APPROXIMATE_MATCHING = true;
    private static final boolean USE_APPROXIMATE_CONTAIN = true;

    private void valuta(Place p, String[] words) {
        p.d = 0;
        String r_name = p.toString().toLowerCase();
        if (USE_APPROXIMATE_MATCHING) {
            if (USE_APPROXIMATE_CONTAIN) {

                for (String word : words) {
                    int k = 1 + Math.round(word.length() / 8.0f);
                    int d = Util.containingDistanceLimited(word, r_name, k);
                    if (d < k) {
                        //p.d += 1.0/(d+1.0);
                        p.d += 1.0 - d / ((double) k);
                    }
                }
            } else {
                String[] name_ws = r_name.split(" ");
                for (String word : words) {
                    for (String w : name_ws) {
                        int d = Util.editDistanceLimited(word, w, 3);
                        if (d < 3) {
                            p.d += 1.0 / (d + 1);
                        }
                    }
                }
            }

        } else {
            for (String word : words) {
                if (r_name.contains(word)) {
                    p.d++;
                }
            }
        }
    }

    public List<String> getPlaces(String term) throws SQLException {
        String[] words = term.toLowerCase().trim().split(" ");
        ArrayList<String> r_l = new ArrayList<>();

        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP.states");
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

        stm = con.prepareStatement("SELECT * FROM APP.regions");
        try {
            ResultSet rs = stm.executeQuery();
            PriorityQueue<Place> queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    p.setState(rs.getString("state"));
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

        PreparedStatement stm = con.prepareStatement("SELECT COUNT(*) FROM (SELECT review_counter, global_review FROM APP.RESTAURANTS WHERE review_counter > 0 AND state = ? AND region = ? AND city = ?) AS R WHERE (global_review/review_counter) > ?");
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

    public List<String> getCuisines() throws SQLException {

        ArrayList<String> r_l = new ArrayList<>();

        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.cuisines");
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

    /**
     * ------- GESTIONE FOTO/RECENSIONI -----------------
     */
    
    public void getRestaurantPhotos(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT name, path FROM APP.photos WHERE id_restaurant = ?");
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    r.addPhoto(new Photo(rs.getString("name"), rs.getString("path")));
                    //System.out.println(rs.getString("path"));
                }
            } finally {
                rs.close();
            }
        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
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
    public Integer insertPhoto(String name, String path, int restaurant_id) throws SQLException {

        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.photos VALUES(default,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        int k = -1;
        try {
            stm.setString(1, name);
            stm.setString(2, path);
            stm.setInt(3, restaurant_id);

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

    public void insertReview(Integer global_value, Integer food, Integer service, Integer value_for_money,
            Integer atmosphere, String title, String description,
            Integer id_restaurant, Integer id_creator, Integer id_photo) throws SQLException {

        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.reviews VALUES(?,?,?,?,?,?,?,default,?,?,?)");
        try {
            stm.setInt(1, global_value);
            stm.setInt(2, food);
            stm.setInt(3, service);
            stm.setInt(4, value_for_money);
            stm.setInt(5, atmosphere);
            stm.setString(6, title);
            stm.setString(7, description);
            stm.setInt(8, id_restaurant);
            stm.setInt(9, id_creator);
            if(id_photo >= 0){
                stm.setInt(10, id_photo);
            }else{
                stm.setNull(10, Types.INTEGER);
            }
            

            int rs = stm.executeUpdate();

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        
        stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET REVIEW_COUNTER = REVIEW_COUNTER + 1, GLOBAL_REVIEW = GLOBAL_REVIEW + ?, FOOD_REVIEW = FOOD_REVIEW + ?, ATMOSPHERE_REVIEW = ATMOSPHERE_REVIEW + ?, SERVICE_REVIEW = SERVICE_REVIEW + ?, VALUE_FOR_MONEY_REVIEW = VALUE_FOR_MONEY_REVIEW + ? WHERE id = ?");
        try {
            stm.setInt(1, global_value);
            stm.setInt(2, food);
            stm.setInt(3, atmosphere);
            stm.setInt(4, service);
            stm.setInt(5, value_for_money);
            stm.setInt(6, id_restaurant);
            
            int rs = stm.executeUpdate();

        } finally { // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
    }

    /**
     * -------------- Gestione Notifiche -----------------
     */
    
    
    
    public int getRestaurantOwner(int restaurant) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT id_owner FROM APP.RESTAURANTS WHERE id = ?");
        try {
            stm.setInt(1, restaurant);
            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getInt(1);
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
        PreparedStatement stm = con.prepareStatement("INSERT INTO NOTIFICATIONS_RESTAURANT VALUES (?, ?)");

        try {
            stm.setInt(1, restaurant);
            stm.setInt(2, user);

            stm.executeUpdate();

        } finally {
            stm.close();
        }

    }

    public List<NotificaFoto> getNotificheFoto(User u) throws SQLException {

        StringBuilder builder = new StringBuilder("SELECT R.name, R.id, P.id, P.path, P.name FROM APP.NOTIFICATIONS_PHOTO N JOIN App.PHOTO P ON N.id_photo = P.id JOIN App.RESTAURANTS R ON P.id_restaurant = R.id ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ").append(u.getId());
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target = NULL");
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
