package com.netease.utils;

import java.io.UnsupportedEncodingException;

/**
 * 将前段传入的RequestParam转码成想要的UTF-8的形式
 * @author 松
 * 2017.3.15
 */
public class EncodeString {
	
		public static String encodeStr(String str) {
			try {
				return new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
}
