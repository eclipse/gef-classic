package com.ibm.etools.gef.palette;
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
	extends PaletteEntry{

static String     PALETTE_TYPE_CATEGORY = "Palette_Category", //$NON-NLS-1$
                  PALETTE_TYPE_GROUP = "Palette_Group";//$NON-NLS-1$

/**
 * Return the groups for this palette category
 */
List getChildren();

/**
 * Returns the label for the category.
 * @return java.lang.String
 */
String getLabel();

}
