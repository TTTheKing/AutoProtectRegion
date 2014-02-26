package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class Config extends PluginConfig{

	public Config(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void setupDefault() {
		set("pluginShortName", new String("APR"), String.class);
		set("maxXWidth", new Long(20), Long.class);
		set("maxZWidth", new Long(20), Long.class);
	}
}
