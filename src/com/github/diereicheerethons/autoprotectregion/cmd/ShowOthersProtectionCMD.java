package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;

public class ShowOthersProtectionCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "showprotection.other";
		super.permission = "apr.showprotection.other";
		super.senderType = "player";
		super.aliases.add("show.other");
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("player-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		
		new PluginCommandArgument("off", this){
			public void setUpProperties() {
				return;
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {

		if(!requiredArgs.containsKey("player-name")){
			sender.sendMessage(this.getPluginCommandHelp());
			return false;
		}
		
		if(!requiredArgs.containsKey("region-name")){
			sender.sendMessage(this.getPluginCommandHelp());
			return false;
		}
		
		OfflinePlayer otherPlayer = APR.instance.getServer().
				getOfflinePlayer(requiredArgs.get("player-name"));
		
		if(otherPlayer == null){
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("playerNotFound"));
			return false;
		}
		
		Player player = (Player) sender;
		String regionName = "apr_"+otherPlayer.getName()+"_"+requiredArgs.get("region-name");
		
		if(APRRegionList.get(regionName) == null){
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("regionNotFound"));
		}
		
		boolean off = unreqArgs.containsKey("off");
		ShowProtectionCMD.toggleShowProtection(off, player, regionName);
		return false;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.ShowProtection");
	}

}
