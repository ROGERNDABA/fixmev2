package com.fixme;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class RouterHandler implements Runnable {

	private int port;
	private String name;
	private RouterHandler client;

	private ByteBuffer buffer = ByteBuffer.allocate(1024);

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

			while (true) {
				selector.select();
				Set<SelectionKey> selectionKeys = selector.selectedKeys();

				System.out.println(selectionKeys.toString());
			}

		} catch (IOException e) {
			System.out.println("Port: " + this.port + " error");
			e.printStackTrace();
		}

	}

	public void setHandler(RouterHandler _handler) {
		this.client = _handler;
		System.out.println("This is : " + _handler.getName());
	}

	public String getName() {
		return this.name;
	}

}
