package com.fixme;

import com.fixme.ClientHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Market {
	private Scanner scn;
	private Map<String, Object> details;
	private ClientHandler ch;

	private String ConnID;

	public Market() {
		this.scn = new Scanner(System.in);
	}

	public void setConnID(String _connID) {
		this.ConnID = _connID;
	}

	public void setClientHandler(ClientHandler _ch) {
		this.ch = _ch;
	}

	public static void main(String[] args) {

		Colour.out.green("\n\tWELCOME TO THE FIX BROKER PORTAL\n");

		Market ma = new Market();
		// try {
		ClientHandler ch = ClientHandler.start(5001);
		ma.setClientHandler(ch);
		if (ch.getConnID() != null && !ch.getConnID().isEmpty()) {
			ma.setConnID(ch.getConnID());
			while (true) {

			}
			// br.processBuySell();
			// System.out.println(ch.sendMessage("markets"));
		}
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}
}
