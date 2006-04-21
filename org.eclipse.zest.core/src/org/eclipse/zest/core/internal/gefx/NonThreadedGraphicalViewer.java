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
package org.eclipse.mylar.zest.core.internal.gefx;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public abstract class NonThreadedGraphicalViewer extends ScrollingGraphicalViewer {

	
	
	/**
	 * ThreadedGraphicalViewer constructor.
	 * @param parent The composite that this viewer will be added to.
	 */
	public NonThreadedGraphicalViewer(Composite parent)  {
		super();
		
		// create the FigureCanvas
		createControl(parent);
		
		EditDomain ed = new DefaultEditDomain( null );
		ed.addViewer( this );
		setEditDomain( ed );
		hookControl();
		getControl().addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				fireControlMovedEvent( e );
			}

			public void controlResized(ControlEvent e) {
				fireControlResizedEvent( e );
			}
		});
		
		getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
			}
		});
	
		
		((Canvas)getControl()).setBackground( ColorConstants.white );
		
		// Scroll-wheel Zoom
		setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);		
	}
	
	private List controlListeners = new LinkedList();
	
	public void addControlListener( ControlListener controlListener ) {
		controlListeners.add( controlListener );
	}
	
	public boolean removeControlListener( ControlListener controlListener ) {
		return controlListeners.remove( controlListener );
	}
	
	protected void fireControlMovedEvent( ControlEvent e ) {
		for ( Iterator iterator = controlListeners.iterator(); iterator.hasNext(); ) {
			((ControlListener)iterator.next()).controlMoved( e );
		}
	}
	
	protected void fireControlResizedEvent( ControlEvent e ) {
		for ( Iterator iterator = controlListeners.iterator(); iterator.hasNext(); ) {
			((ControlListener)iterator.next()).controlResized( e );
		}
	}
	
	
	
	
	/**
	 * Does some initializing of the viewer.
	 */
	protected abstract void configureGraphicalViewer();
		
	/**
	 * Sets the contents of the viewer and configures the graphical viewer.
	 * @param model
	 */
	public void setContents(Object model) {
		this.configureGraphicalViewer();
		super.setContents(model);
	}
	
	/**
	 * Updates the contents <b>without</b> configuring the graphical viewer.
	 * Only call this if the graphical viewer has already be configured.
	 * @param model
	 */
	public void updateContents(Object model) {
		super.setContents(model);
	}
	
	/**
	 * Gets the absolute size of the canvas.
	 * @return Dimension in absolute coords
	 */
	public Dimension getCanvasSize() {
		Dimension dim = new Dimension(getFigureCanvas().getSize());
		dim.shrink(getFigureCanvas().getBorderWidth(), getFigureCanvas().getBorderWidth());
		return dim;
	}
		
	/**
	 * Gets the translated size of the canvas.
	 * @return Dimension relative
	 */
	public Dimension getTranslatedCanvasSize() {
		Dimension dim = getCanvasSize();
		//mainCanvas.getViewport().translateToRelative(dim);
		return dim;
	}
		
	/**
	 *  
	 */
	public void progressUpdated(ProgressEvent e) {
		//TODO: Make this use the display thread
	}
	
	public void progressEnded(ProgressEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void progressStarted(ProgressEvent e) {
		// TODO Auto-generated method stub
	}	

}
