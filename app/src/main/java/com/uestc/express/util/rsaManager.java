package com.uestc.express.util;


import com.uestc.express.Constants;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by lucifer on 2016/5/18.
 */
public class RsaManager {
    private String rsaCode;
    private static RsaManager Instance;

    public static RsaManager getrsaManager() {
        if (Instance == null) {
            Instance = new RsaManager();
        }
        return Instance;
    }

    public static String encrypt(String str) {
        return encryptByPublic(str, Constants.RSA_PUBLIC_KEY);
    }


    private static PublicKey getPublicKeyFromX509(String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        return keyFactory.generatePublic(x509);
    }


    public static String encryptByPublic(String content, String pub_key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(pub_key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            String s = new String(Base64.encode(output));

            return s;

        } catch (Exception e) {
            return null;
        }
    }

}
