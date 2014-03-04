package com.github.diereicheerethons.autoprotectregion;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.github.diereicheerethons.autoprotectregion.util.PluginConfig;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class Config extends PluginConfig{

	public Config(JavaPlugin plugin) {
		super("config.yml", plugin);
	}

	@Override
	protected void setupDefault() {
		set("pluginShortName", new String("APR"), String.class);
		set("maxXWidth", new Long(20), Long.class);
		set("maxZWidth", new Long(20), Long.class);
		set("maxYWidth", new Long(15), Long.class);
		set("defaultRegionSettings.priority", new Integer(13), Integer.class);
		set("language", new String("en"), String.class);
		set("calculationVersion", new Integer(1), Integer.class);
		ArrayList<String> wgRegionBlacklist = new ArrayList<String>();
		wgRegionBlacklist.add("teeeessssst");
		wgRegionBlacklist.add("teeeeeesssst2");
		set("wgRegionBlacklist", wgRegionBlacklist, List.class);
		ArrayList<String> wgRegionWhitelist = new ArrayList<String>();
		set("wgRegionWhitelist", wgRegionWhitelist, List.class);
		set("maxPlayerRegions", -1, Integer.class);
		set("showAirBlock", "TRIPWIRE", String.class);
		
		inputDefaultRegionFlags();
	}

	private void inputDefaultRegionFlags() {
		String flagsKey = "defaultRegionFlags.";
		Flag<?>[] flags = DefaultFlag.getFlags();
		
		for(Flag<?> flag:flags){
			if(flag instanceof StateFlag){
				set(flagsKey+flag.getName(), ((StateFlag) flag).getDefault(), Boolean.class);
			}else if(flag instanceof IntegerFlag){
				set(flagsKey+flag.getName(), 0, Integer.class);
			}else if(flag instanceof BooleanFlag){
				set(flagsKey+flag.getName(), false, Boolean.class);
			}else if(flag instanceof StringFlag){
				set(flagsKey+flag.getName(), "",String.class);
			}else if(flag instanceof SetFlag<?>){
				set(flagsKey+flag.getName(), new ArrayList<String>(),List.class);
			}else if(flag instanceof DoubleFlag){
				set(flagsKey+flag.getName(), 0.0, Double.class);
			}else if(flag instanceof LocationFlag){
				set(flagsKey+flag.getName(), new Vector(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Vector.class);
			}else if(flag instanceof EnumFlag<?>){
				set(flagsKey+flag.getName(), "",String.class);
			}
		}
	}
	
	public Map<Flag<?>, Object> getDefaultRegionFlags(World world){
		String flagsKey = "defaultRegionFlags.";
		Flag<?>[] defFlags = DefaultFlag.getFlags();
		
		Map<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
		
		for(Flag<?> flag:defFlags){
			if(flag instanceof StateFlag){
				flags.put(flag, ((StateFlag) flag).unmarshal(getBoolean(flagsKey+flag.getName())));
			}else if(flag instanceof IntegerFlag){
				Integer i = getInt(flagsKey+flag.getName());
				if(i == 0)
					continue;
				flags.put(flag, ((IntegerFlag) flag).unmarshal(i));
			}else if(flag instanceof BooleanFlag){
				Boolean b = getBoolean(flagsKey+flag.getName());
				flags.put(flag, ((BooleanFlag) flag).unmarshal(b));
			}else if(flag instanceof StringFlag){
				String s = getString(flagsKey+flag.getName());
				if(s.equalsIgnoreCase(""))
					continue;
				flags.put(flag, ((StringFlag) flag).unmarshal(s));
			}else if(flag instanceof SetFlag<?>){
				List<String> list = getStringList(flagsKey+flag.getName());
				if(list.isEmpty())
					continue;
				flags.put(flag, ((SetFlag<?>) flag).unmarshal(list));
			}else if(flag instanceof DoubleFlag){
				Double d = getDouble(flagsKey+flag.getName());
				if(d == 0.0)
					continue;
				flags.put(flag, ((DoubleFlag) flag).unmarshal(d));
			}else if(flag instanceof LocationFlag){
				Vector vector = getVector(flagsKey+flag.getName());
				if(vector.getBlockX() == Integer.MIN_VALUE)
					continue;
				if(vector.getBlockY() == Integer.MIN_VALUE)
					continue;
				if(vector.getBlockZ() == Integer.MIN_VALUE)
					continue;
				flags.put(flag, ((LocationFlag) flag).unmarshal(vector.toLocation(world)));
			}else if(flag instanceof EnumFlag<?>){
				String s = getString(flagsKey+flag.getName());
				if(s.equalsIgnoreCase(""))
					continue;
				flags.put(flag, ((EnumFlag<?>) flag).unmarshal(s));
			}
		}
		
		return flags;
	}
}
