package org.eclipse.mylar.zest.core.internal.timelineview;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.mylar.zest.core.internal.timelineview.model.TimeLineDiagram;
import org.eclipse.mylar.zest.core.internal.timelineview.model.TimeTaskPoint;
import org.eclipse.mylar.zest.core.internal.timelineview.parts.EndPointEditPart;
import org.eclipse.mylar.zest.core.internal.timelineview.parts.TimeLineDiagramEditPart;


public class TimeLineEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {

		if ( model instanceof TimeTaskPoint ) {
			return new EndPointEditPart();
		}
		else if ( model instanceof TimeLineDiagram ) {
			EditPart ep = new TimeLineDiagramEditPart();
			ep.setModel( model );
			return ep;
		}
		return null;
	}

}
