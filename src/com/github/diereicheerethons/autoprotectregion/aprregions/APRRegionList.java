package com.github.diereicheerethons.autoprotectregion.aprregions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;

public class APRRegionList {
	private static File saveFile = new File(AutoProtectRegion.instance.getDataFolder(), "regions.yml");
	
	public static ArrayList<APRRegion> list = new ArrayList<APRRegion>();
	
	public static APRRegion get(String name){
		for(APRRegion region:list){
			if(region.getWgRegionID().equalsIgnoreCase(name))
				return region;
		}
		return null;
	}
	
	public static boolean remove(String regionName){
		APRRegion region = APRRegionList.get(regionName);
		if(region != null)
			return list.remove(region);
		return false;
	}
	
	public static void sendDebugData(CommandSender sender){
		for(APRRegion region : list){
			sender.sendMessage(region.getWgRegionID() + "| World = " +region.getWorld().getName());
			sender.sendMessage(region.getWgRegionID() + "| maxXWidth = " +region.getMaxXWidth());
			sender.sendMessage(region.getWgRegionID() + "| maxZWidth = " +region.getMaxZWidth());
			
			sender.sendMessage(region.getWgRegionID() + "| maxy = " +region.allPoints.getMaxY());
			sender.sendMessage(region.getWgRegionID() + "| miny = " +region.allPoints.getMinY());
			
			for(XZPoint point:region.allPoints){
				sender.sendMessage(region.getWgRegionID() + "| XZ === ( "+point.getX()+" | "+point.getZ()+" )");
			}
		}
	}
	
	public static int getRegionCountFor(String playerName){
		int count = 0;
		for(APRRegion region:list){
			if(region.getOwner().getName().equalsIgnoreCase(playerName))
				count++;
		}
		return count;
	}
	
	public static void save(){
		FileConfiguration ymlFile;
		
		ymlFile = new YamlConfiguration();
		
		for(APRRegion region: list){
			String key = region.getWgRegionID();
			ymlFile.set(key + ".maxXWidth", region.getMaxXWidth());
			ymlFile.set(key + ".maxZWidth", region.getMaxZWidth());
			ymlFile.set(key + ".worldName", region.getWorld().getName());
			ymlFile.set(key + ".ownerName", region.getOwner().getName());
			
			int pointCounter = 0;
			for(XZPoint point:region.allPoints){
				pointCounter++;
				
				ymlFile.set(key + ".allPoints."+pointCounter+".x", point.getX());
				ymlFile.set(key + ".allPoints."+pointCounter+".z", point.getZ());
				
				ymlFile.set(key + ".allPoints."+pointCounter+".minY", point.getMinY());
				ymlFile.set(key + ".allPoints."+pointCounter+".maxY", point.getMaxY());
			}
			
			List<String> inviteNames = new ArrayList<String>();
			for(OfflinePlayer player:region.getInvites()){
				inviteNames.add(player.getName());
			}
			
			ymlFile.set(key+ ".invites", inviteNames);
		}
		
		try {
			ymlFile.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load(){
		list = new ArrayList<APRRegion>();
		
		FileConfiguration ymlFile = YamlConfiguration.loadConfiguration(saveFile);
		
		Set<String> keys = ymlFile.getKeys(false);
		for(String key: keys){
			long maxXWidth = ymlFile.getLong(key + ".maxXWidth");
			long maxZWidth = ymlFile.getLong(key + ".maxZWidth");
			long maxYWidth = ymlFile.getLong(key + ".maxYWidth");
			
			String worldName = ymlFile.getString(key + ".worldName");
			World world = AutoProtectRegion.instance.getServer().getWorld(worldName);
			String ownerName = ymlFile.getString(key + ".ownerName");
			OfflinePlayer owner = AutoProtectRegion.instance.getServer().getOfflinePlayer(ownerName);
			
			APRRegion region = new APRRegion(owner, key, world, maxXWidth, maxZWidth, maxYWidth);
			
			if(ymlFile.contains(key + ".allPoints")){
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
			
			if(ymlFile.contains(key + ".invites")){
				List<String> inviteNames = ymlFile.getStringList(key + ".invites");
				for(String name:inviteNames){
					OfflinePlayer player = APR.instance.getServer().getOfflinePlayer(name);
					region.getInvites().add(player);
				}
			}
		}
		
	}
}
