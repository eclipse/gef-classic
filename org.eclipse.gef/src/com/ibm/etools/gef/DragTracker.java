package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A special tool that is used during mouse drags.
 * This interface currently serves as just a marker interface.
 * New methods may be introduced in the future.
 */
public interface DragTracker
	extends Tool
{

void commitDrag();

}
