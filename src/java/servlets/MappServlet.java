/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.Restaurant;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gabriele
 */
public class MappServlet extends HttpServlet {

    
    DBManager manager;
    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        int ID = -1;
        RequestDispatcher rd = request.getRequestDispatcher("mappa.jsp");;
        
        if(id != null){
            try{
                ID = Integer.parseInt(id);

            }catch (NumberFormatException e){
                rd = request.getRequestDispatcher("message.jsp");
                request.setAttribute("message", "Id non valido");
            }
        }else{
            rd = request.getRequestDispatcher("message.jsp");
            request.setAttribute("message", "Id non valido");
        }
        
        Restaurant r = null;
        
        if(ID >= 0){
            try {
                r = manager.getRestaurant(ID);
            } catch (SQLException ex) {
                Logger.getLogger(MappServlet.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
        
        if(r!= null){
            
            request.setAttribute("longitude", r.getLongitude());
            request.setAttribute("latitude", r.getLatitude());
            
        }
        
        rd.forward(request, response);
        
        
    }

}
