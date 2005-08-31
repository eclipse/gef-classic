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
 * @author ccallendar
 */
public class ZestImages {

	public static final String IMG_ZEST_ADD_NODE 			= "IMG_ZEST_ADD_NODE";
	public static final String IMG_ZEST_ADD_NODE_DISABLED	= "IMG_ZEST_ADD_NODE_DISABLED";
	public static final String IMG_ZEST_DELETE				= "IMG_ZEST_DELETE";
	public static final String IMG_ZEST_DELETE_DISABLED 	= "IMG_ZEST_DELETE_DISABLED";
	public static final String IMG_ZEST_PAUSE				= "IMG_ZEST_PAUSE";
	public static final String IMG_ZEST_PAUSE_DISABLED 		= "IMG_ZEST_PAUSE_DISABLED";
	public static final String IMG_ZEST_RESUME				= "IMG_ZEST_RESUME";
	public static final String IMG_ZEST_RESUME_DISABLED 	= "IMG_ZEST_RESUME_DISABLED";
	public static final String IMG_ZEST_PLAY				= "IMG_ZEST_PLAY";
	public static final String IMG_ZEST_PLAY_DISABLED		= "IMG_ZEST_PLAY_DISABLED";
	public static final String IMG_ZEST_STOP				= "IMG_ZEST_STOP";
	public static final String IMG_ZEST_STOP_DISABLED		= "IMG_ZEST_STOP_DISABLED";
	public static final String IMG_ZEST_ZOOM_IN				= "IMG_ZEST_ZOOM_IN";
	public static final String IMG_ZEST_ZOOM_IN_DISABLED	= "IMG_ZEST_ZOOM_IN_DISABLED";
	public static final String IMG_ZEST_ZOOM_OUT			= "IMG_ZEST_ZOOM_OUT";
	public static final String IMG_ZEST_ZOOM_OUT_DISABLED	= "IMG_ZEST_ZOOM_OUT_DISABLED";
	public static final String IMG_ZEST_PLUS				= "IMG_ZEST_PLUS";
	public static final String IMG_ZEST_MINUS				= "IMG_ZEST_MINUS";
	public static final String IMG_ZEST_DOT					= "IMG_ZEST_DOT";
	public static final String IMG_ZEST_DIRECTED_GRAPH		= "IMG_ZEST_DIRECTED_GRAPH";

	private static final String ADD_NODE 					= "/icons/addnode.gif";
	private static final String ADD_NODE_DISABLED			= "/icons/addnode_disabled.gif";
	private static final String DELETE						= "/icons/delete.gif";
	private static final String DELETE_DISABLED 			= "/icons/delete_disabled.gif";
	private static final String PAUSE						= "/icons/pause.gif";
	private static final String PAUSE_DISABLED 				= "/icons/pause_disabled.gif";
	private static final String RESUME						= "/icons/resume.gif";
	private static final String RESUME_DISABLED 			= "/icons/resume_disabled.gif";
	private static final String PLAY						= "/icons/play.gif";
	private static final String PLAY_DISABLED				= "/icons/play_disabled.gif";
	private static final String STOP						= "/icons/stop.gif";
	private static final String STOP_DISABLED				= "/icons/stop_disabled.gif";
	private static final String ZOOM_IN						= "/icons/zoom_in.gif";
	private static final String ZOOM_IN_DISABLED			= "/icons/zoom_in_disabled.gif";
	private static final String ZOOM_OUT					= "/icons/zoom_out.gif";
	private static final String ZOOM_OUT_DISABLED			= "/icons/zoom_out_disabled.gif";
	private static final String PLUS						= "/icons/plus.gif";
	private static final String MINUS						= "/icons/minus.gif";
	private static final String DOT							= "/icons/green_dot.gif";
	private static final String DIRECTED_GRAPH				="/icons/e_forward.gif";
	
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
		imageRegistry.put(IMG_ZEST_ADD_NODE, createImageDescriptor(ADD_NODE));
		imageRegistry.put(IMG_ZEST_ADD_NODE_DISABLED, createImageDescriptor(ADD_NODE_DISABLED));
		imageRegistry.put(IMG_ZEST_DELETE, createImageDescriptor(DELETE));
		imageRegistry.put(IMG_ZEST_DELETE_DISABLED, createImageDescriptor(DELETE_DISABLED));
		imageRegistry.put(IMG_ZEST_PAUSE, createImageDescriptor(PAUSE));
		imageRegistry.put(IMG_ZEST_PAUSE_DISABLED, createImageDescriptor(PAUSE_DISABLED));
		imageRegistry.put(IMG_ZEST_RESUME, createImageDescriptor(RESUME));
		imageRegistry.put(IMG_ZEST_RESUME_DISABLED, createImageDescriptor(RESUME_DISABLED));
		imageRegistry.put(IMG_ZEST_PLAY, createImageDescriptor(PLAY));
		imageRegistry.put(IMG_ZEST_PLAY_DISABLED, createImageDescriptor(PLAY_DISABLED));
		imageRegistry.put(IMG_ZEST_STOP, createImageDescriptor(STOP));
		imageRegistry.put(IMG_ZEST_STOP_DISABLED, createImageDescriptor(STOP_DISABLED));
		imageRegistry.put(IMG_ZEST_ZOOM_IN, createImageDescriptor(ZOOM_IN));
		imageRegistry.put(IMG_ZEST_ZOOM_IN_DISABLED, createImageDescriptor(ZOOM_IN_DISABLED));
		imageRegistry.put(IMG_ZEST_ZOOM_OUT, createImageDescriptor(ZOOM_OUT));
		imageRegistry.put(IMG_ZEST_ZOOM_OUT_DISABLED, createImageDescriptor(ZOOM_OUT_DISABLED));
		imageRegistry.put(IMG_ZEST_PLUS, createImageDescriptor(PLUS));
		imageRegistry.put(IMG_ZEST_MINUS, createImageDescriptor(MINUS));
		imageRegistry.put(IMG_ZEST_DOT, createImageDescriptor(DOT));
		imageRegistry.put(IMG_ZEST_DIRECTED_GRAPH, createImageDescriptor(DIRECTED_GRAPH));
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
