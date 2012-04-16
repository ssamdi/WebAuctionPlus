package me.exote.webauctionplus;

import me.lorenzop.webauctionplus.WebAuctionPlus;
import me.lorenzop.webauctionplus.dao.AuctionPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WebAuctionCommands implements CommandExecutor {

	private final WebAuctionPlus plugin;

	public WebAuctionCommands(WebAuctionPlus plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		int params = args.length;
		String player = "";
		if (sender instanceof Player) {
			player = ((Player) sender).getName();
		}
		// 0 args
		if (params == 0) {
			return false;
		// 1 arg
		} else if (params == 1) {
			// /wa reload
			if (args[0].equalsIgnoreCase("reload")){
				if (sender instanceof Player) {
					if (!sender.hasPermission("wa.reload")){
						((Player)sender).sendMessage(WebAuctionPlus.chatPrefix + "You do not have permission");
						return false;
					}
					((Player)sender).sendMessage(WebAuctionPlus.chatPrefix + "Reloading..");
				}
				WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + "Reloading..");
				plugin.getServer().getScheduler().cancelTasks(plugin);
				plugin.waCronExecutorTask.clearCronUrls();
				plugin.waAnnouncerTask.clearMessages();
				plugin.shoutSigns.clear();
				plugin.recentSigns.clear();
				plugin.dataQueries.forceCloseConnections();
				plugin.reloadConfig();
				plugin.onLoadConfig();
				WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + "Finished reloading");
				if (sender instanceof Player)
					((Player)sender).sendMessage(WebAuctionPlus.chatPrefix + "Reloaded");
				return true;
			// save config
			} else if (args[0].equalsIgnoreCase("save")){
				if (sender instanceof Player) {
					if (!sender.hasPermission("wa.save")){
						((Player)sender).sendMessage(WebAuctionPlus.chatPrefix + "You do not have permission");
						return false;
					}
				}
				WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + "Saving config..");
				WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + ChatColor.RED + "This feature is incomplete");
				plugin.saveConfig();
				if (sender instanceof Player)
					((Player)sender).sendMessage(WebAuctionPlus.chatPrefix + "Config Saved");
			}
			return false;
		// 2 args
		} else if (params == 2 || params == 3) {
			// /wa password
			if (args[0].equalsIgnoreCase("password") ||
				args[0].equalsIgnoreCase("pass")   ) {
				String pass = "";
				// is player
				if (sender instanceof Player) {
					if (params != 2) return false;
					if (args[1].isEmpty()) return false;
					pass = WebAuctionPlus.MD5(args[1]);
				// is console
				} else {
					if (params != 3) return false;
					if (args[1].isEmpty() || args[2].isEmpty()) return false;
					player = args[1];
					pass = WebAuctionPlus.MD5(args[2]);
				}
				if (player.isEmpty()) return false;
				AuctionPlayer waPlayer = plugin.dataQueries.getPlayer(player);
				// create that person in database
				if (waPlayer == null) {
					waPlayer = new AuctionPlayer(player);
WebAuctionPlus.log.warning("canbuy:  " + sender.hasPermission("wa.canbuy"));
WebAuctionPlus.log.warning("cansell: " + sender.hasPermission("wa.cansell"));
WebAuctionPlus.log.warning("isadmin: " + sender.hasPermission("wa.webadmin"));
					waPlayer.setPerms(
						sender.hasPermission("wa.canbuy"),
						sender.hasPermission("wa.cansell"),
						sender.hasPermission("wa.webadmin")
					);
					WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + "Creating a new account for " + player +
						" with perms: " + waPlayer.getPermsString());
					plugin.dataQueries.createPlayer(waPlayer, pass);
				} else {
					plugin.dataQueries.updatePlayerPassword(player, pass);
				}
				if (sender instanceof Player) {
					sender.sendMessage(WebAuctionPlus.chatPrefix + "Password changed");
				} else {
					WebAuctionPlus.log.info(WebAuctionPlus.logPrefix + "Password changed");
				}
				return true;
			}
		} else if (params == 4) {
			// /wa give <player> <item> <count>
			if (args[0].equals("give")) {
// /wa give lorenzop diamond 3
			}
		}
		return false;
	}

}