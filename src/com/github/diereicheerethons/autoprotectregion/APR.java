package com.github.diereicheerethons.autoprotectregion;

import net.milkbowl.vault.permission.Permission;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

//ShortMapper
public class APR {
	public static AutoProtectRegion instance = AutoProtectRegion.instance;
	public static Config config = AutoProtectRegion.config;
	
	public static WorldGuardPlugin worldGuard = AutoProtectRegion.worldGuard;
	public static Permission permission = AutoProtectRegion.permission;
	public static Translator language = AutoProtectRegion.language;
}
