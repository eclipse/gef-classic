/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

/**
 * This class caches {@link Image} resources to minimize the creation of new objects and 
 * to handle the disposal of these Images.
 */
public class ImageCache {

private static List checkedOut = new ArrayList();
private static List checkedIn = new ArrayList();
				  
private static int totalNumberOfImagesCreated = 0;
private static int numberOfImagesDisposed = 0;

/**
 * Checks in the given {@link Image}.
 *  * @param img The image to be checked in * @return <code>true</code> if the image was successfully checked in. */
public static boolean checkin(Image img) {
	if (img == null)
		return false;
	for (int i = 0; i < checkedOut.size(); i++) {
		ImageInfo info = (ImageInfo)checkedOut.get(i);
		if (info.checkin(img)) {
			checkedOut.remove(info);
			checkedIn.add(0, info);
			return true;
		}	
	}
	return false;
}

/**
 * Returns an image that is at least as big as the desired size.
 * <p>NOTE:	
 * <ol>
 * 		<li>The client should not dispose the image that is checked out.
 * 		<li>However, the client should check the image back in when done.
 * 		<li>The given dimension will not be modified by this method.
 * 		<li>The client should not use the returned image's bounds for anything (since the 
 * 		returned image could be bigger than requested).
 * 		<li>If the size of the image is too big, and the image cannot be created, this 
 * 		method will return <code>null</code>.
 * </ol>
 *
 * @param d The desired size of the image to be checked out
 * @param o The object requesting the image
 * @return An image at least as big as the desired size
 */
public static Image checkout(Dimension d, Object o) {
	d = d.getCopy();
	d.width = ((d.width - 1) / 256 + 1) * 256;
	d.height = ((d.height - 1) / 256 + 1) * 256;
	Image result = null;
	for (int i = 0; i < checkedIn.size(); i++) {
		ImageInfo info = (ImageInfo)checkedIn.get(i);
		if (result == null) {
			if ((result = info.checkout(d, o)) != null) {
				checkedIn.remove(info);
				checkedOut.add(info);
				i--;
				continue;
			}
		}
		if (info.getStatistic() == ImageInfo.BAD_STATISTIC) {
			checkedIn.remove(info);
			info.dispose();
			numberOfImagesDisposed++;
			i--;
		}
	}
	
	if (result != null)
		return result;
	
	// Create a new image
	try {
		ImageInfo newInfo = new ImageInfo(d);
		checkedOut.add(newInfo);
		totalNumberOfImagesCreated++;
		return newInfo.checkout(d, o);	
	} catch (IllegalArgumentException e) {
		return null;
	}
}	

}


