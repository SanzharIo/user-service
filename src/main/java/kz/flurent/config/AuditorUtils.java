package kz.flurent.config;

import kz.flurent.model.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@UtilityClass
public class AuditorUtils {

    private final static String ADMIN = "ROLE_ADMIN";

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getCurrentUsername() {
        String username = null;
        try {
            username = getCurrentUser().getUsername();
        } catch (Exception ignored) {
        }
        return username;
    }

    public static boolean hasAdminRole() {
        User user = getCurrentUser();
        if (user == null || user.getAuthorities() == null) {
            return false;
        }
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (ADMIN.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
