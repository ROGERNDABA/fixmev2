package com.fixme;

import com.fixme.Colour;
import com.fixme.Fix;
import com.fixme.Helpers;
import com.fixme.ClientHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Broker {
	private Scanner scn;
	private Map<String, Object> details;
	private ClientHandler ch;

	private String ConnID;

	public Broker() {
		this.scn = new Scanner(System.in);
	}

	public boolean login() {
		boolean valid = false;

		JSONParser jp = new JSONParser();

		try {
			File file = new File("").getCanonicalFile();
			FileReader reader = new FileReader(file.getParent() + "/fixme/assets/Brokers.json");
			Object obj = jp.parse(reader);

			JSONArray brokerList = (JSONArray) obj;

			while (valid == false) {
				Colour.white("Please enter broker username: ");
				String username = this.scn.nextLine().trim();
				Colour.white("Please enter broker password: ");
				String password = this.scn.nextLine().trim();

				for (Object o : brokerList) {
					JSONObject broker = (JSONObject) o;
					String busername = (String) broker.get("broker");
					String bpass = (String) broker.get("password");

					if (username.equals(busername.trim()) && !bpass.trim().equals("")) {
						BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), bpass);
						if (res.verified) {
							valid = true;
							this.details = Helpers.jsonToMap(broker);
							Colour.out.green("\n\tLogged in successfuly.");
							break;
						}
					}

				}
				if (valid == false)
					Colour.out.red("\n\tCould not login with the provided information\n");
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return true;

	}

	public void processBuySell() throws IOException {
		Colour.white("Instruction (buy|sell): ");
		String instr = this.scn.nextLine().trim();

		if (instr.toLowerCase().equals("buy")) {
			this.processBuy();
		} else if (instr.toLowerCase().equals("sell")) {
			this.processSell();
		} else {
			this.processBuySell();
		}
	}

	public void processBuy() throws IOException {
		System.out.println("\nHere's a list of instruments that can be bought\n");
		String[] insts = { "GOLD", "DIAMOND", "PLATINUM", "OIL", "SUGAR", "SUGAR" };
		String[] units = { "oz", "ct", "ct", "l", "kg", "kg" };

		for (int i = 0; i < insts.length; i++) {
			System.out.println((i + 1) + ". " + insts[i] + " (" + units[i] + ")");
		}

		String instName;
		String instQuantity;
		String marketID;

		Colour.white("\nEnter Instrument name: ");
		instName = this.scn.nextLine().trim().toUpperCase();
		while (!Helpers.inArray(insts, instName)) {
			Colour.out.red("Invalid input");
			Colour.white("Enter Instrument name: ");
			instName = this.scn.nextLine().trim().toUpperCase();
		}

		Colour.white("Enter Instrument quantity: ");
		instQuantity = this.scn.nextLine().trim().toUpperCase();
		while (!Helpers.isNumeric(instQuantity)) {
			Colour.out.red("Invalid input");
			Colour.white("Enter Instrument quantity: ");
			instQuantity = this.scn.nextLine().trim().toUpperCase();
		}

		ch.sendMessage("markets");
		String markets = ch.getServerResponse();
		if (markets.isEmpty()) {
			Colour.out.red("\nNo markets to trade in.\n");
			this.processBuySell();
		} else {
			System.out.println("\nThese are available markets' ID's\n");
			String[] marketStrings = markets.split(",", 0);

			for (int i = 0; i < marketStrings.length; i++)
				System.out.println((i + 1) + ". " + marketStrings[i]);
			Colour.white("\nEnter market ID: ");
			marketID = this.scn.nextLine().trim().toUpperCase();
			while (!Helpers.inArray(marketStrings, marketID)) {
				Colour.out.red("Invalid input");
				Colour.white("Enter market ID: ");
				marketID = this.scn.nextLine().trim().toUpperCase();
			}
			String fixString = Fix.encode(this.ConnID, instName, instQuantity, "0", marketID, 1);
			ch.sendMessage(fixString);
			String marketResp = ch.getServerResponse();
			// System.out.println(fixString);
			// System.out.println("---> " + Fix.validFixChecksum(fixString));
			// Fix.getFixPart(fixString, 56);
			// public String name(String clientID, String instname, int quantity, String
			// destClientID, int messageType)
		}

	}

	public void processSell() {

	}

	/**
	 * @param connID the connID to set
	 */
	public void setConnID(String _connID) {
		this.ConnID = _connID;
	}

	public void setClientHandler(ClientHandler _ch) {
		this.ch = _ch;
	}

	public static void main(String[] args) {

		// System.out.println(BCrypt.withDefaults().hashToString(10,
		// "hhh".toCharArray()));

		Colour.out.green("\n\tWELCOME TO THE FIX BROKER PORTAL\n");

		Broker br = new Broker();
		if (br.login()) {
			try {
				ClientHandler ch = ClientHandler.start(5000);
				br.setClientHandler(ch);
				if (ch.getConnID() != null && !ch.getConnID().isEmpty()) {
					br.setConnID(ch.getConnID());
					br.processBuySell();
					// System.out.println(ch.sendMessage("markets"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
