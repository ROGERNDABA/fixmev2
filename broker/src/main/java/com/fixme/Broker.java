package com.fixme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Broker {
	private static SocketChannel client;
	private String ConnID;
	private static ByteBuffer buffer;
	private static Broker instance;

	public static Broker start() {
		if (instance == null)
			instance = new Broker();

		return instance;
	}

	public static void stop() throws IOException {
		client.close();
		buffer = null;
	}

	private Broker() {
		try {
			client = SocketChannel.open(new InetSocketAddress("localhost", 5000));
			buffer = ByteBuffer.allocate(256);

			buffer.clear();
			client.read(buffer);
			String response = new String(buffer.array()).trim();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendMessage(String msg) {
		buffer = ByteBuffer.wrap(msg.getBytes());
		String response = null;
		try {
			client.write(buffer);
			buffer.clear();
			client.read(buffer);
			response = new String(buffer.array()).trim();
			System.out.println("response=" + response);
			buffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;

	}

	public static void main(String[] args) {
		Broker br = Broker.start();
		while (true) {

		}
		// try {

		// // br.sendMessage("new");
		// Broker.stop();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
