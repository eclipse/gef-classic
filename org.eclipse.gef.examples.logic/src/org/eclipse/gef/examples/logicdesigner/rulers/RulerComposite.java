/*
 * Created on Oct 3, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Transposer;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

/**
 * @author Pratik Shah
 */
public class RulerComposite
	extends Composite
{

private Dimension cachedEditorSize;
private GraphicalViewer left, right, top, bottom;
private FigureCanvas editor;
private GraphicalViewer diagramViewer;
private boolean layingOut = false, needToLayout = false;

public RulerComposite(Composite parent, int style) {
	super(parent, style);
}

public void addRuler(Ruler ruler, int orientation) {
	getRulerContainer(orientation).setContents(ruler);
}

protected GraphicalViewer createRulerContainer(int orientation) {
	boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;
	ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
	viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
	if (diagramViewer != null) {
		viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));	
	}
	viewer.createControl(this);
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
	FigureCanvas canvas = getFigureCanvas(viewer);
	canvas.setScrollBarVisibility(FigureCanvas.NEVER);
	if (editor != null) {
		if (isHorizontal) {
			canvas.getViewport().setHorizontalRangeModel(
					editor.getViewport().getHorizontalRangeModel());
		} else {
			canvas.getViewport().setVerticalRangeModel(
					editor.getViewport().getVerticalRangeModel());
		}
	}
	return viewer;
}

public GraphicalViewer getRulerContainer(int orientation) {
	GraphicalViewer result = null;
	switch(orientation) {
		case PositionConstants.NORTH:
			if (top == null) {
				top = createRulerContainer(orientation);
			}
			result = top;
			break;
		case PositionConstants.SOUTH:
			if (bottom == null) {
				bottom = createRulerContainer(orientation);
			}
			result = bottom;
			break;
		case PositionConstants.EAST:
			if (right == null) {
				right = createRulerContainer(orientation);
			}
			result = right;
			break;
		case PositionConstants.WEST:
			if (left == null) {
				left = createRulerContainer(orientation);
			}
			result = left;
	}
	return result;
}

private void invalidateChild(GraphicalViewer child, boolean validateFirst) {
	if (child != null) {
		IFigure root = getFigureCanvas(child).getLightweightSystem().getRootFigure();
		if (validateFirst) {
			root.validate();
		}
		root.invalidateTree();
	}
}

private void invalidateChildren(boolean validateFirst) {
	invalidateChild(left, validateFirst);
	invalidateChild(right, validateFirst);
	invalidateChild(top, validateFirst);
	invalidateChild(bottom, validateFirst);
}

/* (non-Javadoc)
 * @see org.eclipse.swt.widgets.Composite#layout(boolean)
 */
public void layout(boolean change) {
	if (!layingOut) {
		checkWidget();
		layingOut = true;
		invalidateChildren(true);	
		relayout(editor.getSize());	
		needToLayout = false;
		layingOut = false;
	}
}

private boolean layoutChildIfChanged(GraphicalViewer viewer, boolean isHorizontal) {
	boolean result = false;
	if (viewer != null) {
		FigureCanvas child = getFigureCanvas(viewer);
		if (child.getContents() != null) {
			Transposer transposer = new Transposer();
			transposer.setEnabled(isHorizontal);
			invalidateChild(viewer, false);
			Dimension prefSize = transposer.t(child.getContents().getPreferredSize());
			if (prefSize.width != transposer.t(new Dimension(child.getSize())).width) {
				result = true;
				layout(true);
			} else if (prefSize.height 
					!= transposer.t(child.getContents().getSize()).height) {
				child.getLightweightSystem().getRootFigure().revalidate();
				child.getLightweightSystem().getUpdateManager().performUpdate();
			} else {
				child.getContents().repaint();
			}
		}
	}
	return result;
}

public boolean layoutIfNecessary() {
	return layoutChildIfChanged(left, false) || layoutChildIfChanged(right, false) 
			|| layoutChildIfChanged(top, true) || layoutChildIfChanged(bottom, true);
}

private void relayout(Point editorCache) {
	Point size, editorSize;
	int leftWidth, rightWidth, topHeight, bottomHeight;

	int vBarSize = 0, hBarSize = 0;
	if (editor.getVerticalBar().isVisible()) {
		vBarSize = editor.computeTrim(0, 0, 0, 0).width;
	}
	if (editor.getHorizontalBar().isVisible()) {
		hBarSize = editor.computeTrim(0, 0, 0, 0).height;
	}
	leftWidth = left == null ? 0 
			: left.getControl().computeSize(SWT.DEFAULT, editorCache.y - hBarSize).x;
	rightWidth = right == null ? 0 
			: right.getControl().computeSize(SWT.DEFAULT, editorCache.y - hBarSize).x;
	topHeight = top == null ? 0 
			: top.getControl().computeSize(editorCache.x - vBarSize, SWT.DEFAULT).y;
	bottomHeight = bottom == null ? 0 
			: bottom.getControl().computeSize(editorCache.x - vBarSize, SWT.DEFAULT).y;

	size = getSize();
	// editorSize can go into the negative if the RulerComposite is not big enough to fit
	// all the rulers.  However, that is not a problem because editor.setSize(int, int)
	// checks to ensure that the size is at least 0, 0.
	editorSize = new Point(size.x - (leftWidth + rightWidth), 
	                       size.y - (topHeight + bottomHeight));
	if (!editorSize.equals(editorCache)) {
		editor.setSize(editorSize);
		/*
		 * @TODO:Pratik
		 * This is a hack.  Setting the editorSize (line above) is, for some reason, not
		 * causing the scrollbars to disappear in the case where the editor has just
		 * been opened (even though the size being set is big enough so that the scrollbars 
		 * are not needed).  Calling setBounds(editor.getLocation().x, 
		 * editor.getLocation().y,editorSize.x,editorSize.y) does not do it either.  
		 * However, calling setLocation(editor.getLocation()) (line below) does it.  What 
		 * a piece of crap.
		 */
		editor.setLocation(editor.getLocation());
		invalidateChildren(true);
		relayout(editorSize);
	} else {
		editor.setLocation(leftWidth, topHeight);
		if (left != null) {
			left.getControl().setBounds(0, topHeight, leftWidth, editorSize.y - hBarSize);
		}
		if (right != null) {
			right.getControl().setBounds(leftWidth + editorSize.x, topHeight, 
					rightWidth, editorSize.y - hBarSize);
		}
		if (top != null) {
			top.getControl().setBounds(leftWidth, 0, editorSize.x - vBarSize, topHeight);
		}
		if (bottom != null) {
			bottom.getControl().setBounds(leftWidth, topHeight + editorSize.y, 
					editorSize.x - vBarSize, bottomHeight);
		}
	}
}

protected FigureCanvas getFigureCanvas(GraphicalViewer viewer) {
	return (FigureCanvas)viewer.getControl();
}

// NOTE: This method should only be invoked once
public void setGraphicalViewer(GraphicalViewer primaryViewer) {
	diagramViewer = primaryViewer;
	editor = getFigureCanvas(primaryViewer);

	if (left != null) {
		left.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(left).getViewport().setVerticalRangeModel(
				editor.getViewport().getVerticalRangeModel());
	}
	if (right != null) {
		right.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(right).getViewport().setVerticalRangeModel(
				editor.getViewport().getVerticalRangeModel());
	}
	if (top != null) {
		top.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(top).getViewport().setHorizontalRangeModel(
				editor.getViewport().getHorizontalRangeModel());
	}
	if (bottom != null) {
		bottom.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(bottom).getViewport().setHorizontalRangeModel(
				editor.getViewport().getHorizontalRangeModel());
	}

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			layout(true);
		}
	});
	
	// layout whenever the scrollbars are shown or hidden
	Listener scrollBarListener = new Listener() {
		public void handleEvent(Event event) {
			needToLayout = true;
		}
	};
	editor.getHorizontalBar().addListener(SWT.Show, scrollBarListener);
	editor.getHorizontalBar().addListener(SWT.Hide, scrollBarListener);
	editor.getVerticalBar().addListener(SWT.Show, scrollBarListener);
	editor.getVerticalBar().addListener(SWT.Hide, scrollBarListener);

	cachedEditorSize = editor.getContents().getSize();
	PropertyChangeListener rangeModelListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (property.equals(RangeModel.PROPERTY_MAXIMUM)
					|| property.equals(RangeModel.PROPERTY_MINIMUM)) {
				if (!layingOut) {
					if (needToLayout) {
						layout(true);
					} else if (!editor.getContents().getSize().equals(cachedEditorSize)) {
						layoutIfNecessary();
					}
				}
				cachedEditorSize = editor.getContents().getSize();
			}
		}
	};
	editor.getViewport().getHorizontalRangeModel()
			.addPropertyChangeListener(rangeModelListener);
	editor.getViewport().getVerticalRangeModel()
			.addPropertyChangeListener(rangeModelListener);

	ZoomManager manager = 
			(ZoomManager)diagramViewer.getProperty(ZoomManager.class.toString());
	if (manager != null) {
		manager.addZoomListener(new ZoomListener() {
			public void zoomChanged(double zoom) {
				layoutIfNecessary();
			}
		});		
	}
}

}