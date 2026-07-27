package com.snehal.sso.user.controller;

import com.snehal.sso.user.client.AuthServiceClient;
import com.snehal.sso.user.domain.UserProfile;
import com.snehal.sso.user.dto.ProfileUpdateRequest;
import com.snehal.sso.user.dto.UserProfileView;
import com.snehal.sso.user.repo.UserProfileRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserProfileRepository repository;
    private final AuthServiceClient authServiceClient;

    public UserController(UserProfileRepository repository, AuthServiceClient authServiceClient) {
        this.repository = repository;
        this.authServiceClient = authServiceClient;
    }

    @GetMapping("/me")
    public UserProfileView me(@RequestHeader("X-User-Id") String userId) {
        UserProfile profile = repository.findByAuthUserId(userId).orElseGet(() -> newProfile(userId));
        return new UserProfileView(profile, authServiceClient.getUserAccount(userId));
    }

    @PutMapping("/me")
    public UserProfile update(@RequestHeader("X-User-Id") String userId,
                              @Valid @RequestBody ProfileUpdateRequest request) {
        UserProfile profile = repository.findByAuthUserId(userId).orElseGet(() -> newProfile(userId));
        profile.phone = request.phone();
        profile.address = request.address();
        profile.preferences = request.preferences() == null ? Map.of() : request.preferences();
        return repository.save(profile);
    }

    @GetMapping
    public List<UserProfile> all() {
        return repository.findAll();
    }

    private UserProfile newProfile(String userId) {
        UserProfile profile = new UserProfile();
        profile.authUserId = userId;
        return profile;
    }
}
