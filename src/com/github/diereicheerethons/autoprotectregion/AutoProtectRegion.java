package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AutoProtectRegion extends JavaPlugin{
	
	public static AutoProtectRegion instance;
	public static WorldGuardPlugin worldGuard;
	
	@Override
	public void onLoad(){
		instance = this;
	}
	
	@Override
	public void onEnable(){
		worldGuard = WGBukkit.getPlugin();
		if(worldGuard == null){
			Bukkit.getConsoleSender().sendMessage("Disabling [APR]... WorldGuard is missing :(");
			Bukkit.getPluginManager().disablePlugin(instance);
		}
		
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		
		
	}
	
}
