package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
		Block block = event.getBlock();
		
		if(!aprPlayer.isEditingRegion())
			return;
		
		APRRegion aprRegion = aprPlayer.getCurrentRegion();
		
		
		ProtectedRegion wgRegion = WGBukkit.getRegionManager(player.getWorld()).getRegionExact(aprRegion.getWgRegionID());
		if(wgRegion == null){
			player.sendMessage("[APR]: "+Translator.translate("stoppedEditing"));
			aprPlayer.setCurrentRegion(null);
			aprPlayer.setEditingRegion(false);
			return;
		}
		aprRegion.addPoint(block.getX(), block.getZ(), block.getY());
		
		
	}
}
