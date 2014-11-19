package com.media.base.utils;

import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;

public class EncoderUtils {

	/**
	 * 获取文件的编码格式
	 * 
	 * @param path
	 * @return
	 */
	public static String getEncoder(String path) {

		String encoding = null;
		java.io.FileInputStream fis = null;
		try {
			byte[] buf = new byte[4096];
			fis = new java.io.FileInputStream(path);

			// (1)
			UniversalDetector detector = new UniversalDetector(null);

			// (2)
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			// (3)
			detector.dataEnd();

			// (4)
			encoding = detector.getDetectedCharset();
			if (encoding != null) {
				System.out.println("Detected encoding = " + encoding);
			} else {
				System.out.println("No encoding detected.");
			}

			// (5)
			detector.reset();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return encoding;
	}

	public static String getStringEncoder(String content) {
		String encoding = null;
		try {
			UniversalDetector detector = new UniversalDetector(null);
			detector.handleData(content.getBytes(), 0,
					content.getBytes().length);
			detector.dataEnd();
			encoding = detector.getDetectedCharset();
			if (encoding != null) {
				System.out.println("Detected encoding = " + encoding);
			} else {
				System.out.println("No encoding detected.");
			}
			detector.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encoding;
	}

	/**
	 * 判断字符串的编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				System.out.println(encode);
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				System.out.println(encode);
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				System.out.println(encode);
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				System.out.println(encode);
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}
}
