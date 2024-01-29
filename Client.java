package processor;

import java.text.NumberFormat;
import java.util.TreeMap;

public class Client {
	int clientId;
	TreeMap<String, Integer> items;
	TreeMap<String, Double> itemCost;
	String summary;

	public Client(int clientId, TreeMap<String, Double> itemCost) {
		this.clientId = clientId;
		items  = new TreeMap<String, Integer>();
		this.itemCost = itemCost;
		summary = "";
	}

	public void addItem(String name) {
		if (items.containsKey(name)) {
			int currentQty = items.get(name) + 1;
			items.put(name, currentQty);
		}
		else {
			items.put(name, 1);
		}
		return;
	}

	public int getAmountBought(String item) {
		if (items.containsKey(item)) {
			return items.get(item);
		}
		return 0;
	}

	public void setSummary() {
		StringBuilder output = new StringBuilder("");
		output.append("----- Order details for client with Id: " + 
				clientId + " -----\n");
		double itemTotal = 0;
		double orderTotal = 0;
		for (String item: items.keySet()) {
			if (itemCost.containsKey(item)) {
				itemTotal =  itemCost.get(item) * items.get(item);
				orderTotal += itemTotal;

				output.append("Item's name: " + item + ", Cost per item: " 
						+ NumberFormat.getCurrencyInstance().format((double)itemCost.get(item)) 
						+ ", Quantity: " + items.get(item) + ", Cost: " + 
						NumberFormat.getCurrencyInstance().format(itemTotal) + "\n");
			}
		}
		output.append("Order Total: " +  NumberFormat.
				getCurrencyInstance().format(orderTotal) + "\n");
		summary = output.toString();
		return;
	}

	public String getSummary() {
		return summary;
	}
}
