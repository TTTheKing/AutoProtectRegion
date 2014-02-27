package com.github.diereicheerethons.autoprotectregion.defaultlanguages;

import com.github.diereicheerethons.autoprotectregion.APR;
import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;

public class German extends PluginConfig{
	
	public static String fileName = "locale/de.yml";
	
	public German() {
		super(fileName, false, APR.instance);
	}
	
	@Override
	protected void setupDefault() {
		set("stoppedEditing", "Du hast aufgehoert deine Region zu bearbeiten. Hast du die Welt gewechselt?", String.class);
		set("failedSaving", "Speichern der Regionen fehlgeschlagen!", String.class);
		set("cmdHelp.BuildCancel", "Beendet das Bearbeiten der Region", String.class);
		set("cmdHelp.Build", "Beginne mit dem Bearbeiten der Region", String.class);
		set("cmdHelp.Debug", "Sendet debug info", String.class);
		set("cmdHelp.Delete", "Loescht Region", String.class);
		set("cmdHelp.Reload", "Laed die Daten und die Config neu. Achtung du verlierst alle ungespeicherten Daten. Nutze /apr save um dies zu vermeiden!", String.class);
		set("cmdHelp.Save", "Speichert alle daten auf das File System", String.class);
		set("doItAsPlayer", "Das kannst du nur als Spieler tun!", String.class);
		set("noWorldGuard", "WorldGuard fehlt", String.class);
		set("newRegion", "Neue Region erstellt", String.class);
		set("noWorldAndPlayerName", "Bitte gib einen Spielernamen und einen Weltnamen an", String.class);
		set("noPermissionsDeleteOthers", "Keine Berechtigung f�r den Command \"delete\" bei anderen Spielern!", String.class);
		set("noWorldName", "Bitte gib einen Weltnamen an.", String.class);
	}
	
}