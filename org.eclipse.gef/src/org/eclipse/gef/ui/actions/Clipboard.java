/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

/**
 * A GEF clipboard for cut/copy/paste actions between GEF editors.  This will not work
 * between two instances of the workbench (but will work between multiple windows
 * belonging to a single instance of the workbench).  Setting the contents of the
 * clipboard will erase the previous contents of the clipboard.
 * 
 * @author Eric Bordeau
 */
public class Clipboard {

private static Clipboard defaultClipboard = new Clipboard();
private Object contents;

/**
 * Returns the default clipboard.  
 * @return the default clipboard
 */
public static Clipboard getDefault() {
	return defaultClipboard;
}

/**
 * Constructs a new Clipboard object.
 */
public Clipboard() { }

/**
 * Returns the current contents of the clipboard.
 * @return contents of the clipboard
 */
public Object getContents() {
	return contents;
}

/**
 * Sets the contents of the clipboard.  This will erase the previous contents of the
 * clipboard.
 * @param contents the new contents
 */
public void setContents(Object contents) {
	this.contents = contents;
}

}
