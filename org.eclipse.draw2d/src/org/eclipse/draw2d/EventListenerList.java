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
package org.eclipse.draw2d;

import java.util.*;

/**
 * This class is intended for internal use only.
 * TODO: If this is for internal use only, we should move it to the internal package.
 */
public final class EventListenerList {

List listeners = new ArrayList(1);
List types = new ArrayList(1);

/**
 * Adds a listener of type <i>c</i> to the list.
 * @param c the class
 * @param listener the listener
 */
public void addListener(Class c, Object listener) {
	types.add(c);
	listeners.add(listener);
}

/**
 * Returns <code>true</code> if this list of listeners contains a listener of type 
 * <i>c</i>.
 * @param c the type
 * @return whether this list contains a listener of type <i>c</i>
 */
public boolean containsListener(Class c) {
	for (int i = 0; i < types.size(); i++)
		if (types.get(i) == c) 
			return true;
	return false;
}

/**
 * Returns an Iterator of all the listeners of type <i>c</i>.
 * @param c the type
 * @return an Iterator of all the listeners of type <i>c</i>
 */
public Iterator getListeners(Class c) {
	List result = new ArrayList();
	for (int i = 0; i < types.size(); i++)
		if (types.get(i) == c)
			result.add(listeners.get(i));
	return result.iterator();
}

/**
 * Removes <i>listener</i> from the list.
 * @param c the type
 * @param listener the listener
 */
public void removeListener(Class c, Object listener) {
	for (int i = 0; i < types.size(); i++)
		if (listeners.get(i) == listener && types.get(i) == c) {
			listeners.remove(i);
			types.remove(i);
			return;
		}
}

}