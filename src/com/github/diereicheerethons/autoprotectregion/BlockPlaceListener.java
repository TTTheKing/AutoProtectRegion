package com.github.diereicheerethons.autoprotectregion;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion.PointInBlacklistException;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion.PointNotInRangeException;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion.PointNotInWhitelistException;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
		Block block = event.getBlock();
		
		if(!aprPlayer.isEditingRegion())
			return;
		
		APRRegion aprRegion = aprPlayer.getCurrentRegion();
		
		if(!player.getWorld().getName().equalsIgnoreCase(aprRegion.getWorld().getName())){
			player.sendMessage(ChatColor.GOLD+"[APR]: "+
					ChatColor.RED+Translator.translate("notInSameWorld"));
		}
		
		try {
			aprRegion.addPoint(block.getX(), block.getZ(), block.getY());
		} catch (PointNotInRangeException e) {
			player.sendMessage(ChatColor.GOLD+"[APR]: "+ChatColor.RED+
					Translator.translate("blockNotInRegionPart1")+
					aprRegion.getWgRegionID()+
					Translator.translate("blockNotInRegionPart2"));
		} catch (PointInBlacklistException e) {
			player.sendMessage(ChatColor.GOLD+"[APR]: "+ChatColor.RED+ Translator.translate("cantBuildInThisRegion"));
		} catch (PointNotInWhitelistException e) {
			player.sendMessage(ChatColor.GOLD+"[APR]: "+ChatColor.RED+ Translator.translate("canOnlyBuildInSomeRegions"));
		}
	}
}
