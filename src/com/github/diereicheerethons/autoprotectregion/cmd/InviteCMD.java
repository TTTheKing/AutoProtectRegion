package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommandArgument;

public class InviteCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "invite";
		super.permission = "apr.invite";
		super.senderType = "player";
	}

	@Override
	public void setArguments() {
		new PluginCommandArgument("region-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
		
		new PluginCommandArgument("player-name", this){
			public void setUpProperties() {
				properties.put("required", true);
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.Invite");
	}

}
