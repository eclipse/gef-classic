/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NestedGraphRootLayoutEditPolicy;

/**
 * @author Ian bull
 * @author Del Myers
 */
public class NestedPaneArea extends AbstractGraphicalEditPart {

	public static final int SUPPLIER_PANE = 0;
	public static final int MAIN_PANE = 1;
	public static final int CLIENT_PANE = 2;
	
	private int paneType = 0;
	
	public NestedPaneArea(int paneType) {
		this.paneType = paneType;
	}
	
	protected IFigure createFigure() {
		Figure f = new RectangleFigure();

		
		if ( paneType == 0 ) {
			f.setBackgroundColor(ColorConstants.black);
			f.setBounds(new Rectangle(0,0,100,100));
		}
		else if ( paneType == 1 ) {
			f.setBackgroundColor(ColorConstants.green);
			f.setBounds(new Rectangle(100,300,100,100));
			f.setLayoutManager(new FreeformLayout() {

				protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
					return null;
				}
				public void layout(IFigure container) {
					((GraphicalEditPart)getCastedModel().getModel().getCurrentNode().getEditPart()).getFigure().setBounds(container.getBounds());
				}});
			
		}
		else if ( paneType == 2 ) {
			f.setBackgroundColor(ColorConstants.red);
			f.setBounds(new Rectangle(100,0,100,100));
		}
		return f;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.SimpleRootEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		//@tag bug(152393-TopSelection(fix)) : add an edit policy to the EditPart which will create policies that don't allow the nodes to move.
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NestedGraphRootLayoutEditPolicy());
	}
	
	protected List getModelChildren() {
		if ( paneType == MAIN_PANE )
			return getCastedModel().getModel().getNodes();
		else
			return Collections.EMPTY_LIST;
	}
	
	
	protected NestedPane getCastedModel() {
		
		return (NestedPane) getModel();
	}	


	
}
