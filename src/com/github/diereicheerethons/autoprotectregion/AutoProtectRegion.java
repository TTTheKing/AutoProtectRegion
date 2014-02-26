package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.cmd.CommandHandler;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AutoProtectRegion extends JavaPlugin{
	
	public static AutoProtectRegion instance;
	public static WorldGuardPlugin worldGuard;
	public static Permission permission;
	
	public static Config config;
	
	@Override
	public void onLoad(){
		instance = this;
	}
	
	@Override
	public void onEnable(){
		setupPermissions();
		permission = permissionProvider;
		worldGuard = WGBukkit.getPlugin();
		config = new Config(this);
		
		
		if(worldGuard == null){
			Bukkit.getConsoleSender().sendMessage("Disabling [APR]... WorldGuard is missing :(");
			Bukkit.getPluginManager().disablePlugin(instance);
		}
		
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		
		this.getCommand("autoprotectregion").setExecutor(new CommandHandler());
		
		loadData();
		initializeSavers();
	}
	
	@Override
	public void onDisable(){
		saveAll();
	}

	private void initializeSavers() {
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				saveData();
			}
		}, 0L, 18000L);
	}
	
	public void saveData(){
		APRRegionList.save();
		APRPlayerList.savePlayers();
	}
	
	public void loadData(){
		APRRegionList.load();
		APRPlayerList.loadPlayers();
	}
	
	public void saveAll(){
		config.save();
		saveData();
	}
	
	public void loadAll(){
		config.load();
		loadData();
	}
	
	// Permissions
	private Permission permissionProvider = null;

	private Boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			this.permissionProvider = permissionProvider.getProvider();
		}
		return (this.permissionProvider != null);
	}

	public Permission getPermissionHandler() {
		return permissionProvider;
	}
	
}
