package org.eclipse.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


import org.eclipse.gef.EditPart;

import org.eclipse.draw2d.*;

/**
 * Responsible for locating <i>layers</i> in a <code>GraphicalViewer</code>.  Layers are
 * just transparent {@link Figure figures}.
 * <P>
 * Typically, the <code>RootEditPart</code> will register() itself as the LayerManager for
 * the GraphicalViewer. All other EditParts/EditPolicies looking for a layer use the
 * Viewer's {@link org.eclipse.gef.EditPartViewer#getEditPartRegistry() editPartRegsitry}
 * to find the <code>LayerManager</code>.
 * @author hudsonr
 * @since 2.0 */
public interface LayerManager {

/**
 * This key used to register the LayerManager in the Viewer's EditPartRegistry.
 */
Object ID = new Object();

/**
 * Returns a specified layer.
 * @param key a key identifying the layer * @return the specified layer */
IFigure getLayer(Object key);

/**
 * A static helper
 * @since 2.0 */
class Helper {
	/**
	 * Finds the LayerManager given any EditPart in the Viewer.
	 * @param part any EditPart in a GraphicalViewer	 * @return the <code>LayerManager</code>	 */
	public static LayerManager find(EditPart part) {
		return (LayerManager)part.getRoot().getViewer().getEditPartRegistry().get(ID);
	}
}

}