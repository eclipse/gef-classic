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
package org.eclipse.gef;

/**
 * The constants used to identify typical layers in a graphical application.
 */
public interface LayerConstants {

/**
 * Identifies the layer containing the primary pieces of the application.
 */
String PRIMARY_LAYER = "Primary Layer"; //$NON-NLS-1$

/**
 * Identifies the layer containing connections, which typically appear
 * on top of anything in the primary layer.
 */
String CONNECTION_LAYER = "Connection Layer"; //$NON-NLS-1$

/**
 * Identifies the layer where Guides add feedback to the primary viewer.
 */
String GUIDE_LAYER = "Guide Layer"; //$NON-NLS-1$

/**
 * Identifies the layer containing handles, which are typically editing
 * decorations that appear on top of any model representations.
 */
String HANDLE_LAYER = "Handle Layer"; //$NON-NLS-1$

/**
 * The layer containing feedback, which generally temporary visuals that
 * appear on top of all other visuals.
 */
String FEEDBACK_LAYER = "Feedback Layer"; //$NON-NLS-1$

/**
 * The layer that contains all printable layers.
 */
String PRINTABLE_LAYERS = "Printable Layers"; //$NON-NLS-1$

/**
 * The layer that contains all scaled layers.
 */
String SCALABLE_LAYERS = "Scalable Layers"; //$NON-NLS-1$

}