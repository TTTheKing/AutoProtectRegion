package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.Translator;
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
		
		World world = ((Player) sender).getWorld();
		String playerName = ((Player) sender).getName();
		
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		regionManager.removeRegion("apr_"+playerName+"_"+requiredArgs.get("region-name"));
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
		
		String worldName;
		if(unreqArgs.containsKey("worldName"))
			worldName = unreqArgs.get("worldName");
		else
			worldName = ((Player) sender).getWorld().getName();
		String playerName = unreqArgs.get("otherPlayer");
		
		RegionManager regionManager = WGBukkit.getRegionManager(APR.instance.getServer().getWorld(worldName));
		
		regionManager.removeRegion("apr_"+playerName+"_"+requiredArgs.get("region-name"));
		
		return true;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Delete");
	}

}
