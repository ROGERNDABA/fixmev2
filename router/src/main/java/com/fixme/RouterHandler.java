package com.fixme;

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

	private ByteBuffer buffer = ByteBuffer.allocate(1024);
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

					if (key.isAcceptable()) {
						this.handleAccept(selector, ssc);
					} else if (key.isReadable()) {
						System.out.println("yep!!!");

					}
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

		this.clients.put(ID, client);
	}

	public void setHandler(RouterHandler _handler) {
		this.client = _handler;
		System.out.println("This is : " + _handler.getName());
	}

	public String getName() {
		return this.name;
	}

}
