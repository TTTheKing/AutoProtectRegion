package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;

public class BuildCMD extends PluginCommand {
	
	private String moreRegionsPerms = "apr.bypassMaxRegions";
	
	@Override
	public void setUp() {
		super.command    = "build";
		super.permission = "apr.build";
		super.senderType = "player";
		super.aliases.add("create");
		super.aliases.add("edit");
	}
	
	@Override
	public void setArguments() {
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
		
		Player player = (Player) sender;
		if(!requiredArgs.containsKey("region-name")){
			sender.sendMessage(this.getPluginCommandHelp());
			return false;
		}
		APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
		String regionID = "apr_"+player.getName()+"_"+requiredArgs.get("region-name");
		APRRegion aprRegion = APRRegionList.get(regionID);
		if(aprRegion==null){
			if(APRRegionList.getRegionCountFor(player.getName())<aprPlayer.getMaxRegions()
					|| APR.permission.has(player, moreRegionsPerms)
					|| player.isOp()){
			sender.sendMessage(Translator.translate("newRegion"));
			aprRegion = new APRRegion(player, regionID, player.getWorld(), 
					AutoProtectRegion.config.getLong("maxXWidth"), 
					AutoProtectRegion.config.getLong("maxZWidth"), 
					AutoProtectRegion.config.getLong("maxYWidth"));
			}else{
				player.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("maxRegionsReached"));
			}
				
		}
		aprPlayer.setCurrentRegion(aprRegion);
		aprPlayer.setEditingRegion(true);
		
		return true;
	}
	
	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Build");
	}
}
