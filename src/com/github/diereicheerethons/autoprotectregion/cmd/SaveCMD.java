package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.diereicheerethons.autoprotectregion.AutoProtectRegion;
import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;

public class SaveCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "save";
		super.permission = "apr.save";
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
		AutoProtectRegion.instance.saveAll();
		return false;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Save");
	}

}
