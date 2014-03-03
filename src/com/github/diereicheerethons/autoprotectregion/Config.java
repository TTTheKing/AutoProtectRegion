package com.github.diereicheerethons.autoprotectregion;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class Config extends PluginConfig{

	public Config(JavaPlugin plugin) {
		super("config.yml", plugin);
	}

	@Override
	protected void setupDefault() {
		set("pluginShortName", new String("APR"), String.class);
		set("maxXWidth", new Long(20), Long.class);
		set("maxZWidth", new Long(20), Long.class);
		set("maxYWidth", new Long(15), Long.class);
		set("defaultRegionSettings.priority", new Integer(13), Integer.class);
		set("language", new String("en"), String.class);
		set("calculationVersion", new Integer(1), Integer.class);
		ArrayList<String> wgRegionBlacklist = new ArrayList<String>();
		wgRegionBlacklist.add("teeeessssst");
		wgRegionBlacklist.add("teeeeeesssst2");
		set("wgRegionBlacklist", wgRegionBlacklist, List.class);
		ArrayList<String> wgRegionWhitelist = new ArrayList<String>();
		set("wgRegionWhitelist", wgRegionWhitelist, List.class);
	}
}
