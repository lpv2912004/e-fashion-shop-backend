package com.poly.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
@Service
public class PasswordEncode {
	public static String EncodePass(String password) {
		String digest = null;
        try {
        	//tạo đối tượng MessageDigest để sử dụng thuật toán băm SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            //tạo một mảng kí tự được thiết lập dạng UTF-8
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            
            //tạo đối tượng stringbuilder với memory gấp 2 lần chuỗi ban đầu
            StringBuilder sb = new StringBuilder(2 * hash.length);
            
            //duyệt mảng để tạo 1 chuỗi kí tự theo dạng hex
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            
            //set hex vào từng kí tự vào stringbuilder để thành 1 chuỗi hoàn chỉnh
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
	}
	
	
}
