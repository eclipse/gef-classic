/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;


public final class ImageCache {
private static final Map map = new WeakHashMap();

static class WeakEquivalenceSet {
private int hashCode;
Reference items[];
WeakEquivalenceSet(Object seed, ReferenceQueue queue) {
	hashCode = seed.hashCode();
	add(seed, queue);
}
static class NestedWeakReference extends WeakReference {
	public NestedWeakReference(Object referent, ReferenceQueue q) {
		super(referent, q);
	}
	
	public void removeFromSet() {
	//remove this reference from the set's items[].
	}
}

public void add(Object o, ReferenceQueue queue) {
	WeakReference ref = new NestedWeakReference(o, queue);
	items[0] = ref;
}

private Object getFirstReachable() {
	Object result;
	for (int i = 0; i < items.length; i++) {
		result = items[i].get();
		if (result != null) return result;
	}
	//Just return some bogus object instead of null
	return new Object();
}

public boolean equals(Object obj) {
	if (obj instanceof WeakEquivalenceSet) {
		WeakEquivalenceSet set = (WeakEquivalenceSet)obj;
		return set.getFirstReachable().equals(getFirstReachable());
	}
	return true;
}
}

static class ImageDisposer {
final Image image;

public ImageDisposer(Image image) {
	this.image = image;
}

protected void finalize() throws Throwable {
	image.dispose();
	super.finalize();
}
}

public static Image getImage(ImageDescriptor desc) {
	ImageDisposer disposer = (ImageDisposer)map.get(desc);
	if (disposer == null) {
		disposer = new ImageDisposer(desc.createImage());
		map.put(desc, disposer);
	}
	return disposer.image;
}
}