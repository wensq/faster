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

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * 加密解密工具类
 *
 * @author sqwen
 */
public class Encrypts {

	private Encrypts() {}

	public static final String encrypt(String algorithm, String plainText, String salt) {
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
		byte[] unencodedText = plainText.getBytes();
		md.update(unencodedText);

		byte[] encodedText = md.digest();
        return Base64.encodeBase64URLSafeString(encodedText);
	}

	public static final String encrypt(String algorithm, String plainText) {
		return encrypt(plainText, algorithm, null);
	}

}
