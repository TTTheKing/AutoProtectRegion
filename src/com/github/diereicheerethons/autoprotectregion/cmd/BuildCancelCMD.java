package com.github.diereicheerethons.autoprotectregion.cmd;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.diereicheerethons.autoprotectregion.Translator;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayer;
import com.github.diereicheerethons.autoprotectregion.players.APRPlayerList;
import com.github.diereicheerethons.autoprotectregion.util.PluginCommand;

public class BuildCancelCMD extends PluginCommand {

	@Override
	public void setUp() {
		super.command    = "build.cancel";
		super.permission = "apr.build.cancel";
		super.senderType = "player";
		super.aliases.add("create.cancel");
		super.aliases.add("edit.cancel");
	}

	@Override
	public void setArguments() {
		return;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			HashMap<String, String> requiredArgs,
			HashMap<String, String> unreqArgs, String[] otherArgs) {
		
		Player player = (Player) sender;
		APRPlayer aprPlayer = APRPlayerList.getOrCreateAPRPlayer(player);
		
		aprPlayer.setCurrentRegion(null);
		aprPlayer.setEditingRegion(false);
		
		return true;
	}

	@Override
	public String getCommandHelp() {
		return Translator.translate("cmdHelp.BuildCancel");
	}

}
