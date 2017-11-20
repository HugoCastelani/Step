package com.enoughspam.step.ui.intro.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Hugo Castelani
 * Date: 12/07/17
 * Time: 23:29
 */

public class MessageCodeHandler {
    public static String sCode;
    public static String sPassword;

    private MessageCodeHandler() {}

    public static String encryptIt(@NonNull final String value) {
        try {

            final DESKeySpec keySpec = new DESKeySpec(sPassword.getBytes("UTF8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(keySpec);

            final byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT); // encryped value

        } catch (final InvalidKeyException e) {
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final BadPaddingException e) {
            e.printStackTrace();
        } catch (final NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (final IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String decryptIt(@NonNull final String value) {
        try {

            final DESKeySpec keySpec = new DESKeySpec(sPassword.getBytes("UTF8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(keySpec);

            final byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            return new String(decrypedValueBytes); // decryped value

        } catch (final InvalidKeyException e) {
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (final InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (final BadPaddingException e) {
            e.printStackTrace();
        } catch (final NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (final IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return value;
    }
}
