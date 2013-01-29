package org.faster.commons;

import static org.faster.commons.Encrypts.encrypt;

/**
 * @author sqwen
 */
public class Hashes {

    private Hashes() {}

    public static final String md5(String plainText) {
        return encrypt(plainText, "MD5");
    }

    public static final String md5(String plainText, String salt) {
        return encrypt(plainText, "MD5", salt);
    }

    public static final String sha(String plainText) {
        return encrypt(plainText, "SHA");
    }

    public static final String sha(String plainText, String salt) {
        return encrypt(plainText, "SHA", salt);
    }

    public static final String sha256(String plainText) {
        return encrypt(plainText, "SHA-256");
    }

    public static final String sha256(String plainText, String salt) {
        return encrypt(plainText, "SHA-256", salt);
    }

    public static final String sha384(String plainText) {
        return encrypt(plainText, "SHA-384");
    }

    public static final String sha384(String plainText, String salt) {
        return encrypt(plainText, "SHA-384", salt);
    }

    public static final String sha512(String plainText) {
        return encrypt(plainText, "SHA-512");
    }

    public static final String sha512(String plainText, String salt) {
        return encrypt(plainText, "SHA-512", salt);
    }

}
