/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.RequestDispatcher;


public class PasswordRecoveryServlet extends HttpServlet {

    private DBManager manager;

    
    @Override
    public void init() throws ServletException {

        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    
    /**
     * Controlla che la mail fornita appartenga a un utente valido, crea un token di validità 2 ore e chiama il metodo sendMail
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String mail = request.getParameter("mail");
        
        User user = null;
        try {
            user = manager.getUserByMail(mail);
        } catch (SQLException ex) {
            Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(user == null){
            request.setAttribute("message", "Indirizzo email non conosciuto.");
            
            RequestDispatcher rd = request.getRequestDispatcher("/mail_form.jsp");

            rd.forward(request, response);
            
        }else{
            final String token = UUID.randomUUID().toString();
            
            try {
                manager.insertToken(user.getId(), token, 2);
            } catch (SQLException ex) {
                System.out.println("errore inserimento token");
                Logger.getLogger(PasswordRecoveryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            sendMail(user.getEmail(), user.getName() + " " + user.getSurname(), token);
            
            request.setAttribute("message", "E' stata inviata una mail al suo indirizzo di posta elettronica contenente un link alla pagina per reimpostare la sua password.");
            RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");

            rd.forward(request, response);
        }
        
        
    }
    
    private final String username = "gabriele.cesa@gmail.com";
    private final String password = "aithixbtwdqyrppd";
    
    /**
     * Crea ed invia il contenuto della mail di recovery password all'indirizzo specificato, usando un token dalla validità limitata
     * @param to
     * @param user
     * @param token 
     */
    private void sendMail(String to, String user, String token){
        
        String messageText = 
                "Salve " + user + "\n\n" + 
                "Per ripristinare la sua password prema il seguente link  (il link ha una validità di 2 ore):\n\n" + 
                "http://" + getServletContext().getInitParameter("webAddress") + getServletContext().getContextPath() + "/ChangePassword?t="+token +"\n\n\n";
                
        
        
        
        Properties props = System.getProperties();

        props.put("mail.smtp.host", "smtp.gmail.com" );
        props.setProperty("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put( "mail.debug", "false" );
        
        
        Session session = Session.getDefaultInstance(props, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        Message message = new MimeMessage( session );
        InternetAddress from_address = null, to_address[] = null;
        
            
        try {
            from_address = new InternetAddress(username);
            
            to_address = InternetAddress.parse(to);
            
            message.setText(messageText);
            message.setSubject("Recupero Password");
            message.setSentDate(new Date());
            message.setFrom(from_address);
            
            message.setRecipients(Message.RecipientType.TO, to_address);
            
            
            Transport transport = session.getTransport("smtps");
            transport.connect ("smtp.gmail.com", 465, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
