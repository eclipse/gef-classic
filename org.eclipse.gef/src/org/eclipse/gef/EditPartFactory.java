package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A factory for creating new EditParts.  {@link EditPartViewer EditPartViewers} can be
 * configured with an <code>EditPartFactory</code>.  Whenever an <code>EditPart</code> in
 * that viewer needs to create another EditPart, it can use the Viewer's factory.  The
 * factory is also used by the viewer whenever {@link EditPartViewer#setContents(Object)}
 * is called.
 * @since 2.0
 */
public interface EditPartFactory {

/**
 * Creates a new EditPart given the specified <i>context</i> and <i>model</i>.
 * @param context The context in which the EditPart is being created, such as its parent. * @param model the model of the EditPart being created * @return EditPart the new EditPart */
EditPart createEditPart(EditPart context, Object model);

}