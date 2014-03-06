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
		set("cmdHelp.ShowProtection", "Shows the protection for a region", String.class);
		set("doItAsPlayer", "This should be done as Player!", String.class);
		set("noWorldGuard", "WorldGuard is missing", String.class);
		set("newRegion", "Created new Region", String.class);
		set("noWorldAndPlayerName", "Please define a player name and a world name.", String.class);
		set("noPermissionsDeleteOthers", "No permissions for the command \"delete\" on other players!", String.class);
		set("noWorldName", "Please define a world name.", String.class);
		set("blockNotInRegionPart1", "This Block hasn't been added to your Region: ", String.class);
		set("blockNotInRegionPart2", ". Are you too far away? Use /apr build cancel to stop Building your Region.", String.class);
		set("notInSameWorld", "You stopped building in your Region, due to changing the world.", String.class);
		set("defineOrSelectRegion", "Please select a region, or enter the regions Name.", String.class);
		set("playerNotFound", "No player found for this name.", String.class);
		set("regionNotFound", "No region found.", String.class);
		set("cantBuildInThisRegion", "You cant protect anything in this region.", String.class);
		set("canOnlyBuildInSomeRegions", "You can only protect in some regions. Not here", String.class);
		set("maxRegionsReached", "You have reached the limit of regions.", String.class);

		set("PluginCommand.mustBeExecutedFromConsole", "Command must be executed from the Console!", String.class);
		set("PluginCommand.noPermissionsForCMD", "No permissions for this command!", String.class);
		set("PluginCommand.mustBeExecutedAsPlayer", "Command must be executed as Player!", String.class);
		set("PluginCommand.failureInPlugin", "Failure in Plugin! Please report this to an Admin!", String.class);
		set("PluginCommand.notAllrequiredArguments", "Please provide all required arguments", String.class);
	}
}
