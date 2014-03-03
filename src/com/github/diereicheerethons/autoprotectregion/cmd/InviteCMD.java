package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class InviteCMD extends PluginCommand {
	
	@Override
	public void setUp() {
		super.command    = "invite";
		super.permission = "apr.invite";
		super.senderType = "player";
	}
	
	@Override
	public void setArguments() {
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		
		new PluginCommandArgument("player-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		
		OfflinePlayer otherPlayer = APR.instance.getServer().getOfflinePlayer(requiredArgs.get("player-name"));
		Player player = (Player) sender;
		
		String regionName = "apr_"+player.getName()+"_"+requiredArgs.get("region-name");
		RegionManager regionManager = WGBukkit.getRegionManager(player.getWorld());
		ProtectedRegion region = regionManager.getRegionExact(regionName);
		APRRegion aprRegion = APRRegionList.get(regionName);
		
		if(aprRegion == null || region == null){
			sender.sendMessage(ChatColor.RED+Translator.translate("regionNotFound"));
			return false;
		}
		DefaultDomain domain = region.getMembers();
		
		aprRegion.getInvites().add(otherPlayer);
		domain.addPlayer(otherPlayer.getName());
		region.setMembers(domain);
		
		return true;
	}
	
	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Invite");
	}
}
