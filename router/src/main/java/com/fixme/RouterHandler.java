package com.fixme;

import com.fixme.Colour;
import com.fixme.TimePrint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RouterHandler implements Runnable {

	private int port;
	private String name;
	private RouterHandler oclient;

	private HashMap<String, SocketChannel> clients = new HashMap<String, SocketChannel>();

	public RouterHandler(String _name, int _port) {
		this.port = _port;
		this.name = _name;
	}

	public void run() {
		try {
			Selector selector = Selector.open();
			ServerSocketChannel ssc = ServerSocketChannel.open();

			ssc.bind(new InetSocketAddress(this.port));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (ssc.isOpen()) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();

				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {

					SelectionKey key = iter.next();

					if (!key.isValid())
						continue;
					if (key.isAcceptable())
						this.handleAccept(selector, ssc);
					if (key.isReadable())
						this.handleReadWrite(selector, key);
					iter.remove();
				}
			}

		} catch (IOException e) {
			System.out.println("Port: " + this.port + " error");
			e.printStackTrace();
		}

	}

	private void handleAccept(Selector selector, ServerSocketChannel ssc) throws IOException {
		SocketChannel client = ssc.accept();

		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);

		String ID = String.format("%06d", client.socket().getPort());
		ByteBuffer IDresp = ByteBuffer.wrap(ID.getBytes());

		TimePrint.print(this.name + " " + ID + " connected.");

		client.write(IDresp);
		IDresp.rewind();

		this.clients.put(ID, client);
	}

	private void handleReadWrite(Selector selector, SelectionKey sKey) throws IOException {
		SocketChannel client = (SocketChannel) sKey.channel();
		String ID = String.format("%06d", client.socket().getPort());
		ByteBuffer buff = ByteBuffer.allocate(1024);

		try {
			String msg = "";
			int read;

			buff.flip();
			buff.clear();

			while ((read = client.read(buff)) > 0) {
				buff.flip();
				msg = Charset.forName("UTF-8").decode(buff).toString().trim();
				buff.clear();
			}
			if (read < 0) {
				TimePrint.print(this.name + " " + ID + " disconnected.");
				this.clients.remove(ID);
				client.close();
			} else {
				if (msg.equals("markets")) {
					buff.flip();
					buff.clear();
					buff.put(this.getMarkets().getBytes());
					buff.flip();
					client.write(buff);
				} else {
					System.out.println("==================");
				}

			}

		} catch (IOException ioe) {
			if (ioe.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
				TimePrint.print(this.name + " " + ID + " disconnected.");
			}
			this.clients.remove(ID);
			client.close();
		}
	}

	private void proccessMessage() {
	}

	private String getMarkets() {
		HashMap<String, SocketChannel> markets = this.oclient.getClients();
		List<String> keys = new ArrayList<>(markets.keySet());
		return String.join(",", keys) + " ";
	}

	private void respondToClient(ByteBuffer buff, SelectionKey skey) throws IOException {
		// SocketChannel
	}

	public void setHandler(RouterHandler _handler) {
		this.oclient = _handler;
	}

	public String getName() {
		return this.name;
	}

	public HashMap<String, SocketChannel> getClients() {
		return this.clients;
	}

}
