package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * The constants used to identify typical requests.
 */
public interface RequestConstants {

public static String
	REQ_CONNECTION_START  = "connection start",     //$NON-NLS-1$
	REQ_CONNECTION_END    = "connection end",       //$NON-NLS-1$
	REQ_RECONNECT_SOURCE  = "Reconnection source",  //$NON-NLS-1$
	REQ_RECONNECT_TARGET  = "Reconnection target",  //$NON-NLS-1$
	REQ_MOVE_BENDPOINT    = "move bendpoint",       //$NON-NLS-1$
	REQ_DELETE_BENDPOINT  = "delete bendpoint",     //$NON-NLS-1$
	REQ_CREATE_BENDPOINT  = "create bendpoint",     //$NON-NLS-1$

	REQ_RESIZE            = "resize",               //$NON-NLS-1$
	REQ_RESIZE_CHILDREN   = "resize children",      //$NON-NLS-1$

	REQ_MOVE              = "move",                 //$NON-NLS-1$
	REQ_MOVE_CHILDREN     = "move children",        //$NON-NLS-1$
	REQ_ORPHAN            = "orphan",               //$NON-NLS-1$
	REQ_ORPHAN_CHILDREN   = "orphan children",      //$NON-NLS-1$
	REQ_CREATE            = "create child",         //$NON-NLS-1$
	REQ_ADD               = "add children",         //$NON-NLS-1$

	REQ_SOURCE_DELETED    = "source deleted",       //$NON-NLS-1$
	REQ_TARGET_DELETED    = "target deleted",       //$NON-NLS-1$
	REQ_DELETE            = "delete",               //$NON-NLS-1$
	REQ_DELETE_DEPENDANT  = "delete dependant",     //$NON-NLS-1$
	REQ_ANCESTOR_DELETED  = "ancestor deleted",     //$NON-NLS-1$

	REQ_ALIGN             = "align",                //$NON-NLS-1$
	REQ_ALIGN_CHILDREN    = "align children",       //$NON-NLS-1$

	REQ_DIRECT_EDIT       = "direct edit",          //$NON-NLS-1$
	REQ_SELECTION         = "selection",            //$NON-NLS-1$
	REQ_SELECTION_HOVER   = "selection hover";      //$NON-NLS-1$

/**@deprecated use REQ_MOVE_CHILDREN*/
public static String REQ_MOVE_CHILD = REQ_MOVE_CHILDREN;

/**@deprecated use REQ_RECONNECT_SOURCE*/
public static String REQ_RECONNECT_START = REQ_RECONNECT_SOURCE;

/**@deprecated use REQ_RECONNECT_TARGET*/
public static String REQ_RECONNECT_END = REQ_RECONNECT_TARGET;

/**@deprecated use REQ_ALIGN_CHILDREN*/
public static String REQ_ALIGN_CHILD = REQ_ALIGN_CHILDREN;

/**@deprecated use REQ_RESIZE_CHILDREN*/
public static String REQ_RESIZE_CHILD = REQ_RESIZE_CHILDREN;

}
