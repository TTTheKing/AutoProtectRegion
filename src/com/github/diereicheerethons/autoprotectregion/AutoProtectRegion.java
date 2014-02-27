package com.github.diereicheerethons.autoprotectregion;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.apr.Metrics;

import net.milkbowl.vault.permission.Permission;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.cmd.CommandHandler;
import com.github.diereicheerethons.autoprotectregion.defaultlanguages.English;
import com.github.diereicheerethons.autoprotectregion.defaultlanguages.German;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AutoProtectRegion extends JavaPlugin{
	
	public static AutoProtectRegion instance;
	public static WorldGuardPlugin worldGuard;
	public static Permission permission;
	
	public static Translator language;
	
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
		
		File translationFolder = new File(this.getDataFolder(), "locale");
		if(!translationFolder.exists())
			translationFolder.mkdir();
		initializeDefaultLanguages();
		language = new Translator("locale/"+config.getString("language")+".yml",this);
		
		if(worldGuard == null){
			Bukkit.getConsoleSender().sendMessage("Disabling [APR]... "+Translator.translate("noWorldGuard")+" :(");
			Bukkit.getPluginManager().disablePlugin(instance);
		}
		
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		
		this.getCommand("autoprotectregion").setExecutor(new CommandHandler());
		
		loadData();
		initializeSavers();
		
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
	}
	
	private void initializeDefaultLanguages() {
		new German();
		new English();
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
		language.load("locale/"+APR.config.getString("language")+".yml");
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
