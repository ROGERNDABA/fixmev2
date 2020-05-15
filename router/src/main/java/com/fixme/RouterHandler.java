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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RouterHandler implements Runnable {

	private int port;
	private String name;
	private RouterHandler client;

	private HashMap<String, SocketChannel> clients = new HashMap<String, SocketChannel>();
	private ByteBuffer buff = ByteBuffer.allocate(1024);

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

		try {
			StringBuilder sb = new StringBuilder();

			buff.clear();
			int read = 0;
			while ((read = client.read(buff)) > 0) {
				buff.flip();
				byte[] bytes = new byte[buff.limit()];
				buff.get(bytes);
				sb.append(new String(bytes));
				buff.clear();
			}
			String msg;
			if (read < 0) {
				msg = sKey.attachment() + " left the chat.\n";
				client.close();
			} else {
				msg = sKey.attachment() + ": " + sb.toString();
			}

			System.out.println(msg);

		} catch (IOException ioe) {
			if (ioe.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
				String ID = String.format("%06d", client.socket().getPort());
				TimePrint.print(this.name + " " + ID + " disconnected.");
				client.close();
			}
		}

		// System.out.println("Yep: |" + clients.toString() + "| " +
		// client.socket().getPort());
	}

	private void respondToClient(ByteBuffer buff, SelectionKey skey) throws IOException {
		// SocketChannel
	}

	public void setHandler(RouterHandler _handler) {
		this.client = _handler;
		System.out.println("This is : " + _handler.getName());
	}

	public String getName() {
		return this.name;
	}

}
