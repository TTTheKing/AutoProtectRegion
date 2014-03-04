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
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;

public class BuildOtherCMD extends PluginCommand {

	private String extraPermission="apr.build.all";
	
	@Override
	public void setUp() {
		super.command    = "build.other";
		super.permission = "apr.build";
		super.senderType = "player";
		super.aliases.add("create.other");
		super.aliases.add("edit.other");
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("owner-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		
		OfflinePlayer oPlayer = APR.instance.getServer().getOfflinePlayer(requiredArgs.get("owner-name"));
		if(oPlayer == null){
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("playerNotFound"));
			return false;
		}
		String regionName = "apr_"+oPlayer.getName()+"_"+requiredArgs.get("region-name");
		
		APRRegion aprRegion = APRRegionList.get(regionName);
		if(aprRegion == null){
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("regionNotFound"));
			return false;
		}
		Player player = (Player) sender;
		if(aprRegion.getInvites().contains((OfflinePlayer) player) ||
				APR.permission.playerHas(player, extraPermission)  ||
				player.isOp()){

			APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
			aprPlayer.setCurrentRegion(aprRegion);
			aprPlayer.setEditingRegion(true);
		}
		
		
		return true;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Build");
	}

}
