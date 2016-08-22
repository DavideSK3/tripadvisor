/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.NotificaFoto;
import db.Restaurant;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class NotificationRestaurantServlet extends HttpServlet {

    private DBManager manager;
    
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     
        HttpSession session = req.getSession(true);
        List<NotificaFoto> results = null;
           
        try {
            results = manager.getNotificheFoto((User)(session.getAttribute("user")));
        }catch (SQLException ex) {
            Logger.getLogger(ManageRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("results_foto",results);


        RequestDispatcher rd = req.getRequestDispatcher("notification.jsp");
        
        rd.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        
        HttpSession session = req.getSession(true);
        String message="";
        
        String segnala_foto= req.getParameter("segnala_foto");
        if(segnala_foto!=null && !segnala_foto.isEmpty()){
            Integer id_photo = Integer.parseInt(req.getParameter("id_photo"));
            if(segnala_foto.equals("conferma")){
                try {
                    manager.confermaFoto(id_photo);
                    message = "Foto "+id_photo+" Approvata!";
                } catch (SQLException ex) {
                    message = "L'operazione non è andata a buon fine";
                    Logger.getLogger(NotificationRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else if(segnala_foto.equals("segnala")){
                 try {
                    manager.segnalaAdminFoto(id_photo);
                    message = "Foto "+id_photo+" Segnalata agli Amministratori!";
                } catch (SQLException ex) {
                    message = "L'operazione non è andata a buon fine";
                    Logger.getLogger(NotificationRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("message.jsp");
        
        rd.forward(req, resp);
        
    }

}
