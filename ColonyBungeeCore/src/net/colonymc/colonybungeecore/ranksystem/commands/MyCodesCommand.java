package net.colonymc.colonybungeecore.ranksystem.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.colonymc.colonybungeecore.Main;
import net.colonymc.colonybungeecore.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MyCodesCommand extends Command {

	public MyCodesCommand() {
		super("mycodes");
	}
	
	SimpleDateFormat sdm = new SimpleDateFormat("dd/MM/yy");
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(Main.getInstance().isConnected()) {
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) sender;
				try {
					TextComponent ranks = new TextComponent("");
					PreparedStatement ps = Main.getConnection().prepareStatement("SELECT * FROM VoucherCodes WHERE uuid='" + p.getUniqueId().toString() + "';");
					ResultSet rs = ps.executeQuery();
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
					if(!ranks.toPlainText().isEmpty()) {
						TextComponent finaltext = new TextComponent(ChatColor.translateAlternateColorCodes
								('&', ChatColor.translateAlternateColorCodes('&', "&f&m--------------------&r &5&lYour Voucher Codes &f&m--------------------")));
						finaltext.addExtra(ranks);
						finaltext.addExtra(ChatColor.translateAlternateColorCodes('&', "&f&m------------------------------------------------------------"));
						p.sendMessage(finaltext);
					}
					else {
						p.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou don't have any rank voucher codes :(")));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					Main.writeToLog("Failed to retrieve the rank voucher list of " + p.getName() + "! Executed by " + p.getName() + ".");
					p.sendMessage(new TextComponent(Messages.errorOccured));
				}
			}
			else {
				sender.sendMessage(new TextComponent(Messages.onlyPlayers));
			}
		}
		else {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &cAn error occured trying to connect to the database. Please contact an administrator.")));
		}
	}

}
