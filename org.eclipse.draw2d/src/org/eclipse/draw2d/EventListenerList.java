package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

/**
 * This class is intended for internal use only.
 */
public final class EventListenerList {

List listeners = new ArrayList(1);
List types = new ArrayList(1);

public void addListener(Class c, Object listener){
	types.add(c);
	listeners.add(listener);
}

public boolean containsListener(Class c){
	for (int i=0; i < types.size(); i++)
		if (types.get(i) == c) return true;
	return false;
}

public Iterator getListeners(Class c){
	List result = new ArrayList();
	for (int i=0; i < types.size(); i++)
		if (types.get(i) == c)
			result.add(listeners.get(i));
	return result.iterator();
}

public void removeListener(Class c, Object listener){
	for (int i=0; i < types.size(); i++)
		if (listeners.get(i) == listener && types.get(i)==c){
			listeners.remove(i);
			types.remove(i);
			return;
		}
}

}