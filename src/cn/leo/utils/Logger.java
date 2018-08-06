package cn.leo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static void i(String msg) {
		System.out.println(getCurrentTime() + "Information:" + msg);
	}

	public static void d(String msg) {
		System.out.println(getCurrentTime() + "Debug:" + msg);
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss-SSS]");
		return sdf.format(new Date());
	}
}
