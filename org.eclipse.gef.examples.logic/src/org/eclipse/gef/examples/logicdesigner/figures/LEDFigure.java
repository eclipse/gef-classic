package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.swt.graphics.Image;

public class LEDFigure 
	extends NodeFigure 
{

private Label backgoundFig;
private static Image[] DIGITS = new Image[10];
private static Image LED_BG;
private Label onesFigure;
private Dimension preferredSize;
private Label tensFigure;

static {
	LED_BG = new Image(null, LEDFigure.class.getResourceAsStream("icons/ledbg.gif"));  //$NON-NLS-1$
	DIGITS[0] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led0.gif"));//$NON-NLS-1$
	DIGITS[1] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led1.gif"));//$NON-NLS-1$
	DIGITS[2] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led2.gif"));//$NON-NLS-1$
	DIGITS[3] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led3.gif"));//$NON-NLS-1$
	DIGITS[4] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led4.gif"));//$NON-NLS-1$
	DIGITS[5] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led5.gif"));//$NON-NLS-1$
	DIGITS[6] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led6.gif"));//$NON-NLS-1$
	DIGITS[7] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led7.gif"));//$NON-NLS-1$
	DIGITS[8] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led8.gif"));//$NON-NLS-1$
	DIGITS[9] = new Image(null, LEDFigure.class.getResourceAsStream("icons/led9.gif"));//$NON-NLS-1$
}

public LEDFigure() {
	XYLayout xy = new XYLayout();
	backgoundFig = new Label(getBackgroundImage());
	tensFigure	 = new Label(DIGITS[0]);
	onesFigure	 = new Label(DIGITS[0]);
	setLayoutManager(xy);
	add(backgoundFig, new Rectangle(0,0,60,48));
	add(tensFigure, new Rectangle(14,14,16,20));
	add(onesFigure, new Rectangle(29,14,16,20));
	
	getBounds().width = 60;
	getBounds().height = 48;

	FixedConnectionAnchor c;
	c = new FixedConnectionAnchor(this);
	c.offsetH=52;
	connectionAnchors.put(LED.TERMINAL_1_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=37;
	connectionAnchors.put(LED.TERMINAL_2_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=22;
	connectionAnchors.put(LED.TERMINAL_3_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=7;
	connectionAnchors.put(LED.TERMINAL_4_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=52;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_1_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=37;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_2_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=22;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_3_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH=7;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_4_OUT, c);
	outputConnectionAnchors.addElement(c);
}

protected Image getBackgroundImage() {
	return LED_BG;
}

public Dimension getPreferredSize(int w, int h) {
	return new Dimension(60,48);
}

public void setBackgroundImage( Image bgImg ){
	if(bgImg==null)
		backgoundFig.setIcon(LED_BG);
	else
		backgoundFig.setIcon(bgImg);
}

public void setValue( int val ) {
	tensFigure.setIcon(DIGITS[val/10]);
	onesFigure.setIcon(DIGITS[val%10]);
}

public String toString() {
	return "LEDFigure"; //$NON-NLS-1$
}

}