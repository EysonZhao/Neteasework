package com.netease.utils;

import java.io.UnsupportedEncodingException;

/**
 * ��ǰ�δ����RequestParamת�����Ҫ��UTF-8����ʽ
 * @author ��
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
