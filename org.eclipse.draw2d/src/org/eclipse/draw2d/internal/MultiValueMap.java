/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultiValueMap<U, V> {
	private final Map<U, List<V>> map = new HashMap<>();

	public List<V> get(Object key) {
		return map.get(key);
	}

	public void put(U key, V value) {
		List<V> values = map.computeIfAbsent(key, ignored -> new ArrayList<>(1));
		if (!values.contains(value)) {
			values.add(value);
		}
	}

	public int remove(U key, V value) {
		List<V> values = map.get(key);
		if (values != null) {
			int position = values.indexOf(value);
			if (position == -1) {
				return -1;
			}
			values.remove(position);
			if (values.isEmpty()) {
				map.remove(key);
			}
			return position;
		}
		return -1;
	}

	public V removeValue(V value) {
		Iterator<List<V>> iter = map.values().iterator();
		List<V> current;
		while (iter.hasNext()) {
			current = iter.next();
			if (current.remove(value)) {
				if (current.isEmpty()) {
					iter.remove();
				}
				return value;
			}
		}
		return null;
	}

	public int size() {
		return map.size();
	}
}
