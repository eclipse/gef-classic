/*
 * Created on Oct 3, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.eclipse.gef.examples.logicdesigner.model.*;

/**
 * @author Pratik Shah
 */
public class RulerComposite
	extends Composite
{
	
private GraphicalViewer left, right, top, bottom;
private FigureCanvas editor;
private GraphicalViewer diagramViewer;
private boolean layingOut = false;
private Font font;

public RulerComposite(Composite parent, int style) {
	super(parent, style);
}

/*
 * @TODO:Pratik    add methods to remove rulers, add them to a specific index, hide them,
 * etc.
 */

public void addRuler(Ruler ruler, int orientation) {
	getRulerContainer(orientation).setContents(ruler);
}

protected GraphicalViewer createRulerContainer(int orientation) {
	final boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;
	ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer() {
		/* (non-Javadoc)
		 * @see org.eclipse.gef.EditPartViewer#appendSelection(org.eclipse.gef.EditPart)
		 */
		public void appendSelection(EditPart editpart) {
			boolean setFocus = editpart != focusPart;
			super.appendSelection(editpart);
			if (setFocus) {
				setFocus(editpart);
			}
		}
	};
	viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
	if (diagramViewer != null) {
		viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));	
	}
	viewer.createControl(this);
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
	FigureCanvas canvas = getFigureCanvas(viewer);
	canvas.setScrollBarVisibility(FigureCanvas.NEVER);
	if (font == null) {
		FontData[] data = canvas.getFont().getFontData();
		for (int i = 0; i < data.length; i++) {
			data[i].height -= 1;
		}
		font = new Font(Display.getCurrent(), data);
	}
	canvas.setFont(font);
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

public void dispose() {
	super.dispose();
	if (font != null) {
		font.dispose();
	}
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
		doLayout();	
		layingOut = false;
	}
}

private void doLayout() {
	int leftWidth, rightWidth, topHeight, bottomHeight;
	leftWidth = left == null ? 0 
			: left.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
	rightWidth = right == null ? 0 
			: right.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
	topHeight = top == null ? 0 
			: top.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
	bottomHeight = bottom == null ? 0 
			: bottom.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

	Point size = getSize();
	Point editorSize = new Point(size.x - (leftWidth + rightWidth), 
	                       size.y - (topHeight + bottomHeight));
	if (!editor.getSize().equals(editorSize)) {
		editor.setSize(editorSize);		
	}
	Point editorLocation = new Point(leftWidth, topHeight);
	if (!editor.getLocation().equals(editorLocation)) {
		editor.setLocation(editorLocation);
	}

	int vBarWidth = 0, hBarHeight = 0;
	if (editor.getVerticalBar().getVisible()) {
		vBarWidth = editor.computeTrim(0, 0, 0, 0).width;
	}
	if (editor.getHorizontalBar().getVisible()) {
		hBarHeight = editor.computeTrim(0, 0, 0, 0).height;
	}
	
	if (left != null) {
		left.getControl().setBounds(0, topHeight, leftWidth, editorSize.y - hBarHeight);
	}
	if (right != null) {
		right.getControl().setBounds(leftWidth + editorSize.x, topHeight, 
				rightWidth, editorSize.y - hBarHeight);
	}
	if (top != null) {
		top.getControl().setBounds(leftWidth, 0, editorSize.x - vBarWidth, topHeight);
	}
	if (bottom != null) {
		bottom.getControl().setBounds(leftWidth, topHeight + editorSize.y, 
				editorSize.x - vBarWidth, bottomHeight);
	}
}

protected FigureCanvas getFigureCanvas(GraphicalViewer viewer) {
	return (FigureCanvas)viewer.getControl();
}

// NOTE: This method should only be invoked once
public void setGraphicalViewer(GraphicalViewer primaryViewer) {
	diagramViewer = primaryViewer;
	editor = getFigureCanvas(primaryViewer);

	/*
	 * @TODO:Pratik
	 * can all these graphical viewers share the same factory?
	 */
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

	// layout whenever the scrollbars are shown or hidden, and whenever the RulerComposite
	// is resized
	Listener layoutListener = new Listener() {
		public void handleEvent(Event event) {
			if (!layingOut) {
				layout(true);				
			}
		}
	};
	addListener(SWT.Resize, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Show, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Hide, layoutListener);
	editor.getVerticalBar().addListener(SWT.Show, layoutListener);
	editor.getVerticalBar().addListener(SWT.Hide, layoutListener);
}

}