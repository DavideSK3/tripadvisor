/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Orario;
import db.Restaurant;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author gabriele
 */
public class RestaurantServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }
    
    protected void manageRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd;
        
        String key = req.getParameter("restaurantID");
        
        Restaurant r = null;
        
        if(key != null){
            
            
            try{
                r = manager.getRestaurant(Integer.parseInt(key));
                
                if(r != null){
                    req.setAttribute("restaurant", r);
                    rd = req.getRequestDispatcher("/restaurant.jsp");
                }else{
                    req.setAttribute("message", "Ristorante non esistente");
                    rd = req.getRequestDispatcher("/message.jsp");
                }
                
            }catch (SQLException ex){
                req.setAttribute("message", "Ops, c'è stato un errore");
                rd = req.getRequestDispatcher("/message.jsp");
                Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NumberFormatException ex){
                req.setAttribute("message", "Ristorante non esistente");
                rd = req.getRequestDispatcher("/message.jsp");
            }
            
        }else{
            
            rd = req.getRequestDispatcher("/restaurant_list.jsp");
        }
        
        if(r != null){
            try{
                manager.getRestaurantPhotos(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                manager.getRestaurantTimes(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                manager.calcolaPosizioneInClassifica(r);
            } catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                manager.getReviews(r);
            }catch (SQLException ex) {
                Logger.getLogger(RestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            getQR(r);
        }
        
        rd.include(req, resp);
        
        if(r!=null){
            File f = new File(getServletContext().getRealPath("/"+r.getQr_path()));
            f.deleteOnExit();
        }
        
    }
    
    public void getQR(Restaurant r){
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
                s.append("   ")
                    .append(o.getGiorno())
                    .append(": ")
                    .append(o.getAperturaString())
                    .append(" - ")
                    .append(o.getChiusuraString())
                    .append("\n");
            }
        }
        
        
        
        ByteArrayOutputStream out = QRCode.from(s.toString()).to(ImageType.JPG).withSize(150, 150).stream();
        
        File dir = new File(getServletContext().getRealPath("") +  super.getServletContext().getInitParameter("qrDir"));

        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "dat");

        File file = new File(dir, name);

        try {
                FileOutputStream fout = new FileOutputStream(file);

                fout.write(out.toByteArray());

                fout.flush();
                fout.close();

        } catch (FileNotFoundException e) {
                // Do Logging
        } catch (IOException e) {
                // Do Logging
        }
        r.setQr_path(super.getServletContext().getInitParameter("qrDir") + "/" + name);
        
    }
    
}
