package com.github.diereicheerethons.autoprotectregion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginCommand {
	
	private static JavaPlugin plugin;
	private static Permission permissionsHandler;
	private static String pluginCommand;
	private static String pluginShortName;
	
	public static HashMap<String, PluginCommand> aliasCMDMap = new HashMap<String, PluginCommand>();
	
	protected String command;
	protected String permission;
	protected String senderType;
	protected List<String> aliases = new ArrayList<String>();
	
	public List<String> getAliases(){
		return aliases;
	}
	
	public abstract void setUp();
	
	public abstract void setArguments();
	
	protected PluginCommand(){
		setUp();
		aliasCMDMap.put(command, this);
		for(String alias:aliases){
			aliasCMDMap.put(alias, this);
		}
		setArguments();
	}
	
	public static void setPluginShortName(String shortName){
		pluginShortName = shortName;
	}
	
	public boolean onPluginCommand(CommandSender sender, Command command, String cmd, String[] args){
		if (sender instanceof Player){
			if(!senderType.toLowerCase().contains("player")){
				sender.sendMessage(ChatColor.RED+"["+pluginShortName+"]: Command \""+cmd+"\" must be executed from the Console!");
				return false;
			}
			
			Player player = (Player) sender;
			if((!permissionsHandler.playerHas(player, permission)) || (!player.isOp())){
				sender.sendMessage(ChatColor.RED+"["+pluginShortName+"]: No permissions for the command \""+cmd+"\"!");
				return false;
			}
			
			
		} else {
			if(!senderType.toLowerCase().contains("console")){
				sender.sendMessage(ChatColor.RED+"["+pluginShortName+"]: Command \""+cmd+"\" must be executed as Player!");
				return false;
			}
		}
		if(!(this.command.equalsIgnoreCase(cmd) || this.aliases.contains(cmd))){
			sender.sendMessage(ChatColor.RED+"["+pluginShortName+"]: Failure in Plugin! Please report this to an Admin!");
			return false;
		}
		
		
		
		HashMap<String, String> requiredArgs = new HashMap<String, String>();
		HashMap<String, String> unreqArgs = new HashMap<String, String>();
		String[] otherArgs = null;
		sortArgs(args, requiredArgs, unreqArgs, otherArgs);
		
		return onCommand(sender, command, requiredArgs, unreqArgs, otherArgs);
	}
	
	private void sortArgs(String[] args, HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		int argsCounter = 0;
		ArrayList<PluginCommandArgument> cmdArgs = PluginCommandArgument.getPluginCmdArgs(this);
		if(cmdArgs != null){
			for(PluginCommandArgument arg: cmdArgs){
				String currentArg;
				if(args.length >= (argsCounter+1))
					currentArg = args[argsCounter];
				else
					continue;
				boolean required;
				if(arg.getProperties().get("required")!=null)
					required = (Boolean) arg.getProperties().get("required");
				else
					required = false;
				if(required)
					requiredArgs.put(arg.getArgumentName(), currentArg);
				else
					unreqArgs.put(arg.getArgumentName(), currentArg);
				
				argsCounter++;
			}
		}
		
		if(args.length >= (argsCounter+1))
			otherArgs = Arrays.copyOfRange(args, argsCounter, args.length);
	}

	public abstract boolean onCommand(CommandSender sender, Command command, HashMap<String, String> requiredArgs, HashMap<String, String> unreqArgs, String[] otherArgs);
	
	public String getPluginCommandHelp(){
		String plainCmd = command.replace(".", " ");
		String helpText = ChatColor.GOLD + PluginCommand.pluginCommand +" " + plainCmd +" ";
		ArrayList<PluginCommandArgument> cmdArgs = PluginCommandArgument.getPluginCmdArgs(this);
		if(cmdArgs != null){
			for(PluginCommandArgument arg : cmdArgs){
				boolean required;
				if(arg.getProperties().get("required")!=null)
					required = (Boolean) arg.getProperties().get("required");
				else
					required = false;
				
				if(required)
					helpText += "["+arg.getArgumentName()+"] ";
				else
					helpText += "<"+arg.getArgumentName()+"> ";
			}
		}
		
		String aliasString = "";
		int size = aliases.size();
		if(size>0)
			aliasString = " (";
		int counter = 0;
		for(String alias:aliases){
			counter++;
			aliasString += alias.replace(".", " ");
			
			if(counter < size)
				aliasString += "/";
		}
		if(size>0)
			aliasString += ")";
		return helpText +ChatColor.GREEN + aliasString + ChatColor.YELLOW+ "- " + getCommandHelp();
	}
	
	public String getPermission(){
		return permission;
	}
	
	public String getCommandName(){
		return command;
	}
	
	public String getSenderType(){
		return senderType;
	}
	
	public abstract String getCommandHelp();
	
	public static void setPermissionsHandler(Permission permHandler) {
		permissionsHandler = permHandler;
	}
	public static void setPlugin(JavaPlugin plugin){
		PluginCommand.plugin = plugin;
		PluginCommand.pluginShortName = PluginCommand.plugin.getName();
	}

	public static String getPluginCommand() {
		return PluginCommand.pluginCommand;
	}

	public static void setPluginCommand(String pluginCommand) {
		PluginCommand.pluginCommand = pluginCommand;
	}
	
}
