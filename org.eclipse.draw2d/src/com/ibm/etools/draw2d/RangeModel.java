package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

/**
 * 
 */
public interface RangeModel {

public static String PROPERTY_VALUE="value";//$NON-NLS-1$
public static String PROPERTY_EXTENT="extent";//$NON-NLS-1$
public static String PROPERTY_MINIMUM="minimum";//$NON-NLS-1$
public static String PROPERTY_MAXIMUM="maximum";//$NON-NLS-1$

/**
 * Registers listener as a PropertyChangeListener of this
 * RangeModel.
 */
public void addPropertyChangeListener(PropertyChangeListener listener);

/**
 * Returns the extent.
 */
public int getExtent();

/**
 * Returns the maximum value in the range.
 */
public int getMaximum();

/**
 * Returns the minimum value in the range.
 */
public int getMinimum();

/**
 * Returns the current value.
 */
public int getValue();

/**
 * Returns true if this RangeModel is enabled.
 */
public boolean isEnabled();

/**
 * Removes the given listener from this RangeModel's list
 * of PropertyChangeListeners.
 */
public void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Sets the extent.
 */
public void setExtent(int extent);

/**
 * Sets the maximum value of the range.
 */
public void setMaximum(int max);

/**
 * Sets the minimum value of the range.
 */
public void setMinimum(int min);

/**
 * Sets the current value.
 */
public void setValue(int value);

}