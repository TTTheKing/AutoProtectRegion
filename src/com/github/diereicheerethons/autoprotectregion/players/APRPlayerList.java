package com.github.diereicheerethons.autoprotectregion.players;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class APRPlayerList {

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
}
