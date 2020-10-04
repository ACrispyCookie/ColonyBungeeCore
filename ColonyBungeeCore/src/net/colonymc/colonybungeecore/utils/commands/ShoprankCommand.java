package net.colonymc.colonybungeecore.utils.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.colonymc.colonybungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ShoprankCommand extends Command implements TabExecutor{

	String capitalizedName = "";
	String name = "";
	
	public ShoprankCommand(String name) {
		super("shop" + name);
		this.name = name;
		this.capitalizedName = name.substring(0,1).toUpperCase() + name.substring(1);
	}

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
		TextComponent alreadyHasRank = new TextComponent("█████████                                                    \n");
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "████&c█&r████                                                     \n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "███&c█&0█&c█&r███                                    &4&lWARNING!\n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "██&c█&6█&0█&6█&c█&r██       &cA rank was bought for your username\n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "██&c█&6█&0█&6█&c█&r██       &cbut you already have a higher (or equal) rank!\n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "██&c█&6█&0█&6█&c█&r██       &cYou have received a voucher for the corresponding\n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "█&c█&6█████&c█&r█       &crank.\n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "&c█&6███&0█&6███&c█          \n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "&c█████████                                                   \n"));
		alreadyHasRank.addExtra(ChatColor.translateAlternateColorCodes('&', "█████████                                                   "));
		if(Main.getInstance().isConnected()) {
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				if(sender.hasPermission("colonymc.staffmanager") || p.getUniqueId().toString().equals("37c3bfb6-6fa9-4602-a9bd-a1e95baea85f")) {
					if(args.length == 1) {
						if(!args[0].isEmpty()) {
							if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
								ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
								switch(name) {
								case "colony":
									if(target.hasPermission("colony.store")) {
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + target.getName() + " &falready has a higher (or equal) rank than this!")));
										target.sendMessage(alreadyHasRank);
										String token = getRandomToken(16);
										try {
											PreparedStatement ps = Main.getConnection().prepareStatement("INSERT INTO VoucherCodes (name, uuid, rank, voucherCode, boughtOn, claimed) "
													+ "VALUES ('" + target.getName() + "', '" + target.getUniqueId().toString() + "', '" + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + "', '" + token + "', " + System.currentTimeMillis() + ", 0);");
											ps.execute();
										} catch (SQLException e) {
											p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured while trying to create a new voucher code! Please contact a staff member and save this code: " + token)));
											e.printStackTrace();
										}
										break;
									}
									else if(target.hasPermission("staff.store") || target.hasPermission("famous.store")) {
										p.chat("/lp user " + args[0] + " parent add " + name);
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou added &d" + capitalizedName + " &frank to &d" + target.getName() + "&f!")));
										target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d&l"  + capitalizedName + " &frank has been added to your current one!")));
										break;
									}
									else {
										p.chat("/lp user " + args[0] + " parent set " + name);
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou gave &d" + target.getName() + " &fthe rank &d&l" + capitalizedName)));
										target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
										break;
									}
								default:
									if(target.hasPermission(name + ".store")) {
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + target.getName() + " &falready has a higher (or equal) rank than this!")));
										target.sendMessage(alreadyHasRank);
										String token = getRandomToken(16);
										try {
											PreparedStatement ps = Main.getConnection().prepareStatement("INSERT INTO VoucherCodes (name, uuid, rank, voucherCode, boughtOn, claimed) "
													+ "VALUES ('" + target.getName() + "', '" + target.getUniqueId().toString() + "', '" + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + "', '" + token + "', " + System.currentTimeMillis() + ", 0);");
											ps.execute();
										} catch (SQLException e) {
											p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured while trying to create a new voucher code! Please contact a staff member and save this code: " + token)));
											e.printStackTrace();
										}
										break;
									}
									else {
										p.chat("/lp user " + args[0] + " parent set " + name);
										p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&l" + capitalizedName)));
										target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
										break;
									}
								}
									
							}
							else {
								p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou cannot give a rank to an offline player!")));
							}
						}
						else {
							sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/shop" + name + " <player>")));
						}
					}
					else {
						sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/shop" + name + " <player>")));
					}
				}
				else {
					sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot execute this command.")));
				}
			}
			else {
				if(!args[0].isEmpty()) {
					if(ProxyServer.getInstance().getPlayer(args[0]) != null) {
						ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
						switch(name) {
						case "colony":
							if(target.hasPermission("colony.store")) {
								System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + target.getName() + " &falready has a higher (or equal) rank than this!"));
								target.sendMessage(alreadyHasRank);
								break;
							}
							else if(target.hasPermission("staff.store") || target.hasPermission("famous.store")) {
								ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + args[0] + " parent add " + name);
								System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou added &d" + capitalizedName + " &frank to &d" + target.getName() + "&f!"));
								target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &d&l"  + capitalizedName + " &frank has been added to your current one!")));
								break;
							}
							else {
								ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + args[0] + " parent set " + name);
								System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou gave &d" + target.getName() + " &fthe rank &d&l" + capitalizedName));
								target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
								break;
							}
						default:
							if(target.hasPermission(name + ".store")) {
								System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + target.getName() + " &falready has a higher (or equal) rank than this!"));
								target.sendMessage(alreadyHasRank);
								break;
							}
							else {
								ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "lpb user " + args[0] + " parent set " + name);
								System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou set &d" + target.getName() + "&f's rank to &d&l" + capitalizedName));
								target.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYour rank is now &d&l" + capitalizedName)));
								break;
							}
						}
							
					}
					else {
						System.out.println(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou cannot give a rank to an offline player!"));
					}
				}
			}
		}
		else {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured trying to connect to the database. Please contact an administrator.")));
			String st = "";
			for(String s : args) {
				st = st + " " + s;
			}
			Main.writeToLog("Couldn't execute command: shop" + name + st);
		}
	}
	
	private static String getRandomToken(int n) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVYXZ1234567890";
		String token = "";
		for(int i = 0; i < n; i++) {
			Random rand = new Random();
			int index = rand.nextInt(35);
			token = token + characters.substring(index, index + 1);
		}
		String tokenWithDashes = token.substring(0,4) + "-" + token.substring(4, 8) + "-" + token.substring(8, 12) + "-" + token.substring(12, 16);
		try {
			PreparedStatement ps = Main.getConnection().prepareStatement("SELECT voucherCode FROM VoucherCodes");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getString("voucherCode").equals(tokenWithDashes)) {
					for(int i = 0; i < n; i++) {
						Random rand = new Random();
						int index = rand.nextInt(35);
						token = token + characters.substring(index, index + 1);
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
