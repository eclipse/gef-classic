package org.eclipse.draw2d.util;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.HashMap;

public class MultiValueMap {
	private HashMap map = new HashMap();
	
	public MultiValueMap() { super(); }
	
	public ArrayList get(Object key) {
		Object value = map.get(key);
		if (value == null) return null;
		
		if (value instanceof ArrayList) 
			return (ArrayList) value;
		ArrayList v = new ArrayList(1);
		v.add(value);
		return v;
	}
	
	public void put(Object key, Object value) {
		Object existingValues = map.get(key);
		if (existingValues == null) {
			map.put(key, value);
			return;
		}
		if (existingValues instanceof ArrayList) {
			ArrayList v = (ArrayList) existingValues;
			if (!v.contains(value))
				v.add(value);
			return;
		}
		if (existingValues != value) {
			ArrayList v = new ArrayList(2);
			v.add(existingValues);
			v.add(value);
			map.put(key, v);
		}
	}
	
	public void remove(Object key, Object value) {
		Object existingValues = map.get(key);
		if (existingValues != null) {
			if (existingValues instanceof ArrayList) {
				ArrayList v = (ArrayList) existingValues;
				v.remove(value);
				if (v.isEmpty())
					map.remove(key);
				return;
			}
			map.remove(key);
		}
	}
	
	public int size(){
		return map.size();
	}
}
