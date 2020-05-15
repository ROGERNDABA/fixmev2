package com.fixme;

public class RouterHandler implements Runnable {

	private int port;
	private String name;
	private RouterHandler client;

	public RouterHandler(String _name, int _port) {
		this.port = _port;
		this.name = _name;
	}

	public void run() {
		System.out.println("Port : " + this.port);
		while (true) {
			System.out.println("*** " + this.name + " ***");
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
