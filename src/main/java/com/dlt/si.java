package com.dlt;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Locale;

public class si extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        String javaTime = ZonedDateTime.now().format(formatter);
        String javaZoneId = ZoneId.systemDefault().toString();
        String javaVersion = System.getProperty("java.version");
        String serverInfo = getServletContext().getServerInfo();
        String osName = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        String osTime = "-";
        String osTZ = "";

        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (os.contains("win")) {
            String raw = executeCommand("cmd /c wmic os get LocalDateTime /value");
            osTime = parseWMICDate(raw);
        } else {
            // Linux y WSL2
            String raw = executeCommand("date");
            if (raw != null && !raw.trim().isEmpty()) {
                String[] parts = raw.trim().split(" ");
                if (parts.length >= 6) {
                    String day = parts[2];
                    String time = parts[3];
                    String tz = parts[4];
                    String year = parts[5];
                    osTime = String.format("%s-%s-%s %s %s", year, getMonthNumber(parts[1]), day, time, tz);
                    osTZ = ZoneId.systemDefault().toString();
                }
            }
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println("Java Time: " + javaTime);
        response.getWriter().println("Java Zone: " + javaZoneId);
        response.getWriter().println("Java: " + javaVersion);
        response.getWriter().println("App Server: " + serverInfo);
        response.getWriter().println("OS: " + osName + " (" + osArch + ")");
        response.getWriter().println("OS Time: " + osTime);
        if (!osTZ.isEmpty()) {
            response.getWriter().println("OS TZ: " + osTZ);
        }
    }

    private String getMonthNumber(String monthAbbreviation) {
        switch (monthAbbreviation.toLowerCase()) {
            case "jan": return "01";
            case "feb": return "02";
            case "mar": return "03";
            case "apr": return "04";
            case "may": return "05";
            case "jun": return "06";
            case "jul": return "07";
            case "aug": return "08";
            case "sep": return "09";
            case "oct": return "10";
            case "nov": return "11";
            case "dec": return "12";
            default: return "--";
        }
    }

    private String parseWMICDate(String raw) {
        if (raw == null) return "-";
        for (String line : raw.split("\n")) {
            line = line.trim();
            if (line.startsWith("LocalDateTime=")) {
                String datetime = line.replace("LocalDateTime=", "").trim();
                if (datetime.length() >= 14) {
                    String year = datetime.substring(0, 4);
                    String month = datetime.substring(4, 6);
                    String day = datetime.substring(6, 8);
                    String hour = datetime.substring(8, 10);
                    String minute = datetime.substring(10, 12);
                    String second = datetime.substring(12, 14);
                    return String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);
                }
            }
        }
        return "-";
    }

    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }
}