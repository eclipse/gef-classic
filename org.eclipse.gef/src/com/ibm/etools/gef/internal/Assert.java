package com.ibm.etools.gef.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public class Assert {

static class AssertionFailedException
	extends RuntimeException
{
	public AssertionFailedException(){}
	public AssertionFailedException(String detail) {
	    super(detail);
	}
}

public static void isNotNull(Object object) {
	if (object == null)
		throw new AssertionFailedException( GEFMessages.ERR_Assert_IsNotNull_Exception_AssertionFailed );
}

public static void isNotNull(Object object, String message) {
	if (object == null)
		throw new AssertionFailedException( GEFMessages.ERR_Assert_IsNotNull_Exception_AssertionFailed + 
											"; " + message);  //$NON-NLS-1$
}

public static void isTrue(boolean expression) {
	if (!expression)
		throw new AssertionFailedException( GEFMessages.ERR_Assert_IsTrue_Exception_AssertionFailed );
}

public static void isTrue(boolean expression, String message) {
	if (!expression)
		throw new AssertionFailedException(	GEFMessages.ERR_Assert_IsTrue_Exception_AssertionFailed + 
											"; "+ //$NON-NLS-1$
											message);
}

}


