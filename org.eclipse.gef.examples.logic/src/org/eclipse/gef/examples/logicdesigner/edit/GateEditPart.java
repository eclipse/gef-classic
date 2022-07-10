/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleAnchorProvider;

import org.eclipse.gef.examples.logicdesigner.figures.AndGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.GateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OrGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OutputFigure;
import org.eclipse.gef.examples.logicdesigner.figures.XOrGateFigure;
import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

/**
 * EditPart for holding gates in the Logic Example.
 */
public class GateEditPart extends OutputEditPart {

	/**
	 * Returns a newly created Figure of this.
	 * 
	 * @return A new Figure of this.
	 */
	protected IFigure createFigure() {
		OutputFigure figure;
		if (getModel() == null)
			return null;
		if (getModel() instanceof OrGate)
			figure = new OrGateFigure();
		else if (getModel() instanceof AndGate)
			figure = new AndGateFigure();
		else if (getModel() instanceof XORGate)
			figure = new XOrGateFigure();
		else
			figure = new GateFigure();
		return figure;
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == AccessibleAnchorProvider.class)
			return key.cast(new DefaultAccessibleAnchorProvider() {
				public List<Point> getSourceAnchorLocations() {
					List<Point> list = new ArrayList<>();
					Vector sourceAnchors = getNodeFigure()
							.getSourceConnectionAnchors();
					for (int i = 0; i < sourceAnchors.size(); i++) {
						ConnectionAnchor anchor = (ConnectionAnchor) sourceAnchors
								.get(i);
						list.add(anchor.getReferencePoint().getTranslated(0,
								-3));
					}
					return list;
				}

				public List<Object> getTargetAnchorLocations() {
					List<Object> list = new ArrayList<>();
					Vector targetAnchors = getNodeFigure()
							.getTargetConnectionAnchors();
					for (int i = 0; i < targetAnchors.size(); i++) {
						ConnectionAnchor anchor = (ConnectionAnchor) targetAnchors
								.get(i);
						list.add(anchor.getReferencePoint());
					}
					return list;
				}
			});
		return super.getAdapter(key);
	}

}
