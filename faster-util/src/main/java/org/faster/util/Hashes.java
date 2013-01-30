package org.faster.util;

import static org.faster.util.Encrypts.encrypt;

/**
 * @author sqwen
 */
public class Hashes {

    private Hashes() {}

    public static final String md5(String plainText) {
        return encrypt("MD5", plainText);
    }

    public static final String md5(String plainText, String salt) {
        return encrypt("MD5", plainText, salt);
    }

    public static final String sha(String plainText) {
        return encrypt("SHA", plainText);
    }

    public static final String sha(String plainText, String salt) {
        return encrypt("SHA", plainText, salt);
    }

    public static final String sha256(String plainText) {
        return encrypt("SHA-256", plainText);
    }

    public static final String sha256(String plainText, String salt) {
        return encrypt("SHA-256", plainText, salt);
    }

    public static final String sha384(String plainText) {
        return encrypt("SHA-384", plainText);
    }

    public static final String sha384(String plainText, String salt) {
        return encrypt("SHA-384", plainText, salt);
    }

    public static final String sha512(String plainText) {
        return encrypt("SHA-512", plainText);
    }

    public static final String sha512(String plainText, String salt) {
        return encrypt("SHA-512", plainText, salt);
    }

}
