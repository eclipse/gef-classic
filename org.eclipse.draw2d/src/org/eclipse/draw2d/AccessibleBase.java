package org.eclipse.draw2d;

public class AccessibleBase {

public final int getAccessibleID() {
	/*This assumes that the native implementation of hashCode in Object is to
	 * return the pointer to the Object, which should be U-unique.
	 */
	int value = super.hashCode();
	/*
	 * Values -3, -2, and -1 are reserved by SWT's ACC class to have special meaning.
	 * Therefore, a child cannot have this value.
	 */
	if (value < 0)
		value -= 4;
	return value;
}

}