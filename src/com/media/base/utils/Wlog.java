/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.utils;

import android.util.Log;

/**
 * @author pengfei.xu
 * @date 2014-11-19 下午1:09:14
 * @Description: TODO
 * 
 */
public class Wlog {

	private static final String TAG = "MediaBase";

	private static final String SEPARATOR1 = "=======";
	private static final String SEPARATOR2 = ">>>";
	private static final String SEPARATOR3 = " ";

	private static boolean isOpen;

	static {
		// TODO
		isOpen = true;
	}

	public static void d(String className, Object... contents) {
		if (isOpen) {
			StringBuffer sb = new StringBuffer();
			sb.append(SEPARATOR1).append(className).append(SEPARATOR2);
			for (Object content : contents) {
				sb.append(content).append(SEPARATOR3);
			}
			sb.append(SEPARATOR1);
			Log.d(TAG, sb.toString());
		}
	}

}
