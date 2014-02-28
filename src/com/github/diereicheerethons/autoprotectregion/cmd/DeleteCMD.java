package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class DeleteCMD extends PluginCommand {

	private String extraPermission = "apr.delete.others";
	
	@Override
	public void setUp() {
		super.command    = "delete";
		super.permission = "apr.delete";
		super.senderType = "player and console";
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		new PluginCommandArgument("otherPlayer", this){
			public void setUpProperties() {
				return;
			}
		};
		new PluginCommandArgument("worldName", this){
			public void setUpProperties() {
				return;
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {

		if(!requiredArgs.containsKey("region-name")){
			sender.sendMessage(this.getPluginCommandHelp());
			return false;
		}
		
		if(unreqArgs.containsKey("otherPlayer"))
			return deleteFromOther(sender, command, requiredArgs,unreqArgs, otherArgs);
		else
			return delete(sender, command, requiredArgs,unreqArgs, otherArgs);
	}

	private boolean delete(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {

		if(!(sender instanceof Player)){
			sender.sendMessage(Translator.translate("noWorldAndPlayerName"));
			sender.sendMessage(this.getPluginCommandHelp());
			return false;
		}
		Player player = (Player) sender;
		
		World world = player.getWorld();
		String playerName = player.getName();
		
		remove(requiredArgs, player, playerName, world);
		
		return true;
	}

	private boolean deleteFromOther(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		
		if(!(sender instanceof Player)){
			if(!unreqArgs.containsKey("worldName")){
				sender.sendMessage(Translator.translate("noWorldName"));
				sender.sendMessage(this.getPluginCommandHelp());
				return false;
			}
		}else{
			if(!APR.permission.playerHas((Player) sender, extraPermission)){
				sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("noPermissionsDeleteOthers"));
			}
		}
		Player player = (Player) sender;
		String worldName;
		if(unreqArgs.containsKey("worldName"))
			worldName = unreqArgs.get("worldName");
		else
			worldName = player.getWorld().getName();
		String playerName = unreqArgs.get("otherPlayer");
		World world = APR.instance.getServer().getWorld(worldName);
		
		remove(requiredArgs, player, playerName, world);
		
		return true;
	}

	private void remove(HashMap<String, String> requiredArgs, Player player,
			String playerName, World world) {
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		String regionName = "apr_"+playerName+"_"+requiredArgs.get("region-name");
		regionManager.removeRegion(regionName);
		APRRegionList.remove(regionName);
		APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
		aprPlayer.setCurrentRegion(null);
		aprPlayer.setEditingRegion(false);
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Delete");
	}

}
