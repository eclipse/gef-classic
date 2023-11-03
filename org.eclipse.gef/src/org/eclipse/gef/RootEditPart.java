/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

/**
 * A RootEditPart is the <i>root</i> of an EditPartViewer. It bridges the gap
 * between the EditPartViewer and its {@link EditPartViewer#getContents()
 * contents}. It does not correspond to anything in the model, and typically can
 * not be interacted with by the User. The Root provides a homogeneous context
 * for the applications "real" EditParts.
 */
public interface RootEditPart extends EditPart {

	/**
	 * Returns the <i>contents</i> EditPart. A RootEditPart only has a single child,
	 * called its <i>contents</i>.
	 *
	 * @return the contents.
	 */
	EditPart getContents();

	/**
	 * Returns the root's EditPartViewer.
	 *
	 * @return The <code>EditPartViewer</code>
	 */
	@Override
	EditPartViewer getViewer();

	/**
	 * Sets the <i>contents</i> EditPart. A RootEditPart only has a single child,
	 * called its <i>contents</i>.
	 *
	 * @param editpart the contents
	 */
	void setContents(EditPart editpart);

	/**
	 * Sets the root's EditPartViewer.
	 *
	 * @param viewer the EditPartViewer
	 */
	void setViewer(EditPartViewer viewer);

}
