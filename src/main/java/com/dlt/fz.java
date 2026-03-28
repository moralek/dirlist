package com.dlt;

/*
    File and Folder Zipper (ZIP)
*/

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class fz extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");

        if (path != null && !path.isEmpty()) {
            File file = new File(path);

            if (file.exists()) {
                List<String> inaccessibleFiles = new ArrayList<>();

                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + ".zip\"");

                try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
                    if (file.isDirectory()) {
                        zipFolder(file, file.getName(), zos, inaccessibleFiles);
                    } else {
                        zipFile(file, file.getName(), zos, inaccessibleFiles);
                    }

                    if (!inaccessibleFiles.isEmpty()) {
                        zos.putNextEntry(new ZipEntry("inaccessible_files.txt"));
                        for (String fileName : inaccessibleFiles) {
                            zos.write((fileName + "\n").getBytes());
                        }
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    showErrorPage(response, "Error al comprimir.");
                }
            } else {
                showErrorPage(response, "Archivo o carpeta no encontrado.");
            }
        } else {
            showErrorPage(response, "Ruta no especificada.");
        }
    }

    private void zipFolder(File folder, String parentFolder, ZipOutputStream zos, List<String> inaccessibleFiles) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String zipEntryName = parentFolder + "/" + file.getName();
                if (file.isDirectory()) {
                    zipFolder(file, zipEntryName, zos, inaccessibleFiles);
                } else {
                    zipFile(file, zipEntryName, zos, inaccessibleFiles);
                }
            }
        }
    }

    private void zipFile(File file, String zipEntryName, ZipOutputStream zos, List<String> inaccessibleFiles) throws IOException {
        if (file.canRead()) {
            try (InputStream fis = Files.newInputStream(file.toPath())) {
                zos.putNextEntry(new ZipEntry(zipEntryName));
                byte[] buffer = new byte[4096];
                int length;
                while ((length = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            } catch (IOException e) {
                inaccessibleFiles.add(zipEntryName);
            }
        } else {
            inaccessibleFiles.add(zipEntryName);
        }
    }

    private void showErrorPage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write("<html><body><h2>" + message + "</h2></body></html>");
        }
    }
}
