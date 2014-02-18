package com.github.diereicheerethons.autoprotectregion.util;

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
}
