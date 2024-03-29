package net.colonymc.colonybungeecore;

import net.colonymc.colonyapi.database.MainDatabase;
import net.colonymc.colonybungeecore.ranksystem.commands.ClaimCommand;
import net.colonymc.colonybungeecore.ranksystem.commands.MyCodesCommand;
import net.colonymc.colonybungeecore.reaction.ReactionCommand;
import net.colonymc.colonybungeecore.reaction.ReactionGame;
import net.colonymc.colonybungeecore.utils.commands.*;
import net.colonymc.colonybungeecore.utils.listeners.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
	
	private static File file, logfile;
	private static Configuration configuration;
	private static Main instance;
	
	public void onEnable() {
		if(MainDatabase.isConnected()) {
			instance = this;
			System.out.println("[ColonyBungeeCore] has been enabled successfully!");
			loadFile();
			loadLogFile();
			loadUtilCommands();
			loadUtilListeners();
			loadRankCommands();
			loadShopRankCommands();
			setupSkyblockReactionsGame();
		}
		else {
			System.out.println("[ColonyBungeeCore] Couldn't connect to the main database!");
		}
	}

	private void setupSkyblockReactionsGame() {
		ProxyServer.getInstance().getScheduler().schedule(this, () -> {
			ReactionGame game = new ReactionGame(ProxyServer.getInstance().getServerInfo("skyblock"), ReactionGame.type.RANDOM);
			game.startGame();
			ReactionCommand.reactions.put(ProxyServer.getInstance().getServerInfo("skyblock"), game);
		}, 0, 10, TimeUnit.MINUTES);
	}

	public void onDisable() {
		System.out.println("[ColonyBungeeCore] has been disabled successfully!");
	}
	
	private void loadLogFile() {
		logfile = new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/log.txt");
		try {
			if(!logfile.exists()){
				logfile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFile() {
		file = new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/config.yml");
		try {
			if(!new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore").exists()) {
				new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore").mkdirs();
				file.createNewFile();
			}
			else if(!file.exists()){
				file.createNewFile();
				configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				configuration.set("maintenance", false);
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
			}
			else {
				configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadUtilListeners(){
		getProxy().getPluginManager().registerListener(this, new RedirectListener());
		getProxy().getPluginManager().registerListener(this, new DonationsListener());
		getProxy().getPluginManager().registerListener(this, new StaffChatListener());
		getProxy().getPluginManager().registerListener(this, new MeowChatEvent());
		getProxy().getPluginManager().registerListener(this, new DisableCommandsListener());
		getProxy().getPluginManager().registerListener(this, new ChatCooldownListener());
		getProxy().getPluginManager().registerListener(this, new MaintenanceListener());
		getProxy().getPluginManager().registerListener(this, new PingListener());
	}
	
	public void loadUtilCommands() {
		getProxy().getPluginManager().registerCommand(this, new ReactionCommand());
		getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand());
		getProxy().getPluginManager().registerCommand(this, new DiscordCommand());
		getProxy().getPluginManager().registerCommand(this, new MyCodesCommand());
		getProxy().getPluginManager().registerCommand(this, new ClaimCommand());
		getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());
		getProxy().getPluginManager().registerCommand(this, new StaffChatToggleCommand());
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		getProxy().getPluginManager().registerCommand(this, new ApplyCommand());
		getProxy().getPluginManager().registerCommand(this, new MediaCommand());
		getProxy().getPluginManager().registerCommand(this, new RulesCommand());
		getProxy().getPluginManager().registerCommand(this, new StoreCommand());
		getProxy().getPluginManager().registerCommand(this, new MeowCommand());
		getProxy().getPluginManager().registerCommand(this, new VoucherCommand());
	}
	
	public void loadRankCommands() {
		getProxy().getPluginManager().registerCommand(this, new RankCommand("knight"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("prince"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("king"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("archon"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("overlord"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("colony"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("builder"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("mediarank"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("helper"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("moderator"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("administrator"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("manager"));
		getProxy().getPluginManager().registerCommand(this, new RankCommand("owner"));
	}
	
	public void loadShopRankCommands() {
		getProxy().getPluginManager().registerCommand(this, new ShoprankCommand("prince"));
		getProxy().getPluginManager().registerCommand(this, new ShoprankCommand("king"));
		getProxy().getPluginManager().registerCommand(this, new ShoprankCommand("archon"));
		getProxy().getPluginManager().registerCommand(this, new ShoprankCommand("overlord"));
		getProxy().getPluginManager().registerCommand(this, new ShoprankCommand("colony"));
	}
	
	public static Configuration getConfig() {
		return configuration;
	}
	
	public static Main getInstance() {
		return instance;
	}

	public static void writeToLog(String s) {
		try {
			FileWriter fw = new FileWriter(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/log.txt", true);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fw.append("[ColonyBungeeCore] ").append(sdf.format(new Date(System.currentTimeMillis()))).append(" : ").append(s).append("\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
		} catch (IOException e) {
			System.out.println("[ColonyBungeeCore] Couldn't save the configuration file!");
			e.printStackTrace();
		}
	}

}
