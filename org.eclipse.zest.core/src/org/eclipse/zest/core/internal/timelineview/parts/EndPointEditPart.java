package org.eclipse.mylar.zest.core.internal.timelineview.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class EndPointEditPart extends AbstractGraphicalEditPart {

	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		RectangleFigure rf = new RectangleFigure();
		rf.setBackgroundColor( ColorConstants.black );
		rf.setForegroundColor( ColorConstants.black );
		rf.setSize(100,100);
		return new RectangleFigure();
	}

	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		

	}

}
