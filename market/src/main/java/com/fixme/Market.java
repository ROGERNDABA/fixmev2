package com.fixme;

import com.fixme.ClientHandler;
import com.fixme.Helpers;
import com.fixme.Fix;

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
import java.util.Random;
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

	public boolean login() {
		boolean valid = false;

		JSONParser jp = new JSONParser();

		try {
			File file = new File("").getCanonicalFile();
			FileReader reader = new FileReader(file.getParent() + "/fixme/assets/Markets.json");
			Object obj = jp.parse(reader);

			JSONArray brokerList = (JSONArray) obj;

			while (valid == false) {
				Colour.white("Please enter market username: ");
				String username = this.scn.nextLine().trim();
				Colour.white("Please enter market password: ");
				String password = this.scn.nextLine().trim();

				for (Object o : brokerList) {
					JSONObject broker = (JSONObject) o;
					String busername = (String) broker.get("market");
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

	public String processFixBuyMessage(String msg) {
		JSONParser jp = new JSONParser();

		try {
			File file = new File("").getCanonicalFile();
			FileReader reader = new FileReader(file.getParent() + "/fixme/assets/Instruments.json");
			Object obj = jp.parse(reader);

			JSONArray sList = (JSONArray) obj;

			String bb = this.details.get("instruments_sale");

			int[] inst_ids = Helpers.parseStringArr(bb, ",");

			String toBuy = Fix.getFixPart(msg, 99);

			String v1 = "";
			String v2 = "";
			String v3 = "";
			String v4 = "";

			for (Object o : sList) {
				JSONObject b = (JSONObject) o;

				if (toBuy.equals((String) b.get("instrument_name"))) {
					for (int i : inst_id) {
						if (i == (int) b.get("index")) {
							int rti = new Random().nextBoolean() ? 1 : 2;
							if (rti == 1) {
								int v = (int) b.get("value");
								int q = (int) b.get("quantity");
								int pu = (int) b.get("price_per_unit");
								if (v > 0) {
									b.put("quantity", q - 1);
									b.put("value", v - pu);
								}
							}

						}
					}
				}
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return "";

	}

	public void setConnID(String _connID) {
		this.ConnID = _connID;
	}

	public void setClientHandler(ClientHandler _ch) {
		this.ch = _ch;
	}

	public static void main(String[] args) {

		Colour.out.green("\n\tWELCOME TO THE FIX MARKET PORTAL\n");

		Market ma = new Market();
		if (ma.login()) {
			ma.processFixBuyMessage("8=FIX-42|9=39|49=059801|56=059876|99=GOLD|88=33|35=1|10=098|");
			// try {
			// ClientHandler ch = ClientHandler.start(5001);
			// ma.setClientHandler(ch);
			// if (ch.getConnID() != null && !ch.getConnID().isEmpty()) {
			// ma.setConnID(ch.getConnID());
			// // while (true) {
			// // String fromServer = ch.getServerResponse();
			// // if (fromServer == null)
			// // return;
			// // System.out.println("Server message:" + fromServer);
			// // ch.sendMessage(Fix.encode(ch.getConnID(), "roger", "0",
			// // Fix.getFixPart(fromServer, 56), 1));
			// // // fromServer

			// // }
			// // br.processBuySell();
			// // System.out.println(ch.sendMessage("markets"));
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}

	}
}
