package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class Translator extends PluginConfig {

	public static Translator translator;
	
	public Translator(String languageFileName,JavaPlugin plugin) {
		super(languageFileName, false, plugin);
		
		translator = this;
	}

	
	
	public static String translate(String key){
		return translator.getString(key);
	}
	
	@Override
	protected void setupDefault() {
		set("stoppedEditing", "You stoped editing your region. Did you change the world?", String.class);
		set("failedSaving", "Failed to save Regions!", String.class);
		set("cmdHelp.BuildCancel", "Cancel building in a Region", String.class);
		set("cmdHelp.Build", "start Building", String.class);
		set("cmdHelp.Debug", "Sends debug info", String.class);
		set("cmdHelp.Delete", "delete region", String.class);
		set("cmdHelp.Reload", "Reloads the Data and the Config. Warning you will loose unsaved data. Use /apr save to prevent this!", String.class);
		set("cmdHelp.Save", "Saves all Data to Disk", String.class);
		set("doItAsPlayer", "This should be done as Player!", String.class);
		set("noWorldGuard", "WorldGuard is missing", String.class);
		set("newRegion", "Created new Region", String.class);
		set("noWorldAndPlayerName", "Please define a player name and a world name.", String.class);
		set("noPermissionsDeleteOthers", "No permissions for the command \"delete\" on other players!", String.class);
		set("noWorldName", "Please define a world name.", String.class);
		set("blockNotInRegionPart1", "This Block hasn't been added to your Region: ", String.class);
		set("blockNotInRegionPart1", ". Are you too far away? Use /apr build cancel to stop Building your Region.", String.class);
		set("notInSameWorld", "You stopped building in your Region, due to changing the world.", String.class);
	
	}

}
