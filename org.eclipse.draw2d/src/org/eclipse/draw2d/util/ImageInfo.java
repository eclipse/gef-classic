package org.eclipse.draw2d.util;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Date;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This class contains various information about an {@link Image} (i.e. the number of 
 * times it has been checked out, the last time it was checked out, etc).  Based on this 
 * information, a statistic is calculated to see if the Image is being used enough to keep
 * it around.
 * <p>
 * Things taken into consideration when calculating the statistic:
 * <ol>
 * 		<li>Number of times the image has been checked out - 45% 
 * 		<li>Time elapsed since last use - 40% 
 * 		<li>Size of the image (and thus the resources it consumes) - 15% 
 * </ol>
 * If the calculated statistic is below the {@link #STAT_THRESHOLD}, the Image should be 
 * disposed.
 */
public class ImageInfo {

/** If {@link #getStatistic()} returns this, the Image should be disposed. */
public static final int BAD_STATISTIC = 0;
/** If {@link #getStatistic()} returns this, the Image should _not_ be disposed. */
public static final int GOOD_STATISTIC = 1;

private static final double STAT_THRESHOLD = 0.46;

private Image img;
private boolean checkedIn = true;
private int timesCheckedOut = 0;
private Date checkinTime = new Date();
private Dimension iSize;
private Object source;

/**
 * Creates a new ImageInfo with an {@link Image} that is at least as big as the desired 
 * Dimension <i>d</i>.
 * 
 * @param d The desired size of the Image */
public ImageInfo(Dimension d) {
	this(d.width, d.height);
}

/**
 * Creates a new ImageInfo with an {@link Image} that is at least as big as the desired 
 * <i>width</i> and <i>height</i>.
 * 
 * @param width The desired width of the Image
 * @param height The desired height of the Image
 */
public ImageInfo(int width, int height) {
	img = new Image(Display.getDefault(), width, height);
	iSize = new Dimension(width, height);
}

// Things taken into consideration --
// (1) Number of times the image has been checked out - 45%
// (2) Time elapsed since last use - 40%
// (3) Size of the image (and thus the resources it consumes) - 15%
// NOTE: It will return 1 if the image is currently checkedout
private double calculateStatistic() {
	// 15 checkouts = 45 pts.
	double step1 = timesCheckedOut * 3;
	step1 = step1 > 45 ? 45 : step1;
	
	// 4 minutes elapsed w/o any checkouts = 0 pts.
	double step2 = timeSinceLastUse() * 3.0 / 16;
	step2 = 45 - step2;
	step2 = step2 < 0 ? 0 : step2;
	
	// Area of 1.31 million pixels (1280 x 1024) = 0 pts.
	double step3 = iSize.width * iSize.height * 15.0 / 1310720;
	step3 = 15 - step3;
	step3 = step3 < 0 ? 0 : step3;

	return (step1 + step2 + step3) / 100;
}

/**
 * Checks in the Image associated with this ImageInfo. If the Image passed in is not the 
 * same Image associated with this ImageInfo, the checkin fails.
 *  * @param i The Image to check in * @return <code>true</code> if the checkin was successful */
public boolean checkin(Image i) {
	if (i != img)
		return false;
	checkinTime = new Date();
	checkedIn = true;
	source = null;
	return true;
}

/**
 * Checks out the Image associated with this ImageInfo, as long as it is currently checked   * in and it is equal to or bigger than the desired size <i>d</i>.
 * 
 * @param d The desired size * @param holder The object requesting the Image * @return This ImageInfo's Image if it is of adequate size and checked in. */
public Image checkout(Dimension d, Object holder) {
	if (!checkedIn || !fits(d, img))
		return null;
	timesCheckedOut++;
	checkedIn = false;
	source = holder;
	return img;
}

/**
 * Disposes of the {@link Image}. */
public void dispose() {
	img.dispose();
}	

/**
 * Returns <code>true</code> if the given Image is equal to or bigger than the desired 
 * size <i>d</i>.
 * @param d The desired size * @param i The Image to test * @return <code>true</code> if the given Image is an adequate size */
public static boolean fits(Dimension d, Image i) {
	Dimension imageSize = new Dimension(i.getBounds().width, i.getBounds().height);
	if (imageSize.equals(d))
		return true;
	if (imageSize.contains(d) && imageSize.getArea() < d.getArea() * 3)
		return true;
	return false;
}

/**
 * Returns the statistic for this ImageInfo.  Possible values are {@link #GOOD_STATISTIC} 
 * and {@link #BAD_STATISTIC}.
 * @return The statistic */
public int getStatistic() {
	if (!checkedIn)
		return GOOD_STATISTIC;
	return calculateStatistic() < STAT_THRESHOLD ? BAD_STATISTIC : GOOD_STATISTIC;
}

/**
 * Returns <code>true</code> if the Image is checked in. * @return <code>true</code> if the Image is checked in */
public boolean isCheckedIn() {
	return checkedIn;
}

/**
 * Merges this ImageInfo with the given ImageInfo.  The passed in ImageInfo must be 
 * the same size as this ImageInfo and both must be currently checked in.
 * 
 * @param info The ImageInfo to merge with this ImageInfo * @return <code>true</code> if the merge was successful */
public boolean merge(ImageInfo info) {
	if (isCheckedIn() && info.isCheckedIn() && info.iSize.equals(iSize) && info != this) {
		timesCheckedOut += info.timesCheckedOut;
		source = info.source;
		checkinTime = checkinTime.before(info.checkinTime) ? info.checkinTime : checkinTime;
		info.dispose();
		return true;
	}
	return false;
}

/**
 * Returns the number of milliseconds since this ImageInfo was last used.
 *  * @return long The number of milliseconds since this ImageInfo was last used
 */
public long timeSinceLastUse() {
	if (!checkedIn)
		return 0;
	return (new Date().getTime() - checkinTime.getTime()) / 1000;
}

/** * @see java.lang.Object#toString() */
public String toString() {
	return super.toString() + "\n\tImage Checked In: " + //$NON-NLS-1$
			checkedIn + "\n\tImage Bounds: " + img.getBounds() + //$NON-NLS-1$
			"\n\tNumber of times checked out: " + timesCheckedOut + //$NON-NLS-1$
			"\n\tTime since last use: " + timeSinceLastUse() + //$NON-NLS-1$
			" seconds\n\tStatistic: " + calculateStatistic() //$NON-NLS-1$
			+ "\n\tChecked out by: " + source;//$NON-NLS-1$
}

}


