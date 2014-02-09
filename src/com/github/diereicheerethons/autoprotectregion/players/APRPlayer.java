package com.github.diereicheerethons.autoprotectregion.players;

import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.APRRegion;

public class APRPlayer {
	
	private Player player;
	private APRRegion currentRegion;
	private boolean editingRegion = false;
	
	
	public APRPlayer(Player player){
		this.player=player;
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
	
	public Player getPlayer(){
		return player;
	}
	
	
}
