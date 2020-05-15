package com.fixme;

import com.fixme.RouterHandler;

public class Router {
	public static void main(String[] args) {
		RouterHandler rh1 = new RouterHandler("Broker", 5000);
		RouterHandler rh2 = new RouterHandler("Market", 5001);

		rh1.setHandler(rh2);
		rh2.setHandler(rh1);

		Thread t1 = new Thread(rh1);
		Thread t2 = new Thread(rh2);

		t1.start();
		t2.start();
	}

}
