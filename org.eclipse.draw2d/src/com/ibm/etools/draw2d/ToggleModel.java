package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * ButtonModel that supports toggle buttons.
 */
public class ToggleModel
	extends ButtonModel
{

/**
 * Notifies any ActionListeners on this ButtonModel that an action
 * has been performed. Sets this ButtonModel's selection to be 
 * the opposite of what it was.
 * 
 * @since 2.0
 */
public void fireActionPerformed(){
	setSelected(!isSelected());
	super.fireActionPerformed();
}

}