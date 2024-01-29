package processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReadProcessOrders implements Runnable{
	String orderFileName;
	TreeMap<String, Double> itemsCost;
	TreeSet<Client> clients;
	
	public ReadProcessOrders(String orderFileName, TreeMap<String, Double> itemsCost, TreeSet<Client> clients) {
		this.orderFileName = orderFileName;
		this.itemsCost = itemsCost;
		this.clients = clients;
	}
	
	public void setFileName(String orderFileName) {
		this.orderFileName = orderFileName;
	}
	
	

	@Override
	public void run() {
		File order = new File(orderFileName);
		try {
			Scanner fileReader = new Scanner(order);
			fileReader.next();
			int clientId = fileReader.nextInt();
			System.out.println("Reading order for client with id: " + clientId);
			Client client = new Client(clientId, itemsCost);
			synchronized(clients) {
				clients.add(client);
			}
			while (fileReader.hasNextLine()) {
				String item = fileReader.next();
				client.addItem(item);
			}
			client.setSummary();
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
	}

}
