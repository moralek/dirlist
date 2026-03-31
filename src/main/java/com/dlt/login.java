package com.dlt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class login extends HttpServlet {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logout = request.getParameter("logout");
        if ("1".equals(logout)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }

        renderLogin(response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        renderLogin(response, "Credenciales incorrectas.");
    }

    private void renderLogin(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Login</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: #1e1e1e; color: #fff; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; }");
            out.println(".card { width: 320px; background: #2a2a2a; padding: 24px; border-radius: 8px; box-shadow: 0 10px 30px rgba(0,0,0,.35); }");
            out.println("h1 { margin: 0 0 16px; font-size: 22px; }");
            out.println("label { display: block; margin: 12px 0 6px; }");
            out.println("input { width: 100%; box-sizing: border-box; padding: 10px; border: 1px solid #555; border-radius: 4px; background: #111; color: #fff; }");
            out.println("button { width: 100%; margin-top: 16px; padding: 10px; border: 0; border-radius: 4px; background: #2d8cff; color: #fff; cursor: pointer; }");
            out.println(".error { color: #ff7b7b; margin-top: 12px; }");
            out.println("</style></head><body>");
            out.println("<div class='card'>");
            out.println("<h1>Acceso</h1>");
            out.println("<form method='post' action='login'>");
            out.println("<label for='username'>Usuario</label>");
            out.println("<input id='username' name='username' type='text' autocomplete='username' required>");
            out.println("<label for='password'>Clave</label>");
            out.println("<input id='password' name='password' type='password' autocomplete='current-password' required>");
            out.println("<button type='submit'>Entrar</button>");
            if (errorMessage != null) {
                out.println("<div class='error'>" + errorMessage + "</div>");
            }
            out.println("</form>");
            out.println("</div></body></html>");
        }
    }
}
