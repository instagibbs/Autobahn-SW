package demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

public final class SystemHelper {

	public static final String EMPTY = "";
	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String DASH = "-";
	public static final String UNDERSCORE = "_";
	public static final String SPACE = " ";
	public static final String PLUS = "+";
	public static final String NEW_LINE = "\n";
	public static final String TAB = "\t";
	public static final String SLASH = "/";

	private SystemHelper() {
	}

	public static String buildStringNoSep(final Object... args) {
		StringBuilder buffer = new StringBuilder();
		for (Object arg : args) {
			buffer.append((arg != null) ? arg.toString() : "");
		}
		return buffer.toString();
	}

	public static boolean isStringEmpty(String string) {
		return string == null || string.length() == 0;
	}

	
}
