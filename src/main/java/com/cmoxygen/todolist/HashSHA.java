package com.cmoxygen.todolist;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashSHA {

    private static byte[] salt = null;
    private static byte[] hashedString = null;

    public static byte[] generateHash(byte[] stringToHash) {

        try {
            deserializeSalt();

        } catch (IOException e) {

            e.printStackTrace();
            salt = null;
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            hashedString = md.digest(stringToHash);
            return hashedString;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void deserializeSalt() throws IOException {

        FileInputStream fis = new FileInputStream("/etc/server/store/salt/not_salt");

        byte[] buffer = new byte[16];

        int hasData = fis.read(buffer, 0, buffer.length);

        while (hasData != -1) {
            hasData = fis.read();
        }
        fis.close();

        salt = buffer;
    }
}
