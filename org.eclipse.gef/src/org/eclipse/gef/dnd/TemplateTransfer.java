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
package org.eclipse.gef.dnd;

/**
 * 
 * @author ebordeau
 * @since 2.1 */
public class TemplateTransfer
	extends SimpleObjectTransfer
{

private static final TemplateTransfer INSTANCE = new TemplateTransfer();
private static final String TYPE_NAME = "Template transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + INSTANCE.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

private TemplateTransfer() { }

/**
 * Returns the singleton instance
 * @return the singleton
 */
public static TemplateTransfer getInstance() {
	return INSTANCE;
}

/**
 * Returns the <i>template</i> object.
 * @return the template */
public Object getTemplate() {
	return getObject();
}

/**
 * @see Transfer#getTypeIds()
 */
protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

/**
 * @see Transfer#getTypeNames()
 */
protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

/**
 * Sets the <i>template</template> Object.
 * @param template the template */
public void setTemplate(Object template) {
	setObject(template);
}

}