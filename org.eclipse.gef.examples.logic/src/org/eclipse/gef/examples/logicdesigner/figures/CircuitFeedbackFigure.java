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

import org.eclipse.draw2d.RectangleFigure;

public class CircuitFeedbackFigure
	extends RectangleFigure
{
	
public CircuitFeedbackFigure() {
	this.setFill(false);
	this.setXOR(true);
	setBorder(new CircuitFeedbackBorder());
}

protected boolean useLocalCoordinates(){return true;}
}
