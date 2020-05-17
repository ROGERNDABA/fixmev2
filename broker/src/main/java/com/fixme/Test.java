// package com.fixme;

// import com.fixme.Colour;

// import java.io.IOException;
// import java.net.ConnectException;
// import java.net.InetSocketAddress;
// import java.nio.ByteBuffer;
// import java.nio.channels.SelectionKey;
// import java.nio.channels.Selector;
// import java.nio.channels.SocketChannel;

// public class BrokerHandler {
// private static SocketChannel channel;
// private String ConnID;
// private static ByteBuffer buffer = ByteBuffer.allocate(1024);
// private static BrokerHandler instance;

// public static BrokerHandler start() {
// if (instance == null)
// instance = new BrokerHandler();
// return instance;
// }

// public static void stop() throws IOException {
// channel.close();
// buffer = null;
// }

// private BrokerHandler() {

// try {
// channel = SocketChannel.open();
// channel.connect(new InetSocketAddress("localhost", 5000));
// channel.configureBlocking(false);
// while (!channel.finishConnect())
// Colour.out.green("\n\tConnecting to FIX router...");
// Selector selector = Selector.open();
// channel.register(selector, SelectionKey.OP_READ);
// buffer.clear();
// channel.read(buffer);
// String response = new String(buffer.array()).trim();
// this.ConnID = response;
// Colour.out.green("\tConnected to FIX router: Connection ID = " + this.ConnID
// + ".\n");

// // this.addr = new InetSocketAddress(this.hostName, this.port);

// // client = SocketChannel.open(new InetSocketAddress("localhost", 5000));
// // buffer = ByteBuffer.allocate(256);

// // buffer.clear();
// // client.read(buffer);
// // String response = new String(buffer.array()).trim();
// // this.ConnID = response;

// } catch (ConnectException ce) {
// if (ce.getMessage().contains("Connection refused:"))
// Colour.out.red("\n\tConnection refused by FIX router.\n");
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// // public String sendMessage(String msg) {
// // buffer = ByteBuffer.wrap(msg.getBytes());
// // String response = null;
// // try {
// // client.write(buffer);
// // buffer.clear();
// // client.read(buffer);
// // response = new String(buffer.array()).trim();
// // System.out.println("response=" + response);
// // buffer.clear();
// // } catch (IOException e) {
// // e.printStackTrace();
// // }
// // return response;

// // }

// /**
// * @return the connID
// */
// public String getConnID() {
// return this.ConnID;
// }

// }
