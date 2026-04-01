package com.dlt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class lg extends HttpServlet {

    private static final String USERNAME = "AJANA2";
    private static final String PASSWORD = "SIGAS8";

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
            out.println(".pw { position: relative; }");
            out.println(".pw input { padding-right: 44px; }");
            out.println(".pw button { position: absolute; right: 6px; top: 50%; transform: translateY(-50%); width: 32px; height: 32px; margin: 0; padding: 0; border: 0; border-radius: 4px; background: transparent; color: #bbb; display: flex; align-items: center; justify-content: center; cursor: pointer; }");
            out.println(".pw button:hover { background: #3a3a3a; color: #fff; }");
            out.println(".pw button svg { width: 18px; height: 18px; fill: currentColor; }");
            out.println("button { width: 100%; margin-top: 16px; padding: 10px; border: 0; border-radius: 4px; background: #2d8cff; color: #fff; cursor: pointer; }");
            out.println(".error { color: #ff7b7b; margin-top: 12px; }");
            out.println("</style></head><body>");
            out.println("<div class='card'>");
            out.println("<h1>Acceso</h1>");
            out.println("<form method='post' action='login'>");
            out.println("<label for='username'>Usuario</label>");
            out.println("<input id='username' name='username' type='text' autocomplete='username' required>");
            out.println("<label for='password'>Clave</label>");
            out.println("<div class='pw'>");
            out.println("<input id='password' name='password' type='password' autocomplete='current-password' required>");
            out.println("<button type='button' aria-label='Mostrar u ocultar clave' onclick=\"var p=document.getElementById('password');var o=document.getElementById('eye-open');var c=document.getElementById('eye-closed');var hidden=p.type==='password';p.type=hidden?'text':'password';o.style.display=hidden?'none':'block';c.style.display=hidden?'block':'none';\">");
            out.println("<svg id='eye-open' viewBox='0 0 24 24'><path d='M12 5c-5.5 0-9.5 4.6-10.8 6.4a1 1 0 0 0 0 1.2C2.5 14.4 6.5 19 12 19s9.5-4.6 10.8-6.4a1 1 0 0 0 0-1.2C21.5 9.6 17.5 5 12 5Zm0 11a4 4 0 1 1 0-8 4 4 0 0 1 0 8Zm0-2.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3Z'/></svg>");
            out.println("<svg id='eye-closed' viewBox='0 0 24 24' style='display:none;'><path d='m3.3 2 18.7 18.7-1.4 1.4-3.1-3.1A12.8 12.8 0 0 1 12 19c-5.5 0-9.5-4.6-10.8-6.4a1 1 0 0 1 0-1.2A18.7 18.7 0 0 1 6.1 7L1.9 2.8 3.3 2Zm5.7 8.5 4.5 4.5A4 4 0 0 1 9 10.5ZM12 8a4 4 0 0 1 4 4c0 .5-.1 1-.3 1.5l3.6 3.6a18.7 18.7 0 0 0 3.5-4.5 1 1 0 0 0 0-1.2C21.5 9.6 17.5 5 12 5c-1.5 0-2.9.3-4.2.8l2.7 2.7c.5-.3 1-.5 1.5-.5Z'/></svg>");
            out.println("</button>");
            out.println("</div>");
            out.println("<button type='submit'>Entrar</button>");
            if (errorMessage != null) {
                out.println("<div class='error'>" + errorMessage + "</div>");
            }
            out.println("</form>");
            out.println("</div></body></html>");
        }
    }
}
