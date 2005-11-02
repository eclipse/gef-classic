package org.eclipse.mylar.zest.core.internal.timelineview.model;

import java.util.ArrayList;
import java.util.List;

public class TimeLineDiagram {
	
	public List getEndPoints() {
		ArrayList al = new ArrayList();
		al.add( new TimeTaskPoint() );
		
		return al;
	}

}
