package org.eclipse.draw2d.text;

import java.util.*;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public abstract class FlowFigure
	extends Figure
{

static final boolean SHOW_BASELINE = true;

public FlowFigure() {
	setLayoutManager(createDefaultFlowLayout());
}

public void add(IFigure figure, Object constraint, int index){
	super.add(figure, constraint, index);
	if (figure instanceof FlowFigure){
		FlowFigure ff = (FlowFigure) figure;
		ff.setFlowContext((FlowContext)getLayoutManager());
	}
}

protected abstract TextFlowLayout createDefaultFlowLayout();

protected void paintFigure(Graphics g){
//	FlowBox block;
//	Rectangle r;
	Color bgColor = g.getBackgroundColor();
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

public abstract void postValidate();

/**
 * FlowFigures override setBounds() to prevent translation of children. "bounds" is a
 * derived property for FlowFigures, calculated from the fragments that make up the
 * FlowFigure.
 */
public void setBounds(Rectangle r) {
	if (getBounds().equals(r))
		return;
	erase();
	bounds.x = r.x;
	bounds.y = r.y;
	bounds.width = r.width;
	bounds.height= r.height;
	repaint();
}

public void setFlowContext(FlowContext flowContext){
	((TextFlowLayout)getLayoutManager()).setFlowContext(flowContext);
}

}