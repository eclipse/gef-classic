package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

/**
 * Interface for a Palette Category
 */
public interface PaletteContainer 
	extends PaletteEntry
{

/**
 * Property name indicating that this PaletteContainer's children have changed
 */
public static final String
	PROPERTY_CHILDREN_CHANGED = "Children Changed"; //$NON-NLS-1$

		

static String     PALETTE_TYPE_CATEGORY = "Palette_Category", //$NON-NLS-1$
                  PALETTE_TYPE_GROUP = "Palette_Group";//$NON-NLS-1$

void add(PaletteEntry entry);
void add(int index, PaletteEntry entry);
void addAll(List newChildren);

/**
 * @return the children of this container
 */
List getChildren();

/**
 * @return the label for this container
 */
String getLabel();

void remove(PaletteEntry entry);

}
