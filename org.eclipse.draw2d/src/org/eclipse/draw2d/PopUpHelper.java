package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Provides abstract support for classes that manage popups.
 * Popups in draw2d consist of a LightweightSystem object with an
 * SWT shell as its Control. Desired popup behavior is attained by adding
 * appropriate listeners to this shell. 
 */
public abstract class PopUpHelper {

private Shell shell;
private LightweightSystem lws;
private boolean tipShowing;
protected Control control;

/**
 * Constructs a PopUpHelper to assist with popups on
 * Control c.
 * 
 * @param c The Control where popup assistance is desired.
 * @since 2.0
 */
protected PopUpHelper(Control c){
	control = c;
}

/**
 * Creates and returns the LightweightSystem object used by
 * PopUpHelper to draw upon.
 * 
 * @since 2.0
 */
protected LightweightSystem createLightweightSystem(){
	return new LightweightSystem();
}

/**
 * Creates a new org.eclipse.swt.widgets.Shell object with
 * the parameters SWT.NO_TRIM | SWT.NO_FOCUS | SWT.ON_TOP.
 * 
 * @since 2.0
 */
protected Shell createShell(){
	return new Shell(control.getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.ON_TOP);
}

/**
 * Dispose of this PopUpHelper object.
 * 
 * @since 2.0
 */
public void dispose(){
	if (shell != null && !shell.isDisposed())
		shell.dispose();
}

/**
 * Returns this PopUpHelper's Shell.
 * If no Shell exists for this PopUpHelper,
 * a new Shell is created and hookShellListeners()
 * is called.
 * 
 * @since 2.0
 */
protected Shell getShell(){
	if (shell == null){
		shell = createShell();
		hookShellListeners();
	}		
	return shell;
}

/**
 * Returns this PopUpHelper's LightweightSystem.
 * If no LightweightSystem exists for this PopUpHelper,
 * a new LightweightSystem is created with this 
 * PopUpHelper's Shell as its Control.
 * 
 * @since 2.0
 */
protected LightweightSystem getLightweightSystem(){
	if (lws == null){
		lws = createLightweightSystem();
		lws.setControl(getShell());
	}
	return lws;
}

/**
 * Hides this PopUpHelper's Shell.
 * 
 * @since 2.0
 */
protected void hide(){
	if (shell != null && !shell.isDisposed())
		shell.setVisible(false);
	tipShowing = false;
}

/** 
 * Desired popup helper behavior is achieved by writing 
 * listeners that manipulate the behavior of the 
 * PopUpHelper's Shell. Override this method and add these
 * listeners here.
 * 
 * @since 2.0
 */
protected abstract void hookShellListeners();

/**
 * Returns true if this PopUpHelper's Shell is 
 * visible, false otherwise.
 * 
 * @since 2.0
 */
public boolean isShowing(){
	return tipShowing;
}

/**
 * Sets the background color of this PopUpHelper's Shell.
 * 
 * @param c The desired background color of this PopUpHelper's Shell.
 * @since 2.0
 */
public void setBackgroundColor(Color c){
	getShell().setBackground(c);
}

/**
 * Sets the foreground color of this PopUpHelper's Shell.
 * 
 * @param c The desired foreground color of this PopUpHelper's Shell.
 * @since 2.0
 */
public void setForegroundColor(Color c){
	getShell().setForeground(c);
}	

/**
 * Sets the bounds on this PopUpHelper's Shell to the passed values.
 * 
 * @param x Desired X coordinate of Shell.
 * @param y Desired Y coordinate of Shell.
 * @param width Desired width of Shell.
 * @param height Desired height of Shell.
 * @since 2.0
 */
protected void setShellBounds(int x, int y, int width, int height){
	getShell().setBounds(x,y,width,height);
}

/**
 * Displays this PopUpHelper's Shell.
 * 
 * @since 2.0
 */
protected void show(){
	getShell().setVisible(true);
	tipShowing = true;
}	

}