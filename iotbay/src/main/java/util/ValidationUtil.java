package util;

import java.util.regex.Pattern;

public class ValidationUtil {
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    // Password requirements: at least 8 chars, 1 uppercase, 1 lowercase, 1 number
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}$"
    );
    
    // Phone number validation (Australian format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(?:\\+61|0)[2-478](?:[ -]?[0-9]){8}$"
    );
    
    // Price validation (positive number with up to 2 decimal places)
    private static final Pattern PRICE_PATTERN = Pattern.compile(
        "^\\d+(\\.\\d{1,2})?$"
    );

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 && name.trim().length() <= 50;
    }
    
    public static boolean isValidPhone(String phone) {
        return phone == null || phone.isEmpty() || PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidAddress(String address) {
        return address == null || address.isEmpty() || (address.trim().length() >= 5 && address.trim().length() <= 200);
    }
    
    public static boolean isValidPrice(String price) {
        return price != null && PRICE_PATTERN.matcher(price).matches();
    }
    
    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }
    
    public static boolean isValidProductName(String name) {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 100;
    }
    
    public static boolean isValidDescription(String description) {
        return description == null || description.isEmpty() || description.trim().length() <= 1000;
    }
    
    public static boolean isValidImageUrl(String url) {
        return url == null || url.isEmpty() || (url.startsWith("http") && url.length() <= 1024);
    }
    
    public static String sanitizeHtml(String input) {
        if (input == null) return null;
        return input.replaceAll("<[^>]*>", "")
                   .replaceAll("&", "&amp;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;");
    }
} 