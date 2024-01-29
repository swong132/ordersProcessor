package processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class OrdersProcessor {	
	public static void main(String args[]) throws InterruptedException, IOException {
		boolean multiThread = false;
		int numberOfOrders;
		String baseFile;
		String resultFile;
		TreeSet<Client> clients = new TreeSet<>(new clientsComparator());
		TreeMap<String, Double> itemsCost = new TreeMap<>();
		TreeMap<String, Integer> numberSold = new TreeMap<>();

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter item's data file name: ");
		File itemsData = new File(sc.nextLine());
		Scanner fileReader = new Scanner(itemsData);
		while (fileReader.hasNextLine()) {
			String item = fileReader.next();
			itemsCost.put(item, fileReader.nextDouble());
			numberSold.put(item, 0);
		}
		System.out.println("Enter 'y' for multiple threads, any other "
				+ "character otherwise: ");
		if (sc.nextLine().equals("y")) {
			multiThread = true;
		}
		System.out.println("Enter number of orders to process: ");
		numberOfOrders = sc.nextInt();
		System.out.println("Enter order's base filename: ");
		baseFile = sc.next();
		System.out.println("Enter result's filename: ");
		resultFile = sc.next();
		sc.close();
		fileReader.close();
		long startTime = System.currentTimeMillis();
		if (multiThread == false) {
			ReadProcessOrders order = 
					new ReadProcessOrders(baseFile, itemsCost, clients);
			for (int i = 1; i <= numberOfOrders; i++) {
				order.setFileName(baseFile + i + ".txt");
				order.run();
			}
		} else {
			ArrayList<Thread> allThreads = new ArrayList<>();
			for (int i = 1; i <= numberOfOrders; i++) {
				allThreads.add(new Thread(new ReadProcessOrders(baseFile + i + 
						".txt", itemsCost, clients)));
			}
			for (Thread t : allThreads) {
				t.start();
			}
			for (Thread t : allThreads) {
				t.join();
			}  
		}
		FileWriter writer = new FileWriter(resultFile, true);
		for (Client c : clients) {
			writer.append(c.getSummary());
			for (String item : numberSold.keySet()) {
				numberSold.put(item, numberSold.get(item) + 
						c.getAmountBought(item));
			}
		}
		StringBuffer output = new StringBuffer("");
		output.append("***** Summary of all orders *****\n");
		double grandTotal = 0;
		double itemTotal = 0;
		for (String item : numberSold.keySet()) {
			itemTotal = numberSold.get(item) * itemsCost.get(item);
			grandTotal += itemTotal;
			output.append("Summary - Item's name: " + item + ", Cost per item: " 
					+ NumberFormat.getCurrencyInstance().format
					(itemsCost.get(item)) + ", Number sold: " + 
					numberSold.get(item) + ", Item's Total: " + 
					NumberFormat.getCurrencyInstance().format(itemTotal) + "\n");
		}
		writer.append(output);
		writer.append("Summary Grand Total: " + 
				NumberFormat.getCurrencyInstance().format(grandTotal) + "\n");
		writer.close();
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time (msec): " + (endTime - startTime));
		System.out.println("Results can be found in the file: " + resultFile);
	}
}