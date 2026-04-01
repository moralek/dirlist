package com.dlt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Comparator;

public class ut {

    public static String getOwner(File file) {
        try {
            UserPrincipal owner = Files.getOwner(file.toPath());
            return owner.getName();
        } catch (IOException e) {
            return "-";
        }
    }

    public static String getPermissions(File file) {
        StringBuilder permissions = new StringBuilder();
        permissions.append(file.canRead() ? "r" : "-");
        permissions.append(file.canWrite() ? "w" : "-");
        permissions.append(file.canExecute() ? "x" : "-");
        return permissions.toString();
    }

    public static String getCreationDate(File file, SimpleDateFormat sdf) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return sdf.format(attrs.creationTime().toMillis());
        } catch (IOException e) {
            return "-";
        }
    }

    public static String getSortableHeader(String label, String column, String currentSort, String currentOrder, String path, String filter) {
        String arrow = "";
        if (column.equals(currentSort)) {
            arrow = currentOrder.equals("asc") ? " <i class='fas fa-arrow-up'></i>" : " <i class='fas fa-arrow-down'></i>";
        }
        String newOrder = column.equals(currentSort) && currentOrder.equals("asc") ? "desc" : "asc";
        return "<th><a href='?path=" + path + "&sort=" + column + "&order=" + newOrder + "&filter=" + (filter != null ? filter : "") + "'>" + label + arrow + "</a></th>";
    }

    public static Comparator<File> getFileComparator(String sort, String order) {
        Comparator<File> comparator;

        switch (sort) {
            case "owner":
                comparator = Comparator.comparing(ut::getOwner, String.CASE_INSENSITIVE_ORDER);
                break;
            case "permissions":
                comparator = Comparator.comparing(ut::getPermissions);
                break;
            case "created":
                comparator = Comparator.comparingLong(file -> {
                    try {
                        return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toMillis();
                    } catch (IOException e) {
                        return 0;
                    }
                });
                break;
            case "date":
                comparator = Comparator.comparingLong(File::lastModified);
                break;
            case "size":
                comparator = Comparator.comparingLong(File::length);
                break;
            default:
                comparator = Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER);
                break;
        }

        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }

        return Comparator.comparing((File f) -> !f.isDirectory()).thenComparing(comparator);
    }

    public static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int z = (int) (Math.log(size) / Math.log(1024));
        return String.format("%.1f %s", size / Math.pow(1024, z), "KMGTPE".charAt(z - 1) + "B");
    }

    // Server Information
    public static String getServerTime() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return now.format(formatter);
    }

    public static String getServerTimeZone() {
        return ZoneId.systemDefault().toString();
    }

    public static String getOSName() {
        return System.getProperty("os.name") + " " + System.getProperty("os.version");
    }

    public static String getOSArch() {
        return System.getProperty("os.arch");
    }
}
