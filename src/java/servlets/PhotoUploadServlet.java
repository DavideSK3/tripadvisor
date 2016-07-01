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
public class PhotoUploadServlet extends HttpServlet {
    
    private DBManager manager;

    private String dirName;
    
    private RenamePolicy rp = new RenamePolicy();

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
        
        // read the uploadDir from the servlet parameters
        dirName = getServletContext().getRealPath("/WEB-INF") + "/" + super.getServletContext().getInitParameter("photoDir");
        File p_dir = new File(dirName);    
        p_dir.mkdirs();
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Integer id_restaurant;
        String name;
        String path;
        try {

            
            /*
             * TODO - meglio usare una sola istanza di RenamePolicy o questo può avere problemi in caso di richieste concorrenti gestite in multithread???
             */
            MultipartRequest multi = new MultipartRequest(request, dirName+"/temp", 50*1024*1024, "ISO-8859-1", rp);
            
            
            name = multi.getParameter("photoName");
            
            id_restaurant = Integer.parseInt(multi.getParameter("id_restaurant"));
            
            
            
            File photo = multi.getFile("img");
            
            File copy = new File(dirName+"/"+id_restaurant + "/" + photo.getName());
            
            Files.move(photo, copy);
            photo.delete();
            
            path = copy.getAbsolutePath();
            System.out.println("Salvata immagine al percorso: " + path);
            
            int id = -1;
            try {
                id = manager.insertPhoto(name, path, id_restaurant);
            } catch (SQLException ex) {
                Logger.getLogger(PhotoUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            request.setAttribute("photo_id", id);
            
            request.setAttribute("multi", multi);
        }catch (IOException lEx) {
            this.getServletContext().log("error saving file", lEx);
        }
    }
    
    private class RenamePolicy implements FileRenamePolicy{
 
        @Override
        public File rename(File f) {
            File f1 = new File(dirName + "/temp/" + new Date().toString().replace(" ", "") + "." + FilenameUtils.getExtension(f.getName()));
            
            f.renameTo(f1);
            return f1;
        }
        
    }
}
