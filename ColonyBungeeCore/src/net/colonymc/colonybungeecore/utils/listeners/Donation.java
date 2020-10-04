package net.colonymc.colonybungeecore.utils.listeners;

public class Donation {
	
	String name;
	String packageName;
	int packagePrice;
	long timeDonated;
	
	public Donation(String name, String packageName, int packagePrice, long timeDonated) {
		this.name = name;
		this.packageName = packageName;
		this.packagePrice = packagePrice;
		this.timeDonated = timeDonated;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public int getPackagePrice() {
		return packagePrice;
	}
	
	public long getTimeDonated() {
		return timeDonated;
	}

}
