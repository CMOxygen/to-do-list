package com.cmoxygen.todolist;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionRSA {

    private static PrivateKey privateKey = null;
    private static byte[] encryptedBuffer = null;

    public static byte[] decrypt() {

        try {
            deserializePrivateKey();
            deserializeEncryptedBuffer();

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {

            e.printStackTrace();
            privateKey = null;

            return null;
        }

        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

            return decryptCipher.doFinal(encryptedBuffer);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private void deserializePublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//
//        File publicKeyFile = new File("/etc/server/store/keys/public.key");
//        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
//
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//        publicKey = keyFactory.generatePublic(publicKeySpec);
//    }

    private static void deserializePrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        File privateKeyFile = new File("/etc/server/store/keys/private.key");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);
    }

    private static void deserializeEncryptedBuffer() throws IOException {

        FileInputStream fis = new FileInputStream("/etc/server/store/data/data");

        byte[] buffer = new byte[256];
        int hasData = fis.read(buffer, 0, buffer.length);

        while (hasData != -1) {
            hasData = fis.read();
        }
        fis.close();
        encryptedBuffer = buffer;
    }
}
