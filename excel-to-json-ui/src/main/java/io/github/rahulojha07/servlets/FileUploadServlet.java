package io.github.rahulojha07.servlets;

import io.github.rahulojha07.services.ExcelToJsonService;
import io.github.rahulojha07.services.ExcelToJsonServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/fileuploadservlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = LogManager.getLogger(FileUploadServlet.class);
    private static String responseText = "null";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        if (responseText.equals("null")) {
            response.setContentType("text/html");
            response.getWriter().append("Not the correct way to get here!\n").append("<p style=\"font-size:100px\">&#128540;</p>");
        }else {
            response.getWriter().append(responseText);
            responseText = "null";
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Receive file uploaded to the Servlet from the HTML5 form */
        Part filePart = request.getPart("file");


        String currentWorkingFolder = System.getProperty("user.dir");
        logger.info(currentWorkingFolder);

        String filePathSeparator = System.getProperty("file.separator");
        logger.info(filePathSeparator);

        Files.createDirectories(Paths.get(currentWorkingFolder + filePathSeparator+ "TempFiles"));

        String fileName = currentWorkingFolder + filePathSeparator+ "TempFiles" + filePathSeparator + filePart.getSubmittedFileName();
        logger.info(">>>>>>>>>>>>>" + fileName);
        for (Part part : request.getParts()) {
            part.getName();
            part.write(fileName);
        }

        ExcelToJsonService excel = new ExcelToJsonServiceImpl();

        response.setContentType("text/json");
        logger.info("The file uploaded sucessfully.");
        responseText = ((ExcelToJsonServiceImpl) excel).createJsonStringText(fileName);
    }
}
