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

/**
 * Utility class for print out debug statements.
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class DebugPrint {
	
	/** Set this to false to turn off all debug print statements. */
	private static final boolean DEBUG = true;
	
	/**
	 * Prints out the String value of the given object.
	 * @param obj	The object to print out.
	 */
	public static void println(Object obj) {
		println(String.valueOf(obj));
	}
	
	/**
	 * Prints out the exception message (with a link to the exception line number).
	 * @param ex	The exception
	 * @param printStackTrace	if a stack trace should be printed out
	 */
	public static void println(Exception ex, boolean printStackTrace) {
		if (DEBUG) {
			System.out.print("Exception: " + ex.getMessage() + " ");
			String location = "";
			try {
				StackTraceElement ste[] = new Exception().getStackTrace();
				location = " { " + ste[1].toString() + " } ";
			}
			catch ( Exception e ) {				
			}
			System.out.println( location );
			if (printStackTrace) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Prints out the given string to the standard out IF the
	 * static variable DEBUG is true. 
	 * @param s
	 */
	public static void println( String s ) {
		if (DEBUG) {
			System.out.println( s ) ;
		}
	}

	/**
	 * Prints a new line if DEBUG is true.
	 */
	public static void println() {
		if (DEBUG) {
			System.out.println();
		}
	}
	
	/**
	 * Prints out the given string to standard out IF the static variable DEBUG
	 * is true.  Otherwise nothing happens.  
	 * @param s					The string to print out.
	 * @param showLocationLink	If the calling method line number should also be written out with a link
	 */
	public static void println( String s, boolean showLocationLink ) {
		if (DEBUG) {
			System.out.println( s ) ;
			
			if (showLocationLink) {
				String location = "";
				try {
					StackTraceElement ste[] = new Exception().getStackTrace();
					location = ste[1].toString();
					System.out.println( location );
				}
				catch ( Exception e ) {				
				}
			}
		}		
	}

}
