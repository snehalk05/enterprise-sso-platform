package com.snehal.sso.auth.internal;

import com.snehal.sso.auth.domain.UserAccount;
import com.snehal.sso.auth.dto.AuthUserResponse;
import com.snehal.sso.auth.repository.UserAccountRepository;
import com.snehal.sso.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {
    private final UserAccountRepository users;

    public InternalUserController(UserAccountRepository users) {
        this.users = users;
    }

    @GetMapping("/{id}")
    public AuthUserResponse getById(@PathVariable String id) {
        UserAccount user = users.findById(id)
                .orElseThrow(() -> new NotFoundException("User account not found"));

        return new AuthUserResponse(
                user.id,
                user.username,
                user.email,
                user.roles.stream().map(Enum::name).collect(Collectors.toSet()),
                user.emailVerified,
                user.locked,
                user.mfaType != null && user.mfaType.name().equals("TOTP")
        );
    }
}
