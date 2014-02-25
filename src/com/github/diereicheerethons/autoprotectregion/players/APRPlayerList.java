package com.github.diereicheerethons.autoprotectregion.players;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;

public class APRPlayerList {

	private static File saveFile = new File(AutoProtectRegion.instance.getDataFolder(), "players.yml");
	
	public static ArrayList<APRPlayer> list = new ArrayList<APRPlayer>();
	
	public static APRPlayer getAPRPlayer(Player player){
		for(APRPlayer aprPlayer : list){
			if(aprPlayer.getPlayer()==player){
				return aprPlayer;
			}
		}
		return null;
	}
	
	public static APRPlayer getOrCreateAPRPlayer(Player player){
		APRPlayer aprPlayer = getAPRPlayer(player);
		if(aprPlayer == null){
			return new APRPlayer(player);
		}else{
			return aprPlayer;
		}
	}
	
	
	public static void savePlayers(){
		FileConfiguration ymlFile;
		if(saveFile.exists())
			ymlFile = YamlConfiguration.loadConfiguration(saveFile);
		else
			ymlFile = new YamlConfiguration();
		
		for(APRPlayer player : list){
			String key = player.getPlayer().getName();
			if(player.getCurrentRegion() != null)
				ymlFile.set(key+".currentRegion", player.getCurrentRegion().getWgRegionID());
			ymlFile.set(key+".isEditing", player.isEditingRegion());
		}
		
		try {
			ymlFile.save(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadPlayers(){
		FileConfiguration ymlFile = YamlConfiguration.loadConfiguration(saveFile);
		
		Set<String> keys = ymlFile.getKeys(false);
		
		for(String key: keys){
			Player player = AutoProtectRegion.instance.getServer().getPlayer(key);
			APRPlayer aprPlayer = getOrCreateAPRPlayer(player);
			aprPlayer.setEditingRegion(ymlFile.getBoolean(key+".isEditing"));
			if(ymlFile.contains(key+".currentRegion"))
				aprPlayer.setCurrentRegion(APRRegionList.get(ymlFile.getString(key+".currentRegion")));
			else
				aprPlayer.setCurrentRegion(null);
		}
	}
	
}
