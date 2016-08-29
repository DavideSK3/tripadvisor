/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Termina la sessione in caso di Logout dell'utente
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {

            session.removeAttribute("user");

            session.invalidate();

        }
        
        
        req.setAttribute("message", "Logout effettuato con successo");

        

        RequestDispatcher rd = req.getRequestDispatcher("message.jsp");
        rd.forward(req, resp);

    }
}
