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
package org.eclipse.mylar.zest.core;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * @author Ian Bull
 */
public class ZestPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static ZestPlugin plugin;
	
	//colors used by Zest.
	private ColorRegistry colors;
	
	/**
	 * The constructor.
	 */
	public ZestPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		addImage(IZestImageConstants.TREE_HANGING_ICON, getImageDescriptor(IZestImageConstants.TREE_HANGING_ICON).createImage());
		addImage(IZestImageConstants.TREE_NORMAL_ICON, getImageDescriptor(IZestImageConstants.TREE_NORMAL_ICON).createImage());
		addImage(IZestImageConstants.TREE_HANGING_INVERSE_ICON, getImageDescriptor(IZestImageConstants.TREE_HANGING_INVERSE_ICON).createImage());
		addImage(IZestImageConstants.TREE_NORMAL_INVERSE_ICON, getImageDescriptor(IZestImageConstants.TREE_NORMAL_INVERSE_ICON).createImage());

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ZestPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.mylar.zest.core", path);
	 }
	
	public void addImage( String key, Image image )  {
		super.createImageRegistry();
		getImageRegistry().put(key, image);
	}
	
	public void removeImage( String key ) {
		super.createImageRegistry();
		getImageRegistry().remove(key);
	}
	
	public Image getImage( String key ) {
		super.createImageRegistry();
		return getImageRegistry().get(key);
	}
	
	
	/**
	 * Gets the color registered for the given key. Keys can be found in
	 * org.eclipse.mylar.zest.core.IZestColorConstants.
	 * @param key the key used to reference the color.
	 * @return the color, or null if no color can be found.
	 * @see org.eclipse.mylar.zest.core.IZestColorConstants.
	 */ 
	 //@tag zest(bug(151332-Colors)) : resolution
	public Color getColor(String key) {
		if (colors == null) {
			Display display = getWorkbench().getDisplay();
			colors = new ColorRegistry(display);
			populateColors(colors);
		}
		return colors.get(key);
	}
	
	/**
	 * Sends an error from the ZestPlugin.
	 * @param code the error code.
	 * @see #ZestException
	 */
	public static void error(int code) {
		ZestException.throwError(code, "", null);
	}

	private void populateColors(ColorRegistry colors) {
		colors.put(IZestColorConstants.LIGHT_BLUE, new RGB(216, 228, 248));
		colors.put(IZestColorConstants.DARK_BLUE, new RGB(1, 70, 122));
		colors.put(IZestColorConstants.GREY_BLUE, new RGB(139, 150, 171));
		colors.put(IZestColorConstants.LIGHT_BLUE_CYAN, new RGB(213, 243, 255));
		colors.put(IZestColorConstants.LIGHT_YELLOW, new RGB(255, 255, 206));
		colors.put(IZestColorConstants.GRAY, new RGB(128, 128, 128));
		colors.put(IZestColorConstants.LIGHT_GRAY, new RGB(220, 220, 220));
		colors.put(IZestColorConstants.BLACK, new RGB(0,0,0));
		colors.put(IZestColorConstants.RED, new RGB(255,0,0));
		colors.put(IZestColorConstants.DARK_RED, new RGB(127,0,0));
		colors.put(IZestColorConstants.ORANGE, new RGB(255, 196, 0));
		colors.put(IZestColorConstants.YELLOW, new RGB(255,255,0));
		colors.put(IZestColorConstants.GREEN, new RGB(0,255,0));
		colors.put(IZestColorConstants.DARK_GREEN, new RGB(0,127,0));
		colors.put(IZestColorConstants.LIGHT_GREEN, new RGB(96,255,96));
		colors.put(IZestColorConstants.CYAN, new RGB(0,255,255));
		colors.put(IZestColorConstants.BLUE, new RGB(0,0,255));
		colors.put(IZestColorConstants.DARK_BLUE, new RGB(0,0,127));
		colors.put(IZestColorConstants.WHITE, new RGB(255,255,255));
		colors.put(IZestColorConstants.EDGE_WEIGHT_0, new RGB(192, 192, 255));
		colors.put(IZestColorConstants.EDGE_WEIGHT_01, new RGB(64, 128, 225));
		colors.put(IZestColorConstants.EDGE_WEIGHT_02, new RGB(32, 32, 128));
		colors.put(IZestColorConstants.EDGE_WEIGHT_03, new RGB(0, 0, 128));
		colors.put(IZestColorConstants.EDGE_DEFAULT, new RGB(64, 64, 128));
		colors.put(IZestColorConstants.EDGE_HIGHLIGHT, new RGB(192, 32, 32));
		colors.put(IZestColorConstants.DISABLED,new RGB(230, 240, 250));
		
	}
}
