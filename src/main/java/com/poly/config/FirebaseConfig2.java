package com.poly.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import jakarta.annotation.PostConstruct;
@Configuration
public class FirebaseConfig2 {

	private FirebaseApp firebaseApp2;

    @PostConstruct
    public void initialize() {
        try {
            firebaseApp2 = FirebaseInitializer.initializeAuthApp();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize Firebase App2: " + e.getMessage());
        }
    }

    @Bean(name = "firebaseAuth")
    public FirebaseAuth firebaseAuth() {
    	System.out.println("success initlize");
        return FirebaseAuth.getInstance(firebaseApp2);
    }
}