package com.github.diereicheerethons.autoprotectregion.util;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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
	protected ConfigList configEntries = new ConfigList();
	protected String fileName;
	
	public PluginConfig(String fileName, JavaPlugin plugin){
		this(fileName, 18000L, plugin); // Default all 15 Mins
	} 
	
	public PluginConfig(String fileName, boolean shouldSave, JavaPlugin plugin){
		this(fileName, Long.MAX_VALUE, plugin);
	}
	
	public PluginConfig(String fileName, long savetime, JavaPlugin plugin){
		this.fileName = fileName;
		configEntries = new ConfigList();
		
		configFile = new File(plugin.getDataFolder(), fileName);
		
		this.saveTimes = savetime;
		this.plugin=plugin;
		
		if(!configExists()){
			setupDefaults();
		}else{
			load();
		}
		
		if(!(savetime == Long.MAX_VALUE))
			initializeSaver();
		else
			save();
	}
	
	protected abstract void setupDefault();
	
	private void setupDefaults() {
		setupDefault();
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
			ymlFile.set(key, configEntries.get(key));
		}
		try {
			ymlFile.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean load(String fileName) {
		configFile = new File(plugin.getDataFolder(), fileName);
		return load();
	}
	
	public boolean load() {
		if(!configExists())
			return false;
		setupDefault();
		FileConfiguration ymlFile = YamlConfiguration.loadConfiguration(configFile);
		
		for(String key: configEntries.keySet()){
			if(ymlFile.contains(key)){
				Class<?> type = configEntries.getType(key);
				
				if(type == Integer.class){
					configEntries.put(key, ymlFile.getInt(key));
				}else if(type == Boolean.class){
					configEntries.put(key, ymlFile.getBoolean(key));
				}else if(type == Double.class){
					configEntries.put(key, ymlFile.getDouble(key));
				}else if(type == Long.class){
					configEntries.put(key, ymlFile.getLong(key));
				}else if(type == String.class){
					configEntries.put(key, ymlFile.getString(key));
				}else if(type == List.class){
					configEntries.put(key, ymlFile.getList(key));
				}else if(type == Color.class){
					configEntries.put(key, ymlFile.getColor(key));
				}else if(type == ItemStack.class){
					configEntries.put(key, ymlFile.getItemStack(key));
				}else if(type == OfflinePlayer.class){
					configEntries.put(key, ymlFile.getOfflinePlayer(key));
				}else if(type == Vector.class){
					configEntries.put(key, ymlFile.getVector(key));
				}else if(type == List.class){
					configEntries.put(key, ymlFile.getList(key));
				}else{
					configEntries.put(key, ymlFile.get(key));
				}
			}
		}
		
		return true;
	}
	
	public void set(String key, Object value, Class<?> type){
		configEntries.put(key, value, type);
	}
	
	public void sendDebugInfo(CommandSender sender){
		String string = "";
		for(String allkey:configEntries.keySet()){
			string += allkey +" --> "+configEntries.get(allkey)+"\n";
		}
		sender.sendMessage(string);
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
