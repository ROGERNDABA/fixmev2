package com.fixme;

import com.fixme.Colour;

import java.util.HashMap;
import java.util.Scanner;

// import java.io.IOException;
// import java.net.InetSocketAddress;
// import java.nio.ByteBuffer;
// import java.nio.channels.SocketChannel;

public class Broker {
	private Scanner scn;
	private String ConnID = "";
	private HashMap<String, Object> details;

	// private static SocketChannel client;
	// private String ConnID;
	// private static ByteBuffer buffer;
	// private static Broker instance;

	public Broker() {
		this.scn = new Scanner(System.in);
	}

	public boolean login() {
		Colour.white("Please enter broker username: ");
		String username = this.scn.nextLine().trim();
		Colour.white("Please enter broker username: ");
		String password = this.scn.nextLine().trim();

	}

	// public static void stop() throws IOException {
	// client.close();
	// buffer = null;
	// }

	// private Broker() {
	// try {
	// client = SocketChannel.open(new InetSocketAddress("localhost", 5000));
	// buffer = ByteBuffer.allocate(256);

	// buffer.clear();
	// client.read(buffer);
	// String response = new String(buffer.array()).trim();

	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// public String sendMessage(String msg) {
	// buffer = ByteBuffer.wrap(msg.getBytes());
	// String response = null;
	// try {
	// client.write(buffer);
	// buffer.clear();
	// client.read(buffer);
	// response = new String(buffer.array()).trim();
	// System.out.println("response=" + response);
	// buffer.clear();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return response;

	// }

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		Colour.out.green("\n\tWELCOME TO THE FIX PORTAL\n");

		// Broker br = Broker.start();
		// br.sendMessage("new");

		// try {

		// Broker.stop();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
