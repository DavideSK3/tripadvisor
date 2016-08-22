/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.common.io.Files;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import db.DBManager;
import db.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabriele
 */
public class ClaimRestaurantServlet extends HttpServlet {
    
    private DBManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Integer ID=null;
        String message;
        
        ID = Integer.parseInt(request.getParameter("restaurantID"));
        Integer user = ((User)(session.getAttribute("user"))).getId();
        System.out.println(ID);
        System.out.println(user);
        
        try {
            manager.richiestaReclamoRistorante(user,ID);
            message = "Richiesta inoltrata agli Amministratori!";
        } catch (SQLException ex) {
            message = "La richiesta non è andata a buon fine!";
            Logger.getLogger(ClaimRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.setAttribute("message", message);
        RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
        rd.forward(request, response);
    }
}
