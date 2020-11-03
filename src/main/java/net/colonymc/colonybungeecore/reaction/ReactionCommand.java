package net.colonymc.colonybungeecore.reaction;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReactionCommand extends Command {

	public ReactionCommand() {
		super("reaction");
	}

	final TextComponent usage = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/reaction math/word/unscramble/random [server]"));
	public static final HashMap<ServerInfo, ReactionGame> reactions = new HashMap<>();
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			if(sender.hasPermission("*")) {
				if(args.length >= 1) {
					ServerInfo server = ((ProxiedPlayer) sender).getServer().getInfo();
					if(args.length == 2) {
						server = ProxyServer.getInstance().getServerInfo(args[1]);
					}
					switch(args[0]) {
					case "math":
						startReactionGame(server, ReactionGame.type.MATH);
						break;
					case "word":
						startReactionGame(server, ReactionGame.type.WORD);
						break;
					case "unscramble":
						startReactionGame(server, ReactionGame.type.UNSCRAMBLE);
						break;
					case "random":
						startReactionGame(server, ReactionGame.type.RANDOM);
						break;
					default:
						sender.sendMessage(usage);
						break;
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
		}
	}
	
	public void startReactionGame(ServerInfo server, ReactionGame.type type) {
		ReactionGame game = new ReactionGame(server, type);
		game.startGame();
		reactions.put(server, game);
	}

}
