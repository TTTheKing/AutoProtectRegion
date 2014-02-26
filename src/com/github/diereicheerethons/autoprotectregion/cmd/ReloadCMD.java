package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;

public class ReloadCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "reload";
		super.permission = "apr.reload";
		super.senderType = "player and console";
	}

	@Override
	public void setArguments() {
		return;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		AutoProtectRegion.instance.loadAll();
		return false;
	}

	@Override
	public String getCommandHelp() {
		return "Reloads the Data and the Config. Warning you will loose unsaved data. Use /apr save to prevent this!";
	}

}
