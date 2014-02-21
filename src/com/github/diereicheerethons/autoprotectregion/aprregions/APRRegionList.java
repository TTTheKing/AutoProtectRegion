package com.github.diereicheerethons.autoprotectregion.aprregions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;

public class APRRegionList {
	private static File saveFile = new File(AutoProtectRegion.instance.getDataFolder(), "regions.yml");
	
	public static ArrayList<APRRegion> list = new ArrayList<APRRegion>();
	
	public static APRRegion get(String name){
		for(APRRegion region:list){
			if(region.getWgRegionID() == name)
				return region;
		}
		return null;
	}
	
	public static void save(){
		FileConfiguration ymlFile;
		if(saveFile.exists())
			ymlFile = YamlConfiguration.loadConfiguration(saveFile);
		else
			ymlFile = new YamlConfiguration();
		
		for(APRRegion region: list){
			String key = region.getWgRegionID();
			ymlFile.set(key + ".maxXWidth", region.getMaxXWidth());
			ymlFile.set(key + ".maxZWidth", region.getMaxZWidth());
			ymlFile.set(key + ".worldName", region.getWorld().getName());
			
			int pointCounter = 0;
			for(XZPoint point:region.allPoints){
				pointCounter++;
				
				ymlFile.set(key + ".allPoints."+pointCounter+".x", point.getX());
				ymlFile.set(key + ".allPoints."+pointCounter+".z", point.getZ());
				
				ymlFile.set(key + ".allPoints."+pointCounter+".minY", point.getMinY());
				ymlFile.set(key + ".allPoints."+pointCounter+".maxY", point.getMaxY());
			}
			
		}
		
		try {
			ymlFile.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load(){
		FileConfiguration ymlFile = YamlConfiguration.loadConfiguration(saveFile);
		
		Set<String> keys = ymlFile.getKeys(false);
		for(String key: keys){
			long maxXWidth = ymlFile.getLong(key + ".maxXWidth");
			long maxZWidth = ymlFile.getLong(key + ".maxZWidth");
			
			String worldName = ymlFile.getString(key + ".worldName");
			World world = AutoProtectRegion.instance.getServer().getWorld(worldName);
			
			APRRegion region = new APRRegion(key, world, maxXWidth, maxZWidth);
			
			ConfigurationSection points = ymlFile.getConfigurationSection(key + ".allPoints");
			Set<String> pointCounter = points.getKeys(false);
			
			for(String point: pointCounter){
				long x = points.getLong(point + ".x");
				long z = points.getLong(point + ".z");
				long minY = points.getLong(point + ".minY");
				long maxY = points.getLong(point + ".maxY");
				
				region.allPoints.addLoaded(new XZPoint(x,z,maxY,minY));
			}
			region.recalculateLists();
			
		}
		
	}
}
