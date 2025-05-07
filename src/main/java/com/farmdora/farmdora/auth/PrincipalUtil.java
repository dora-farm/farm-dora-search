package com.farmdora.farmdora.auth;

import com.farmdora.farmdora.common.exception.AccessDeniedException;
import java.security.Principal;
import org.springframework.stereotype.Component;

@Component
public class PrincipalUtil {

    public Integer extractUserIdRequired(Principal principal) {
        Integer userId = null;
        if (principal != null) {
            userId = Integer.parseInt(principal.getName());
        }
        return userId;
    }

    public Integer extractUserIdNotRequired(Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException();
        }
        return Integer.parseInt(principal.getName());
    }
}
