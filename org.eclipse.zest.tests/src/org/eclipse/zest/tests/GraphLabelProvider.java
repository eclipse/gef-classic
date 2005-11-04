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

import org.eclipse.jdt.internal.ui.SharedImages;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * This is a sample label provider that uses a large list of names as the labels.
 * The images used are from the {@link org.eclipse.jdt.internal.ui.SharedImages} collection
 * and are determined by the first letter of the name.
 * This label provider also implements the {@link org.eclipse.jface.viewers.IFontProvider} and
 * {@link org.eclipse.jface.viewers.IColorProvider} interfaces to give support for setting the  
 * graph node colorings and font.
 * 
 * @author Chris Callendar
 */
public class GraphLabelProvider extends LabelProvider implements IFontProvider, IColorProvider {
	
	protected Font font;
	protected Color foreground;
	protected Color background;
	
	/** Used to get the images. */
	private SharedImages images = null;
	
	/**
	 * Initializes this sample label provider.
	 */
	public GraphLabelProvider() {
		super();
		images = new SharedImages();
		Font defaultFont = Display.getDefault().getSystemFont();	// don't dispose
		font = new Font(null, defaultFont.getFontData());
		foreground = new Color(null, 0, 0, 0);
		background = new Color(null, 216, 228, 248);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#dispose()
	 */
	public void dispose() {
		super.dispose();
		images = null;
		font.dispose();
		foreground.dispose();
		background.dispose();
	}

	/**
	 * Currently this is implemented by returning element.toString().
	 * Null elements will have the text 'null'.
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		return (element == null ? "null" : element.toString());
	}
	
	/**
	 * Gets an image depending on the object. Currently this is implemented
	 * to convert the obj to a String and use the first letter of that string
	 * to determine which image is used from the SharedImages collection.
	 * @param obj
	 * @return Image
	 */
    public Image getImage(Object element) {
    	Image img = null;
        if (element instanceof String) {
            String imageKey = ISharedImages.IMG_OBJS_DEFAULT;
        	String name = (String) element;
        	char c = name.charAt(0);
        	switch (c) {
	        	case 'A' : 
	        		imageKey = ISharedImages.IMG_OBJS_ANNOTATION; break;
	        	case 'B' :
	        		imageKey = ISharedImages.IMG_OBJS_IMPCONT; break;
	        	case 'C' :
	        		imageKey = ISharedImages.IMG_OBJS_CLASS; break;
	        	case 'D' :
	        		imageKey = ISharedImages.IMG_OBJS_EMPTY_LOGICAL_PACKAGE; break;
	        	case 'E' :
	        		imageKey = ISharedImages.IMG_OBJS_ENUM; break;
	        	case 'F' :
	        		imageKey = ISharedImages.IMG_OBJS_INNER_INTERFACE_PRIVATE; break;
	        	case 'G' :
	        		imageKey = ISharedImages.IMG_OBJS_INNER_CLASS_PROTECTED; break;
	        	case 'H' :
	        		imageKey = ISharedImages.IMG_OBJS_EXTERNAL_ARCHIVE; break;
	        	case 'I' :
	        		imageKey = ISharedImages.IMG_OBJS_INTERFACE; break;
	        	case 'J' :
	        		imageKey = ISharedImages.IMG_OBJS_CUNIT; break;
	        	case 'K' :
	        		imageKey = ISharedImages.IMG_OBJS_CFILE; break;
	        	case 'L' :
	        		imageKey = ISharedImages.IMG_OBJS_LOCAL_VARIABLE; break;
	        	case 'M' :
	        		imageKey = ISharedImages.IMG_FIELD_PRIVATE; break;
	        	case 'N' :
	        		imageKey = ISharedImages.IMG_FIELD_PROTECTED; break;
	        	case 'O' :
	        		imageKey = ISharedImages.IMG_OBJS_DEFAULT; break;
	        	case 'P' :
	        	case 'Q' :
	        		imageKey = ISharedImages.IMG_OBJS_PACKAGE; break;
	        	case 'R' :
	        		imageKey = ISharedImages.IMG_OBJS_PUBLIC; break;
	        	case 'S' :
	        		imageKey = ISharedImages.IMG_OBJS_PROTECTED; break;
	        	case 'T' :
	        		imageKey = ISharedImages.IMG_OBJS_PRIVATE; break;
	        	case 'U' :
	        		imageKey = ISharedImages.IMG_OBJS_JAR; break;
	        	case 'V' :
	        	case 'W' :
	        		imageKey = ISharedImages.IMG_OBJS_LIBRARY; break;
	        	case 'X' :
	        	case 'Y' :
	        	case 'Z' :
	        	default :
	        		imageKey = ISharedImages.IMG_FIELD_DEFAULT;
        	}
            img = images.getImage(imageKey);
        }
        return img;
    }

	/**
	 * Returns the font, currently this is just the default system font.
	 * @see org.eclipse.jface.viewers.IFontProvider#getFont(java.lang.Object)
	 */
	public Font getFont(Object element) {
		return font;
	}

	/**
	 * Returns the background color, currently this is a light shade of blue (216, 228, 248).
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element) {
		return background;
	}

	/** 
	 * Returns the foreground color.  Currently this is black (0, 0, 0).
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element) {
		return foreground;
	}	
	
}
