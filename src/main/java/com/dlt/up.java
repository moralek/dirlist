package com.dlt;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@MultipartConfig
public class up extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        Part filePart = request.getPart("file");
        String name = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        File target = new File(path, name);
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if (path == null || name.isEmpty()) {
                out.println("<p style='color:red;'>Parámetros inválidos.</p>");
                return;
            }

            if (!new File(path).canWrite()) {
                out.println("<p style='color:red;'>Sin permiso para escribir aquí.</p>");
                return;
            }

            if (target.exists()) {
                out.println("<p style='color:red;'>Ya existe un archivo con ese nombre.</p>");
                return;
            }

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, target.toPath());
                response.sendRedirect("list?path=" + path);
            } catch (Exception e) {
                out.println("<p style='color:red;'>Error al guardar archivo.</p>");
            }
        }
    }
}
