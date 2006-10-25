/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * A simple edit part that displays a rectangle representing a node.
 * @author Del Myers
 *
 */
public class OverviewNodeEditPart extends AbstractGraphicalEditPart implements FigureListener, PropertyChangeListener  {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Class clazz = getCastedModel().getClass();
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(proxy, args);
			}
		};
		try {
			if (getCastedModel() instanceof AbstractGraphicalEditPart) {
				Method setModel = clazz.getMethod("setModel", new Class[] {Object.class});
				Object instance = clazz.newInstance();
				setModel.invoke(instance, new Object[]{getCastedModel().getModel()});
				Method method = AbstractGraphicalEditPart.class.getDeclaredMethod("getFigure", new Class[0]);
				Object result = handler.invoke(instance, method, new Object[0]);
				return (IFigure)result;
			}
		}  catch (Throwable e) {
			//do nothing, just create a rectangle.
		}
		return new RectangleFigure();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		getCastedModel().getFigure().addFigureListener(this);
		getCastedModel().getFigure().addPropertyChangeListener(this);
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		getCastedModel().getFigure().removeFigureListener(this);
		getCastedModel().getFigure().removePropertyChangeListener(this);
		super.deactivate();
	}
	
	//@tag zest.overview.todo : the model should actually be a proxy edit part that we can listen to figure changes on, that way our figures could look exactly the same.
	private GraphicalEditPart getCastedModel() {
		return (GraphicalEditPart)getModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

		
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		Rectangle bounds = getCastedModel().getFigure().getBounds().getCopy();
		getFigure().setForegroundColor(getCastedModel().getFigure().getForegroundColor());
		getFigure().setBackgroundColor(getCastedModel().getFigure().getBackgroundColor());
		getFigure().getParent().setConstraint(getFigure(), bounds);
		getParent().refresh();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
	 */
	public void figureMoved(IFigure source) {
		refreshVisuals();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}

}
