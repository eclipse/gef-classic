package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * The constants used to identify typical layers in a graphical application.
 */
public interface LayerConstants {

/**
 * Identifies the layer containing the primary pieces of the application.
 */
final public static String PRIMARY_LAYER = "Primary Layer"; //$NON-NLS-1$

/**
 * Identifies the layer containing connections, which typically appear
 * on top of anything in the primary layer.
 */
final public static String CONNECTION_LAYER = "Connection Layer"; //$NON-NLS-1$

/**
 * Identifies the layer containing handles, which are typically editing
 * decorations that appear on top of any model representations.
 */
final public static String HANDLE_LAYER = "Handle Layer"; //$NON-NLS-1$

/**
 * The layer containing feedback, which generally temporary visuals that
 * appear on top of all other visuals.
 */
final public static String FEEDBACK_LAYER = "Feedback Layer"; //$NON-NLS-1$

}