package br.com.cultive_api.utils;

import br.com.cultive_api.entites.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("Utility class, cannot be instantiated");
    }

    public static UUID getUserIdFromToken(JwtAuthenticationToken token) {
        String userIdStr = token.getToken().getClaimAsString("sub");
        if (userIdStr == null)
            throw new BadCredentialsException("Token does not contain user ID");

        try {
            return UUID.fromString(userIdStr);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid user ID format in token");
        }
    }

    public static String masEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        return parts[0].charAt(0) + "*****@" + parts[1];
    }

}
