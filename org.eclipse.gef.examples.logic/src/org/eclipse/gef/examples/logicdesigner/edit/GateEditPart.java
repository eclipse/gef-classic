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

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleAnchorProvider;

import org.eclipse.gef.examples.logicdesigner.figures.AndGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.GateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OrGateFigure;
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
	@Override
	protected IFigure createFigure() {
		if (getModel() == null) {
			return null;
		}
		if (getModel() instanceof OrGate) {
			return new OrGateFigure();
		}
		if (getModel() instanceof AndGate) {
			return new AndGateFigure();
		}
		if (getModel() instanceof XORGate) {
			return new XOrGateFigure();
		}

		return new GateFigure();
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == AccessibleAnchorProvider.class)
			return key.cast(new DefaultAccessibleAnchorProvider() {
				@Override
				public List<Point> getSourceAnchorLocations() {
					List<Point> list = new ArrayList<>();
					List<ConnectionAnchor> sourceAnchors = getNodeFigure().getSourceConnectionAnchors();
					sourceAnchors.forEach(anchor -> list.add(anchor.getReferencePoint().getTranslated(0, -3)));
					return list;
				}

				@Override
				public List<Object> getTargetAnchorLocations() {
					List<Object> list = new ArrayList<>();
					List<ConnectionAnchor> targetAnchors = getNodeFigure().getTargetConnectionAnchors();
					targetAnchors.forEach(anchor -> list.add(anchor.getReferencePoint()));
					return list;
				}
			});
		return super.getAdapter(key);
	}

}
