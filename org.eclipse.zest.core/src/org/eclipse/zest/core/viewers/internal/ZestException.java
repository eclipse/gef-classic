/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylyn.zest.core.viewers.internal;

import org.eclipse.mylyn.zest.core.messages.ZestErrorMessages;

/**
 * Exceptions for Zest-specific code.
 * @author Del Myers
 *
 */
public class ZestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * An invalid input was given to a zest component.
	 */
	public static final int ERROR_INVALID_INPUT = 0;
	/**
	 * A style was set on a viewer while the input wasn't null. 
	 */
	public static final int ERROR_CANNOT_SET_STYLE = 1;
	
	/**
	 * An invalid style was set for a part.
	 */
	public static final int ERROR_INVALID_STYLE = 2;
	
	public ZestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	protected static final void throwError(int code, String info, Throwable cause) {
		String message = "";
		if (info == null) info = "";
		switch(code) {
			case ERROR_INVALID_INPUT:
				message = ZestErrorMessages.ERROR_INVALID_INPUT;
				break;
			case ERROR_CANNOT_SET_STYLE:
				message = ZestErrorMessages.ERROR_CANNOT_SET_STYLE;
				break;
			case ERROR_INVALID_STYLE:
				message = ZestErrorMessages.ERROR_INVALID_STYLE;
				break;
			default:
				message = info;
				break;
		}
		throw new ZestException(message + ":" + info, cause);
	}
	
}
