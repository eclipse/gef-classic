package org.eclipse.draw2d.util;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.internal.Draw2dMessages;

public class ImageInfo {
	
	public static int BAD_STATISTIC = 0, GOOD_STATISTIC = 1;
	
	private Image img;
	private boolean checkedIn = true;
	private int timesCheckedOut = 0;
	private Date checkinTime = new Date();
	private Dimension iSize;
	private Object source;
	
	public ImageInfo( Dimension d ){
		this( d.width, d.height );
	}
	
	public ImageInfo( int width, int height ){
		img = new Image( Display.getDefault(), width, height );
		iSize = new Dimension( width, height );
	}
	
	// Things taken into consideration --
	// (1) Number of times the image has been checked out - 45%
	// (2) Time elapsed since last use - 40%
	// (3) Size of the image (and thus the resources it consumes) - 15%
	// NOTE: It will return 1 if the image is currently checkedout
	private double calculateStatistic(){
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
	
	public boolean checkin( Image i ){
		if( i != img )
			return false;
		checkinTime = new Date();	
		return (checkedIn = true);
	}
	
	public Image checkout( Dimension d, Object holder ){
		if( !checkedIn || !fits(d,img) )
			return null;
		timesCheckedOut++;
		checkedIn = false;
		source = holder;
		return img;
	}
	
	public void dispose(){
//System.out.println( "\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++++++++++++++++++++++\nAbout to dispose: " + toString() + "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n++++++++++++++++++++++++++++++++++++++++\n" );
		img.dispose();
	}	
	
	public static boolean fits( Dimension d, Image i ){
		Dimension imageSize = new Dimension( i.getBounds().width, i.getBounds().height );
		if( imageSize.equals(d) )
			return true;
		if( imageSize.greaterThan(d) && imageSize.getArea() < d.getArea() * 3 )
			return true;
		return false;
	}

	public int getStatistic(){
		if( !checkedIn )
			return GOOD_STATISTIC;
			
		return calculateStatistic() < 0.46 ? BAD_STATISTIC : GOOD_STATISTIC;
	}
	
	public boolean isCheckedIn(){
		return checkedIn;
	}
	
	public boolean merge( ImageInfo info ){
		if( isCheckedIn() && info.isCheckedIn() && info.iSize.equals(iSize) && info != this ){
//System.out.println( "-------------------------------------------\nMerging: " + info + "\nInto: " + this + "\n-------------------------------------------------\n" );
			timesCheckedOut += info.timesCheckedOut;
			source = info.source;
			checkinTime = checkinTime.before( info.checkinTime ) ? 
							info.checkinTime : checkinTime;
			info.dispose();
			return true;
		}
		return false;
	}
		
	public long timeSinceLastUse(){
		if( !checkedIn )
			return 0;
		return (new Date().getTime() - checkinTime.getTime()) / 1000;
	}
	
	public String toString(){
		return super.toString() + "\n\tImage Checked In: " + //$NON-NLS-1$
				checkedIn + "\n\tImage Bounds: " + img.getBounds() +//$NON-NLS-1$
				"\n\tNumber of times checked out: " + timesCheckedOut + //$NON-NLS-1$
				"\n\tTime since last use: " + timeSinceLastUse() + //$NON-NLS-1$
				" seconds\n\tStatistic: " + calculateStatistic() //$NON-NLS-1$
				+ "\n\tChecked out by: " + source;//$NON-NLS-1$
	}

}


