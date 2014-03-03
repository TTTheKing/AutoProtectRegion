package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;

public class ShowProtectionCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "showprotection";
		super.permission = "apr.showprotection";
		super.senderType = "player";
		super.aliases.add("show");
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				return;
			}
		};
		
		new PluginCommandArgument("off", this){
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
		
		if(APRRegionList.get(regionName)==null){
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("regionNotFound"));
			return false;
		}
		
		boolean off = unreqArgs.containsKey("off");
		toggleShowProtection(off, player, regionName);
		return true;
	}

	public static void toggleShowProtection(boolean off,Player player, String regionName) {
		World world = player.getWorld();
		RegionManager regionManager = WGBukkit.getRegionManager(world);
		BukkitWorld bWorld = new BukkitWorld(world);
		ProtectedPolygonalRegion wgRegion = (ProtectedPolygonalRegion) regionManager.getRegionExact(regionName);
		Polygonal2DRegion weRegion = new Polygonal2DRegion(bWorld, wgRegion.getPoints(), wgRegion.getMinimumPoint().getBlockY(), wgRegion.getMaximumPoint().getBlockY());
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for(BlockVector vector:weRegion){
			int x = vector.getBlockX();
			int z = vector.getBlockZ();
			int y = vector.getBlockY();
			blocks.add(world.getBlockAt(new Location(world, x ,y ,z )));
		}
		if(!off){
			for(Block block: blocks){
				if(block.getType() == Material.AIR){
					player.sendBlockChange(new Location(block.getWorld(), block.getX() ,block.getY() ,block.getZ()), Material.WEB, (byte) 0);
				}else if(block.getType().isSolid()){
					player.sendBlockChange(new Location(block.getWorld(), block.getX() ,block.getY() ,block.getZ()), Material.WOOL, (byte) 5);
				}
			}
		}else{
			for(Block block: blocks){
				player.sendBlockChange(new Location(block.getWorld(), block.getX() ,block.getY() ,block.getZ()), block.getType(), block.getData());
			}
		}
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.ShowProtection");
	}

}
