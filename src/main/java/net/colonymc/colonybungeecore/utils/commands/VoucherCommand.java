package net.colonymc.colonybungeecore.utils.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.colonymc.colonyapi.MainDatabase;
import net.colonymc.colonybungeecore.Main;
import net.colonymc.colonybungeecore.Messages;
import net.colonymc.colonybungeecore.UUIDGetter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class VoucherCommand extends Command implements TabExecutor{
	
	public VoucherCommand() {
		super("rankvoucher");
	}
	
	final ArrayList<String> ranks = new ArrayList<>();
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		Set<String> matches = new HashSet<>();
		String search = args[0].toLowerCase();
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if(p.getName().toLowerCase().startsWith(search)) {
        		matches.add(p.getName());
            }
		}
		return matches;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(MainDatabase.isConnected()) {
			ranks.add("prince");
			ranks.add("king");
			ranks.add("archon");
			ranks.add("overlord");
			ranks.add("colony");
			if(sender.hasPermission("*")) {
				if(args.length == 3) {
					if(args[0].equals("add")) {
						if(ProxyServer.getInstance().getPlayer(args[1]) != null) {
							ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
							boolean rankExists = false;
							for(String s : ranks) {
								if (s.equalsIgnoreCase(args[2])) {
									rankExists = true;
									break;
								}
							}
							if(rankExists) {
								String token = getRandomToken();
								MainDatabase.sendStatement("INSERT INTO VoucherCodes (name, uuid, rank, voucherCode, boughtOn, claimed) "
										+ "VALUES ('" + target.getName() + "', '" + target.getUniqueId().toString() + "', '" + args[2].substring(0, 1).toUpperCase() + args[2].substring(1).toLowerCase() + "', '" + token + "', " + System.currentTimeMillis() + ", 0);");
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou gave &d" + target.getName() + " &fa rank voucher for the rank &d" + args[2] + "&f!")));
							}
							else {
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis rank doesn't exist. Please enter a valid one.")));
							}
						}
						else if(UUIDGetter.getUUID(args[1]) != null){
							boolean rankExists = false;
							for(String s : ranks) {
								if (s.equalsIgnoreCase(args[2])) {
									rankExists = true;
									break;
								}
							}
							if(rankExists) {
								String token = getRandomToken();
								MainDatabase.sendStatement("INSERT INTO VoucherCodes (name, uuid, rank, voucherCode, boughtOn, claimed) "
										+ "VALUES ('" + args[1] + "', '" + UUIDGetter.getUUID(args[1]) + "', '" + args[2].substring(0, 1).toUpperCase() + args[2].substring(1).toLowerCase() + "', '" + token + "', " + System.currentTimeMillis() + ", 0);");
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou gave &d" + args[1] + " &fa rank voucher for the rank &d" + args[2] + "&f!")));
							}
							else {
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis rank doesn't exist. Please enter a valid one.")));
							}
						}
						else {
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis player doesn't exist.")));
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/rankvoucher add <player> <rank>")));
					}
				}
				else if(args.length == 2) {
					switch(args[0]) {
					case "remove":
						try {
							ResultSet rs = MainDatabase.getResultSet("SELECT * FROM VoucherCodes WHERE voucherCode='" + args[1] + "';");
							if(rs.next()) {
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou removed the voucher &d" + args[1] + " &ffrom the player &d" + rs.getString("name") + " &ffor the rank &d" + rs.getString("rank") +"&f!")));
								MainDatabase.sendStatement("DELETE FROM VoucherCodes WHERE voucherCode='" + args[1] + "';");
							}
							else {
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis voucher code doesn't exist!")));
							}
						} catch (SQLException e) {
							sender.sendMessage(new TextComponent(Messages.errorOccured));
							Main.writeToLog("Failed to remove the rank voucher " + args[1] + "! Executed by " + sender.getName() + ".");
							e.printStackTrace();
						}
						break;
					case "list":
						if(UUIDGetter.getUUID(args[1]) != null) {
							try {
								ResultSet rs = MainDatabase.getResultSet("SELECT * FROM VoucherCodes WHERE name='" + args[1] + "';");
								boolean next = rs.next();
								if(next) {
									TextComponent ranks = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------------------------------------\n &5&l» &fRank vouchers of the player &d" + rs.getString("name") + "&f:\n"));
									SimpleDateFormat sdm = new SimpleDateFormat("dd/MM/yy");
									ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n &5&l» &fVoucher Code: &d" + rs.getString("voucherCode") + "\n     &fRank: &d" 
									+ rs.getString("rank") + "\n     Purchased on: &d" + sdm.format(new Date(rs.getLong("boughtOn")))));
									if(!rs.getString("claimed").equals("0")) {
										ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n     &c(Claimed)\n"));
									}
									else {
										ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n     &a(Not claimed)\n"));
									}
									while(rs.next()) {
										ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n &5&l» &fVoucher Code: &d" + rs.getString("voucherCode") + "\n     &fRank: &d" 
										+ rs.getString("rank") + "\n     Purchased on: &d" + sdm.format(new Date(rs.getLong("boughtOn")))));
										if(!rs.getString("claimed").equals("0")) {
											ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n     &c(Claimed)\n"));
										}
										else {
											ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "\n     &a(Not claimed)\n"));
										}
									}
									ranks.addExtra(ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------------------------------------"));
									sender.sendMessage(ranks);
								}
								else {
									sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis player doesn't have any voucher codes!")));
								}
							} catch (SQLException e) {
								sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured while trying to do this! Please contact an admin!")));
								Main.writeToLog("Failed to retrieve the rank vouchers list of " + args[1] + "! Executed by " + sender.getName() + ".");
								e.printStackTrace();
							}
						}
						else {
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThis player doesn't exist.")));
						}
						break;
					default:
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/rankvoucher add/remove/list <player OR code to remove> <rank>")));
						break;
					}
				}
				else {
					sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/rankvoucher add/remove/list <player OR code to remove> <rank>")));
				}
			}
			else {
				sender.sendMessage(new TextComponent(Messages.noPerm));
			}
		}
		else {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured trying to connect to the database. Please contact an administrator.")));
		}
	}
	
	private static String getRandomToken() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVYXZ1234567890";
		StringBuilder token = new StringBuilder();
		for(int i = 0; i < 16; i++) {
			Random rand = new Random();
			int index = rand.nextInt(35);
			token.append(characters.charAt(index));
		}
		String tokenWithDashes = token.substring(0,4) + "-" + token.substring(4, 8) + "-" + token.substring(8, 12) + "-" + token.substring(12, 16);
		try {
			ResultSet rs = MainDatabase.getResultSet("SELECT voucherCode FROM VoucherCodes");
			while(rs.next()) {
				if(rs.getString("voucherCode").equals(tokenWithDashes)) {
					for(int i = 0; i < 16; i++) {
						Random rand = new Random();
						int index = rand.nextInt(35);
						token.append(characters.charAt(index));
					}
					tokenWithDashes = token.substring(0,4) + "-" + token.substring(4, 8) + "-" + token.substring(8, 12) + "-" + token.substring(12, 16);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tokenWithDashes;
	}

}
