package com.github.diereicheerethons.autoprotectregion;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class Config extends PluginConfig{

	public Config(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	protected void setupDefault(HashMap<String, Object> configEntries) {
		configEntries.put("pluginName", "APR");
		
	}

}
