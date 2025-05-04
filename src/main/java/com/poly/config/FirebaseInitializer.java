package com.poly.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class FirebaseInitializer {
	
	 public static FirebaseApp initializeAuthApp() throws IOException {
	        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals("firebaseApp2"))) {
	            InputStream serviceAccount = FirebaseInitializer.class.getClassLoader()
	                    .getResourceAsStream("serviceAccountKey.json");

	            if (serviceAccount == null) {
	                throw new IOException("File serviceAccountKey.json không tồn tại trong thư mục resources");
	            }

	            FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                    .build();

	            return FirebaseApp.initializeApp(options, "firebaseApp2");
	        }
	        return FirebaseApp.getInstance("firebaseApp2");
	    }
}




//public static void initialize() throws IOException {
//// Kiểm tra nếu FirebaseApp đã tồn tại, bỏ qua khởi tạo
//if (FirebaseApp.getApps().isEmpty()) {
//  // Lấy file serviceAccountKey.json từ thư mục resources
//  InputStream serviceAccount = FirebaseInitializer.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");
//
//  if (serviceAccount == null) {
//      throw new FileNotFoundException("File serviceAccountKey.json không tồn tại trong thư mục resources");
//  }
//
//  FirebaseOptions options = new FirebaseOptions.Builder()
//          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//          .build();
//
//  FirebaseApp.initializeApp(options);
//  System.out.println("Firebase initialized successfully.");
//} else {
//  System.out.println("Firebase already initialized.");
//}
//
//
//}





//public static void initialize() throws IOException {
//if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals("firebaseApp2"))) {
//    InputStream serviceAccount = FirebaseInitializer.class.getClassLoader()
//            .getResourceAsStream("serviceAccountKey.json");
//
//    if (serviceAccount == null) {
//        throw new IOException("File serviceAccountKey.json không tồn tại trong thư mục resources");
//    }
//
//    FirebaseOptions options = new FirebaseOptions.Builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//            .build();
//
//    FirebaseApp.initializeApp(options, "firebaseApp2");
//    System.out.println("Firebase App2 initialized successfully.");
//} else {
//    System.out.println("Firebase App2 already initialized.");
//}
//}
