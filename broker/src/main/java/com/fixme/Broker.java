package com.fixme;

import com.fixme.Colour;
import com.fixme.Helpers;
import com.fixme.BrokerHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Broker {
	private Scanner scn;
	private Map<String, Object> details;

	private String ConnID;

	public Broker() {
		this.scn = new Scanner(System.in);
	}

	public boolean login() {
		boolean valid = false;

		JSONParser jp = new JSONParser();

		try (FileReader reader = new FileReader("../assets/Brokers.json")) {
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

	public void processBuySell() {
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

	public void processBuy() {

	}

	public void processSell() {

	}

	/**
	 * @param connID the connID to set
	 */
	public void setConnID(String _connID) {
		this.ConnID = _connID;
	}

	public static void main(String[] args) {

		// System.out.println(BCrypt.withDefaults().hashToString(10,
		// "hhh".toCharArray()));

		Colour.out.green("\n\tWELCOME TO THE FIX PORTAL\n");

		Broker br = new Broker();
		if (br.login()) {
			BrokerHandler bh = BrokerHandler.start();
			br.setConnID(bh.getConnID());
			br.processBuySell();
		}
	}
}
