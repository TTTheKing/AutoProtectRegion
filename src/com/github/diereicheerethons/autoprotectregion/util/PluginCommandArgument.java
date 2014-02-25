package com.github.diereicheerethons.autoprotectregion.util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PluginCommandArgument {
	
	private static HashMap<PluginCommand, ArrayList<PluginCommandArgument>> list = new HashMap<PluginCommand, ArrayList<PluginCommandArgument>>();
	
	public PluginCommandArgument(){}
	
	private String argument;
	protected HashMap<String, Object> properties = new HashMap<String, Object>();
	
	public PluginCommandArgument(String argument, PluginCommand command){
		this.argument = argument;
		this.setUpProperties();
		ArrayList<PluginCommandArgument> localList = list.get(command);
		if(localList == null){
			list.put(command, new ArrayList<PluginCommandArgument>());
			localList = list.get(command);
		}
		localList.add(this);
	}
	
	public abstract void setUpProperties();
	
	public void setProperty(String propertyName, Object value){
		properties.put(propertyName, value);
	}
	
	public String getArgumentName(){
		return argument;
	}
	
	public HashMap<String, Object> getProperties(){
		return properties;
	}
	
	public static PluginCommandArgument getPluginCmdArg(PluginCommand cmd, String argument){
		ArrayList<PluginCommandArgument> argList = list.get(cmd);
		for(PluginCommandArgument arg: argList){
			if(arg.getArgumentName().equalsIgnoreCase(argument)){
				return arg;
			}
		}
		return null;
	}
	
	public static ArrayList<PluginCommandArgument> getPluginCmdArgs(PluginCommand cmd){
		return list.get(cmd);
	}
	
}
