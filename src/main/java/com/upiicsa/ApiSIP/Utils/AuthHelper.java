package com.upiicsa.ApiSIP.Utils;

import com.upiicsa.ApiSIP.Model.UserSIP;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHelper {

    public static Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {

            throw new IllegalStateException("Not authorized for this operation.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserSIP user) {
            return user.getId();
        }

        throw new IllegalStateException("The principal is not an instance of authenticated user.");
    }
}
