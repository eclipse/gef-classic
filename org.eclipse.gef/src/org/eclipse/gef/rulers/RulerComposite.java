/*
 * Created on Oct 3, 2003
 */
package org.eclipse.gef.rulers;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * @author Pratik Shah
 */
public class RulerComposite
	extends Composite
{

private FigureCanvas left, right, top, bottom, editor;
private Dimension cachedEditorSize;
private boolean layingOut = false;
private boolean needToLayout = false;

public RulerComposite(Composite parent, int style) {
	super(parent, style);
}

public void addRuler(Ruler ruler, int orientation) {
	getRulerContainer(orientation).getContents().add(ruler);
}

protected FigureCanvas createRulerContainer(int orientation) {
	boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;
	FigureCanvas canvas = new FigureCanvas(this);
	canvas.setViewport(new RulerViewport(isHorizontal, 
			canvas.getLightweightSystem().getUpdateManager()));
	canvas.setContents(new Figure());
	canvas.getContents().setLayoutManager(new ToolbarLayout(!isHorizontal));
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
	return canvas;
}

protected FigureCanvas getRulerContainer(int orientation) {
	FigureCanvas result = null;
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

private void invalidateChild(FigureCanvas child, boolean validateFirst) {
	if (child != null) {
		if (validateFirst) {
			child.getLightweightSystem().getRootFigure().validate();
		}
		child.getLightweightSystem().getRootFigure().invalidateTree();
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

private boolean layoutChildIfChanged(FigureCanvas child, boolean isHorizontal) {
	boolean result = false;
	if (child != null) {
		Transposer transposer = new Transposer();
		transposer.setEnabled(isHorizontal);
		invalidateChild(child, false);
		List rulers = child.getContents().getChildren();
		int prefWidth = 0;
		int prefHeight = 0;
		for (int i = 0; i < rulers.size(); i++) {
			IFigure fig = (IFigure)rulers.get(i);
			Dimension prefSize = transposer.t(fig.getPreferredSize());
			prefWidth += prefSize.width;
			prefHeight = Math.max(prefHeight, prefSize.height);
		}
		if (prefWidth != transposer.t(new Dimension(child.getSize())).width) {
			result = true;
			layout(true);
		} else if (prefHeight != transposer.t(child.getContents().getSize()).height) {
			child.getLightweightSystem().getRootFigure().revalidate();
			child.getLightweightSystem().getUpdateManager().performUpdate();
		} else {
			child.getContents().repaint();
		}
	}
	return result;
}

public void layoutIfNecessary() {
	boolean b = layoutChildIfChanged(left, false) || layoutChildIfChanged(right, false) 
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
			: left.computeSize(SWT.DEFAULT, editorCache.y - hBarSize).x;
	rightWidth = right == null ? 0 
			: right.computeSize(SWT.DEFAULT, editorCache.y - hBarSize).x;
	topHeight = top == null ? 0 
			: top.computeSize(editorCache.x - vBarSize, SWT.DEFAULT).y;
	bottomHeight = bottom == null ? 0 
			: bottom.computeSize(editorCache.x - vBarSize, SWT.DEFAULT).y;

	size = getSize();
	// editorSize can go into the negative if the RulerComposite is not big enough to fit
	// all the rulers.  However, that is not a problem because editor.setSize(int, int)
	// checks to ensure that the size is at least 0, 0.
	editorSize = new Point(size.x - (leftWidth + rightWidth), 
	                       size.y - (topHeight + bottomHeight));
	if (!editorSize.equals(editorCache)) {
		editor.setSize(editorSize);
		invalidateChildren(true);
		relayout(editorSize);
	} else {
		editor.setLocation(leftWidth, topHeight);
		if (left != null) {
			left.setBounds(0, topHeight, leftWidth, editorSize.y - hBarSize);
		}
		if (right != null) {
			right.setBounds(leftWidth + editorSize.x, topHeight, 
					rightWidth, editorSize.y - hBarSize);
		}
		if (top != null) {
			top.setBounds(leftWidth, 0, editorSize.x - vBarSize, topHeight);
		}
		if (bottom != null) {
			bottom.setBounds(leftWidth, topHeight + editorSize.y, 
					editorSize.x - vBarSize, bottomHeight);
		}
	}
}

//NOTE: This method should only be invoked once.
public void setEditor(FigureCanvas ed) {
	editor = ed;

	if (left != null) {
		left.getViewport().setVerticalRangeModel(
				ed.getViewport().getVerticalRangeModel());
	}
	if (right != null) {
		right.getViewport().setVerticalRangeModel(
				ed.getViewport().getVerticalRangeModel());
	}
	if (top != null) {
		top.getViewport().setHorizontalRangeModel(
				ed.getViewport().getHorizontalRangeModel());
	}
	if (bottom != null) {		
		bottom.getViewport().setHorizontalRangeModel(
				ed.getViewport().getHorizontalRangeModel());
	}

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			layout(false);
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

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			layout(true);
		}
	});

	cachedEditorSize = editor.getContents().getSize();
	editor.getContents().addFigureListener(new FigureListener() {
		public void figureMoved(IFigure source) {
			if (layingOut) {
				cachedEditorSize = editor.getContents().getSize();
				return;
			}
			Dimension size = source.getSize();
			if (!size.equals(cachedEditorSize)) {
				if (needToLayout) {
					layout(false);
				} else {
					layoutIfNecessary();
				}
				cachedEditorSize = size;
			}
		}
	});
}

// NOTE: This method should only be invoked once.
public void setZoomManager(ZoomManager manager) {
	manager.addZoomListener(new ZoomListener() {
		public void zoomChanged(double zoom) {
			layoutIfNecessary();
		}
	});
}

public class RulerViewport extends Viewport {
	private Transposer transposer;
	private UpdateManager updateManager;
	public RulerViewport(boolean isHorizontal, UpdateManager manager) {
		super(true);
		this.updateManager = manager;
		transposer = new Transposer();
		transposer.setEnabled(isHorizontal);
		setLayoutManager(null);
	}
	public Dimension getPreferredSize(int wHint, int hHint) {
		int w = 0, h = 0;
		if (getBorder() != null) {
			Insets insets = getBorder().getInsets(this);
			w = insets.getWidth();
			h = insets.getHeight();
		}
		return getContents().getPreferredSize(wHint - w, hHint - h).getExpanded(w, h);
	}
	protected void readjustScrollBars() {
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof RangeModel) {
			repaint();
			RangeModel rModel = (RangeModel)event.getSource();
			Rectangle contentBounds = Rectangle.SINGLETON;
			contentBounds.y = rModel.getMinimum();
			contentBounds.x = 0;
			contentBounds.height = rModel.getMaximum() - rModel.getMinimum();
			contentBounds.width = getContents().getBounds().width;
			contentBounds = transposer.t(contentBounds)
					.translate(getClientArea().x, getClientArea().y);
			if (!getContents().getBounds().equals(contentBounds)) {
				getContents().setBounds(contentBounds);
				getContents().revalidate();
			}
			/*
			 * Immediately repaint or resize the ruler only if this method is not invoked
			 * when the RulerComposite is laying out, i.e., only if scrolling has occurred.  
			 * Performing update when layout is occuring causes an undesired paint which
			 * is quite visible when the width of a vertical ruler is changing.  To see
			 * this effect, do the following:
			 * 1) Remove the if statement below (i.e., this method would always call 
			 *    performUpdate()). 
			 * 2) Make sure the editor has no scroll bars. 
			 * 3) Drag and increase the vertical size of the RulerComposite until the 
			 *    vertical ruler is large enough to show 3-digit numbers.  You will see
			 *    this effect as soon as the vertical ruler increases its width to show
			 *    3-digit numbers.
			 */
			if (!layingOut) {
				updateManager.performUpdate();
			}
		}
	}
}

}