// package com.fixme;

// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;

// import org.json.simple.JSONArray;
// import org.json.simple.parser.ParseException;
// import org.json.simple.parser.JSONParser;

// public class Test {

// @SuppressWarnings("unused")
// public static void main(String[] args) {
// JSONParser jp = new JSONParser();

// try (FileReader reader = new FileReader("../assets/Brokers.json")) {
// Object obj = jp.parse(reader);

// JSONArray brokerList = (JSONArray) obj;

// System.out.println(brokerList);

// } catch (FileNotFoundException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
// }
