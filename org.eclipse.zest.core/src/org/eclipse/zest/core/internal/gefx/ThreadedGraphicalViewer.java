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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.mylar.zest.core.DebugPrint;
import org.eclipse.mylar.zest.core.internal.viewers.Graph;
import org.eclipse.mylar.zest.layouts.Stoppable;
import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.mylar.zest.layouts.progress.ProgressListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A Graphical Viewer Imple which  is thread safe.  The update manager syncs with the default thread
 * before updating anything. This viewer also allows other threads to be added to it.  The threads are added as
 * "Stoppable" Objects. The stoppables are started when they are added, and when the view
 * is disposed they are removed.
 * 
 * @author Ian Bull
 *
 */
public abstract class ThreadedGraphicalViewer extends GraphicalViewerImpl implements ProgressListener  {

	/** Holds all the threads added to this viewer */
	private Map listOfThreads = null; 

	private Graph mainCanvas = null;
	private EditDomain ed = null;

	/**
	 * ThreadedGraphicalViewer constructor.
	 * @param parent The composite that this viewer will be added to.
	 */
	public ThreadedGraphicalViewer(Composite parent)  {
		super();
		listOfThreads = new LinkedHashMap();
		
		updateManager = getLightweightSystem().getUpdateManager();
		// Creates a new graph (a FigureCanvas)
		mainCanvas = new Graph(parent);
		mainCanvas.setLayout(new FillLayout());
		setControl(mainCanvas);
		
		ed = new DefaultEditDomain( null );
		ed.addViewer( this );
		//ed.setDefaultTool(new ZoomableSelectionTool());
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
				removeAllThreads();
			}
			
		});
		
		// TODO is this needed?  A focus listener is added inside hookControl() above...
		getControl().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				//handleFocusGained(e);
			}
			public void focusLost(FocusEvent e) {
			}
		});
	
		((Canvas)getControl()).setBackground( ColorConstants.white );
		
		FreqUpdater updater = new FreqUpdater();
		updater.addProgressListener( new ProgressListener() {

			public void progressStarted(ProgressEvent e) {
				// TODO Auto-generated method stub
				
				
			}

			public void progressUpdated(ProgressEvent e) {
				// TODO Auto-generated method stub
				IFigure rootFigure = null;
				try {
					 rootFigure = ThreadedGraphicalViewer.this.getLightweightSystem().getRootFigure();
				}
				catch ( Exception exception ) {
					System.out.println("Exception");
				}
				
				if (updateManager != null && rootFigure != null ) 
					((MyUpdateManager)updateManager)._addInvalidFigure( rootFigure  );
				else 
					System.out.println(updateManager + " : " + rootFigure );

				
			}

			public void progressEnded(ProgressEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		addThread( updater );
		

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
	 * Adds a new thread to this viewer and starts it.
	 * @param r
	 */
	public void addThread( Stoppable r ) {
		Thread thread = new Thread( r );
		r.addProgressListener( this );
		listOfThreads.put( r, thread );
		thread.setPriority( java.lang.Thread.MAX_PRIORITY );
		thread.start();
	}
	
	/**
	 * Gets the absolute size of the canvas.
	 * @return Dimension in absolute coords
	 */
	public Dimension getCanvasSize() {
		return new Dimension( getCanvas().getSize() );
	}
	
	/**
	 * Gets the translated size of the canvas.
	 * @return Dimension relative
	 */
	public Dimension getTranslatedCanvasSize() {
		Dimension dim = getCanvasSize();
		//getCanvas().get
		//mainCanvas.getViewport().translateToRelative(dim);
		return dim;
	}
	
	public Graph getCanvas() {
		return mainCanvas;
	}
	
	/**
	 * Removes and stop the thread
	 * @param r
	 */
	public void removeThread( Stoppable r ) {
		Thread t = (Thread) listOfThreads.get( r );
		r.stop();
		
		try {
			t.join( 1000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if ( t.isAlive() ) {
			DebugPrint.println("Thread Still Alive!");
			//TODO: Remove thread stop and make sure this is done properly
			StackTraceElement[] elements = t.getStackTrace();
			String stackTrace = "";
			for (int i = 0; i < elements.length; i++) {
				stackTrace += elements[i].toString() + "\n";
			}
			throw new RuntimeException("Thread didn't stop, stack was: " + stackTrace);
//			t.stop();
		}
		listOfThreads.remove( r );
	}
	
	
	public void removeAllThreads () {
		Set keySet = listOfThreads.keySet();
		Stoppable[] keySetArray = (Stoppable[])keySet.toArray(new Stoppable[keySet.size()]);
		
		for ( int i = 0; i < keySetArray.length; i++) {
			removeThread( keySetArray[i] );
			
		}
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
	
	protected LightweightSystem createLightweightSystem() {

		LightweightSystem lws = new MyLightWeightSystem();
		lws.setUpdateManager( new MyUpdateManager() );
		

		
		return  lws;
	}

	
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
			//DebugPrint.println("My LWS Paint!");
			if ( getControl().isVisible() )
				Display.getDefault().syncExec(new DisplaySynchronize( gc ) );
			else 
				DebugPrint.println("Not Visible Now");
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
		
		public synchronized void performUpdate() {
			// TODO Auto-generated method stub
//			DebugPrint.println("Perform update called");
			super.performUpdate();
		}
		
		public synchronized void performUpdate(Rectangle exposed) {
			// TODO Auto-generated method stub
//			System.out.println("Perform Updated");
			super.performUpdate(exposed);
		}
		
		public void queueWork( ) {
			Display.getDefault().asyncExec(new DisplaySynchronize() );
		}
		
		public void _queueWork() {
		   super.queueWork();
		}
		
		public synchronized void _addInvalidFigure( IFigure f ) {
			super.addInvalidFigure( f );
		}
		
		public synchronized void addInvalidFigure(IFigure f) {
			// TODO Auto-generated method stub
			//super.addInvalidFigure(f);
			// do nothing
		}
		
	}
	
	UpdateManager updateManager = null;

	
	public class FreqUpdater implements Stoppable {

		//TODO: Why is this here?  It doesn't do anything?
		ArrayList listOfProgressListeners = new ArrayList();

		public void addProgressListener(ProgressListener listener) {
			listOfProgressListeners.add( listener );
		}

		boolean keepGoing = true;

		public void stop() {
			keepGoing = false;
		}

		public void run() {
			while (keepGoing) {
				if (listOfProgressListeners != null) {
					//TODO: This is not thread safe
					for ( int i = 0; i < listOfProgressListeners.size(); i++ ) {
						((ProgressListener) listOfProgressListeners.get(i)).progressUpdated( null );
					}
				}
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
