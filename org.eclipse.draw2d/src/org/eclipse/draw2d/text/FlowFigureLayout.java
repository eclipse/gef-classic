package org.eclipse.draw2d.text;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * A LayoutManager for use with FlowFigure.
 * @author hudsonr
 * @since 2.1 */
public abstract class FlowFigureLayout
	extends AbstractLayout
{

/**
 * <code>true</code> if the context has changed, and a layout is needed.
 */
protected boolean invalid = true;

/**
 * The flow context in which this LayoutManager exists.
 */
protected FlowContext context;

/**
 * The figure passed by layout(Figure) is held for convenience.
 */
private final FlowFigure flowFigure;

/**
 * Constructs a new FlowFigureLayout with the given FlowFigure.
 * @param flowfigure the FlowFigure */
protected FlowFigureLayout(FlowFigure flowfigure) {
	this.flowFigure = flowfigure;
}

/**
 * TextFlowLayouts do not calculate a preferred size because it is too expensive.
 * {@link FlowPage} will actually layout itself in order to calculate preferredSize.
 * @see AbstractLayout#calculatePreferredSize(IFigure)
 */
public Dimension calculatePreferredSize(IFigure f) {
	return null;
}

/** * @return the FlowFigure */
protected FlowFigure getFlowFigure() {
	return flowFigure;
}

/**
 * Marks this layout as invalid.
 * @see org.eclipse.draw2d.LayoutManager#invalidate() */
public void invalidate() {
	invalid = true;
	super.invalidate();
}

/** * @see org.eclipse.draw2d.LayoutManager#layout(IFigure) */
public final void layout(IFigure figure) {
	layout ();
	invalid = false;
}

/**
 * Called during {@link #layout(IFigure). The {@link  #invalid} flag is reset after this
 * method is called.
 */
protected abstract void layout();

/**
 * Sets the context for this layout manager.
 * @param flowContext the context of this layout */
public void setFlowContext(FlowContext flowContext) {
	context = flowContext;
}

}
