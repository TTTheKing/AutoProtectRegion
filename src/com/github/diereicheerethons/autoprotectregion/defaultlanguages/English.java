package com.github.diereicheerethons.autoprotectregion.defaultlanguages;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class English extends PluginConfig{
	
	public static String fileName = "locale/en.yml";
	
	public English() {
		super(fileName, false, APR.instance);
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
	}
}
