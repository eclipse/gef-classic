/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.draw2d.ImageUtilities;

/**
 * @author Pratik Shah
 */
public class ImageUtilitiesTest 
	extends TestCase 
{

public ImageUtilitiesTest() {
	super();
}

public ImageUtilitiesTest(String name) {
	super(name);
}

public void testImageRotation() {
	Image result1 = null, result2 = null, result3 = null;
	Image img1 = new Image(null, 80, 80);
	Image img2 = new Image(null, 801, 69);
	Image img3 = new Image(null, 501, 799);
	
	try {
		result1 = ImageUtilities.createRotatedImage(img1);
		result2 = ImageUtilities.createRotatedImage(img2);
		result3 = ImageUtilities.createRotatedImage(img3);
	} catch (Throwable error) {
		fail(error.getMessage() + "\n" + error.getStackTrace()); //$NON-NLS-1$
	} finally {
		img1.dispose();
		img2.dispose();
		img3.dispose();
		if (result1 != null && result1.handle != 0)
			result1.dispose();
		if (result2 != null && result2.handle != 0)
			result2.dispose();
		if (result3 != null && result3.handle != 0)
			result3.dispose();
	}
}

public void testRotatingImagesWithDifferentDepths() {
	Image result1 = null, result2 = null, result3 = null, result4 = null;
	Image img1 = ImageDescriptor.createFromFile(getClass(), "icons/bits1.bmp").createImage(); //$NON-NLS-1$
	Image img2 = ImageDescriptor.createFromFile(getClass(), "icons/bits4.bmp").createImage(); //$NON-NLS-1$;
	Image img3 = ImageDescriptor.createFromFile(getClass(), "icons/bits8.gif").createImage(); //$NON-NLS-1$;
	Image img4 = ImageDescriptor.createFromFile(getClass(), "icons/bits24.jpg").createImage(); //$NON-NLS-1$;
	
	try {
		result1 = ImageUtilities.createRotatedImage(img1);
		result2 = ImageUtilities.createRotatedImage(img2);
		result3 = ImageUtilities.createRotatedImage(img3);
		result4 = ImageUtilities.createRotatedImage(img4);
		
//		Shell shell = new Shell(Display.getCurrent());
//		shell.setSize(700, 700);
//		shell.open();
//		GC gc = new GC(shell);
//		gc.drawImage(result1, 0, 0);
//		Thread.sleep(5000);
//		gc.drawImage(result3, 0, 0);
//		Thread.sleep(5000);
//		gc.drawImage(result2, 0, 0);
//		Thread.sleep(5000);
//		gc.drawImage(result4, 0, 0);
//		Thread.sleep(5000);
//		gc.dispose();
//		shell.dispose();
	} catch (Throwable error) {
		fail(error.toString() + "\n" + error.getStackTrace()[0]); //$NON-NLS-1$
	} finally {
		img1.dispose();
		img2.dispose();
		img3.dispose();
		img4.dispose();
		if (result1 != null && result1.handle != 0)
			result1.dispose();
		if (result2 != null && result2.handle != 0)
			result2.dispose();
		if (result3 != null && result3.handle != 0)
			result3.dispose();
		if (result4 != null && result4.handle != 0)
			result4.dispose();
	}
}

}