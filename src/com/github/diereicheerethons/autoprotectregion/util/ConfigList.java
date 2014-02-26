package com.github.diereicheerethons.autoprotectregion.util;

import java.util.HashMap;

public class ConfigList extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private HashMap<String, Class<?>> types = new HashMap<String, Class<?>>();
	
	public Class<?> getType(String key){
		return types.get(key);
	}
	
	public void setType(String key, Class<?> type){
		types.put(key, type);
	}
	
	public Object put(String key, Object value, Class<?> type){
		setType(key,type);
		return put(key,value);
	}
}
