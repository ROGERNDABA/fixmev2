package com.fixme;

import com.fixme.Colour;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BrokerHandler {
	private static SocketChannel client;
	private String ConnID;
	private static ByteBuffer buffer;
	private static BrokerHandler instance;

	public static BrokerHandler start() {
		if (instance == null)
			instance = new BrokerHandler();
		return instance;
	}

	public static void stop() throws IOException {
		client.close();
		buffer = null;
	}

	private BrokerHandler() {
		try {
			client = SocketChannel.open(new InetSocketAddress("localhost", 5000));
			buffer = ByteBuffer.allocate(256);

			buffer.clear();
			client.read(buffer);
			String response = new String(buffer.array()).trim();
			this.ConnID = response;

			Colour.out.green("\n\tConnected to FIX router: Connection ID = " + this.ConnID + ".\n");
		} catch (ConnectException ce) {
			if (ce.getMessage().contains("Connection refused:"))
				Colour.out.red("\tConnection refused by FIX router.\n");
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

	/**
	 * @return the connID
	 */
	public String getConnID() {
		return this.ConnID;
	}

}
