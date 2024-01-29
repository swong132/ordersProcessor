package processor;

import java.util.Comparator;

public class clientsComparator implements Comparator<Client> {

	@Override
	public int compare(Client o1, Client o2) {
		return o1.clientId - o2.clientId;
	}
	
}
