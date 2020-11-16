package net.colonymc.colonybungeecore.utils.listeners;

import net.colonymc.colonyapi.database.MainDatabase;
import net.craftingstore.bungee.events.DonationReceivedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DonationsListener implements Listener {

	@EventHandler
	public void onDonation(DonationReceivedEvent e) {
		if(MainDatabase.isConnected()){
			String username = e.getUsername();
			String uuid = MainDatabase.getUuid(username);
			MainDatabase.sendStatement("INSERT INTO PlayerDonations (name, uuid, packageName, packagePrice, timeDonated) VALUES ("
					+ "'" + username + "', '" + uuid + "', '" + e.getPackageName() + "', " + e.getPackagePrice() + ", " + System.currentTimeMillis() + ");");
		}
	}

}
