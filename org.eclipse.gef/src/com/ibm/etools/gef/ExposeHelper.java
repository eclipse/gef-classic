package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Interface used by EditPartViewer to reveal a given EditPart.
 * <P>Viewer's will ask an EditPart for its ExposeHelper using 
 * {@link com.ibm.etools.gef.editparts.AbstractEditPart#getAdapter(Class)}.  
 * The returned helper is responsible for modifying *only* that EditPart's
 * visuals such that the specified descendant is made visible.
 */
public interface ExposeHelper {

/**
 * Exposes the specified descendant on the visuals of the 
 * EditPart which created this Helper.
 */
void exposeDescendant(EditPart part);

}
