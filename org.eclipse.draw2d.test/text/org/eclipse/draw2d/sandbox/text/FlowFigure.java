package org.eclipse.draw2d.sandbox.text;

import java.util.*;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class FlowFigure
	extends Figure
{

protected static boolean SHOW_BASELINE = true;
protected Vector fragments = new Vector(1);
protected CompositeBox boxInfo;
protected int viewerWidth;

public FlowFigure(){
	setLayoutManager(new BlockFlowLayout());
}

public void add(IFigure figure, Object constraint, int index){
	super.add(figure, constraint, index);
	if (figure instanceof FlowFigure){
		FlowFigure ff = (FlowFigure) figure;
		ff.setFlowContext((IFlowContext)getLayoutManager());
	}
}

public boolean containsPoint(int x, int y){
	if (!super.containsPoint(x,y)) return false;
	FragmentIterator iter = new FragmentIterator(this);
	while (iter.hasNext()){
		if (iter.nextFragment().getBounds().contains(x,y))
			return true;
	}
	return false;
}

public Vector getFragments(){
	return fragments;
}

protected void paintFigure(Graphics g){
	BlockInfo block;
	Rectangle r;
	Color bgColor = g.getBackgroundColor();
	for (int i=0; i< fragments.size(); i++){
		block = (BlockInfo)fragments.get(i);
		r = block.getBounds();
		if (!g.getClip(Rectangle.SINGLETON).intersects(r)) continue;
		g.fillRectangle(r);
		if (SHOW_BASELINE){
			g.setBackgroundColor(FigureUtilities.darker(bgColor));
			g.fillRectangle(r.x,r.y+block.getAscent(), r.width, r.height-block.getAscent());
			g.setBackgroundColor(bgColor);
		}
		//g.drawRectangle(r.x,r.y,r.width-1, r.height-1);
		//FigureUtilities.paintEtchedBorder(g,r);
	}
}

public void postValidate(){
	List v = getFragments();
	BlockInfo block = (BlockInfo)v.get(0);
	Rectangle newBounds = new Rectangle(block.getBounds());
	for (int i=1; i<v.size(); i++)
		newBounds.union(((BlockInfo)v.get(i)).getBounds());
	setBounds(newBounds);
	v = getChildren();
	for (int i=0; i<v.size(); i++){
		((FlowFigure)v.get(i)).postValidate();
	}
}

/**
 * FlowFigures over-ride setBounds() to prevent translation of children.
 * "bounds" is a derived property for FlowFigures, calculated from the
 * fragments that make up the FlowFigure.
 */
public void setBounds(Rectangle r){
	erase();
	bounds.x = r.x;
	bounds.y = r.y;
	bounds.width = r.width;
	bounds.height= r.height;
	repaint();
}

public void setFlowContext(IFlowContext flowContext){
	((TextFlowLayout)getLayoutManager()).setFlowContext(flowContext);
}

static public class FragmentIterator {
	protected /*List*/ List list;
	protected int index;
	public FragmentIterator(FlowFigure f){
		list=f.getFragments();
		index=list.size();
	}
	public BlockInfo nextFragment(){
		return (BlockInfo)list.get(--index);
	}
	public boolean hasNext(){
		return index > 0;
	}
};


}