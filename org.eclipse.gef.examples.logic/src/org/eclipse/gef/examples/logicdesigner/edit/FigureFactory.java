package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;

import org.eclipse.gef.examples.logicdesigner.figures.*;
import org.eclipse.gef.examples.logicdesigner.model.*;

public class FigureFactory {

public static PolylineConnection createNewBendableWire(Wire wire){
	PolylineConnection conn = new PolylineConnection();
	conn.setSourceDecoration(null);
	conn.setTargetDecoration(null);
	return conn;
}

public static PolylineConnection createNewWire(Wire wire){

	PolylineConnection conn = new PolylineConnection();
	PolygonDecoration arrow;
	
	if (wire == null || wire.getSource() instanceof SimpleOutput)
		arrow = null;
	else {
		arrow = new PolygonDecoration();
		arrow.setTemplate(arrow.INVERTED_TRIANGLE_TIP);
		arrow.setScale(5,2.5);
	}
	conn.setSourceDecoration(arrow);
	
	if (wire == null || wire.getTarget() instanceof SimpleOutput)
		arrow = null;
	else {
		arrow = new PolygonDecoration();
		arrow.setTemplate(arrow.INVERTED_TRIANGLE_TIP);
		arrow.setScale(5,2.5);
	}
	conn.setTargetDecoration(arrow);
	return conn;
}

public static IFigure createNewLED(){
	return new LEDFigure();
}

public static IFigure createNewCircuit(){
	CircuitFigure f = new CircuitFigure();
	return f;
}

}