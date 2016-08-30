/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Logger;


public class DBManager implements Serializable {
    
    private static final String duplicateKeyErrorCode = "23505";
    private final transient Connection con;

    
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
            sets.add("name = ?" );
        }
        if(newSurname != null){
            sets.add("surname = ?" );
        }
        if(newEMail != null){
            sets.add("email = ?" );
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
            int i=1;
            if(newName != null){
               stm.setString(i++, newName);
            }
            if(newSurname != null){
                stm.setString(i++, newSurname);
            }
            if(newEMail != null){
                stm.setString(i++, newEMail);
            }

            stm.setInt(i++, id);
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
                rs.close();
            }
        } finally {
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

        } finally { 
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
                
                rs.close();
            }
        } finally { 
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
                rs.close();
            }
        } finally { 
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
        
        PreparedStatement stm = con.prepareStatement("INSERT INTO APP.users VALUES(default,?,?,?,?,'u')");
        try {
            stm.setString(1, name);
            stm.setString(2, surname);
            stm.setString(3, password);
            stm.setString(4, email);

            int rs = stm.executeUpdate();

        } finally {
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

    /**
     * Ritorna il ristorante identificato dalla chiave passata
     * @param key - l'id del ristorante
     * @return
     * @throws SQLException 
     */
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

    /**
     * Ritorna la lista dei ristoranti che hanno questo nome
     * @param name
     * @return
     * @throws SQLException 
     */
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
                
                rs.close();
            }
        } finally { 
            stm.close();
        }
    }
   
 
    
    /**
     * Metodo usato per ottenere i risultati visualizzati dall'autocompletamento.
     * Ritorna una lista (di dimensione massima  di nomi di ristoranti che iniziano (esattamente) per la stringa passata come parametro.
     * @param term - la stringa scritta dall'utente
     * @param limit - il numero massimo di risultati da ritornare
     * @return - una lista di possibili nomi di ristoranti
     * @throws SQLException 
     */
    public List<String> getRestaurantsNamesByTerm(String term, int limit) throws SQLException {
        
        term = term
            .replace("!", "!!")
            .replace("%", "!%")
            .replace("_", "!_")
            .replace("[", "![");
        
        List<String> result = new ArrayList<String>();
        PreparedStatement stm = con.prepareStatement("SELECT name FROM APP.restaurants R WHERE lcase(name) LIKE ? ESCAPE '!' FETCH FIRST ? ROWS ONLY");
        stm.setString(1, term + "%");
        stm.setInt(2, limit);
        try {
            ResultSet rs = stm.executeQuery();

            try {
                
                while (rs.next()) {
                    result.add(rs.getString("name"));
                }

            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }
        return result;

    }
    
    /*
     * Diversi metodi per ottenere una lista di ristoranti filtrati e ordinati
     * in modo predefinito per la ricerca
     */
    

    
    /**
     * Metodo usato per la ricerca normale.
     * Ricerca ristoranti che si trovino nel luogo specificato 
     * (N.B.: deve essere nel formato 'Stato Regione Città', 'Stato Regione' o 'Stato';
     * si consiglia di sfruttare la classe db.Place con il suo metodo .toString(); nel caso 
     * il parametro "place" sia null o contenga una stringa vuota non vengono filtrati i ristoranti per luogo),
     * ordinati per 'name', 'price' o 'position' (se non specificato uno di questi valori, i risultati non vengono ordinati).
     * Successivamente vengono filtrati per somiglianza alla stringa "name" usando il metodo "getRestaurantsFilteredByNameSimilarity()"
     * (se il parametro "name" non è null o non contiene una stringa vuota)
     * @param name
     * @param place
     * @param order
     * @return
     * @throws SQLException 
     */
    public List<Restaurant> getRestaurantsOrderedBy(String name, String place, String order) throws SQLException {

        
        StringBuilder query = new StringBuilder();
        if(place != null && !place.isEmpty()){
            query.append("SELECT * FROM APP.restaurants R WHERE state || ' ' || region || ' ' ||  city LIKE  ? ESCAPE '!'");
        }else{
            query.append("SELECT * FROM APP.restaurants R");
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
        if(place != null && !place.isEmpty()){
            place = place
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
            stm.setString(1, place + "%");
        }
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
    
    
    
    /**
     * Metodo usato per la ricerca avanzata.
     * Ricerca ristoranti che si trovino nel luogo specificato 
     * (N.B.: deve essere nel formato 'Stato Regione Città', 'Stato Regione' o 'Stato';
     * si consiglia di sfruttare la classe db.Place con il suo metodo .toString(); nel caso 
     * il parametro "place" sia null o contenga una stringa vuota non vengono filtrati i ristoranti per luogo),
     * che abbiano un prezzo minimo superiore al parametro "minPrice" (se non è nullo),
     * che abbiano un prezzo massimo inferiore al parametro "maxPrice" (se non è nullo),
     * che posseggano le cucine specificate e abbiano una valutazione (approssimata all'intero più vicino) tra quelle specificate,
     * ordinati per 'name', 'price' o 'position' (se non specificato uno di questi valori, i risultati non vengono ordinati).
     * Successivamente vengono filtrati per somiglianza alla stringa "name" usando il metodo "getRestaurantsFilteredByNameSimilarity()"
     * (se il parametro "name" non è null o non contiene una stringa vuota)
     * @param name
     * @param place
     * @param order
     * @return
     * @throws SQLException 
     */
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
                cond += "name = ?";
            }
            cond += ")";

            cond += " C ON RC.id_cuisine = C.id";

            query.append(cond);
        }

        ArrayList<String> conditions = new ArrayList<>();

        
        if(place != null && !place.isEmpty()){
            conditions.add("state || ' ' || region || ' ' ||  city LIKE ?");
        }
        
        
        if (minPrice != null) {
            conditions.add("min_price >= ?");
        }
        if (maxPrice != null) {
            conditions.add("max_price <= ?");
        }
        if (valutazioni.size() > 0) {
            StringBuilder cond = new StringBuilder();
            for (int i = 0; i < valutazioni.size(); i++) {
            
                if (i > 0) {
                    cond.append(" OR ");
                }
                cond.append("ABS(CAST((CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END) as REAL) - ? ) <= 0.5");
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
        //System.out.println(query.toString());
        
        
        try {
            
            int j=1;
            if (cuisines != null && cuisines.length > 0) {
                for (int i = 0; i < cuisines.length; i++) {
                    stm.setString(j++, cuisines[i]);
                }
            }

            if(place != null && !place.isEmpty()){
                place = place
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");
                stm.setString(j++, place + "%");
            }


            if (minPrice != null) {
                stm.setDouble(j++, minPrice);
            }
            if (maxPrice != null) {
                stm.setDouble(j++, maxPrice);
            }
            if (valutazioni.size() > 0) {

                for (int i = 0; i < valutazioni.size(); i++) {
                    stm.setInt(j++, valutazioni.get(i));
                }

            }
            if(name != null && !name.isEmpty()){
                return getRestaurantsFilteredByNameSimilarity(stm, name);
            }else{
                return getRestaurantsUnfiltered(stm);
            }
        } finally { 
            stm.close();
        }

    }
    
  
    
    /**
     * Costruisce e ritorna il prossimo oggetto restaurant letto dal database.
     * N.B.: DEVE ESSERE ESEGUITO DOPO rs.next() E SOLO SE QUESTA HA RESTITUITO 'true'.
     * N.B.: FUNZIONA SOLO SE LA QUERY RITORNA UNA RIGA DELLA TABELLA
     * APP.RESTAURANTS che deve essere nominata 'R' all'interno della FROM
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

    
    /**
     * Esegue il prepared statement passato come parametro e ritorna la lista di Ristoranti che si ricava dalla query.
     * N.B.: la query del prepared statement deve ritornare righe della tabella APP.RESTAURANTS che deve essere nominata 'R' all'interno della FROM
     * @param stm
     * @return
     * @throws SQLException 
     */
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

    /**
     * Esegue il prepared statement passato come parametro e ritorna la lista di Ristoranti che si ricava dalla query 
     * dopo averli filtrati per somiglianza del nome con la stringa "name" passata come parametro.
     * N.B.: la query del prepared statement deve ritornare righe della tabella APP.RESTAURANTS che deve essere nominata 'R' all'interno della FROM.
     * L'algoritmo utilizzato per confrontare le stringhe e determinarne la somiglianza implementato nel metodo "db.Util.containingDistanceLimited()".
     * 
     * @param stm
     * @param name
     * @return
     * @throws SQLException 
     */
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
    
    /**
     * alcuni parametri usati per scegliere il tipo di algoritmo da usare per ottenere la lista dei luoghi nell'autocompletamento
     */
    private static final int algorithm = 4;
    private static final boolean use_full_matching = false;
    private static final boolean use_unique_queue = true;

    /**
     * Metodo che valuta la "somiglianza" del place "p" passato come parametro alla stringa digitata dall'utente.
     * Questo metodo calcola e imposta il valore dell'attributo "p.d" da utilizzare, poi, per scegliere quali risultati restituire
     * 
     * @param p - l'oggetto di tipo Place contenente il luogo da valutare
     * @param words - le parole digitate dall'utente
     */
    private void valuta(Place p, String[] words) {
        p.d = 0;
        String r_name = p.toString().toLowerCase();
        String[] name_ws;
        
        switch(algorithm){
            case 0:
                for (String word : words) {
                    /*
                     * permetto un numero massimo di errori proporzionale alla lunghezza della parola digitata
                     * essendo il minor numero di caratteri 4, con la seguente formula si accetta un solo errore all'inizio
                     */
                    int k = 1 + Math.round(word.length() / 8.0f);
                    int d = Util.containingDistanceLimited(word, r_name, k);
                    if (d < k) {
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
            case 3:
                name_ws = r_name.split(" ");
                for (String w : name_ws) {
                    for (String word : words) {
                        /*
                        * permetto un numero massimo di errori proporzionale alla lunghezza della parola digitata
                        * essendo il minor numero di caratteri 4, con la seguente formula si accetta un solo errore all'inizio
                        */
                        int k = 1 + Math.round(word.length() / 8.0f);
                        int d = Util.containingDistanceLimited(word, w, k);
                        if (d < k) {
                            p.d += 1.0 - d / ((double) k);
                        }
                    }
                }
                break;
            default:
                ArrayList<String> p_names = new ArrayList<String>(3);
                if(p.getCity() != null) p_names.add(p.getCity().toLowerCase());
                if(p.getRegion() != null) p_names.add(p.getRegion().toLowerCase());
                if(p.getState() != null) p_names.add(p.getState().toLowerCase());
                
                for(String w : p_names){
                    for (String word : words) {
                        /*
                        * permetto un numero massimo di errori proporzionale alla lunghezza della parola digitata
                        * essendo il minor numero di caratteri 4, con la seguente formula si accetta un solo errore all'inizio
                        */
                        int k = 1 + Math.round(word.length() / 8.0f);
                        int d = Util.containingDistanceLimited(word, w, k);
                        if (d < k) {
                            p.d += 1.0 - d / ((double) k);
                        }
                    }
                }
                
                break;
        }
    }


    /**
     * Metodo usato per ottenere i risultati visualizzati dall'autocompletamento.
     * Ritorna una lista luoghi che "assomigliano" alla stringa passata come paramentro.
     * @param term - la stringa scritta dall'utente
     * @return - una lista di possibili nomi di luoghi
     * @throws SQLException 
     */
    public List<String> getPlaces(String term) throws SQLException {
        /**
         * Se l'attributo "use_full_matching" è impostato a 'true' per valutare un certo luogo si utilizza il suo nome completo:
         * per una città si utilizza la stringa ottenuta concatenando lo stato d'appartenenza, la regione e poi la città mentre per una regione si utilizza la concatenazione di stato e regione.
         * 
         * Nel caso sia 'false' invece, al fine del matching, si usa solo il nome della città/regione/stato.
         * In questo modo, per esempio, città che appartengono a regioni con un nome simile non risultano avvantaggiate.
         * (es: l'utente digita la stringa "Vene". Se questa flag fosse 'true' la città 'Italia Veneto Venezia' potrebbe matchare 2 volte)
         */
        
        /**
         * Se l'attributo "use_unique_queue" è impostato a 'true', si utilizza un'unica priority queue per scegliere i risultati migliori,
         * indipendentemente da che tipo di area geografica siano.
         * Nel caso sia 'false', invece, si utilizzano 3 code diverse, una per le città, una per le regioni ed una per gli stati;
         * in questo modo i consigli che appaiono sono ordinati prima per tipo di area ed è più facile ci siano dei risultati appartenenti a ciascuna tipologia.
         */
        
        String[] words = term.toLowerCase().trim().split(" ");
        ArrayList<String> r_l = new ArrayList<>();
        
        PriorityQueue<Place> queue = (use_unique_queue) ? new PriorityQueue<>(new Place.PlaceComparator()) : null;
        
        PreparedStatement stm;
        
        stm = con.prepareStatement("SELECT * FROM APP.cities");
        try {
            ResultSet rs = stm.executeQuery();
            if(!use_unique_queue) queue = new PriorityQueue<>(new Place.PlaceComparator());
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
                        if(!use_full_matching){
                            p.setState(rs.getString("state"));
                            p.setRegion(rs.getString("region"));
                        }
                        queue.add(p);
                    }
                    

                }
            } finally {
                rs.close();
            }
            
            if(!use_unique_queue){

                //aggiungo solo 5 città
                for (int i = 0; i < 5; i++) {
                    if (queue.peek() != null) {
                        Place p = queue.poll();
                        r_l.add(p.toString());
                        
                    }
                }
            }

        } finally { 
            stm.close();
        }
        
        stm = con.prepareStatement("SELECT * FROM APP.regions");
        try {
            ResultSet rs = stm.executeQuery();
            if(!use_unique_queue) queue = new PriorityQueue<>(new Place.PlaceComparator());
            try {
                while (rs.next()) {
                    Place p = new Place();
                    if(use_full_matching){
                        p.setState(rs.getString("state"));
                    }
                    p.setRegion(rs.getString("name"));
                    valuta(p, words);

                    if (p.d > 0) {
                        if(!use_full_matching){
                            p.setState(rs.getString("state"));
                        }
                        queue.add(p);
                    }

                }
            } finally {
                rs.close();
            }
            if(!use_unique_queue){
                //aggiungo solo 3 regioni
                for (int i = 0; i < 3; i++) {
                    if (queue.peek() != null) {
                        r_l.add(queue.poll().toString());
                    }
                }
            }

        } finally { 
            stm.close();
        }

        
        stm = con.prepareStatement("SELECT * FROM APP.states");
        try {
            ResultSet rs = stm.executeQuery();
            if(!use_unique_queue) queue = new PriorityQueue<>(new Place.PlaceComparator());
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
            if(!use_unique_queue){
                //aggiungo solo 2 stati
                for (int i = 0; i < 2; i++) {
                    if (queue.peek() != null) {
                        r_l.add(queue.poll().toString());
                    }
                }
            }
        } finally {
            stm.close();
        }
        
        if(use_unique_queue){
            //aggiungo 10 luoghi
            for (int i = 0; i < 10; i++) {
                if (queue.peek() != null) {
                    Place p = queue.poll();
                    r_l.add(p.toString());
                    
                }
            }
            
        }
        

        return r_l;

    }

    /**
     * Ritorna il luogo che più somiglia alla stringa digitata dall'utente.
     * Se non viene trovato nessun risultato abbastanza simile (un valore calcolato dal metodo "valuta()" maggiore di 0) viene ritornato il valore null.
     * 
     * Questo metodo viene usato in quanto l'utente ha la possibilità di scrivere qualunque stringa nel campo di ricerca, non essendo vincolato dall'autocompletamento,
     * mentre le query usate per effettuare la ricerca pretendono un luogo esistente e scritto in un formato predefinito.
     * 
     * @param query - la stringa digitata dall'utente
     * @return - il luogo più somigliante alla richiesta o null nel caso non sia stato trovato un risultato soddisfacente
     * @throws SQLException 
     */
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
        } finally { 
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
        } finally {
            stm.close();
        }

        stm = con.prepareStatement("SELECT * FROM APP.cities");
        try {
            ResultSet rs = stm.executeQuery();
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

        } finally {
            stm.close();
        }
        
        if(place.d > 0){
            return place;
        }else{
            return null;
        }

    }

    
    /**
     * -------- GESTIONE TABELLE LEGATE AI RISTORANTI -----------
     */
    
    /**
     * Calcola la posizione in classifica di questo ristorante nella sua città.
     * Se nell'oggetto "r" passato come parametro gli attributi che indicano il luogo in cui risiede il ristorante non sono completi/specificati
     * il metodo ritorna immediatamente senza calcolare la posizione.
     * 
     * @param r - l'oggetto che contiene le informazioni del ristorante
     * @throws SQLException 
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
        } finally { 
            stm.close();
        }

    }

    /**
     * Cerca nella tabella APP.ORARI gli orari del ristorante.
     * Il metodo inserisce direttamente dentro l'oggetto Restaurant passato la lista degli orari.
     * 
     * @param r - l'oggetto che contiene le informazioni del ristorante
     * @throws SQLException 
     */
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
        } finally {
            stm.close();
        }
    }

    
    /**
     * Calcola i valori medi delle recensioni ricevute dal ristorante.
     * Il metodo inserisce direttamente dentro l'oggetto Restaurant passato i valori calcolati.
     * 
     * @param r - l'oggetto che contiene le informazioni del ristorante
     * @throws SQLException 
     */
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
        } finally { 
            stm.close();
        }
    }
    
    
    /**
     * Cerca nella tabella APP.RESTAURANTS_CUISINES le cucine del ristorante.
     * Il metodo inserisce direttamente dentro l'oggetto Restaurant passato la lista delle cucine.
     * 
     * 
     * @param r - l'oggetto che contiene le informazioni del ristorante
     * @throws SQLException 
     */
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
     * Ritorna la lista di tutte le cucine accettabili nel database.
     * 
     * @return - lista delle cucine esistenti
     * @throws SQLException 
     */
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
        } finally { 
            stm.close();
        }

        return r_l;

    }

    
    /**
     * Elimina dal database l'orario specificato
     * @param id
     * @param day
     * @param apertura
     * @param chiusura
     * @throws SQLException 
     */
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
        
    /**
     * Inserisce nel database l'orario specificato.
     * 
     * N.B.: l'orario di apertura è ottenuto come (ora_apertura+":"+minuti_apertura+":00") mentre quello di chiusura come (ora_chiusura+":"+minuti_chiusura+":00").
     * Perciò, i parametri passati devono essere nel formato giusto affinchè il risultato possa essere poi valutato come java.sql.Time
     * 
     * @param id
     * @param day
     * @param ora_apertura
     * @param minuti_apertura
     * @param ora_chiusura
     * @param minuti_chiusura
     * @return
     * @throws SQLException 
     */
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
    
    /**
     * Rimuove la cucina specificata dal ristorante identificato da "restaurantID".
     * 
     * @param restaurantID
     * @param cuisine
     * @throws SQLException 
     */
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
    
    /**
     * Inserici nel ristorante identificato da "restaurantID" la cucina specificata.
     * @param restaurantID
     * @param cuisineID
     * @return
     * @throws SQLException 
     */
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
    
    /**
     * Ritorna la città, la regione e lo stato in cui risiede il ristorante
     * @param restaurant
     * @return
     * @throws SQLException 
     */
    private String[] getRestaurantLocation(int restaurant) throws SQLException {
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

    
    
    /**
     * Metodo per modificare le informazioni di un ristorante.
     * Se un parametro ha come valore 'null' o la stringa vuota "", allora non viene sovrascritto.
     * Se cambiato l'indirizzo, calcola in automatico le nuove coordinate geografiche.
     * Se c'è stato un errore nel calcolo delle nuove coordinate, il metodo ritorna false prima di eseguire la query.
     * @param id - chiave che identifica il ristorante
     * @param name - nuovo nome del ristorante
     * @param description - nuova descrizione
     * @param url - nuovo url del sito
     * @param address - nuovo indirizzo
     * @param min_price - nuovo prezzo minimo
     * @param max_price - nuovo prezzo massimo
     * @return - true se l'esecuzione è andata a buon fine, false se non è stato possibile trovare le coordinate del nuovo indirizzo
     * @throws SQLException 
     */
    public boolean manageRestaurant(Integer id, String name, String description, String url, String address, Integer min_price, Integer max_price)throws SQLException {
        
        double[] coordinates = null;
        
        if(address != null && !address.isEmpty()){
            String[] location = getRestaurantLocation(id);
            coordinates = Util.getCoordinates(address, location[0], location[1], location[2]);
            
            if(coordinates == null || coordinates.length != 2){
                return false;
            }
        }
        
        
        StringBuilder query=new StringBuilder("UPDATE App.Restaurants SET ");
        
        
        ArrayList<String> lista = new ArrayList<>();
        if(name != null && !name.isEmpty()){
            lista.add("name=?");
        }
        if(description != null && !description.isEmpty()){
            lista.add("description=?");
        }
        if(url != null && !url.isEmpty()){
            lista.add("web_site_url=?");
        }
        if(address != null && !address.isEmpty()){
            lista.add("address=?");
            lista.add("latitude = ?");
            lista.add("longitude = ?");
        }
        if(min_price!=null){
            lista.add("min_price=?");
        }
        if(max_price!=null){
            lista.add("max_price=?");
        }
        
        for (int i=0; i<lista.size(); i++) {
            if(i>0){
                query.append(", ");
            }
            query.append(lista.get(i));
        }
        
        query.append(" WHERE id=?");
        
        //System.out.println(query);
        PreparedStatement stm = con.prepareStatement(query.toString());
        
        int i = 1;
        
        if(name != null && !name.isEmpty()){
            stm.setString(i++, name);
        }
        
        if(description != null && !description.isEmpty()){
            stm.setString(i++, description);
        }
        if(url != null && !url.isEmpty()){
            stm.setString(i++, url);
        }
        if(address != null && !address.isEmpty()){
            
            stm.setString(i++, address);
            stm.setDouble(i++, coordinates[0]);
            stm.setDouble(i++, coordinates[1]);
        }
        if(min_price!=null){
            stm.setDouble(i++, min_price);
        }
        if(max_price!=null){
            stm.setDouble(i++, max_price);
        }
        stm.setInt(i++, id);
        
        try {
            stm.executeUpdate();
        } finally {
            stm.close();
        }
        return true;
    }

    /**
     * Aggiorna il database con il nuovo percorso per il file contenente il QR CODE rappresentate il ristorante identificato da "id"
     * 
     * @param id
     * @param path
     * @throws SQLException 
     */
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
    
    /**
     * Cerca la prima foto da mostrare del ristorante.
     * Nello specifico, è la prima foto trovata dando, però, maggior priorità a quelle inserite dal proprietario del ristorante (avendo type = 1)
     * 
     * @param r
     * @throws SQLException 
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
        } finally {
            stm.close();
        }        
    }

    /**
     * Cerca le foto caricate sul ristorante.
     * Nello specifico, è la prima foto trovata (dando sempre maggior priorità a quelle inserite dal proprietario del ristorante)
     * viene assegnata all'attributo "firstPhoto" mentre le altre all'attributo "photos".
     * 
     * @param r
     * @throws SQLException 
     */
    public void getRestaurantPhotos(Restaurant r) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT name, path, type FROM APP.photos WHERE id_restaurant = ? ORDER BY type DESC");
        ArrayList<Photo> photos = new ArrayList<>();
        try {
            stm.setInt(1, r.getId());
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    photos.add(new Photo(rs.getString("name"), rs.getString("path"), rs.getInt("type")));
                }
            } finally {
                rs.close();
            }
        } finally { 
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
     * Inserisce una nuova foto con i parametri specificati nel ristorante e
     * restituisce la chiave generata automaticamente dal database .
     * @param name
     * @param path
     * @param restaurant_id
     * @return - la chiave primaria che identifica questa foto nel db
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
        } finally {
            stm.close();
        }
        return k;
    }

    /**
     * Inserisce una nuova recensione nel database con in parametri specificati.
     * Se l'esecuzione non è andata a buon fine a causa di un iserimento di una chiave duplicata
     * (l'utente ha già inserito una recensione in questo ristorante questo giorno), allora il metodo ritorna false.
     * Nel caso l'esecuzione sia andata a buon fine viene ritornato true.
     * 
     * Aggiorna in automatico il review_counter e il global_review della tabella APP.RESTAURANT
     * 
     * @param global_value
     * @param food
     * @param service
     * @param value_for_money
     * @param atmosphere
     * @param title
     * @param description
     * @param id_restaurant
     * @param id_creator
     * @param id_photo
     * @return - true se l'esecuzione è andata a buon fine, false se l'utente ha già recesnito questo ristorante oggi
     * @throws SQLException 
     */
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
        } finally {
            stm.close();
        }
        
        stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET REVIEW_COUNTER = REVIEW_COUNTER + 1, GLOBAL_REVIEW = GLOBAL_REVIEW + ? WHERE id = ?");
        try {
            stm.setInt(1, global_value);
            stm.setInt(2, id_restaurant);
            
            int rs = stm.executeUpdate();

        } finally { 
            stm.close();
        }
        
        return true;
    }
    
    /**
     * Cerca nella tabella APP.REVIEWS le recensioni del ristorante specificato.
     * 
     * @param res
     * @throws SQLException 
     */
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
    
    /**
     * Cerca nella tabella APP.REVIEWS le recensioni del ristorante specificato che abbiano un titolo, evitando così le recensioni "veloci" (quelle in cui è specificato solo il voto generico)
     * @param res
     * @throws SQLException 
     */
    public void getCompletedReviews(Restaurant res) throws SQLException {

        PreparedStatement stm = con.prepareStatement("SELECT r.*, u.name as name, u.surname as surname, p.path as photo_path, p.name as photo_name FROM App.REVIEWS r JOIN App.USERS u ON u.id = r.id_creator LEFT OUTER JOIN APP.PHOTOS p ON r.id_photo = p.id WHERE r.ID_RESTAURANT = ? AND r.title IS NOT NULL");

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
                    
                    r.setPhotoPath(rs.getString("photo_path"));
                    r.setPhotoName(rs.getString("photo_name"));
                    
                    res.addRecensione(r);
                }
            } finally {
                rs.close();
            }
        } finally {
            stm.close();
        }

        
    }
    
    /**
     * Controlla se l'utente identificato dalla chiave contenuta in "user" coincide con l'utente proprietario del ristorante identificato dalla chiave contenuta in "restaurant".
     * @param user
     * @param restaurant
     * @return
     * @throws SQLException 
     */
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
     * Ritorna la chiave identificativa del proprietario del ristorante identificato dalla chiave contenuta in "restaurant".
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
     * Metodo che crea una nuova notifica di inserimento/segnalazione foto nel database.
     * Se "target" viene impostato ad un valore minore di 0, viene inserito il valore null.
     * In tal caso la notifica viene inviata a tutti gli admin.
     * @param photo - id della foto
     * @param target - identificativo del ristoratore proprietario del ristorante o un numero negativo per indicare tutti gli admin
     * @throws SQLException
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

    /**
     * Metodo che crea una nuova notifica di reclamo ristorante per gli admin.
     * 
     * @param user - identificativo dell'utente che vuole reclamare il ristorante
     * @param restaurant - identificativo del ristorante
     * @throws SQLException 
     */
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

    /**
     * Metodo che conta le notifiche esistenti nella tabella notifiche_foto per l'user in questione.
     * 
     * @param u
     * @return
     * @throws SQLException 
     */
    public int contaNotificheFoto(User u) throws SQLException{
        StringBuilder builder = new StringBuilder("SELECT COUNT(*) FROM APP.NOTIFICATIONS_PHOTO N ");

        
       if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ?");
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }

        PreparedStatement stm = con.prepareStatement(builder.toString());
        
        if (u.getCharType() == 'r') {
            stm.setInt(1, u.getId());
        }
        
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
    
    /**
     * Metodo che restituisce una tra le notifiche foto esistenti per l'utente corrente (ristoratore o admin)
     * Se non son presenti notifiche ritorna null.
     * 
     * @param u
     * @return
     * @throws SQLException 
     */
    public NotificaFoto getUnaNotificaFoto(User u) throws SQLException {

        StringBuilder builder = new StringBuilder("SELECT R.name, R.id, P.id, P.path, P.name FROM APP.NOTIFICATIONS_PHOTO N JOIN APP.PHOTOS P "
                                                 + "ON N.id_photo = P.id JOIN App.RESTAURANTS R ON P.id_restaurant = R.id ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ?");
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }
        builder.append(" FETCH FIRST 1 ROWS ONLY");

        PreparedStatement stm = con.prepareStatement(builder.toString());
        
        if (u.getCharType() == 'r') {
            stm.setInt(1, u.getId());
        }        
        
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

    /**
     * Restituisce la lista di notifiche foto presenti nel database per l'utente, differenziando tra:
     * ristoratore (prende tutte le notifiche che puntano al suo id)
     * admin (prende tutte le notifiche con id_target null)
     * 
     * @param u
     * @return
     * @throws SQLException 
     */
    public List<NotificaFoto> getNotificheFoto(User u) throws SQLException {

        StringBuilder builder = new StringBuilder("SELECT R.name, R.id, P.id, P.path, P.name FROM APP.NOTIFICATIONS_PHOTO N JOIN APP.PHOTOS P "
                                                 + "ON N.id_photo = P.id JOIN App.RESTAURANTS R ON P.id_restaurant = R.id ");

        if (u.getCharType() == 'r') {
            builder.append("WHERE id_target = ?");
        } else if (u.getCharType() == 'a') {
            builder.append("WHERE id_target IS NULL");
        }
        
        

        PreparedStatement stm = con.prepareStatement(builder.toString());
        
        if (u.getCharType() == 'r') {
            stm.setInt(1, u.getId());
        } 
        
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

    

    /**
     * Metodo che conta le notifiche esistenti nella tabella notifiche_reclami per l'user in questione (admin)
     * 
     * @return
     * @throws SQLException 
     */ 
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
    
    /**
     * Metodo che restituisce una tra le notifiche di reclami esistenti per l'utente corrente (admin)
     * Se non son presenti notifiche ritorna null.
     * 
     * @return
     * @throws SQLException 
     */
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

    /**
     * Restituisce la lista di notifiche reclami presenti nel database per l'admin.
     * 
     * @return
     * @throws SQLException 
     */
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

        return list;

    }

    
    /**
     * Approva la foto in questione, eliminando la notifica di conferma/supervisione dalla tabella notifiche_foto
     * 
     * @param id_photo
     * @throws SQLException 
     */
    public void confermaFoto(int id_photo) throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM APP.NOTIFICATIONS_PHOTO WHERE ID_PHOTO=?");
        
        try{
            stm.setInt(1, id_photo);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    /**
     * Segnala ad un admin la foto, cambiando il target della notifica in NULL
     * 
     * @param id_photo
     * @throws SQLException 
     */
    public void segnalaAdminFoto(int id_photo) throws SQLException {
        PreparedStatement stm = con.prepareStatement("UPDATE APP.NOTIFICATIONS_PHOTO SET ID_TARGET=NULL WHERE ID_PHOTO=?");
        
        try{
            stm.setInt(1, id_photo);
            stm.executeUpdate();
        } finally {
            stm.close();
        }
    }
    
    /**
     * Elimina una foto segnalata dalla tabella delle foto, elimina inoltre la notifica ad essa collegata
     * 
     * @param id_photo
     * @throws SQLException 
     */
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
    
    /**
     * Elimina la notifica di un reclamo di ristorante dalla tabella apposita
     * 
     * @param id_restaurant
     * @param id_user
     * @throws SQLException 
     */
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
    
    /**
     * Conferma il reclamo di un ristorante, aggiornando:
     * l'owner_id del ristorante
     * il tipo dell'utente (user->ristoratore)
     * 
     * ed eliminando la notifica di reclamo dalla sua tabella
     * 
     * @param id_restaurant
     * @param id_user
     * @throws SQLException 
     */
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
    
    
    
    
    /**
     * Metodo che calcola per tutti i ristoranti che ne sono ancora privi le coordinate geografiche a partire dal loro indirizzo
     * 
     * @throws SQLException 
     */
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
            
            double[] coordinates = Util.getCoordinates(r.getAddress(), r.getCity(), r.getRegion(), r.getState());
            if(coordinates != null && coordinates.length == 2){
                stm = con.prepareStatement("UPDATE APP.RESTAURANTS SET latitude = ?, longitude = ? WHERE id = ?");
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
        
    }

    /**
     * Crea un file contenente insert per backup e testing
     * 
     * @param file_name
     * @param table
     * @param id_as_default
     * @throws IOException
     * @throws SQLException 
     */    
    public void creaInsert(String file_name, String table, boolean id_as_default) throws IOException, SQLException{
        PrintWriter out = new PrintWriter(new FileWriter("/home/gabriele/Scrivania/" + file_name, false));
        
        PreparedStatement stm = con.prepareStatement("SELECT * FROM APP." + table);
        try {
            ResultSet rs = stm.executeQuery();
            try {
                while (rs.next()) {
                    out.print("INSERT INTO APP." + table + " VALUES (");
                    for(int i=1; i<=rs.getMetaData().getColumnCount(); i++){
                        if(i>1){
                            out.print(", ");
                        }
                        if(id_as_default && rs.getMetaData().getColumnLabel(i).toLowerCase().equals("id")){
                            out.print("default");
                        }else if(rs.getObject(i) == null){
                            out.print("null");
                        }else if(rs.getObject(i) instanceof String || rs.getObject(i) instanceof java.sql.Date || rs.getObject(i) instanceof java.sql.Time || rs.getObject(i) instanceof java.sql.Timestamp){
                            out.print("'" + rs.getObject(i) + "'");
                        }else{
                            out.print(rs.getObject(i));
                        }
                        
                    }
                    out.println(");");
                }
            } finally {
                rs.close();
            }
            
        } finally {
            stm.close();
        }
        out.close();
    } 
    
}

