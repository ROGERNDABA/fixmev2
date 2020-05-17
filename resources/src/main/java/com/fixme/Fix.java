package com.fixme;

import java.util.regex.*;

/**
 * Fix
 */
public class Fix {
	// pattern for fix message
	// \|(([0-9]){1,3}=([a-z0-9]){1,}\|){4,}$

	public static String encode(String clientID, String instname, String quantity, String destClientID,
			int messageType) {
		String fixString = "";
		String fixString2 = "";

		fixString += "8=FIX-42|9=";
		fixString2 += "|49=" + destClientID;
		fixString2 += "|56=" + clientID;
		fixString2 += "|99=" + instname;
		fixString2 += "|88=" + quantity;
		fixString2 += "|35=" + messageType + "|";

		String tmp = fixString2;
		fixString += tmp.length() - 1;
		fixString += fixString2;
		fixString += "10=" + checksum(fixString) + "|";
		return fixString;
	}

	private static String checksum(String s) {
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			n += (c == '|') ? 1 : c;
		}
		return String.format("%03d", (n % 256));
	}

	public static boolean validFixMessage(String s) {
		boolean valid_checksum = Pattern.matches("8=FIX-42\\|(([0-9]){1,3}=([A-Za-z0-9\\.]){1,}\\|){4,}$", s);
		return valid_checksum;
	}

	public static boolean validFixChecksum(String s) {
		if (validFixMessage(s)) {
			int x = 0;
			int cs = 0;
			String s1;
			String s2;

			for (int i = s.length() - 1; i >= 0; i--) {
				if (s.charAt(i) == '|') {
					x += 1;
					cs = i;
				}
				if (x == 2)
					break;
			}
			s1 = s.substring(0, cs + 1);
			s2 = s.substring(cs + 4, s.length() - 1);

			if (checksum(s1).equals(s2))
				return true;
		}
		return false;
	}

	public static String getFixPart(String fix, int part) {
		Pattern p = Pattern.compile("(" + part + "=.*?(?=\\|))");
		Matcher m = p.matcher(fix);

		if (m.find()) {
			return m.group(0).split("=")[1];
		}
		return "";
	}

	public static String decode(String fixMessage) {
		// String ClientID = "";
		// int inst_id;
		// int inst_quantity;
		// int br_type;
		// int ma_id;
		// int inst_type;
		// int price;

		// System.out.println("====> " + valid_checksum);
		// this.p = Pattern.compile();

		return "";
	}
}
