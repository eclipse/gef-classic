package org.eclipse.draw2d.util;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.internal.Draw2dMessages;

public class ImageCache {
	
	private static List checkedOut = new ArrayList(), 
				  checkedIn  = new ArrayList();
				  
	private static int totalNumberOfImagesCreated = 0, numberOfImagesDisposed = 0;

	public static boolean checkin( Image img ){
		if( img == null )
			return false;
			
//System.out.println( "About to check in an image of size: " + img.getBounds() );
//printContents();

		for( int i = 0; i < checkedOut.size(); i++ ){
			ImageInfo info = (ImageInfo)checkedOut.get(i);
			if( info.checkin( img ) ){
				checkedOut.remove( info );
				checkedIn.add( info );
				return true;
			}	
		}
		
		return false;
	}
	
	public static Image checkinCheckout( Image i, Dimension d, Object o ){
		if( i != null && ImageInfo.fits( d, i ) )
			return i;
		checkin( i );
		return checkout( d, o );
	}

	/**
	 * Returns an image that is at least as big as the desired size.
	 * NOTE:	(1) The client should not dispose the image that is
	 *			  checked out
	 *	    	(2) However, the client should check the image back
	 *			in when done
	 *	    	(3) The given dimension will not be modified by this
	 *			  method
	 *		(4) The client should not use the returned image's bounds
	 *			  for anything (since the returned image could be 
	 *			  bigger than requested)
	 *		(5) If the size of the image is too big, and the image
	 *			  cannot be created, this method will return null
	 *
	 * @param d The desired size of the image to be checked out
	 */
	public static Image checkout( Dimension d, Object o ){
		
//System.out.println( "About to checkout an image of size: " + d );
//printContents();

		Image result = null;
		for( int i = 0; i < checkedIn.size(); i++ ){
			ImageInfo info = (ImageInfo)checkedIn.get(i);
			if( (result = info.checkout(d,o)) != null ){
				checkedIn.remove( info );
				checkedOut.add( info );
				return result;
			} else if( info.getStatistic() == ImageInfo.BAD_STATISTIC ){
				checkedIn.remove( info );
				info.dispose();
				numberOfImagesDisposed++;
				i--;
			}
		}
		
		// Create a new image that is 15% larger than requested in each dimension 
		Dimension newD = d.getExpanded( (int)(d.width * 0.15),
							  (int)(d.height * 0.15) );
//System.out.println( "Creating a new image of size: " + newD );	
		totalNumberOfImagesCreated++;	
//System.out.println( "Total number of images created so far: " + totalNumberOfImagesCreated );
		try{
			ImageInfo newInfo = new ImageInfo( newD );
			checkedOut.add( newInfo );
			return newInfo.checkout( newD, o );	
		} catch( IllegalArgumentException e ){
			return null;
		}
	}	
	
	public static void printContents(){
		System.out.println( "Checked in List: \n" );//$NON-NLS-1$
		for( int i = 0; i < checkedIn.size(); i++ ){
			System.out.println( i + ": " + checkedIn.get(i) );//$NON-NLS-1$
		}
		System.out.println( "*************************************************\n" );//$NON-NLS-1$
		
		System.out.println( "Checked out list: \n" );//$NON-NLS-1$
		for( int i = 0; i < checkedOut.size(); i++ ){
			System.out.println( i + ": " + checkedOut.get(i) );//$NON-NLS-1$
		}
		System.out.println( "=================================================" );//$NON-NLS-1$
		System.out.println( "=================================================\n" );//$NON-NLS-1$
	}

}


