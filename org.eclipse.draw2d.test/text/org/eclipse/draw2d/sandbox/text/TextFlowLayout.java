package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public abstract class TextFlowLayout
	extends AbstractLayout
{

protected boolean invalid = true;

/**
 * The flow context in which this LayoutManager exists.
 */
protected IFlowContext context;

/**
 * The figure passed by layout(Figure) is held for convenience.
 */
protected FlowFigure flowFigure;

/**
 * Currently FlowLayouts do not calculate preferred size because they cannot
 * do so without the block width.  Even with width, a layout would be required.
 */
public Dimension calculatePreferredSize(IFigure f){return new Dimension(1,1);}

public void invalidate(){
	invalid = true;
	super.invalidate();
}

public boolean isContextChanged(){
	if (invalid) return true;
	if (context != null) return context.isContextChanged();
	return false;
}

public final void layout(IFigure figure){
	flowFigure = (FlowFigure) figure;
	layout ();
	invalid = false;
}

protected abstract void layout();

public void setFlowContext(IFlowContext flowContext){
	context = flowContext;
}

}
