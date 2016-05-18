package com.uestc.express.util;


import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by lucifer on 2016/5/18.
 */
public class rsaManager {
    private String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9aXfnIwLe9FmTcjidZ7159QZC+pkXGMGbUTOnG/i4WV3cTI4OKGcAbHUCQgbDpQIYx0UyLIgrZDScdc9mgAgbul2tuW9ateOiIk5J0lUK2qN1VWH7IHaDawnyCjLForvpbSqcqPVRZZCM0LB2Is814YyoF+36s/5XRg9Xe1nHVwIDAQAB";
    private String rsaCode;
    private static rsaManager Instance;

    public static rsaManager getrsaManager() {
        if (Instance == null) {
            Instance = new rsaManager();
        }
        return Instance;
    }

    public String encrypt(String str) {
        rsaCode = encryptByPublic(str, pubkey);
        System.out.println("加密串:" + rsaCode);
        return rsaCode;
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
