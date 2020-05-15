package com.fixme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TimeMessage
 */
public class TimePrint {

	static public void print(String msg) {
		LocalDateTime lTime = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

		String formattedDate = lTime.format(myFormatObj);
		System.out.print("\u001B[1;37m[\u001B[1;36m" + formattedDate + "\u001B[1;37m]\u001B[1;0m");
		System.out.println(" " + msg);
	}
}
