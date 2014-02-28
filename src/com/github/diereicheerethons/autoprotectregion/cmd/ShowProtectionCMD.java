package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegion;
import com.github.diereicheerethons.autoprotectregion.aprregions.APRRegionList;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class ShowProtectionCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "showprotection";
		super.permission = "apr.showprotection";
		super.senderType = "player";
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				return;
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		Player player = (Player) sender;
		World world = player.getWorld();
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		
		String regionName;
		APRRegion aprRegion;
		if(unreqArgs.containsKey("region-name")){
			regionName = "apr_"+player.getName()+"_"+unreqArgs.get("region-name");
			aprRegion = APRRegionList.get(regionName);
		}else{
			aprRegion = APRPlayerList.getOrCreateAPRPlayer(player).getCurrentRegion();
			if(aprRegion == null){
				sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("defineOrSelectRegion"));
				return false;
			}
			regionName = aprRegion.getWgRegionID();
		}
		ProtectedPolygonalRegion wgRegion = (ProtectedPolygonalRegion) regionManager.getRegionExact(regionName);
		List<BlockVector2D> wgPoints = wgRegion.getPoints();
		int minY = wgRegion.getMinimumPoint().getBlockY();
		int maxY = wgRegion.getMaximumPoint().getBlockY();
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for(BlockVector2D vector:wgPoints){
			int x = vector.getBlockX();
			int z = vector.getBlockZ();
			for(int y = minY; y <= maxY; y++){
				blocks.add(world.getBlockAt(new Location(world, x ,y ,z )));
			}
		}
		
		for(Block block: blocks){
			if(block.getType() == Material.AIR){
				player.sendBlockChange(new Location(block.getWorld(), block.getX() ,block.getY() ,block.getZ()), Material.WEB, (byte) 0);
			}else{
				player.sendBlockChange(new Location(block.getWorld(), block.getX() ,block.getY() ,block.getZ()), Material.WOOL, (byte) 5);
			}
		}
		
		return true;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.ShowProtection");
	}

}
