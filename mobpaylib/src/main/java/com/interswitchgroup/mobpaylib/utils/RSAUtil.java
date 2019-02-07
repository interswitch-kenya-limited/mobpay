package com.interswitchgroup.mobpaylib.utils;

import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {
    public static PublicKey getPublicKey(String modulus, String publicExponent) throws Exception {
//        Security.addProvider(new BouncyCastleProvider()); // Android ships with bouncy castle
        RSAPublicKeySpec publicKeyspec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(publicKeyspec);
    }

    public static String getAuthDataMerchant(PublicKey publicKey, String pan, String expiryDate, String cvv2) throws Exception {
        String authDataCipher = pan + "D" + cvv2 + "D" + expiryDate;
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] authDataBytes = encryptCipher.doFinal(authDataCipher.getBytes("UTF8"));
        return Base64.encodeToString(authDataBytes, Base64.NO_WRAP);
    }
}
