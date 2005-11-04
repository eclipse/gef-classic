/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.tests;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 * 
 * 
 * @author Chris Callendar
 */
public class NestedGraphLabelProvider extends GraphLabelProvider {

	private ZestImages images = null;
	
	public NestedGraphLabelProvider() {
		super();
		images = new ZestImages();
		//foreground = new Color(null, 0, 0, 0);
		background = new Color(null, 255, 255, 255);
	}
	
	public void dispose() {
		super.dispose();
		images = null;
	}
	
	/**
	 * Gets an image depending on the object.  If the object has children
	 * then a plus image is return, otherwise a minus image is returned.
	 * @param element
	 * @return Image
	 */
    public Image getImage(Object element) {
    	return images.getImage(ZestImages.IMG_ZEST_DOT);
    }	

}
