package com.github.diereicheerethons.autoprotectregion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
	
	protected String command;
	protected String permission;
	protected String senderType;
	protected String pluginShortName;
	
	
	public abstract void setUp();
	
	public abstract void setArguments();
	
	protected PluginCommand(){
		setUp();
		setArguments();
		pluginShortName = plugin.getName();
	}
	
	public void setPluginShortName(String shortName){
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
				APRUtil.playerMsg(sender, ChatColor.RED+"["+pluginShortName+"]: No permissions for the command \""+cmd+"\"!");
				return false;
			}
			
			
		} else {
			if(!senderType.toLowerCase().contains("console")){
				sender.sendMessage(ChatColor.RED+"["+pluginShortName+"]: Command \""+cmd+"\" must be executed as Player!");
				return false;
			}
		}
		if(!this.command.equalsIgnoreCase(cmd)){
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
					currentArg = "";
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
		
		return helpText +ChatColor.YELLOW+ "- " + getCommandHelp();
	}
	
	public abstract String getCommandHelp();
	
	public static void setPermissionsHandler(Permission permHandler) {
		permissionsHandler = permHandler;
	}
	public static void setPlugin(JavaPlugin plugin){
		PluginCommand.plugin = plugin;
	}

	public static String getPluginCommand() {
		return pluginCommand;
	}

	public static void setPluginCommand(String pluginCommand) {
		PluginCommand.pluginCommand = pluginCommand;
	}
	
}
