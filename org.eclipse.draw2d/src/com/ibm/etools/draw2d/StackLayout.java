package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.draw2d.geometry.*;

/**
 * Figures using the StackLayout as their layout manager have
 * their children placed on top of one another. Order of 
 * placement is determined by the order in which the children
 * were added, first child added placed on the bottom.
 */
public class StackLayout
	extends AbstractLayout
{

public StackLayout(){}

/**
 * Calculates and returns the preferred size of the input container.
 * This is the size of the largest child of the container, as all
 * other children fit into this size.
 *
 * @param figure  Container figure for which preferred size is required.
 * @return  The preferred size of the input figure.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure figure){
	Dimension d = new Dimension();
	List children = figure.getChildren();
	IFigure child;
	for (int i=0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		d.union(child.getPreferredSize());
	}
	
	d.expand(figure.getInsets().getWidth(),
	         figure.getInsets().getHeight());
	d.union(getBorderPreferredSize(figure));
	return d;
}

/*
 * Returns the minimum size required by the input container.
 * This is the size of the largest child of the container, as all
 * other children fit into this size.
 */
public Dimension getMinimumSize(IFigure figure){
	Dimension d = new Dimension();
	List children = figure.getChildren();
	IFigure child;
	for (int i=0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		d.union(child.getMinimumSize());
	}
	d.expand(figure.getInsets().getWidth(),
	         figure.getInsets().getHeight());
	return d;
}

public Dimension getPreferredSize(IFigure figure){
	return calculatePreferredSize(figure);
}

/*
 * Lays out the children on top of each other with
 * their sizes equal to that of the available
 * paintable area of the input container figure.
 */
public void layout(IFigure figure){
	Rectangle r = figure.getClientArea();
	List children = figure.getChildren();
	IFigure child;
	for (int i=0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		child.setBounds(r);
	}
}

}