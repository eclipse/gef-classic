package org.eclipse.draw2d.internal;
/*
 * Licensed Material - Property of IBM
 *(C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
			checkedIn.add(info);
			return true;
		}	
	}
	return false;
}

/**
 * If the given {@link Image} is an adequate size (for Dimension <i>d</i>), this same 
 * Image is simply returned.  Otherwise, the given Image is checked in and a new Image is 
 * checked out that is an adequate size.
 *  * @param i The Image to check in * @param d The desired Dimension for the return Image * @param o The object requesting the Image * @return Image An Image that is big enough for the desired Dimension */
public static Image checkinCheckout(Image i, Dimension d, Object o) {
	if (i != null && ImageInfo.fits(d, i))
		return i;
	checkin(i);
	return checkout(d, o);
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
	Image result = null;
	for (int i = 0; i < checkedIn.size(); i++) {
		ImageInfo info = (ImageInfo)checkedIn.get(i);
		if ((result = info.checkout(d, o)) != null) {
			checkedIn.remove(info);
			checkedOut.add(info);
			return result;
		} else if (info.getStatistic() == ImageInfo.BAD_STATISTIC) {
			checkedIn.remove(info);
			info.dispose();
			numberOfImagesDisposed++;
			i--;
		}
	}
	
	// Create a new image that is 15% larger than requested in each dimension 
	Dimension newD = d.getExpanded((int)(d.width * 0.15), (int)(d.height * 0.15));
	totalNumberOfImagesCreated++;	
	try {
		ImageInfo newInfo = new ImageInfo(newD);
		checkedOut.add(newInfo);
		return newInfo.checkout(newD, o);	
	} catch (IllegalArgumentException e) {
		return null;
	}
}	

}


