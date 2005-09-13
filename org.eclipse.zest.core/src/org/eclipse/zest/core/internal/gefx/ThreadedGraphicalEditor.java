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

import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.mylar.zest.layouts.progress.ProgressListener;

/**
 * 
 * @author Ian Bull
 *
 */
public abstract class ThreadedGraphicalEditor extends GraphicalEditor implements ProgressListener {	
	
	
	class MyScrollingGraphicalViewer extends ScrollingGraphicalViewer {

		
		class MyLightWeightSystem extends LightweightSystem {
			
			class DisplaySynchronize implements Runnable {
					GC _gc = null;
					public DisplaySynchronize( GC gc ) {
						_gc = gc;
					}
		
					public void run() {
						MyLightWeightSystem.this._paint( _gc );
					}	
			}
		
			public void paint(GC gc) {
				Display.getDefault().syncExec(new DisplaySynchronize( gc ) );
			}
			
			private void _paint( GC gc ) {
				super.paint( gc );
			}
		}
		
		class MyUpdateManager extends DeferredUpdateManager {
			class DisplaySynchronize implements Runnable {
	
				public void run() {
					MyUpdateManager.this._queueWork(  );
				}

			}
			
			public void queueWork( ) {
				//Display.getDefault().asyncExec(new DisplaySynchronize() );
				Display.getDefault().syncExec(new DisplaySynchronize() );
			}
			
			public void _queueWork() {
			   super.queueWork();
			}
		}
		
		
		protected LightweightSystem createLightweightSystem() {
			LightweightSystem lws = new MyLightWeightSystem();
			lws.setUpdateManager( new MyUpdateManager() );
			return  lws;
		}
		
	}

	public void progressUpdated(ProgressEvent e) {
		//TODO: Make this use the display thread
	}
	
	public Dimension getCanvasSize() {
		//TODO: I don't think this really gets the canvas size
		Point p =  this.getGraphicalViewer().getControl().getSize();
		return new Dimension(p.x, p.y );
	}
	
	/**
	 * Creates the GraphicalViewer on the specified <code>Composite</code>.
	 * @param parent the parent composite
	 */
	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new MyScrollingGraphicalViewer();	
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}
	
}
