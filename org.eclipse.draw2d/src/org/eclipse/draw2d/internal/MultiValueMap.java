/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal;

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
	
	public int size() {
		return map.size();
	}
}
