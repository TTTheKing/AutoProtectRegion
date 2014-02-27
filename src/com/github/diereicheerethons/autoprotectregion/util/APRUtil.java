package com.github.diereicheerethons.autoprotectregion.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.Translator;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class APRUtil {
	
	
	//Copied from WorldGuard RegionCommands
	//src/main/java/com/sk89q/worldguard/bukkit/commands/RegionCommands.java
	public static String validateRegionId(String id)
            throws CommandException {
        if (!ProtectedRegion.isValidId(id)) {
            throw new CommandException(
                    "The region name of '" + id + "' contains characters that are not allowed.");
        }
        
        return id;
    }
	
	
	public static void playerMsg(CommandSender sender, String msg){
		if(sender instanceof Player)
			playerMsg((Player) sender, msg);
		else
			sender.sendMessage(ChatColor.RED+"[APR]: "+Translator.translate("doItAsPlayer"));
	}
	
	public static void playerMsg(Player sender, String msg){
		sender.sendMessage(ChatColor.GOLD+"[APR]: "+ChatColor.WHITE+ msg);
	}
	
}
