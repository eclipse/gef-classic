/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

public class TextFragmentBox
	extends FlowBox
{
public int offset;
public int length;
private int ascent;

public TextFragmentBox(){}

public int getAscent(){
	return ascent;
}

public void setAscent(int a){
	ascent = a;
}

public void setHeight(int h){
	height = h;
}

public void setWidth (int w){
	width = w;
}

}