/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.examples.logicdesigner.model.SimpleOutput;
import org.eclipse.gef.examples.logicdesigner.model.Wire;

public class FigureFactory {

public static PolylineConnection createNewBendableWire(Wire wire){
	PolylineConnection conn = new PolylineConnection();
	conn.addRoutingListener(RoutingAnimator.getDefault());
//	conn.setSourceDecoration(new PolygonDecoration());
//	conn.setTargetDecoration(new PolylineDecoration());
	return conn;
}

public static PolylineConnection createNewWire(Wire wire){

	PolylineConnection conn = new PolylineConnection();
	conn.addRoutingListener(RoutingAnimator.getDefault());
	PolygonDecoration arrow;
	
	if (wire == null || wire.getSource() instanceof SimpleOutput)
		arrow = null;
	else {
		arrow = new PolygonDecoration();
		arrow.setTemplate(PolygonDecoration.INVERTED_TRIANGLE_TIP);
		arrow.setScale(5,2.5);
	}
	conn.setSourceDecoration(arrow);
	
	if (wire == null || wire.getTarget() instanceof SimpleOutput)
		arrow = null;
	else {
		arrow = new PolygonDecoration();
		arrow.setTemplate(PolygonDecoration.INVERTED_TRIANGLE_TIP);
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
