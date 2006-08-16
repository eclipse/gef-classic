/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A simple box with a label.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix)) : create a simple label box for the nodes.
public class SimpleLabelBox extends Panel {
	private Label label;
	private Panel contentPane;
	public SimpleLabelBox() {
		super();
		label = new Label();
		setLayoutManager(new FreeformLayout(){
			/* (non-Javadoc)
			 * @see org.eclipse.draw2d.XYLayout#layout(org.eclipse.draw2d.IFigure)
			 */
			public void layout(IFigure parent) {
				Rectangle textbounds = (label != null) ? label.getTextBounds() : new Rectangle(0,0,0,0);
				
				Rectangle parentBounds = parent.getBounds();
				if (label != null) label.setSize(parent.getSize().width, textbounds.height);
				contentPane.setBounds(new Rectangle(parentBounds.x, parentBounds.y+textbounds.height, parent.getSize().width, parent.getSize().height-textbounds.height));
				
			}
		});
		contentPane = new Panel();
		contentPane.setBackgroundColor(ColorConstants.white);
		contentPane.setOpaque(true);
		add(label);
		add(contentPane);
		setBackgroundColor(ColorConstants.black);
		setForegroundColor(ColorConstants.black);
		setBorder(new LineBorder(ColorConstants.black));
		setOpaque(false);
	}
	
	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}
	
	/**
	 * @return the contentPane
	 */
	public Panel getContentPane() {
		return contentPane;
	}
	
	public String toString() {
		return label.getText();
	}
}
