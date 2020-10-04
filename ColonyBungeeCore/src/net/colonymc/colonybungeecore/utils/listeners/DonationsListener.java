package net.colonymc.colonybungeecore.utils.listeners;

import java.util.ArrayList;

import net.colonymc.colonyapi.MainDatabase;
import net.colonymc.colonyapi.bungee.DatabaseConnectEvent;
import net.craftingstore.bungee.events.DonationReceivedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DonationsListener implements Listener {
	
	ArrayList<Donation> failedDonations = new ArrayList<Donation>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDonation(DonationReceivedEvent e) {
		String username = e.getUsername();
		String uuid = MainDatabase.getUuid(username);
		MainDatabase.sendStatement("INSERT INTO PlayerDonations (name, uuid, packageName, packagePrice, timeDonated) VALUES ("
				+ "'" + username + "', '" + uuid + "', '" + e.getPackageName() + "', " + e.getPackagePrice() + ", " + System.currentTimeMillis() + ");");
		failedDonations.add(new Donation(e.getUsername(), e.getPackageName(), e.getPackagePrice(), System.currentTimeMillis()));
	}
	
	public void onDatabaseConnect(DatabaseConnectEvent e) {
		for(Donation don : failedDonations) {
			String username = don.getName();
			String uuid = MainDatabase.getUuid(username);
			MainDatabase.sendStatement("INSERT INTO PlayerDonations (name, uuid, packageName, packagePrice, timeDonated) VALUES ("
					+ "'" + username + "', '" + uuid + "', '" + don.getPackageName() + "', " + don.getPackagePrice() + ", " + don.getTimeDonated() + ");");
		}
	}

}
