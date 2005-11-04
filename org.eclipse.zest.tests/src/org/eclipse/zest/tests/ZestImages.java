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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;


/**
 * This class stores the images used in the Zest plugin.
 * 
 * @author Chris Callendar
 */
public class ZestImages {

	public static final String IMG_ZEST_ADD_NODE 				= "/icons/addnode.gif";
	public static final String IMG_ZEST_ADD_NODE_DISABLED		= "/icons/addnode_disabled.gif";
	public static final String IMG_ZEST_DELETE					= "/icons/delete.gif";
	public static final String IMG_ZEST_DELETE_DISABLED 		= "/icons/delete_disabled.gif";
	public static final String IMG_ZEST_PAUSE					= "/icons/pause.gif";
	public static final String IMG_ZEST_PAUSE_DISABLED 			= "/icons/pause_disabled.gif";
	public static final String IMG_ZEST_RESUME					= "/icons/resume.gif";
	public static final String IMG_ZEST_RESUME_DISABLED 		= "/icons/resume_disabled.gif";
	public static final String IMG_ZEST_PLAY					= "/icons/play.gif";
	public static final String IMG_ZEST_PLAY_DISABLED			= "/icons/play_disabled.gif";
	public static final String IMG_ZEST_STOP					= "/icons/stop.gif";
	public static final String IMG_ZEST_STOP_DISABLED			= "/icons/stop_disabled.gif";
	public static final String IMG_ZEST_ZOOM_IN					= "/icons/zoom_in.gif";
	public static final String IMG_ZEST_ZOOM_IN_DISABLED		= "/icons/zoom_in_disabled.gif";
	public static final String IMG_ZEST_ZOOM_OUT				= "/icons/zoom_out.gif";
	public static final String IMG_ZEST_ZOOM_OUT_DISABLED		= "/icons/zoom_out_disabled.gif";
	public static final String IMG_ZEST_PLUS					= "/icons/plus.gif";
	public static final String IMG_ZEST_MINUS					= "/icons/minus.gif";
	public static final String IMG_ZEST_DOT						= "/icons/green_dot.gif";
	public static final String IMG_ZEST_DIRECTED_GRAPH			= "/icons/e_forward.gif";
	public static final String IMG_ZEST_LAYOUT_GRID				= "/icons/icon_grid_layout.gif";
	public static final String IMG_ZEST_LAYOUT_RADIAL			= "/icons/icon_radial_layout.gif";
	public static final String IMG_ZEST_LAYOUT_SPRING			= "/icons/icon_spring_layout.gif";
	public static final String IMG_ZEST_LAYOUT_TREE				= "/icons/icon_tree_layout.gif";
	public static final String IMG_ZEST_LAYOUT_TREE_HORIZ		= "/icons/icon_tree_layout_horizontal.gif";
	public static final String IMG_ZEST_LAYOUT_HORIZONTAL		= "/icons/icon_horizontal_layout.gif";
	public static final String IMG_ZEST_LAYOUT_VERTICAL			= "/icons/icon_vertical_layout.gif";
	
	/** The image registry. */
	private ImageRegistry imageRegistry = null;
	
	public ZestImages() {
		this.imageRegistry = new ImageRegistry();
		loadImages();
	}
	
    /**
     * Retrieves the specified image.
     * @param name the name of the image.
     * @return the image, or <code>null</code> if not found
     */
    private void loadImages() {
		imageRegistry.put(IMG_ZEST_ADD_NODE, createImageDescriptor(IMG_ZEST_ADD_NODE));
		imageRegistry.put(IMG_ZEST_ADD_NODE_DISABLED, createImageDescriptor(IMG_ZEST_ADD_NODE_DISABLED));
		imageRegistry.put(IMG_ZEST_DELETE, createImageDescriptor(IMG_ZEST_DELETE));
		imageRegistry.put(IMG_ZEST_DELETE_DISABLED, createImageDescriptor(IMG_ZEST_DELETE_DISABLED));
		imageRegistry.put(IMG_ZEST_PAUSE, createImageDescriptor(IMG_ZEST_PAUSE));
		imageRegistry.put(IMG_ZEST_PAUSE_DISABLED, createImageDescriptor(IMG_ZEST_PAUSE_DISABLED));
		imageRegistry.put(IMG_ZEST_RESUME, createImageDescriptor(IMG_ZEST_RESUME));
		imageRegistry.put(IMG_ZEST_RESUME_DISABLED, createImageDescriptor(IMG_ZEST_RESUME_DISABLED));
		imageRegistry.put(IMG_ZEST_PLAY, createImageDescriptor(IMG_ZEST_PLAY));
		imageRegistry.put(IMG_ZEST_PLAY_DISABLED, createImageDescriptor(IMG_ZEST_PLAY_DISABLED));
		imageRegistry.put(IMG_ZEST_STOP, createImageDescriptor(IMG_ZEST_STOP));
		imageRegistry.put(IMG_ZEST_STOP_DISABLED, createImageDescriptor(IMG_ZEST_STOP_DISABLED));
		imageRegistry.put(IMG_ZEST_ZOOM_IN, createImageDescriptor(IMG_ZEST_ZOOM_IN));
		imageRegistry.put(IMG_ZEST_ZOOM_IN_DISABLED, createImageDescriptor(IMG_ZEST_ZOOM_IN_DISABLED));
		imageRegistry.put(IMG_ZEST_ZOOM_OUT, createImageDescriptor(IMG_ZEST_ZOOM_OUT));
		imageRegistry.put(IMG_ZEST_ZOOM_OUT_DISABLED, createImageDescriptor(IMG_ZEST_ZOOM_OUT_DISABLED));
		imageRegistry.put(IMG_ZEST_PLUS, createImageDescriptor(IMG_ZEST_PLUS));
		imageRegistry.put(IMG_ZEST_MINUS, createImageDescriptor(IMG_ZEST_MINUS));
		imageRegistry.put(IMG_ZEST_DOT, createImageDescriptor(IMG_ZEST_DOT));
		imageRegistry.put(IMG_ZEST_DIRECTED_GRAPH, createImageDescriptor(IMG_ZEST_DIRECTED_GRAPH));
		// layout icons
		imageRegistry.put(IMG_ZEST_LAYOUT_GRID, createImageDescriptor(IMG_ZEST_LAYOUT_GRID));
		imageRegistry.put(IMG_ZEST_LAYOUT_RADIAL, createImageDescriptor(IMG_ZEST_LAYOUT_RADIAL));
		imageRegistry.put(IMG_ZEST_LAYOUT_SPRING, createImageDescriptor(IMG_ZEST_LAYOUT_SPRING));
		imageRegistry.put(IMG_ZEST_LAYOUT_TREE, createImageDescriptor(IMG_ZEST_LAYOUT_TREE));
		imageRegistry.put(IMG_ZEST_LAYOUT_TREE_HORIZ, createImageDescriptor(IMG_ZEST_LAYOUT_TREE_HORIZ));
		imageRegistry.put(IMG_ZEST_LAYOUT_HORIZONTAL, createImageDescriptor(IMG_ZEST_LAYOUT_HORIZONTAL));
		imageRegistry.put(IMG_ZEST_LAYOUT_VERTICAL, createImageDescriptor(IMG_ZEST_LAYOUT_VERTICAL));
    }
    
    
	/**
	 * Creates a new image descriptor for the given filename (relative
	 * to this classes base directory).
	 */
	private ImageDescriptor createImageDescriptor(String filename) {
		return ImageDescriptor.createFromFile(ZestImages.class, filename);
	}
    
    /**
     * Retrieves the image with the given name.
     * @param name
     * @return the image, or <code>null</code> if not found
     */
    public Image getImage(String name) {
    	return imageRegistry.get(name);
    }

    /**
     * Retrieves the image descriptor for specified image name.
     * @param name the name of the image.
     * @return the image descriptor, or <code>null</code> if not found
     */
    public ImageDescriptor getImageDescriptor(String name) {
    	return imageRegistry.getDescriptor(name);
    }
}
