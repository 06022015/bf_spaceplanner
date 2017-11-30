package com.spaceplanner.java.security;

import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 20/8/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    @Autowired
    private Properties properties;

    public String encodePassword(String password, Object o) {
        String hashValue = password;
        try {
            for (int i = 0; i < 20; i++) {
                hashValue = passwordAlgorithm(hashValue);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.print("No Such Algorithm Exists");
        } catch (NullPointerException e) {
            System.out.print("The Encoding Is Not Supported");
        }
        return hashValue;
    }

    public boolean isPasswordValid(String userInput, String password, Object o) {
        return password.equals(encodePassword(userInput, o));
    }

    private String passwordAlgorithm(String raw) throws NoSuchAlgorithmException, NullPointerException {
        try {
            MessageDigest m = MessageDigest.getInstance(properties.getProperty("password.encoding.digest.algorithm"));
            m.reset();
            m.update(raw.getBytes());
            byte[] digest = m.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aDigest : digest) {
                String hex = Integer.toHexString(0xff & aDigest);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("No Such Algorithm Exists");
        } catch (NullPointerException e) {
            throw new NullPointerException("The Encoding Is Not Supported");
        }
    }

    public String encode(CharSequence charSequence) {
        return encodePassword((String) charSequence,null);
    }

    public boolean matches(CharSequence charSequence, String s) {
        return isPasswordValid((String) charSequence,s,null);
    }
}
