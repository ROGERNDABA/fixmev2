package com.fixme;

import java.util.regex.*;

/**
 * Fix
 */
public class Fix {
	// pattern for fix message
	// \|(([0-9]){1,3}=([a-z0-9]){1,}\|){4,}$

	public Fix() {
	}

	public String encode(String clientID, int id, int quantity, int br_type, int ma_id, int inst_type, float price) {
		String fixString = "";
		String fixString2 = "";
		fixString += "8=FIX-42|9=";
		fixString2 += "|35=" + br_type + "|49=" + clientID.trim() + "|56=" + ma_id;
		fixString2 += "|77=" + inst_type + "|88=" + quantity;
		fixString2 += "|99=" + price + "|";

		String tmp = fixString2;
		fixString += tmp.length() - 1;
		fixString += fixString2;
		fixString += "10=" + checksum(fixString) + "|";

		return fixString;
	}

	public String checksum(String s) {
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			n += (c == '|') ? 1 : c;
		}
		return String.format("%03d", (n % 256));
	}

	public String decode(String fixMessage) {
		// String ClientID = "";
		// int inst_id;
		// int inst_quantity;
		// int br_type;
		// int ma_id;
		// int inst_type;
		// int price;

		boolean valid_checksum = Pattern.matches("8=FIX-42\\|(([0-9]){1,3}=([a-z0-9\\.]){1,}\\|){4,}$", fixMessage);

		System.out.println("====> " + valid_checksum);
		// this.p = Pattern.compile();

		return "";
	}
}
