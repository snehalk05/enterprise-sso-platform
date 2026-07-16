package com.snehal.sso.user.repo;

import com.snehal.sso.user.domain.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    Optional<UserProfile> findByAuthUserId(String id);
}