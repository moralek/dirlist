package com.dlt;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class hrso extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		String osName = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		String time = "";
		String zoneText = "";
		String zoneId = "";

		if (os.contains("win")) {
			String raw = executeCommand("cmd /c wmic os get LocalDateTime /value");
			time = parseWMICDate(raw);
		} else {
			ZonedDateTime now = ZonedDateTime.now();
			time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}

		ZoneId zone = ZoneId.systemDefault();
		zoneText = ZonedDateTime.now().getZone().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
		zoneId = zone.getId();

		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().println("OS Time: " + time + " " + zoneText);
		response.getWriter().println("OS TZ: " + zoneId);
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

