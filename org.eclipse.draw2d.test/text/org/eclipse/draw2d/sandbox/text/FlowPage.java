package org.eclipse.draw2d.sandbox.text;
import java.util.List;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class FlowPage
	extends FlowFigure
{

public static int VALIDATIONS = 0;

private int viewerWidth;

public FlowPage(){
	setLayoutManager(new PageFlowLayout());
}

public Dimension getPreferredSize(){
	return new Dimension(1,bounds.height);
}

public void setBounds(Rectangle r){
	if (viewerWidth != r.width) invalidate();
	viewerWidth = r.width;
	super.setBounds(r);
}

public void invalidate(){
	super.invalidate();
}

public void postValidate(){
	BlockInfo myBlock = (BlockInfo)fragments.get(0);
	Rectangle r = new Rectangle(myBlock.getBounds());
	
	//Don't let the page shrink to a width less than the viewer
	r.width = Math.max(r.width, viewerWidth);
	setBounds(r);
	List v = getChildren();
	for (int i=0; i<v.size(); i++)
		((FlowFigure)v.get(i)).postValidate();
}

public void validate(){
	super.validate();
	postValidate();
	VALIDATIONS++;
}

}