package org.eclipse.draw2d.text;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The base implementation for text flow figures. A flow figure is used to render a
 * document in which elements are laid out horizontally within a "line" until that line is
 * filled. Layout continues on the next line.
 * @author hudsonr
 * @since 2.1 */
public abstract class FlowFigure
	extends Figure
{

//static final boolean SHOW_BASELINE = true;

/**
 * Constructs a new FlowFigure. */
public FlowFigure() {
	setLayoutManager(createDefaultFlowLayout());
}

/**
 * If the child is a <code>FlowFigure</code>, its FlowContext is passed to it.
 * @see org.eclipse.draw2d.IFigure#add(IFigure, Object, int) */
public void add(IFigure child, Object constraint, int index) {
	super.add(child, constraint, index);
	if (child instanceof FlowFigure) {
		FlowFigure ff = (FlowFigure) child;
		ff.setFlowContext((FlowContext)getLayoutManager());
	}
}

/**
 * 
 * Creates the default layout manager
 * @return The default layout */
protected abstract FlowFigureLayout createDefaultFlowLayout();

protected void paintFigure(Graphics g){
	super.paintFigure(g);
//	FlowBox block;
//	Rectangle r;
//	Color bgColor = g.getBackgroundColor();
//	for (int i=0; i< fragments.size(); i++){
//		block = (FlowBox)fragments.get(i);
//		r = block.getBounds();
//		if (!g.getClip(Rectangle.SINGLETON).intersects(r))
//			continue;
//		//g.fillRectangle(r);
//		if (SHOW_BASELINE){
////			g.setBackgroundColor(FigureUtilities.darker(bgColor));
////			g.fillRectangle(r.x,r.y+block.getAscent(), r.width, r.height-block.getAscent());
////			g.setBackgroundColor(bgColor);
//		}
//	}
//// 	g.drawRectangle(getBounds().getResized(-1,-1));
}

/**
 * Called after validate has occurred. This is used to update the bounds of the FlowFigure
 * to encompass its new flow boxed created during validate.
 */
public abstract void postValidate();

/**
 * FlowFigures override setBounds() to prevent translation of children. "bounds" is a
 * derived property for FlowFigures, calculated from the fragments that make up the
 * FlowFigure.
 * @see Figure#setBounds(Rectangle)
 */
public void setBounds(Rectangle r) {
	if (getBounds().equals(r))
		return;
	erase();
	bounds.x = r.x;
	bounds.y = r.y;
	bounds.width = r.width;
	bounds.height = r.height;
	fireMoved();
	repaint();
}

/**
 * Sets the flow context.
 * @param flowContext the flow context for this flow figure */
public void setFlowContext(FlowContext flowContext) {
	((FlowFigureLayout)getLayoutManager()).setFlowContext(flowContext);
}

}