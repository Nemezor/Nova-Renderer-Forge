package com.continuum.nova.injector;

import java.util.HashMap;

public class Tokens {

	private HashMap<String, String> map;
	
	public Tokens(HashMap<String, String> map) {
		this.map = map;
	}
	
	public String lookup(String key) {
		return map.get(key);
	}
}
