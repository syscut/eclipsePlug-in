package com.plugin.test.encrypt;

import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

	private final static String key = "aK7+UX24ttB=fTnA";
	private final static String iv = "d4ee=RaW1prQ1F+f";

	public String decode(String content) throws Exception {
		final Decoder decoder = Base64.getDecoder();
		final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = decoder.decode(content);
		return new String(cipher.doFinal(encrypted)).trim();
	}

	public String encode(String content) throws Exception {
		final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = cipher.doFinal(String.format("%-16s", content).getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encrypted);
	}

}
