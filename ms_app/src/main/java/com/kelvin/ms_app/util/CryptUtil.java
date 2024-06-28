package com.kelvin.ms_app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class CryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(CryptUtil.class);

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public static String encrypt(String Data) {
        String output = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            output = DatatypeConverter.printBase64Binary(encVal);
        } catch (Exception e) {
            logger.warn("Fail to encrypt password : " + Data);
        }
        return output;
    }

    public static String decrypt(String encryptedData) {
        String output = encryptedData;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            output = new String(decValue);
        } catch (Exception e) {
            logger.warn("Fail to decrypt Password : " + encryptedData);
        }
        return output;
    }

    public static CipherInputStream doChipherDecryption(String propertiesFileName) {
        CipherInputStream cis = null;
        try {
            String parent = new File(propertiesFileName).getParent();
            String fileName = new File(propertiesFileName).getName();
            String cipherPath = parent.replace("\\", "/") + "/" + fileName.split("\\.")[0] + "_cipher" + "." + fileName.split("\\.")[1];
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cipherPath));
            DESKeySpec ks = new DESKeySpec((byte[]) ois.readObject());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey key = skf.generateSecret(ks);
            Cipher c = Cipher.getInstance("DES/CFB8/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec((byte[]) ois.readObject()));
            cis = new CipherInputStream(new FileInputStream(propertiesFileName), c);
            ois.close();
        } catch (NoSuchAlgorithmException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("NoSuchAlgorithmException : " + e.getStackTrace()[i]);
            }
        } catch (ClassNotFoundException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("ClassNotFoundException : " + e.getStackTrace()[i]);
            }
        } catch (InvalidKeySpecException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("InvalidKeySpecException : " + e.getStackTrace()[i]);
            }
        } catch (IOException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("IOException : " + e.getStackTrace()[i]);
            }
        } catch (InvalidKeyException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("InvalidKeyException : " + e.getStackTrace()[i]);
            }
        } catch (NoSuchPaddingException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("NoSuchPaddingException : " + e.getStackTrace()[i]);
            }
        } catch (InvalidAlgorithmParameterException e) {
            for (Integer i = 0; i < e.getStackTrace().length; i++) {
                logger.info("InvalidAlgorithmParameterException : " + e.getStackTrace()[i]);
            }
        }
        return cis;
    }

    public static String mask(String data) {
        if(ObjectUtils.isEmpty(data)) {
            return "";
        }
        int length = data.length();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            sb.append('x');
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        String a = "admin1234";
        System.out.println(encrypt(a));
        System.out.println(decrypt("c82AYVJ8VkwhSPvcaiOL8Q=="));
    }

}
