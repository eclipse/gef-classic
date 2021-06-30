/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.connections;

import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.*;

/**
 * This class demonstrates the use of connections, anchors, and locators in draw2dl.
 * @author delee
 */
public class ConnectionAndAnchorExample extends AbstractExample {

	public static void main(String[] args) {
		new ConnectionAndAnchorExample().run();
	}
	
/**
 * @see AbstractExample#getContents()
 */
protected org.eclipse.draw2dl.IFigure getContents() {
	org.eclipse.draw2dl.IFigure panel = new org.eclipse.draw2dl.Figure();
	
	org.eclipse.draw2dl.Figure endPointPanel = new org.eclipse.draw2dl.Figure();
	endPointPanel.setBounds(new Rectangle(0,0,400,400));

	endPointPanel.setBorder(new org.eclipse.draw2dl.GroupBoxBorder("Midpoint Locator"));
	
	org.eclipse.draw2dl.RectangleFigure
		node1 = new org.eclipse.draw2dl.RectangleFigure(),
		node2 = new org.eclipse.draw2dl.RectangleFigure();
	node1.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.red);
	node1.setBounds(new Rectangle(40,40, 50, 30));
	node2.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.blue);
	node2.setBounds(new Rectangle(200,40, 50, 30));
	
	org.eclipse.draw2dl.PolylineConnection conn = new org.eclipse.draw2dl.PolylineConnection();
	conn.setSourceAnchor(new org.eclipse.draw2dl.ChopboxAnchor(node1));
	conn.setTargetAnchor(new org.eclipse.draw2dl.ChopboxAnchor(node2));
	conn.setTargetDecoration(new org.eclipse.draw2dl.PolygonDecoration());
	  
	org.eclipse.draw2dl.Label label = new org.eclipse.draw2dl.Label("Midpoint");
	label.setOpaque(true);
	label.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.buttonLightest);
	label.setBorder(new org.eclipse.draw2dl.LineBorder());
	conn.add(label, new MidpointLocator(conn, 0));
	//conn.setSourceDecoration(new ConnectionLabel());
	
	endPointPanel.add(node1);
	endPointPanel.add(node2);
	endPointPanel.add(conn);
	new Dragger(node1);
	new Dragger(node2);

	org.eclipse.draw2dl.Figure ellipsePanel = new org.eclipse.draw2dl.Figure();
	ellipsePanel.setBounds(new Rectangle(420,0,400,400));

	ellipsePanel.setBorder(new GroupBoxBorder("Connection endpoint Locator"));
	
	Shape
		node3 = new org.eclipse.draw2dl.RectangleFigure(),
		node4 = new RectangleFigure();
	node3.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.green);
	node3.setBounds(new Rectangle(480,40, 50, 30));
	node4.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.black);
	node4.setBounds(new Rectangle(480, 230, 50, 30));
	
	org.eclipse.draw2dl.PolylineConnection conn2 = new PolylineConnection();
	conn2.setSourceAnchor(new org.eclipse.draw2dl.ChopboxAnchor(node3));
	conn2.setTargetAnchor(new ChopboxAnchor(node4));
	conn2.setTargetDecoration(new PolygonDecoration());
	  
	org.eclipse.draw2dl.Label endPointLabel = new org.eclipse.draw2dl.Label("Endpoint");
	endPointLabel.setOpaque(true);
	endPointLabel.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.buttonLightest);
	endPointLabel.setBorder(new org.eclipse.draw2dl.LineBorder());
	conn2.add(endPointLabel, new org.eclipse.draw2dl.ConnectionEndpointLocator(conn2, true));
	
	org.eclipse.draw2dl.Label endPointLabel2 = new org.eclipse.draw2dl.Label("Endpoint");
	endPointLabel2.setOpaque(true);
	endPointLabel2.setBackgroundColor(ColorConstants.buttonLightest);
	endPointLabel2.setBorder(new org.eclipse.draw2dl.LineBorder());
	org.eclipse.draw2dl.ConnectionEndpointLocator endpointLocator =
				new ConnectionEndpointLocator(conn2,false);
	endpointLocator.setUDistance(4);
	endpointLocator.setVDistance(-3);
	conn2.add(endPointLabel2, endpointLocator);		
	
	org.eclipse.draw2dl.Label instructions =
				new Label(" Drag the shapes to see the Locators in action ");
	instructions.setBorder(new LineBorder());
	instructions.setLocation(new Point(10, 420));
	instructions.setSize(instructions.getPreferredSize());
	
	ellipsePanel.add(node3);
	ellipsePanel.add(node4);
	ellipsePanel.add(conn2);
	
	new Dragger(node3);
	new Dragger(node4);
	
	
	panel.add(endPointPanel);
	panel.add(ellipsePanel);
	panel.add(instructions);
	return panel;
}
	
	/**
	 * @see AbstractExample#hookShell()
	 */
	protected void hookShell() {
		getFigureCanvas().setSize(820,440);	
	}

	static class Dragger extends MouseMotionListener.Stub implements MouseListener {
		  public Dragger(IFigure figure){
				figure.addMouseMotionListener(this);
				figure.addMouseListener(this);
		  }
		  Point last;
		  public void mouseReleased(org.eclipse.draw2dl.MouseEvent e){}
		  public void mouseClicked(org.eclipse.draw2dl.MouseEvent e){}
		  public void mouseDoubleClicked(org.eclipse.draw2dl.MouseEvent e){}
		  public void mousePressed(org.eclipse.draw2dl.MouseEvent e){
				last = e.getLocation();
				e.consume();
		  }
		  public void mouseDragged(MouseEvent e){
				Point p = e.getLocation();
				Dimension delta = p.getDifference(last);
				last = p;
				org.eclipse.draw2dl.Figure f = ((Figure)e.getSource());
				f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
		  }
	}

}
