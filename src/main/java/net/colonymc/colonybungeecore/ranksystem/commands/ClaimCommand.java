package net.colonymc.colonybungeecore.ranksystem.commands;

import net.colonymc.colonyapi.database.MainDatabase;
import net.colonymc.colonybungeecore.Main;
import net.colonymc.colonybungeecore.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ClaimCommand extends Command{

	public ClaimCommand() {
		super("claim");
	}
	
	static final HashMap<ProxiedPlayer, Long> claimed = new HashMap<>();

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(MainDatabase.isConnected()) {
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				if(!claimed.containsKey(p) || claimed.get(p) <= System.currentTimeMillis()) {
					claimed.put(p, System.currentTimeMillis() + 10000);
					if(args.length == 1) {
						if(isValidToken(args[0])) {
							try {
								ResultSet rs = MainDatabase.getResultSet("SELECT * FROM VoucherCodes WHERE voucherCode='" + args[0] + "';");
								if(rs.next()) {
									if(!rs.getString("claimed").equals("0")) {
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis code has been claimed by someone else!")));
									}
									else {
										String rank = rs.getString("rankName");
										if(!p.hasPermission(rank.toLowerCase() + ".store")) {
											MainDatabase.sendStatement("UPDATE VoucherCodes SET claimed='" + p.getUniqueId().toString()
													+ "' WHERE voucherCode='" + rs.getString("voucherCode") + "';");
											ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + p.getName() + " parent set " + rank.toLowerCase());
											p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have claimed your &d&l" + rank + " &frank!")));
										}
										else {
											p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot claim this code because you have a higher rank!")));
										}
									}
								}
								else {
									p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis code does not exist!")));
								}
							} catch (SQLException e) {
								e.printStackTrace();
								p.sendMessage(new TextComponent(Messages.errorOccured));
								Main.writeToLog("Failed to claim the rank voucher " + args[0] + "! Executed by " + p.getName() + ".");
							}
						}
						else {
							p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid code! A code looks like this: XXXX-XXXX-XXXX-XXXX")));
						}
					}
					else {
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/claim <code>")));
					}
				}
				else if(claimed.containsKey(p)) {
					p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease wait another " + (((claimed.get(p) - System.currentTimeMillis())/1000) + 1) + 
							" seconds before doing this again!")));
				}
			}
			else {
				sender.sendMessage(new TextComponent(Messages.onlyPlayers));
			}
		}
		else {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occurred trying to connect to the database. Please contact an administrator.")));
		}
	}
	
	private boolean isValidToken(String s) {
		return s.length() == 19 && s.charAt(4) == '-' && s.charAt(9) == '-' && s.charAt(14) == '-';
	}

}
