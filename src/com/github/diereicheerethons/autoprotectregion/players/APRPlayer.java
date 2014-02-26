package com.github.diereicheerethons.autoprotectregion.players;

import org.bukkit.OfflinePlayer;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;

public class APRPlayer {
	
	private OfflinePlayer player;
	private APRRegion currentRegion;
	private boolean editingRegion = false;
	
	
	public APRPlayer(OfflinePlayer player){
		this.player=player;
		APRPlayerList.list.add(this);
	}
	
	public void setCurrentRegion(APRRegion region){
		currentRegion = region;
	}
	
	public void setEditingRegion(boolean isEditing){
		editingRegion = isEditing;
	}
	
	public boolean isEditingRegion(){
		return editingRegion;
	}
	
	public APRRegion getCurrentRegion(){
		return currentRegion;
	}
	
	public OfflinePlayer getPlayer(){
		return player;
	}
	
	
}
