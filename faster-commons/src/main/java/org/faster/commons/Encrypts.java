/*
 * Copyright (c) 2013 @iSQWEN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.faster.commons;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * 加密解密工具类
 *
 * @author sqwen
 */
@SuppressWarnings("restriction")
public class Encrypts {

	private Encrypts() {}

	public static final String encryptByBASE64(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	public static final String encryptByBASE64(String plainText) {
		if (plainText == null) {
			return null;
		}
		return new BASE64Encoder().encode(plainText.getBytes());
	}

	public static final byte[] decodeByBASE64(String encryptText) {
		if (encryptText == null) {
			return null;
		}
		try {
			return new BASE64Decoder().decodeBuffer(encryptText);
		} catch (IOException ioe) {
			throw new RuntimeException("Decrypt by BASE64 for:[" + encryptText + "] failed!", ioe);
		}
	}

	public static final String decryptByBASE64(String encryptText) {
		if (encryptText == null) {
			return null;
		}
		byte[] buffer = decodeByBASE64(encryptText);
		return new String(buffer);
	}

	public static final String encrypt(String plainText, String algorithm, String salt) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}

		md.reset();
		if (salt != null) {
			md.update(salt.getBytes());
		}
		byte[] unencodedPassword = plainText.getBytes();
		md.update(unencodedPassword);

		byte[] encodedPassword = md.digest();
		return encryptByBASE64(encodedPassword);
	}

	public static final String encrypt(String plainText, String algorithm) {
		return encrypt(plainText, algorithm, null);
	}

	public static final String encryptByMD5(String plainText) {
		return encrypt(plainText, "MD5");
	}

	public static final String encryptByMD5WithSalt(String plainText, String salt) {
		return encrypt(plainText, "MD5", salt);
	}

	public static final String encryptBySHA(String plainText) {
		return encrypt(plainText, "SHA");
	}

	public static final String encryptBySHAWithSalt(String plainText, String salt) {
		return encrypt(plainText, "SHA", salt);
	}

	public static final String encryptBySHA256(String plainText) {
		return encrypt(plainText, "SHA-256");
	}

	public static final String encryptBySHA256WithSalt(String plainText, String salt) {
		return encrypt(plainText, "SHA-256", salt);
	}

	public static final String encryptBySHA384(String plainText) {
		return encrypt(plainText, "SHA-384");
	}

	public static final String encryptBySHA384WithSalt(String plainText, String salt) {
		return encrypt(plainText, "SHA-384", salt);
	}

	public static final String encryptBySHA512(String plainText) {
		return encrypt(plainText, "SHA-512");
	}

	public static final String encryptBySHA512WithSalt(String plainText, String salt) {
		return encrypt(plainText, "SHA-512", salt);
	}

}
