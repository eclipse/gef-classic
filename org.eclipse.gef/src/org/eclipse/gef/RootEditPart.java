package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 The top-level EditPart that bridges the gap between EditParts
 and their EditPartViewer.
 The Root can be used to gain access to the viewer.
 */
public interface RootEditPart extends EditPart {

EditPart getContents();

/**
 * Return the EditPartViewerviewer for this root.
 * @return viewer The viewer.
 */
EditPartViewer getViewer();

void setContents(EditPart editpart);

/**
 * Sets the viewer.
 */
void setViewer(EditPartViewer view);

}
