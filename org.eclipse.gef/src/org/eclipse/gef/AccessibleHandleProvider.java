package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

/**
 * This class provides keyboard accessibility support for the {@link Handle Handles} of
 * the primary-selected {@link EditPart}. Handles are grabbable regions on the screen. 
 * Typically the user will grab a handle by pressing and holding the mouse button.  This
 * function must also be available via the keyboard for accessibility reasons.  Even mouse
 * Users will appreciate the fine-grained control offered by the keyboard for operations
 * like "nudging".
 * <P>The SelectionTool is the primary client for Handles. When it is active, the PERIOD
 * keystroke will cycle through the list of Accessibile locations (Points) supplied by the
 * current EditPart.  The "current EditPart" is defined as the EditPart with primary
 * selection.
 */

public interface AccessibleHandleProvider {

/**
 * Returns a list of Points in <B>absolute</B> coordinates where {@link Handle Handles}
 * are located.  {@link Tool Tools} that work with Handles should use these locations when
 * operating in accesible keyboard modes.
 * @return A list of absolute locations (Points relative to the Viewer's Control)
 */
List getAccessibleHandleLocations();

}
