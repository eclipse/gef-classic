package org.eclipse.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A translatable object can be translated (or moved) vertically and/or horizontally.
 */
public interface Translatable {

/**
 * Translates this object horizontally by <code>dx</code> and vertically by 
 * <code>dy</code>.
 * 
 * @param dx The amount to translate horizontally
 * @param dy The amount to translate vertically
 * @since 2.0
 */
void performTranslate(int dx, int dy);

/**
 * Scales this object by the scale factor.
 * 
 * @param factor The scale factor
 * @since 2.0
 */
void performScale(float factor);

}