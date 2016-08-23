/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import db.DBManager;
import db.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabriele
 */
public class EncodingFilter implements Filter {
    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    
    private DBManager manager;
    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        this.manager = (DBManager)filterConfig.getServletContext().getAttribute("dbmanager");
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        
        HttpSession session = req.getSession(false);
        
        
        if(session != null && session.getAttribute("user")!=null){
            if(((User)session.getAttribute("user")).getCharType() == 'r'){
                try {
                    request.setAttribute("notificaFoto", manager.getUnaNotificaFoto((User)session.getAttribute("user")));
                    request.setAttribute("numeroNotifiche", manager.contaNotificheFoto((User)session.getAttribute("user")));
                } catch (SQLException ex) {
                    Logger.getLogger(EncodingFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(((User)session.getAttribute("user")).getCharType() == 'a'){
                try {
                    request.setAttribute("notificaFoto", manager.getUnaNotificaFoto((User)session.getAttribute("user")));
                    request.setAttribute("notificaReclamo", manager.getUnReclamoRistorante());
                    request.setAttribute("numeroNotifiche", manager.contaNotificheFoto((User)session.getAttribute("user")) + manager.contaNotificheReclami());
                } catch (SQLException ex) {
                    Logger.getLogger(EncodingFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        chain.doFilter(request, response);
        
    }

   

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    
    
}
