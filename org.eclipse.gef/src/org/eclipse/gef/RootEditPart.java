package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A RootEditPart is the <i>root</i> of an EditPartViewer. It bridges the gap between
 * the EditPartViewer and its {@link EditPartViewer#getContents() contents}. It does not
 * correspond to anything in the model, and typically can not be interacted with by the
 * User. The Root provides a homogeneous context for the applications "real" EditParts.
 */
public interface RootEditPart extends EditPart {

/**
 * Returns the <i>contents</i> EditPart. A RootEditPart only has a single child, called its
 * <i>contents</i>.
 * @return the contents. */
EditPart getContents();

/**
 * Returns the root's EditPartViewer.
 * @return The <code>EditPartViewer</code>
 */
EditPartViewer getViewer();

/**
 * Sets the <i>contents</i> EditPart. A RootEditPart only has a single child, called its
 * <i>contents</i>.
 * @param editpart the contents */
void setContents(EditPart editpart);

/**
 * Sets the root's EditPartViewer.
 * @param viewer the EditPartViewer
 */
void setViewer(EditPartViewer viewer);

}
