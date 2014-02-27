package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;

public class CommandHandler implements CommandExecutor {
	
	private HashMap<String, PluginCommand> cmds = new HashMap<String, PluginCommand>();
	
	public CommandHandler(){
		PluginCommand.setPermissionsHandler(AutoProtectRegion.permission);
		PluginCommand.setPlugin(AutoProtectRegion.instance);
		PluginCommand.setPluginCommand("autoprotectregion");
		PluginCommand.setPluginShortName("APR");
		
		cmds.put("build.cancel", new BuildCancelCMD());
		cmds.put("build", new BuildCMD());
		cmds.put("reload", new ReloadCMD());
		cmds.put("debug", new DebugCMD());
		cmds.put("save", new SaveCMD());
		cmds.put("delete", new DeleteCMD());
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		String keyMatch = "";
		int rating = 0;
		
		for(String key: cmds.keySet()){
			String[] argsForName = key.split("[.]");
			
			if(args.length >= argsForName.length){
				boolean success = true;
				for(int i=0; i<argsForName.length; i++){
					if(!args[i].equalsIgnoreCase(argsForName[i]))
						success = false;
				}
				if(success){
					if(rating < argsForName.length){
						keyMatch = key;
						rating = argsForName.length;
					}
				}
			}
		}
		if(!keyMatch.equalsIgnoreCase(""))
			return cmds.get(keyMatch).onPluginCommand(sender, command, keyMatch, Arrays.copyOfRange(args, rating, args.length));
		
		
		sender.sendMessage(getHelp(sender instanceof Player, sender));
		return false;
	}
	
	private String getHelp(boolean isPlayer, CommandSender sender){
		String helpText = ChatColor.AQUA+"=======================[APR]=========================";
		
		for(String key:cmds.keySet()){
			PluginCommand cmd = cmds.get(key);
			if(isPlayer)
				if(!AutoProtectRegion.permission.playerHas((Player) sender, cmd.getPermission()))
					continue;
			helpText += "\n" + cmd.getPluginCommandHelp();
		}
		helpText +=       ChatColor.AQUA+"\n====================[By Tobiii]======================";
		return helpText;
	}

}
