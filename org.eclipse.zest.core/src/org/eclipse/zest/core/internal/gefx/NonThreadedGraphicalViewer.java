package org.eclipse.mylar.zest.core.internal.gefx;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.mylar.zest.core.internal.viewers.Graph;
import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public abstract class NonThreadedGraphicalViewer extends GraphicalViewerImpl {

	
	private Graph mainCanvas = null;
	private EditDomain ed = null;

	/**
	 * ThreadedGraphicalViewer constructor.
	 * @param parent The composite that this viewer will be added to.
	 */
	public NonThreadedGraphicalViewer(Composite parent)  {
		super();
		
		
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
		return mainCanvas.getViewport().getSize().getCopy();
	}
	
	/**
	 * Gets the translated size of the canvas.
	 * @return Dimension relative
	 */
	public Dimension getTranslatedCanvasSize() {
		Dimension dim = getCanvasSize();
		mainCanvas.getViewport().translateToRelative(dim);
		return dim;
	}
	
	public Graph getCanvas() {
		return mainCanvas;
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
