package com.snehal.sso.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document("profiles")
public class UserProfile {
    @Id
    public String id;
    public String authUserId;
    public String email;
    public String phone;
    public String address;
    public Map<String, String> preferences = new HashMap<>();
}