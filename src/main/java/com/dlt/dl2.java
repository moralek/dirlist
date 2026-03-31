package com.dlt;

import java.io.PrintWriter;
import java.io.File;

public class dl2 {

    public static void printStyles(PrintWriter out) {
        out.println("<style>");
        out.println("body { font-family: 'Inter', sans-serif; font-size: 14px; background-color: #1E1E1E; color: #FFFFFF; padding: 20px; }");
        out.println("table, th, td, a { font-family: 'Inter', sans-serif; font-size: 14px; font-weight: normal; color: #FFFFFF; }");
        out.println("h1 { font-size: 16px; margin-bottom: 15px; color: #32CD32; }");
        out.println(".server-info { position: absolute; top: 10px; right: 20px; font-size: 14px; color: #CCCCCC; text-align: right; line-height: 1.6; }");
        out.println("table { border-collapse: collapse; margin: 20px 0; }");
        out.println("th, td { padding: 6px 12px; border-bottom: 1px solid #333; text-align: left; }");
        out.println("tr:hover { background-color: #333333; }");
        out.println("td:first-child { width: 300px; max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }");
        out.println("a { text-decoration: none; color: #40CFFF; }");
        out.println("a:hover { color: #1E90FF; }");
        out.println(".search-box { position: relative; display: inline-block; margin-bottom: 15px; }");
        out.println(".search-box input[type='text'] { width: 250px; padding: 8px 12px; border: 1px solid #555; border-radius: 5px; background-color: #333; color: #FFFFFF; font-size: 14px; padding-right: 50px; }");
        out.println(".search-box .clear-btn, .search-box .search-btn { position: absolute; top: 50%; transform: translateY(-50%); cursor: pointer; font-size: 16px; }");
        out.println(".search-box .clear-btn { right: 40px; color: #AAAAAA; }");
        out.println(".search-box .search-btn { right: 10px; color: #40CFFF; }");
        out.println(".search-box .clear-btn:hover { color: #FFFFFF; }");
        out.println(".search-box .search-btn:hover { color: #1E90FF; }");
        out.println("</style>");
    }
	
	public static void printScripts(PrintWriter out) {
		out.println("<script>");

		out.println("function toggleOSInfo() {");
		out.println("  const infoDiv = document.getElementById('osInfo');");
		out.println("  if (infoDiv.style.display === 'none') {");
		out.println("    fetch('si')");
		out.println("      .then(response => response.text())");
		out.println("      .then(data => {");
		out.println("        const hasDeleteIcons = document.querySelectorAll('.del-icon').length > 0;");
		out.println("        const deleteLink = hasDeleteIcons ? '<a href=\"#\" id=\"toggleDeleteLink\" onclick=\"toggleDeleteIcons(); return false;\">mostrar eliminar</a>' : '';");
		out.println("        infoDiv.innerHTML = '<pre>' + data + '</pre>' + deleteLink;");
		out.println("        infoDiv.style.display = 'block';");
		out.println("      });");
		out.println("  } else {");
		out.println("    infoDiv.style.display = 'none';");
		out.println("  }");
		out.println("}");

		out.println("function confirmDelete(path) {");
		out.println("  if (confirm('¿Seguro que deseas eliminar este archivo?\\n' + path)) {");
		out.println("    const form = document.createElement('form');");
		out.println("    form.method = 'POST';");
		out.println("    form.action = 'del';");
		out.println("    const input = document.createElement('input');");
		out.println("    input.type = 'hidden';");
		out.println("    input.name = 'file';");
		out.println("    input.value = path;");
		out.println("    form.appendChild(input);");
		out.println("    document.body.appendChild(form);");
		out.println("    form.submit();");
		out.println("  }");
		out.println("}");

		out.println("function toggleDeleteIcons() {");
		out.println("  const icons = document.querySelectorAll('.del-icon');");
		out.println("  const toggleLink = document.getElementById('toggleDeleteLink');");
		out.println("  if (!icons.length || !toggleLink) return;");
		out.println("  const visible = icons.length && icons[0].style.display !== 'none';");
		out.println("  icons.forEach(el => el.style.display = visible ? 'none' : 'inline');");
		out.println("  toggleLink.innerText = visible ? 'mostrar eliminar' : 'ocultar eliminar';");
		out.println("}");

		out.println("function clearFilter() {");
		out.println("  const input = document.querySelector(\"input[name='filter']\");");
		out.println("  if (input) {");
		out.println("    input.value = '';");
		out.println("    document.forms['filterForm'].submit();");
		out.println("  }");
		out.println("}");

		out.println("</script>");
	}

	public static void printServerInfo(PrintWriter out, String currentUser, String userHome) {
		out.println("<div class='server-info'>");
		out.println("User: " + currentUser + "<br>");
		out.println("home: <a href='?path=" + userHome + "'>" + userHome.replace("\\", "/") + "/</a><br>");
		out.println("<a href='login?logout=1'>Logout</a><br>");
		out.println("<a href='#' onclick='toggleOSInfo(); return false;'>OS Info</a>");
		out.println("<div id='osInfo' style='display:none; margin-top:5px; font-size:13px; color:#CCCCCC; font-family: Inter, sans-serif;'></div>");
		out.println("</div>");
	}


    public static void printBreadcrumbs(PrintWriter out, String path) {
        out.println("<h1>");
        String cleanPath = path.replace("\\", "/");
        if (cleanPath.matches("^[A-Za-z]:/?$")) {
            out.println("<span><a href='?path=/'>/</a></span>");
        } else {
            if (cleanPath.matches("^[A-Za-z]:/.*")) {
                cleanPath = cleanPath.substring(2);
            }
            out.println("<span><a href='?path=/'>/</a></span>");
            String[] segments = cleanPath.split("/");
            StringBuilder accumulated = new StringBuilder();
            for (String segment : segments) {
                if (!segment.isEmpty()) {
                    accumulated.append("/").append(segment);
                    out.println("<span><a href='?path=" + accumulated.toString() + "'>" + segment + "</a></span> /");
                }
            }
        }
        out.println("</h1>");
    }

    public static void printFilterForm(PrintWriter out, String path, String filter, String sort, String order) {
        out.println("<form name='filterForm' method='get' action='' style='display: inline;'>");
        out.println("<div class='search-box'>");
        out.println("<input type='text' name='filter' value='" + (filter != null ? filter : "") + "' placeholder='Buscar...'>");
        out.println("<span class='clear-btn' onclick='clearFilter()'><i class='fas fa-times'></i></span>");
        out.println("<span class='search-btn' onclick='document.forms[\"filterForm\"].submit();'><i class='fas fa-search'></i></span>");
        out.println("</div>");
        out.println("<input type='hidden' name='path' value='" + path + "'>");
        out.println("<input type='hidden' name='sort' value='" + sort + "'>");
        out.println("<input type='hidden' name='order' value='" + order + "'>");
        out.println("</form>");
    }

    public static void printTableHeader(PrintWriter out, String sort, String order, String path, String filter) {
        out.println("<table>");
        out.println("<tr>");
        out.println(utils.getSortableHeader("Name", "name", sort, order, path, filter));
        out.println(utils.getSortableHeader("Owner", "owner", sort, order, path, filter));
        out.println(utils.getSortableHeader("rwx", "permissions", sort, order, path, filter));
        out.println(utils.getSortableHeader("Created", "created", sort, order, path, filter));
        out.println(utils.getSortableHeader("Last Modified", "date", sort, order, path, filter));
        out.println(utils.getSortableHeader("Size", "size", sort, order, path, filter));
        out.println("<th></th>");
        out.println("</tr>");
    }

}
