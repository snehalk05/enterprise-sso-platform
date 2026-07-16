package com.snehal.sso.auth.repository;

import com.snehal.sso.auth.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByUsername(String username);
}