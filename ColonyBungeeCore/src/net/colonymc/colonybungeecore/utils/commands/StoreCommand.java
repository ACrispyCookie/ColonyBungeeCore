package net.colonymc.colonybungeecore.utils.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class StoreCommand extends Command{

	public StoreCommand() {
		super("store", "", new String[] {"buy","donate"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent store = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou can check our store @ &dhttps://store.colonymc.net"));
		store.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://store.colonymc.net"));
		sender.sendMessage(store);
	}

}
