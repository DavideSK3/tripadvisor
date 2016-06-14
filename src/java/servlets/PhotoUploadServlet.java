/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        this.manager = (DBManager)super.getServletContext().getAttribute("dbmanager");
        
        // read the uploadDir from the servlet parameters
        dirName = super.getServletContext().getInitParameter("photoDir");

    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id_restaurant = Integer.parseInt(request.getParameter("id_restaurant"));
        String name = request.getParameter("photoName");
        String path = null;
        try {

            // Use an advanced form of the constructor that specifies a character

            // encoding of the request (not of the file contents) and a file

            // rename policy.
            File dir = new File(dirName+"/"+id_restaurant);    
            dir.mkdirs();
            MultipartRequest multi = new MultipartRequest(request, dirName+"/"+id_restaurant, 50*1024*1024, "ISO-8859-1", new RenamePolicy(dirName+"/"+id_restaurant));

            
           
            Enumeration files = multi.getFileNames();

            if (files.hasMoreElements()) {

                path = multi.getFilesystemName(name);

            }

        }catch (IOException lEx) {
            this.getServletContext().log("error saving file", lEx);
        }
        
        if(path!= null){
            int id = -1;
            try {
                id = manager.insertPhoto(name, path, id_restaurant);
            } catch (SQLException ex) {
                Logger.getLogger(PhotoUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(id >=0){
                request.setAttribute("photo_id", id);
            }
        }
    
    }
    
    private class RenamePolicy implements FileRenamePolicy{

        private String dirName;
        
        public RenamePolicy(String dir){
            super();
            dirName = dir;
                   
        }
        
        @Override
        public File rename(File f) {
            File f1 = new File(dirName + "/" + new Date().toString().replace(" ", "") + "." + FilenameUtils.getExtension(f.getName()));
            
            f.renameTo(f1);
            return f1;
        }
        
    }
}
