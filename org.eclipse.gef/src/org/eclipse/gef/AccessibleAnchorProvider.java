package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

public interface AccessibleAnchorProvider {

/**
 * Returns a list of Points in <B>absolute</B> coordinates where source anchors are located.
 * Tools that work with connections should use these locations when operating in accesible
 * keyboard modes.
 */
List getSourceAnchorLocations();

/**
 * Returns a list of Points in <B>absolute</B> coordinates where target anchors are located.
 * Tools that work with connections should use these locations when operating in accesible
 * keyboard modes.
 */
List getTargetAnchorLocations();

}
