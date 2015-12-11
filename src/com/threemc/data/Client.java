package com.threemc.data;

import java.util.Comparator;

import com.threemc.view.CategoryGender;

/**
 * @author Rashmier Ynawat
 * @since July 3, 2015
 * @version 1.0
 */
public class Client {

	private int id;
	private String clientFirstName;
	private String clientMiddleName;
	private String clientLastName;
	private String clientAddress;
	private String clientContactNumber;
	private CategoryGender clientGender;
	private boolean hasEvent;

	public Client(String clientFirstName, String clientMiddleName,
			String clientLastName, String clientAddress,
			String clientContactNumber, CategoryGender clientGender) {

		this.clientFirstName = clientFirstName;
		this.clientMiddleName = clientMiddleName;
		this.clientLastName = clientLastName;
		this.clientAddress = clientAddress;
		this.clientContactNumber = clientContactNumber;
		this.clientGender = clientGender;
	}

	public Client(int id, String clientFirstName, String clientMiddleName,
			String clientLastName, String clientAddress,
			String clientContactNumber, CategoryGender clientGender) {

		this(clientFirstName, clientMiddleName, clientLastName, clientAddress,clientContactNumber,clientGender);
		this.id = id;
	}
	
	public Client(int id, String clientFirstName, String clientMiddleName,
			String clientLastName, String clientAddress,
			String clientContactNumber, CategoryGender clientGender, boolean hasEvent) {

		this(clientFirstName, clientMiddleName, clientLastName, clientAddress,clientContactNumber,clientGender);
		this.id = id;
		this.hasEvent = hasEvent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientMiddleName() {
		return clientMiddleName;
	}

	public void setClientMiddleName(String clientMiddleName) {
		this.clientMiddleName = clientMiddleName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientContactNumber() {
		return clientContactNumber;
	}

	public void setClientContactNumber(String clientContactNumber) {
		this.clientContactNumber = clientContactNumber;
	}

	public CategoryGender getClientGender() {
		return clientGender;
	}

	public void setClientGender(CategoryGender clientGender) {
		this.clientGender = clientGender;
	}

	public boolean isHasEvent() {
		return hasEvent;
	}

	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}

	 public static Comparator<Client> CIdComp = new Comparator<Client>() {
			public int compare(Client c1, Client c2) {
				Integer client1 = c1.getId();
				Integer client2 = c2.getId();
				//ascending order
				return client1.compareTo(client2);
				//descending order
				//return client2.compareTo(client1);
			}
		};

	 public static Comparator<Client> CFnameComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientFirstName().toUpperCase();
			String client2 = c2.getClientFirstName().toUpperCase();
			return client1.compareTo(client2);
		}
	};

	public static Comparator<Client> CMnameComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientMiddleName().toUpperCase();
			String client2 = c2.getClientMiddleName().toUpperCase();
			return client1.compareTo(client2);
		}
	};

	public static Comparator<Client> CLnameComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientLastName().toUpperCase();
			String client2 = c2.getClientLastName().toUpperCase();
			return client1.compareTo(client2);
		}
	};

	public static Comparator<Client> CAddressComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientAddress().toUpperCase();
			String client2 = c2.getClientAddress().toUpperCase();
			return client1.compareTo(client2);
		}
	};

	public static Comparator<Client> CContComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientContactNumber().toUpperCase();
			String client2 = c2.getClientContactNumber().toUpperCase();
			return client1.compareTo(client2);
		}
	};

	public static Comparator<Client> CGenComp = new Comparator<Client>() {
		public int compare(Client c1, Client c2) {
			String client1 = c1.getClientGender().name().toUpperCase();
			String client2 = c2.getClientGender().name().toUpperCase();
			return client1.compareTo(client2);
		}
	};
}
