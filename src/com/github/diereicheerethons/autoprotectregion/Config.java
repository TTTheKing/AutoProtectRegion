package com.github.diereicheerethons.autoprotectregion;

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
		set("defaultRegionSettings.priority", new Integer(13), Integer.class);
		set("language", new String("en"), String.class);
		set("calculationVersion", new Integer(1), Integer.class);
	}
}
