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
package org.eclipse.mylyn.zest.layouts.dataStructures;

/**
 * 
 * This class of object was created for the FadeLayoutAlgorithm
 * To represent a Cell of a QuadTree structure. Cells store nodes
 * in locations depending on there x and y values.
 * 
 * @author Keith Pilson
 *
 */
public class FadeCell {

	/** The LayoutEntity associated with a FadeNode object that contains more
	 * than one element is useless. But each FadeNode object must have a 
	 * LayoutEntity Object inside it for the clustering process**/
	private InternalNode _node;
	
	/** IsFull says if the FadeNode object contains any elements **/
	private boolean IsFull = false;
	
	/** HasChildren says if the FadeNode object is split further**/
	private boolean HasChildren = false;
	
	/** height and width are the height and width of the space that the 
	 * FadeNode object occupies **/
	private double height = 0; private double width = 0;
	
	/** start_x & start_y are the x&y positions of the bottom left of the
	 * FadeNode objects space **/
	private double start_x = 0; private double start_y = 0;
	
	/** The following contain the index in the vector of the FadeNodes four
	 * quadrants - if they have been created... **/
	private int NorthWest,NorthEast,SouthEast,SouthWest;
	
	/** Contains the number of entities in the FadeNode object. **/
	private int NumberOfElements = 0;
	
	/** the average position of the nodes, based on the positions of the entities it
	 * contains **/
	private double average_x = 0;
	private double average_y = 0;
	
	/** Index of parent FadeNode object in the vector **/
	private int IndexOfParent = 0;
	
	/** location is the 'scaled-down' location of the entites within the FadeNode
	 * object. It is a sum of all the scaled-down locations of the entities
	 * within the FadeNode object. To use it we must divide this field by the
	 * number of elements in the FadeNode object. **/
	private DisplayIndependentPoint location = new DisplayIndependentPoint(0,0);
	
	/** Constructors **/
	public FadeCell(InternalNode _layoutentity)
	{
		_node = _layoutentity;
	}
	public FadeCell()
	{
		HasChildren = false;
		IsFull = false;
		height = 0; width = 0;
		start_x = 0; start_y = 0;
		NumberOfElements = 0;
		average_x = 0;
		average_y = 0;
		IndexOfParent = 0;
	}
	
	public FadeCell(double ht, double wth)
	{
		height = ht;
		width = wth;
	}
	
	/** The following are self explanatory methods to get and
	 * set the various fields of the class **/

	public void SetLocation(DisplayIndependentPoint p)
	{
		location = p;
	}
	
	public DisplayIndependentPoint GetLocation()
	{
		return location;
	}
	
	public double GetAverageX()
	{
		return average_x;
	}
	
	public double GetAverageY()
	{
		return average_y;
	}
	
	public void SetAverageX(double avx)
	{
		average_x = avx;
	}

	public void SetAverageY(double avy)
	{
		average_y = avy;
	}
	
	public int GetNumElements()
	{
		return NumberOfElements;
	}
	
	public void SetNumElements(int num)
	{
		NumberOfElements = num;
	}
	
	public int GetIndexOfParent()
	{
		return IndexOfParent;
	}
	
	public void SetIndexOfParent(int parent)
	{
		IndexOfParent = parent;
	}
	
	public boolean IsFull()
	{
		return IsFull;
	}
	
	public void SetFull()
	{
		IsFull = true;
	}
	
	public void SetEmpty()
	{
		IsFull = false;
	}
	
	public void SetNode(InternalNode layoutent)
	{
		_node = layoutent;
	}
	
	public InternalNode GetNode()
	{
		return _node;
	}
	
	public boolean HasChildren()
	{
		return HasChildren;
	}

	public void setChildren(boolean setchildren)
	{
		HasChildren = setchildren;
	}
	
	public void SetHeight(double ht)
	{
		height = ht;
	}
	
	public void SetWidth(double wt)
	{
		width = wt;
	}
	
	public double GetHeight()
	{
		return height;
	}
	
	public double GetWidth()
	{
		return width;
	}

	public void SetX(double x)
	{
		start_x = x;
	}
	
	public void SetY(double y)
	{
		start_y = y;
	}
	
	public double GetX()
	{
		return start_x;
	}
	
	public double GetY()
	{
		return start_y;
	}

	public void setNW(int nw)
	{
		NorthWest = nw;
	}

	public void setNE(int ne)
	{
		NorthEast = ne;
	}
	
	public void setSE(int se)
	{
		SouthEast = se;
	}
	
	public void setSW(int sw)
	{
		SouthWest = sw;
	}
	
	public int getNW()
	{
		return NorthWest;
	}
	
	public int getNE()
	{
		return NorthEast;
	}
	
	public int getSE()
	{
		return SouthEast;
	}
	
	public int getSW()
	{
		return SouthWest;
	}
	
}

