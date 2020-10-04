package net.colonymc.colonybungeecore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.colonymc.colonyapi.MainDatabase;
import net.colonymc.colonybungeecore.ranksystem.commands.ClaimCommand;
import net.colonymc.colonybungeecore.ranksystem.commands.MyCodesCommand;
import net.colonymc.colonybungeecore.reaction.ReactionCommand;
import net.colonymc.colonybungeecore.reaction.ReactionGame;
import net.colonymc.colonybungeecore.utils.commands.ApplyCommand;
import net.colonymc.colonybungeecore.utils.commands.DiscordCommand;
import net.colonymc.colonybungeecore.utils.commands.HubCommand;
import net.colonymc.colonybungeecore.utils.commands.MaintenanceCommand;
import net.colonymc.colonybungeecore.utils.commands.MediaCommand;
import net.colonymc.colonybungeecore.utils.commands.MeowCommand;
import net.colonymc.colonybungeecore.utils.commands.RankCommand;
import net.colonymc.colonybungeecore.utils.commands.RulesCommand;
import net.colonymc.colonybungeecore.utils.commands.ShoprankCommand;
import net.colonymc.colonybungeecore.utils.commands.StaffChatCommand;
import net.colonymc.colonybungeecore.utils.commands.StaffChatToggleCommand;
import net.colonymc.colonybungeecore.utils.commands.StoreCommand;
import net.colonymc.colonybungeecore.utils.commands.VoucherCommand;
import net.colonymc.colonybungeecore.utils.listeners.ChatCooldownListener;
import net.colonymc.colonybungeecore.utils.listeners.DisableCommandsListener;
import net.colonymc.colonybungeecore.utils.listeners.DonationsListener;
import net.colonymc.colonybungeecore.utils.listeners.MaintenanceListener;
import net.colonymc.colonybungeecore.utils.listeners.MeowChatEvent;
import net.colonymc.colonybungeecore.utils.listeners.PingListener;
import net.colonymc.colonybungeecore.utils.listeners.RedirectListener;
import net.colonymc.colonybungeecore.utils.listeners.StaffChatListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	
	private static File file, logfile;
	private static Configuration configuration;
	private static Connection codeconnection;
	private static Main instance;
	private String host, database, username, password;
	private int port;
	private String url;
	private boolean connected = false;
	private ScheduledTask checkForDatabase;
	
	public void onEnable() {
		instance = this;
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					MainDatabase.isConnected();
					if(!MainDatabase.isConnecting()) {
						checkForDatabase.cancel();
						connected = MainDatabase.isConnected();
						if(connected) {
							System.out.println("[ColonyBungeeCore] has been enabled successfully!");
							file = new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/config.yml");
							if(!file.exists()) {
								createNewFile();
							}
							setupDatabases();
							if(connected) {
								openConnection();
								setupTablesOnDatabase(codeconnection);
							}
							setupLogFile();
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
				} catch(NoSuchMethodError e) {
					
				}
			}
		};
		checkForDatabase = ProxyServer.getInstance().getScheduler().schedule(this, run, 0, 2, TimeUnit.SECONDS);
	}

	private void setupSkyblockReactionsGame() {
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {
				ReactionGame game = new ReactionGame(ProxyServer.getInstance().getServerInfo("skyblock"), ReactionGame.type.RANDOM);
				game.startGame();
				ReactionCommand.reactions.put(ProxyServer.getInstance().getServerInfo("skyblock"), game);
			}
		}, 0, 10, TimeUnit.MINUTES);
		
	}

	public void onDisable() {
		System.out.println("[ColonyBungeeCore] has been disabled successfully!");
	}
	
	private void setupLogFile() {
		logfile = new File(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/log.txt");
		if(!logfile.exists()) {
			createNewLogFile();
		}
	}
	
	private void createNewLogFile() {
		try {
			logfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createNewFile() {
		try {
			file.createNewFile();
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			configuration.set("mysql.codes.host", "192.168.1.1");
			configuration.set("mysql.codes.port", "3306");
			configuration.set("mysql.codes.database", "Database");
			configuration.set("mysql.codes.username", "Username");
			configuration.set("mysql.codes.password", "Password");
			configuration.set("maintenance", false);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setupDatabases() {
		try {
			//setup for reports' database
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			host = configuration.getString("mysql.codes.host");
			port = Integer.parseInt(configuration.getString("mysql.codes.port"));
			database = configuration.getString("mysql.codes.database");
			username = configuration.getString("mysql.codes.username");
			password = configuration.getString("mysql.codes.password");
			url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void openConnection(){
		try {
			try {
				if (codeconnection != null && !codeconnection.isClosed()) {
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				codeconnection = DriverManager.getConnection(url,username,password);
			    System.out.println("[ColonyBungeeCore] Successfully connected to the database!");
			    connected = true;
			    keepConnectionAlive();
			} catch (SQLException e) {
				e.printStackTrace();
			    System.out.println("[ColonyBungeeCore] Couldn't connect to the database!");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void keepConnectionAlive() {
		try {
			PreparedStatement ps = codeconnection.prepareStatement("SELECT 1");
			Runnable keepAlive = new Runnable() {
				@Override
				public void run() {
					try {
						ps.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			};
			ProxyServer.getInstance().getScheduler().schedule(this, keepAlive, 0, 5, TimeUnit.MINUTES);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setupTablesOnDatabase(Connection conn) {
	    DatabaseMetaData meta;
		try {
			ArrayList<String> tables = new ArrayList<String>();
			if(conn.equals(codeconnection)) {
				tables.add("VoucherCodes");
			}
			for(String s : tables) {
				meta = codeconnection.getMetaData();
			    ResultSet result = meta.getTables(null, null, s, new String[] {"TABLE"});
			    if(!result.next()) {
			    	switch(s) {
			    	case "VoucherCodes":
				    	PreparedStatement activebans = conn.prepareStatement("CREATE TABLE VoucherCodes (name varchar(255), uuid varchar(255), rank varchar(255), voucherCode varchar(255) UNIQUE, boughtOn bigint(255), claimed varchar(255));");
				    	activebans.execute();
				    	break;
			    	}
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		    System.out.println("[ColonyBungeeCore] Couldn't setup the tables on the databases!");
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
		getProxy().getPluginManager().registerCommand(this, new RankCommand("admin"));
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
	
	public static Connection getConnection() {
		return codeconnection;
	}
	
	public static Configuration getConfig() {
		return configuration;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public boolean isConnected() {
		return connected;
	}

	public static void writeToLog(String s) {
		try {
			FileWriter fw = new FileWriter(ProxyServer.getInstance().getPluginsFolder() + "/ColonyBungeeCore/log.txt", true);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fw.append("[ColonyBungeeCore] " + sdf.format(new Date(System.currentTimeMillis())) + " : " + s + "\n");
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
