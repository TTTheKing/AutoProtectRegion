package com.github.diereicheerethons.autoprotectregion.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

@SuppressWarnings("unchecked")
public abstract class PluginConfig {
	
	protected long saveTimes;
	protected JavaPlugin plugin;
	protected File configFile;
	protected HashMap<String, Object> configEntries = new HashMap<String, Object>();
	
	public PluginConfig(JavaPlugin plugin){
		this(18000L, plugin); // Default all 15 Mins
	} 
	
	
	public PluginConfig(long savetime, JavaPlugin plugin){
		
		configFile = new File(plugin.getDataFolder(), "config.yml");
		
		this.saveTimes = savetime;
		this.plugin=plugin;
		
		if(!configExists())
			setupDefaults();
		else
			load();
		
		initializeSaver();
	}
	
	protected abstract void setupDefault(HashMap<String, Object> configEntries);
	
	private void setupDefaults() {
		setupDefault(configEntries);
		save();
	}

	protected boolean configExists(){
		return configFile.exists();
	}
	
	protected void initializeSaver() {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				save();
			}
		}, 0L, saveTimes);
	}

	public void save(){
		FileConfiguration ymlFile;
		if(configExists())
			ymlFile = YamlConfiguration.loadConfiguration(configFile);
		else
			ymlFile = new YamlConfiguration();
		
		for(String key: configEntries.keySet()){
			Object value = configEntries.get(key);
			ymlFile.set(key, value);
		}
		try {
			ymlFile.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean load() {
		if(!configExists())
			return false;
		FileConfiguration ymlFile = YamlConfiguration.loadConfiguration(configFile);
		
		for(String key: configEntries.keySet()){
			configEntries.put(key, ymlFile.get(key));
		}
		
		return true;
	}
	
	public void set(String key, Object value){
		configEntries.put(key, value);
	}
	
	/*-*****************************
	 * **********GETTERS************
	 *******************************/
	
	public Object get(String key){
		return configEntries.get(key);
	}
	
	public Boolean getBoolean(String key){
		return (Boolean) configEntries.get(key);
	}
	
	public List<Boolean> getBooleanList(String key){
		return (List<Boolean>) configEntries.get(key);
	}
	
	public List<Byte> getByteList(String key){
		return (List<Byte>) configEntries.get(key);
	}
	
	public List<Character> getCharacterList(String key){
		return (List<Character>) configEntries.get(key);
	}
	
	public Color getColor(String key){
		return (Color) configEntries.get(key);
	}
	
	public Double getDouble(String key){
		return (Double) configEntries.get(key);
	}
	
	public List<Double> getDoubleList(String key){
		return (List<Double>) configEntries.get(key);
	}
	
	public List<Float> getFloatList(String key){
		return (List<Float>) configEntries.get(key);
	}
	
	public Integer getInt(String key){
		return (Integer) configEntries.get(key);
	}
	
	public List<Integer> getIntegerList(String key){
		return (List<Integer>) configEntries.get(key);
	}
	
	public ItemStack getItemStack(String key){
		return (ItemStack) configEntries.get(key);
	}
	
	public List<?> getList(String key){
		return (List<?>) configEntries.get(key);
	}
	
	public Long getLong(String key){
		return (Long) configEntries.get(key);
	}
	
	
	public List<Long> getLongList(String key){
		return (List<Long>) configEntries.get(key);
	}
	
	public List<Map<?,?>> getMapList(String key){
		return (List<Map<?, ?>>) configEntries.get(key);
	}
	
	public OfflinePlayer getOfflinePlayer(String key){
		return (OfflinePlayer) configEntries.get(key);
	}
	
	public List<Short> getShortList(String key){
		return (List<Short>) configEntries.get(key);
	}
	
	public String getString(String key){
		return (String) configEntries.get(key);
	}
	
	public List<String> getStringList(String key){
		return (List<String>) configEntries.get(key);
	}
	
	public Vector getVector(String key){
		return (Vector) configEntries.get(key);
	}
	
}
