package com.ibm.etools.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Provides support for side space measurements for 
 * {@link com.ibm.etools.draw2d.Border Borders}
 */
public class Insets
	implements Cloneable, java.io.Serializable
{
public int left, top, bottom, right;

static final long serialVersionUID = 1;

/**
 * Constructs an Insets with left, top, bottom and right
 * empty.
 * 
 * @since 2.0
 */
public Insets(){}

/**
 * Constructs an Insets with the initial values supplied
 * by the input Insets.
 *
 * @param i  Insets supplying the initial values.
 * @since 2.0
 */
public Insets(Insets i){this(i.top, i.left, i.bottom, i.right);}

/**
 * Constructs an Insets with all the sides set to the size
 * given as input.
 *
 * @param i  Value applied to all the sides of the Insets.
 * @since 2.0
 */
public Insets(int i){this(i,i,i,i);}

/**
 * Constructs an Insets with the initial values set to the values
 * given as input.
 *
 * @param top  Value of the top space.
 * @param left  Value of the left space.
 * @param bottom  Value of the bottom space.
 * @param right  Value of the right space.
 * @since 2.0
 */
public Insets(int top, int left, int bottom, int right){
	this.top = top;
	this.left=left;
	this.bottom = bottom;
	this.right = right;
}

/**
 * Adds the input Insets to this Insets and returns this
 * Insets for convenience.
 *
 * @since 2.0
 */
public Insets add(Insets insets){
	top    += insets.top;
	bottom += insets.bottom;
	left   += insets.left;
	right  += insets.right;
	return this;
}

/**
 * Returns whether this Insets is equal to the Object input.
 * The Insets are equal if their top, left, bottom, and 
 * right values are equivalent.
 *
 * @param o  Object being tested for equality.
 * @return  boolean giving the result of the equality.
 * @since 2.0
 */
public boolean equals(Object o){
	if (o instanceof Insets){
		Insets i = (Insets)o;
		return i.top == top &&
			 i.bottom == bottom &&
			 i.left == left &&
			 i.right == right;
	}
	return false;
}

/**
 * Returns a new Insets containing the incremented values of
 * this Insets and the input Insets.
 *
 * @param insets  Insets providing the increment values.
 * @return  A new Insets containing the new values.
 * @since 2.0
 */
public Insets getAdded(Insets insets){
	return new Insets(this).add(insets);
}

/**
 * Returns the height for this Insets, which
 * is this Inset's top and bottom values
 * addded together.
 *
 * @return The height of this Insets.
 * @see  #getWidth()
 * @since 2.0
 */
public int getHeight(){return top+bottom;}

/**
 * Returns a new Insets with transposed values.
 * Top and Left are transposed.
 * Bottom and Right are transposed.
 *
 * @return  New Insets with the transposed values.
 * @since 2.0
 */
public Insets getTransposed(){return new Insets(this).transpose();}

/**
 * Returns the width for this Insets which is the 
 * left value added to the right.
 *
 * @return The width of this Insets.
 * @see  #getHeight()
 * @since 2.0
 */
public int getWidth(){return left+right;}

/**
 * Returns whether this Insets has a value of zero
 * for its top, left, right, and bottom values. 
 *
 * @return Returns a <code>true</code> if all the 
 *          spaces are zero, else returns <code>false</code>
 * @since 2.0
 */
public boolean isEmpty(){
	return (
		left == 0 &&
		right== 0 &&
		top  == 0 &&
		bottom==0);
}

/**
 * Returns the description of this Insets as a String.
 *
 * @return  Desription.
 * @since 2.0
 */
public String toString(){
	return "Insets(t="+top+",l="+left+ //$NON-NLS-2$//$NON-NLS-1$
		 ",b="+bottom+",r="+right+")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Interchanges the right-bottom and top-left spaces for this
 * Insets and returns this Insets for convinience. Can be used 
 * in orientation changes.
 *
 * @return This Insets with the transposed sides.
 * @since 2.0
 */
public Insets transpose(){
	int temp = top;
	top = left;
	left = temp;
	temp = right;
	right = bottom;
	bottom = temp;
	return this;
}

}