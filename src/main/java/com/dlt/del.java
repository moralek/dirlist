package com.dlt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class del extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filePath = request.getParameter("file");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if (filePath == null || filePath.isEmpty()) {
                out.println("<p style='color: red;'>Ruta no especificada.</p>");
                return;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                out.println("<p style='color: red;'>El archivo no existe.</p>");
                return;
            }

            if (!file.isFile()) {
                out.println("<p style='color: red;'>No es un archivo válido.</p>");
                return;
            }

            if (!file.canWrite()) {
                out.println("<p style='color: red;'>Sin permisos para eliminar el archivo.</p>");
                return;
            }

            boolean deleted = file.delete();
            if (deleted) {
                response.sendRedirect("list?path=" + file.getParent());
            } else {
                out.println("<p style='color: red;'>No se pudo eliminar el archivo.</p>");
            }
        }
    }
}
