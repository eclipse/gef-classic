package org.eclipse.gef.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public class FlagSupport {

private int flags;

protected boolean getFlag(int flag){
	return (flags & flag) != 0;
}

protected void setFlag(int flag, boolean value){
	if (value)
		flags |= flag;
	else
		flags &= ~flag;
}

}