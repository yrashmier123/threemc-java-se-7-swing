package com.threemc.data;

import java.util.Comparator;

/**
 * @author Rashmier Ynawat
 * @since July 4, 2015
 * @version 1.0
 */
public class Service {

	private int id;
	private int pkId;
	private int scId;
	private String serviceCatname;
	private String serviceName;
	private int serviceCost;
	private String serviceRemarks;
	private String serviceCatStat;

	public Service( String serviceName, int serviceCost, String serviceRemarks, String serviceCatName) {
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
		this.serviceCatname  = serviceCatName;
	}

	public Service( int pkId, String serviceName, int serviceCost, String serviceRemarks, String serviceCatName) {
		this.pkId = pkId;
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
		this.serviceCatname  = serviceCatName;
	}

	public Service(int id , int pkId,  String serviceName, int serviceCost, String serviceRemarks, String serviceCatName) {
		this(pkId , serviceName, serviceCost, serviceRemarks,serviceCatName);
		this.id = id;
	}

	public Service( String serviceName, int serviceCost, String serviceRemarks, int scId) {
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
		this.scId = scId;
	}

	public Service( int pkId, String serviceName, int serviceCost, String serviceRemarks, int scId) {
		this.pkId = pkId;
		this.serviceName = serviceName;
		this.serviceCost = serviceCost;
		this.serviceRemarks = serviceRemarks;
		this.scId = scId;
	}

	public Service(int id , int pkId,  String serviceName, int serviceCost, String serviceRemarks, int scId) {
		this(pkId , serviceName, serviceCost, serviceRemarks,scId);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getPkId() {
		return pkId;
	}

	public void setPkId(int pkId) {
		this.pkId = pkId;
	}

	public int getScId() {
		return scId;
	}

	public void setScId(int scId) {
		this.scId = scId;
	}

	public String getServiceCatname() {
		return serviceCatname;
	}

	public void setServiceCatname(String serviceCatname) {
		this.serviceCatname = serviceCatname;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(int serviceCost) {
		this.serviceCost = serviceCost;
	}

	public String getServiceRemarks() {
		return serviceRemarks;
	}

	public void setServiceRemarks(String serviceRemarks) {
		this.serviceRemarks = serviceRemarks;
	}

	public String getServiceCatStat() {
		return serviceCatStat;
	}

	public void setServiceCatStat(String serviceCatStat) {
		this.serviceCatStat = serviceCatStat;
	}

	public static Comparator<Service> SnameComp = new Comparator<Service>() {
		public int compare(Service o1, Service o2) {
			String s1 = o1.getServiceName().toUpperCase();
			String s2 = o2.getServiceName().toUpperCase();
			return s1.compareTo(s2);
		}
	};

	public static Comparator<Service> SpriceComp = new Comparator<Service>() {
		public int compare(Service o1, Service o2) {
			Integer s1 = o1.getServiceCost();
			Integer s2 = o2.getServiceCost();
			return s1.compareTo(s2);
		}
	};

	public static Comparator<Service> ScatComp = new Comparator<Service>() {
		public int compare(Service o1, Service o2) {
			String s1 = o1.getServiceCatname().toUpperCase();
			String s2 = o2.getServiceCatname().toUpperCase();
			return s1.compareTo(s2);
		}
	};
}
