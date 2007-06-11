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
package org.eclipse.mylyn.zest.layouts.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.mylyn.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.mylyn.zest.layouts.dataStructures.FadeCell;
import org.eclipse.mylyn.zest.layouts.dataStructures.InternalNode;
import org.eclipse.mylyn.zest.layouts.dataStructures.InternalRelationship;
/**
 * 
 * @author Keith Pilson
 * @author Ian Bull
 *
 */
public class FadeLayoutAlgorithm extends ContinuousLayoutAlgorithm {
    private final static String ATTR_TEMP_LOCATION = "spring-temp-location";	
	private final static String ATTR_ANCHOR = "spring-anchor";
	private final static String ATTR_FORCE = "spring-force";
    private final static double CELL_WIDTH = 20;
    private final static double CELL_HEIGHT = 20;
    
	private static ArrayList nodeVector = new ArrayList();
	
    /**
     * The default value for the spring layout number of iterations.
     */
    public static final int DEFAULT_SPRING_ITERATIONS = 1000;

    /**
     * The default value for positioning nodes randomly.
     */
    public static final boolean DEFAULT_SPRING_RANDOM = true;

    /**
     * The default value for ignoring unconnected nodes.
     */
    public static final boolean DEFAULT_SPRING_IGNORE_UNCON = true;

    /**
     * The default value for separating connected components.
     */
    //public static final boolean DEFAULT_SPRING_SEPARATE_COMPONENTS = true;

    /**
     * The default value for the spring layout move-control.
     */
    public static final double DEFAULT_SPRING_MOVE = 1.9f;

    /**
     * The default value for the spring layout strain-control.
     */
    public static final double DEFAULT_SPRING_STRAIN = 0.5f;

    /**
     * The default value for the spring layout length-control.
     */
    public static final double DEFAULT_SPRING_LENGTH = 2.8f;

    /**
     * The default value for the spring layout gravitation-control.
     */
    public static final double DEFAULT_SPRING_GRAVITATION = 0.3f;

    /**
     * The variable can be customized to set the
     * number of iterations used.
     */
    private static int sprIterations = DEFAULT_SPRING_ITERATIONS;

    /**
     * The variable can be customized to set whether or not the spring layout
     * nodes are positioned randomly before beginning iterations.
     */
    private static boolean sprRandom = DEFAULT_SPRING_RANDOM;

	/**
	 * Minimum distance considered between nodes
	 */
	protected static final double MIN_DISTANCE = 1.0d;

	/**
	 * An arbitrarily small value in mathematics.
	 */
	protected static final double EPSILON = 0.001d;

    /**
     * The variable can be customized to set the spring layout
     * move-control.
     */
    private static double sprMove = DEFAULT_SPRING_MOVE;

    /**
     * The variable can be customized to set the spring layout
     * strain-control.
     */
    private static double sprStrain = DEFAULT_SPRING_STRAIN;

    /**
     * The variable can be customized to set the spring layout
     * length-control.
     */
    private static double sprLength = DEFAULT_SPRING_LENGTH;

    /**
     * The variable can be customized to set the spring layout
     * gravitation-control.
     */
    private static double sprGravitation = DEFAULT_SPRING_GRAVITATION;

    /**
     * The largest movement of all vertices that has occured in
     * the most recent iteration. 
     */
    private double largestMovement = 0;

	private boolean finished;

   private int iteration;

   /**
    * Constructor.
    */
    public FadeLayoutAlgorithm ( int styles ) {
    	super ( styles );
	}

    
    /**
     * Creates a Fad Layout algorithm with no style.
     */
    public FadeLayoutAlgorithm() {
    	this( LayoutStyles.NONE );
	}
    
    /**
     * Sets the spring layout move-control.
     * @param move The move-control value.
     */
    public void setSpringMove(double move) {
		sprMove = move;
    }

    /**
     * Returns the move-control value of this ShrimpSpringLayoutAlgorithm in double presion.
     * @return The move-control value.
     */
    public double getSpringMove() {
		return sprMove;
    }

    /**
     * Sets the spring layout strain-control.
     * @param strain The strain-control value.
     */
    public void setSpringStrain(double strain) {
		sprStrain = strain;
    }

    /**
     * Returns the strain-control value of this ShrimpSpringLayoutAlgorithm
     * in double presion.
     * @return The strain-control value.
     */
    public double getSpringStrain() {
		return sprStrain;
    }

    /**
     * Sets the spring layout length-control.
     * @param length The length-control value.
     */
    public void setSpringLength(double length) {
		sprLength = length;
    }

    /**
     * Returns the length-control value of this ShrimpSpringLayoutAlgorithm
     * in double presion.
     * @return The length-control value.
     */
    public double getSpringLength() {
		return sprLength;
    }

    /**
     * Sets the spring layout gravitation-control.
     * @param gravitation The gravitation-control value.
     */
    public void setSpringGravitation(double gravitation) {
		sprGravitation = gravitation;
    }

    /**
     * Returns the gravitation-control value of this ShrimpSpringLayoutAlgorithm
     * in double presion.
     * @return The gravitation-control value.
     */
    public double getSpringGravitation() {
		return sprGravitation;
    }

    /**
     * Sets the number of iterations to be used.
     * @param gravitation The number of iterations.
     */
    public void setIterations(int iterations) {
		sprIterations = iterations;
    }

    /**
     * Returns the number of iterations to be used.
     * @return The number of iterations.
     */
    public int getIterations() {
		return sprIterations;
    }

    /**
     * Sets whether or not this Layout Algorithm will
     * layout the nodes randomly before beginning iterations.
     * @param random The random placement value.
     */
    public void setRandom(boolean random) {
		sprRandom = random;
    }

    /**
     * Returns whether or not this Layout Algorithm will
     * layout the nodes randomly before beginning iterations.
     */
    public boolean getRandom() {
		return sprRandom;
    }

    /**
     * Sets the default conditions.
     */
    public void setDefaultConditions() {
		 sprMove = DEFAULT_SPRING_MOVE;
		 sprStrain = DEFAULT_SPRING_STRAIN;
		 sprLength = DEFAULT_SPRING_LENGTH;
		 sprGravitation = DEFAULT_SPRING_GRAVITATION;
		 sprIterations = DEFAULT_SPRING_ITERATIONS;
    }
    
  	  
  
	protected boolean performAnotherNonContinuousIteration() {
		// TODO Auto-generated method stub
		return (iteration<=sprIterations && largestMovement >= sprMove);
	}

	
	
	protected void computeOneIteration(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
		
		
		width = width == 0 ? 400 : width;
		height = height == 0 ? 400 : height;
		
		clusterNodes(entitiesToLayout, width, height);
		
		for( int i = 0; i < entitiesToLayout.length; i++ ) {
			InternalNode currentNode = entitiesToLayout[i];
			currentNode.setInternalLocation( currentNode.getDx(), currentNode.getDy() );
			/*
            try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		handleSelectedNodes(entitiesToLayout,new DisplayIndependentRectangle (x, y, width, height));
		computeForces(entitiesToLayout, relationshipsToConsider);
		largestMovement = Double.MAX_VALUE;
		computePositions(entitiesToLayout);
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
			node.setInternalLocation(node.getDx(), node.getDy());
		}
		
		defaultFitWithinBounds(entitiesToLayout, new DisplayIndependentRectangle (x, y, width, height));
		//fireProgressEvent (iteration, sprIterations);
		iteration++;
		
		// Yield every now and then
		try {
			//Thread.yield();
			Thread.sleep(50);
		} catch (Exception e) {
		}
	}
	
	
	
	
	private void handleSelectedNodes( InternalNode[] entitiesToLayout, DisplayIndependentRectangle realBounds ) {
    	DisplayIndependentRectangle layoutBounds = getLayoutBounds(entitiesToLayout, false );
		for ( int i = 0; i < entitiesToLayout.length; i++ ) {
			InternalNode currentNode = entitiesToLayout[i];
			if ( currentNode.hasPreferredLocation() ) {
				DisplayIndependentPoint currentPoint =
			    	new DisplayIndependentPoint(currentNode.getPreferredX(), currentNode.getPreferredY());
				DisplayIndependentPoint localPoint = currentPoint.convert(realBounds, layoutBounds);
				currentNode.setDx( localPoint.x );
				currentNode.setDy( localPoint.y );
			}			
		}
	}


	protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
		if ( asynchronous && continueous ) return true;
		else if ( asynchronous && !continueous ) return true;
		else if ( !asynchronous && continueous ) return false;
		else if ( !asynchronous && !continueous ) return true;
		
		return false;
	}

	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
		preCompute (entitiesToLayout,(int)width,(int)height);
		
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
		reset(entitiesToLayout);
	}

	protected int getTotalNumberOfLayoutSteps() {
		// TODO Auto-generated method stub
		return sprIterations;
	}

	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return iteration;
	}

    /**
     * Clean up after done
     * @param entitiesToLayout
     */
    private void reset(InternalNode [] entitiesToLayout) {
		for (int i = 0; i < entitiesToLayout.length; i++) {
			InternalNode node = entitiesToLayout[i];
			node.setAttributeInLayout(ATTR_ANCHOR, null);
			node.setAttributeInLayout(ATTR_FORCE, null);
			node.setAttributeInLayout(ATTR_TEMP_LOCATION, null);
		}
		//setDefaultConditions();
    }
    
//    /**
//     * Calculates and applies the positions of the given entities based on a spring layout using
//     * the given relationships. 
//     */
//	public void applyLayout(List entitiesToLayout, List relationshipsToConsider, double x, double y, double width, double height) {
//		
//		// Filter out any non-wanted entities and relationships
//		super.applyLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height);
//
//		// do the calculations
//		preCompute (entitiesToLayout);
//		while (!finished) {		    
//	    	/** Cluster the nodes. Put them in a Quad-Tree Structure **/
//
//			computeOneIteration(entitiesToLayout,relationshipsToConsider, x, y, width, height);
//		}
//		
//	}

	
	/**
	 * Sets the forces for the relationship
	 * @param relationships the List of relationships to consider
	 */ 
	private void setEdgeForces(InternalRelationship [] relationships) {
	
		// Create an array to hold the forces
		double edgeForces[] = new double[ relationships.length ];
		double maxForce = 0.0;
		
	 	for (int i = 0; i < relationships.length; i++) {
			
	 		InternalRelationship relationship = relationships[i];
			InternalNode source = relationship.getSource();
			InternalNode destination = relationship.getDestination();
			
			DisplayIndependentPoint srcLocation = new DisplayIndependentPoint( source.getDx(), source.getDy() );
			DisplayIndependentPoint destLocation = new DisplayIndependentPoint( destination.getDx(), destination.getDy() );
			
//			DisplayIndependentPoint srcForce = getForce(source);//.clone();
//			DisplayIndependentPoint destForce = getForce(destination);//.clone();
			
			
			
			double dx = srcLocation.x - destLocation.x;
			double dy = srcLocation.y - destLocation.y;
			double distance = Math.sqrt(dx*dx + dy*dy);
			//double distance = 1.0;
			//double distance_sq = distance*distance;
			
			//TODO: Fix the spring tension.  This is a partial fix
			

			double f = sprStrain * Math.log (distance/sprLength);
			//double f = relationship.getWeight();
			edgeForces[ i ] = f;
			maxForce = Math.abs( f ) > maxForce ? Math.abs( f )  : maxForce;
	 	}
	 	if ( maxForce < 0.0001 ) {
	 		// @tag bug(113031)
	 		maxForce = 0.0001;
	 	}
	 	
	 	normalizeArray( edgeForces, maxForce );
	 	applyEdgeWeight(edgeForces, relationships );
	 	multiplyArray( edgeForces, maxForce );
	 	
		for ( int i = 0; i < relationships.length; i++ ) {
			double f = edgeForces [ i ];
			
			InternalRelationship relationship = relationships[i];
			InternalNode source = relationship.getSource();
			InternalNode destination = relationship.getDestination();
			
			DisplayIndependentPoint srcLocation = new DisplayIndependentPoint( source.getDx(), source.getDy() );
			DisplayIndependentPoint destLocation = new DisplayIndependentPoint( destination.getDx(), destination.getDy() );
			
			DisplayIndependentPoint srcForce = getForce(source);//.clone();
			DisplayIndependentPoint destForce = getForce(destination);//.clone();
			
			double dx = srcLocation.x - destLocation.x;
			double dy = srcLocation.y - destLocation.y;
			double distance = Math.sqrt(dx*dx + dy*dy);
			
			double fx = (f * dx/distance);
			double fy = (f * dy/distance);
			
		    destForce.x = (destForce.x + fx);
			destForce.y = (destForce.y + fy);
			
			setForce (destination, destForce);
			
			srcForce.x = srcForce.x - fx;
			srcForce.y = srcForce.y - fy;
			setForce (source, srcForce);
			//System.out.println("Dest Force: " + destForce + " Distance: " + distance);
			//System.out.println("Source Force: " + srcForce);
			}
	 }
	
	private void normalizeArray( double[] numbers, double maxNumber ) {
		for (int i = 0; i < numbers.length; i++) {
			numbers[ i ] /= maxNumber;
		}
	}
	private void applyEdgeWeight( double[] numbers, InternalRelationship [] relationships ) {
		
		for ( int i = 0; i < numbers.length; i++ ) {
			InternalRelationship relationship = relationships[ i ];
			double edgeWeight = relationship.getWeight();
			numbers[ i ] *= edgeWeight; 
		}
	}
	
	private void multiplyArray( double[] numbers, double factor ) {
		for (int i = 0; i < numbers.length; i++) {
			numbers[ i ] *= factor;
		}
	}
	
    private void preCompute (InternalNode [] entitiesToLayout, int width, int height) {		
	
    	//TODO: Fix node with to be something more resonable
    	int node_width = 10;
    	int node_height = 10;
    	
		width = width == 0 ? 100 : width;
		height = height == 0 ? 100 : height;
		
		if (sprRandom)  {
			
			placeRandomly(entitiesToLayout,width,height,node_width,node_height);  //put vertices in random places
			placeRandomly(entitiesToLayout);
		}
		else 
			convertToUnitCoordinates(entitiesToLayout,width, height,node_width,node_height);
				
		iteration = 1;
		finished = false;
		largestMovement = Double.MAX_VALUE;
    }
    
    /** clusterNodes(List entitiesToLayout) takes the enitites and 
     * stores them in a vector. This data structure is one way of
     * representing a Quad-Tree.  **/
    private void clusterNodes(InternalNode [] entitiesToLayout, double width, double height)
    {
    	FadeCell rootnode = new FadeCell();
    	/** Empty the vector each time around to start **/
    	nodeVector.clear();
    	/** Put the root node into the nodeVector after
    	 * setting the height and width etc..**/
        rootnode.SetHeight(width);
        rootnode.SetWidth(height);
        rootnode.SetIndexOfParent(-1);
    	nodeVector.add(rootnode);
                
    	/** Cluster nodes(aka entities) one at a time **/
    	for (int i = 0; i < entitiesToLayout.length; i++) {
    		InternalNode layoutEntity_Cluster = entitiesToLayout[i];
    		addNodeToCluster(layoutEntity_Cluster);
    	}
    	
//    	/** Debug. We want to print out the details of each cell **/
//    	for (int i = 0; i < nodeVector.size(); i++) {
//    		FadeNode fadecell = (FadeNode)nodeVector.elementAt(i);
//    		printcell(fadecell,i);
//    	}
    	
    }
     

        
    /** How to add a node into the vector **/
    private void addNodeToCluster(InternalNode layoutEntity_Cluster)
    {
    	
    	/** locat stores the average scaled-down x and y location for each cluster. The scaling down is
    	 * done as per the SpringLayoutAlgorithm and is required for computing non-edge forces. Two
    	 * possible ways of computing non-edge forces are available here so this way could potentially
    	 * be redundant.**/
    	//DisplayIndependentPoint locat = (DisplayIndependentPoint)getTempLocation(layoutEntity_Cluster);//.clone();
    	DisplayIndependentPoint locat = new DisplayIndependentPoint( layoutEntity_Cluster.getDx(), layoutEntity_Cluster.getDy() );
    	
    	
    	/** indexofInterest will contain the location(index in vector) to try put
          * an entity **/
       int indexofInterest = 0; int parentIndex = -1;
    	
    	/** First thing to do is to find which cell to try
    	 * put the entity into. It must have no children **/
    	FadeCell tempfadenode = (FadeCell)nodeVector.get(indexofInterest);
    	
    	while (tempfadenode.HasChildren())
    	{
    		/** if point is in NW or SW quadrants **/
    		if (((layoutEntity_Cluster.getDx()+ CELL_WIDTH)>=tempfadenode.GetX()) && ( (layoutEntity_Cluster.getDx()+ CELL_WIDTH) < (tempfadenode.GetX() +  (tempfadenode.GetWidth()/2))))
    		{
    			/** if point is in SW quadrant **/
    			
    			if ( layoutEntity_Cluster.getDy() + CELL_HEIGHT>= tempfadenode.GetY() && (layoutEntity_Cluster.getDy() + CELL_HEIGHT < (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) ) 
    			{
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getSW();
    			}
    			/** else point is in NW **/
    			else if ( ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >= tempfadenode.GetY())&& ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >  (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) )
  
    			{
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getNW();
    			}
    		
    			else { 
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getNW();
    						

    			}
    		
    		}
    		else if (((layoutEntity_Cluster.getDx()+ CELL_WIDTH) >= tempfadenode.GetX()) && ( (layoutEntity_Cluster.getDx()+ CELL_WIDTH) > (tempfadenode.GetX() +  (tempfadenode.GetWidth()/2))))
    		/** else do this when point is in NE or SE quadrants **/
    		{
    			/** if point is in SE quadrant **/
    			//if (((layoutEntity_Cluster.getInternalY() + CELL_HEIGHT) >= tempfadenode.GetY())&& ((layoutEntity_Cluster.getInternalY() + CELL_HEIGHT) <  (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))))
    			if ( layoutEntity_Cluster.getDy() + CELL_HEIGHT >= tempfadenode.GetY() && (layoutEntity_Cluster.getDy() + CELL_HEIGHT < (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) )
    			{
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getSE();
    			}
    			/** else point is in NE **/
    			else if ( ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >= tempfadenode.GetY())&& ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >  (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) )
    			{
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getNE();
    			}
    			
    			else { 
    				//System.out.println("Bad Location [2]");
    				parentIndex = indexofInterest;
    				indexofInterest = tempfadenode.getNE();
    			} 
    			   			
    		}
    		else {
    				//System.out.println("Bad Location [3]");
    				//System.exit(0);
        			if ( layoutEntity_Cluster.getDy() + CELL_HEIGHT >= tempfadenode.GetY() && (layoutEntity_Cluster.getDy() + CELL_HEIGHT < (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) )
        			{
        				parentIndex = indexofInterest;
        				indexofInterest = tempfadenode.getSE();
        			}
        			/** else point is in NE **/
        			else if ( ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >= tempfadenode.GetY())&& ((layoutEntity_Cluster.getDy() + CELL_HEIGHT) >  (tempfadenode.GetY() + (tempfadenode.GetHeight()/2))) )
        			{
        				parentIndex = indexofInterest;
        				indexofInterest = tempfadenode.getNE();
        			}
        			
        			else { 
        				//System.out.println("Bad Location [3-2]");
        				parentIndex = indexofInterest;
        				indexofInterest = tempfadenode.getNE();
        			} 

    		}
    		
    		tempfadenode = (FadeCell)nodeVector.get(indexofInterest);
    	}/* End While */
    	
    	/** We now know the index in the vector, of the cell we want to try
    	 * place our FadeNode object into. We now need to find out if the cell is empty
    	 * or full. If its empty we can put the SimpleNode object into the cell. If its
    	 * full we need to split the cell into four quadrants and then recursively
    	 * call the addNodeToCluster method **/
    	
    	
    	if (tempfadenode.IsFull())
    	{
    		/** Split the cell and then call addNodeToCluster() again. Make sure the node
    		 * that initially was in the cell is kept where it was in the appropriate 
    		 * newly created quadrant **/
    	   		
    		/** First create the NorthWest Cell **/
    		FadeCell transientfadenode = new FadeCell();
    		
    		/** Set height,width,start_x & start_y etc..**/
    		transientfadenode.SetHeight(tempfadenode.GetHeight()/2);    		
    		transientfadenode.SetWidth(tempfadenode.GetWidth()/2);
    		transientfadenode.SetIndexOfParent(indexofInterest);
    		transientfadenode.SetX(tempfadenode.GetX());
    		transientfadenode.SetY(tempfadenode.GetY() + transientfadenode.GetHeight());
    		
    		/** Next we add this cell to the vector 
    		 * First update its parent cell. change haschildren to true
    		 * and put in the index of the NorthWest cell**/
    		
    		tempfadenode.setChildren(true);
    		tempfadenode.setNW(nodeVector.size());
    		nodeVector.add(transientfadenode);
    		
    		/** Now repeat the steps for the other three quadrants
    		 * the height and width are the same for all **/
    		    		
    		/** Create the NorthEest Cell **/
    		FadeCell NEfadenode = new FadeCell();
    		
    		/** Set height,width,start_x & start_y **/
    		NEfadenode.SetHeight(tempfadenode.GetHeight()/2);
    		NEfadenode.SetWidth(tempfadenode.GetWidth()/2);
    		NEfadenode.SetIndexOfParent(indexofInterest);
    		NEfadenode.SetX(tempfadenode.GetX() + NEfadenode.GetWidth());
    		NEfadenode.SetY(tempfadenode.GetY() + NEfadenode.GetHeight());
    		    		
    		tempfadenode.setNE(nodeVector.size());
    		nodeVector.add(NEfadenode);  		
    		    		
    		/** Create the SouthEest Cell **/
    		FadeCell SEfadenode = new FadeCell();
    		
    		/** Set height,width,start_x & start_y **/
    		SEfadenode.SetHeight(tempfadenode.GetHeight()/2);
    		SEfadenode.SetWidth(tempfadenode.GetWidth()/2);
    		SEfadenode.SetIndexOfParent(indexofInterest);
    		SEfadenode.SetX(tempfadenode.GetX() + SEfadenode.GetWidth());
    		SEfadenode.SetY(tempfadenode.GetY());
    		    		
    		tempfadenode.setSE(nodeVector.size());
    		nodeVector.add(SEfadenode);  		
    		
    		/** Create the SouthWest Cell **/
    		FadeCell SWfadenode = new FadeCell();
    		
    		/** Set height,width,start_x & start_y for SW quadrant**/
    		SWfadenode.SetHeight(tempfadenode.GetHeight()/2);
    		SWfadenode.SetWidth(tempfadenode.GetWidth()/2);
    		SWfadenode.SetIndexOfParent(indexofInterest);
    		SWfadenode.SetX(tempfadenode.GetX());
    		SWfadenode.SetY(tempfadenode.GetY());   		
    		
    		tempfadenode.setSW(nodeVector.size());
    		nodeVector.add(SWfadenode);  		
    		
    		/** Save tempfadenode back into the vector **/
    		nodeVector.remove(indexofInterest);
    		nodeVector.add(indexofInterest, tempfadenode);
    		
    		
    		/** Now the cell is split, we must put the object residing in the parent cell into
    		 * the appropriate quadrant without incrementing any counters. **/
    		
    		if (tempfadenode.GetAverageX() < tempfadenode.GetX() + tempfadenode.GetWidth()/2)
    		{
    			/** Its Either NorthWest or South West **/
    			if(tempfadenode.GetAverageY() < tempfadenode.GetY() + tempfadenode.GetHeight()/2)
    			{
    				/** We must place it in the South West Qudrant **/
    				FadeCell transfadenode = (FadeCell)nodeVector.get(tempfadenode.getSW());
    				transfadenode.SetNode(tempfadenode.GetNode());
    				transfadenode.SetFull();
    				transfadenode.SetNumElements(1);
    				transfadenode.SetLocation(tempfadenode.GetLocation());
    				transfadenode.SetAverageX(tempfadenode.GetAverageX());
    				transfadenode.SetAverageY(tempfadenode.GetAverageY());
    				nodeVector.remove(tempfadenode.getSW());
    				nodeVector.add(tempfadenode.getSW(),transfadenode);
    			}
    			else
    			{
    				/** We must place it in the North West Quadrant **/
    				FadeCell transfadenode = (FadeCell)nodeVector.get(tempfadenode.getNW());
    				transfadenode.SetNode(tempfadenode.GetNode());
    				transfadenode.SetFull();
    				transfadenode.SetNumElements(1);
    				transfadenode.SetLocation(tempfadenode.GetLocation());
    				transfadenode.SetAverageX(tempfadenode.GetAverageX());
    				transfadenode.SetAverageY(tempfadenode.GetAverageY());
    				nodeVector.remove(tempfadenode.getNW());
    				nodeVector.add(tempfadenode.getNW(), transfadenode);
    			}
    		}
    		else
    		{
    			/** Its either NorthEast or SouthEast **/
    			if(tempfadenode.GetAverageY() < tempfadenode.GetY() + tempfadenode.GetHeight()/2)
    			{
    				/** We must place it in the South East Qudrant **/
    				FadeCell transfadenode = (FadeCell)nodeVector.get(tempfadenode.getSE());
    				transfadenode.SetNode(tempfadenode.GetNode());
    				transfadenode.SetFull();
    				transfadenode.SetNumElements(1);
    				transfadenode.SetLocation(tempfadenode.GetLocation());
    				transfadenode.SetAverageX(tempfadenode.GetAverageX());
    				transfadenode.SetAverageY(tempfadenode.GetAverageY());
    				nodeVector.remove(tempfadenode.getSE());
    				nodeVector.add(tempfadenode.getSE(),transfadenode);
    			}
    			else
    			{
    				/** We must place it in the North East Quadrant **/
    				FadeCell transfadenode = (FadeCell)nodeVector.get(tempfadenode.getNE());
    				transfadenode.SetNode(tempfadenode.GetNode());
    				transfadenode.SetFull();
    				transfadenode.SetNumElements(1);
    				transfadenode.SetLocation(tempfadenode.GetLocation());
    				transfadenode.SetAverageX(tempfadenode.GetAverageX());
    				transfadenode.SetAverageY(tempfadenode.GetAverageY());
    				nodeVector.remove(tempfadenode.getNE());
    				nodeVector.add(tempfadenode.getNE(), transfadenode);
    			}
    		}
    	    		
    		/** The final step after splitting the cell is to call addNodeToCluster again
    		 * with the layout entity we were originally trying to place in the cluster **/
    		
    		addNodeToCluster(layoutEntity_Cluster);
    	}
    	else
    		/** Cell is empty so place it in nodeVector(IndexofInterest) **/
    	{
    		/** Place the SimpleNode object in nodeVector(IndexofInterest) 
    		 * and then update all parents counts and x&y averages **/
    		
    		/** First update tempfadenode's fields before inserting **/
    		
    		tempfadenode.SetNode(layoutEntity_Cluster);
    		tempfadenode.SetFull();
    		tempfadenode.SetNumElements(1);
    		
    		tempfadenode.SetLocation(locat);
    	
    		/** The average x and y of this cell is the x and y value
    		 * of the object that is being placed in it as this is the only 
    		 * one in the cell **/
    		tempfadenode.SetAverageX(layoutEntity_Cluster.getDx() + CELL_WIDTH);
    		tempfadenode.SetAverageY(layoutEntity_Cluster.getDy() + CELL_HEIGHT);
    		
    	    		
    		/** Now add the FadeNode object into the vector
    		 * at the correct index **/
    		nodeVector.remove(indexofInterest);
    		nodeVector.add(indexofInterest, tempfadenode);
    		
    		/** Now update the average x and y positions and number of elements of
    		 * immediate parent and all other parents **/
    	    double totalx = 0 ; double totaly = 0;
    	    int fullcellcount = 0;
    		while(parentIndex >= 0)
    		{
    			/** Add one to the count of nodes **/
    			FadeCell tempparentfadenode = (FadeCell)nodeVector.get(parentIndex);
    			tempparentfadenode.SetNumElements(tempparentfadenode.GetNumElements()+1);
    			
    			/** Next add the DisplayIndependentPoint location to the cells total
    			 * location **/
    			tempparentfadenode.SetLocation(new DisplayIndependentPoint(tempparentfadenode.GetLocation().x + locat.x,tempparentfadenode.GetLocation().y + locat.y));
    			
    			/** Update average x and y value of cell **/
    			FadeCell updateNWfadenode = (FadeCell)nodeVector.get(tempparentfadenode.getNW());
    			totalx = totalx + updateNWfadenode.GetAverageX();
    			totaly = totaly + updateNWfadenode.GetAverageY();
    			if (updateNWfadenode.IsFull() || updateNWfadenode.HasChildren())
    				fullcellcount = fullcellcount + 1;
    			FadeCell updateNEfadenode = (FadeCell)nodeVector.get(tempparentfadenode.getNE());
    			totalx = totalx + updateNEfadenode.GetAverageX();
    			totaly = totaly + updateNEfadenode.GetAverageY();
    			if (updateNEfadenode.IsFull() || updateNWfadenode.HasChildren())
    				fullcellcount = fullcellcount + 1;
    			FadeCell updateSEfadenode = (FadeCell)nodeVector.get(tempparentfadenode.getSE());
    			totalx = totalx + updateSEfadenode.GetAverageX();
    			totaly = totaly + updateSEfadenode.GetAverageY();
    			if (updateSEfadenode.IsFull() || updateNWfadenode.HasChildren())
    				fullcellcount = fullcellcount + 1;
    			FadeCell updateSWfadenode = (FadeCell)nodeVector.get(tempparentfadenode.getSW());
    			totalx = totalx + updateSWfadenode.GetAverageX();
    			totaly = totaly + updateSWfadenode.GetAverageY();
    			if (updateSWfadenode.IsFull() || updateNWfadenode.HasChildren())
    				fullcellcount = fullcellcount + 1;
    			
    			/** Now we have the total x and y from each quadrant we need to divide it by the
    			 * number of filled cells **/
    			totalx = (totalx / fullcellcount);
    			totaly = (totaly / fullcellcount);
    			tempparentfadenode.SetAverageX(totalx);
    			tempparentfadenode.SetAverageY(totaly);
    			
    			/** Save the tempparentfadenode back into the vector **/
    			nodeVector.remove(parentIndex);
    			nodeVector.add(parentIndex, tempparentfadenode);
    			
    			/** Reset the x and y & count summation variables **/
    			totalx=0; totaly=0; fullcellcount = 0;
    			
    			/** Finally make parentIndex the index of the Parent of the
    			 * Current Parent. **/
    			parentIndex = tempparentfadenode.GetIndexOfParent();
    		}
    	}	
    	//System.out.println("Edning AddCluster to node");
    }
    
    /* irbull
	private void computeOneIteration (List entitiesToLayout,List relationshipsToConsider, double x, double y, double width, double height) {
		if (iteration<=sprIterations && largestMovement >= sprMove && !cancelled) {
			computeForces(entitiesToLayout,relationshipsToConsider);
			largestMovement = Double.MAX_VALUE;
			computePositions(entitiesToLayout);
			defaultFitWithinBounds(entitiesToLayout, new DisplayIndependentRectangle (x, y, width, height));
			fireProgressEvent (iteration, sprIterations);
			iteration++;
		} else {
			finished = true;
		}
	 }
	 */

    protected boolean isFinished () {
    	return finished;
    }
    
    /** Note, If identical places are given the program will go into an infinite loop
     * trying to cluster the nodes. But..
     * the chances of this happening due to the generation of
     * random locations are very slim so we'll not waste processor time trying
     * to prevent it.
     **/
    
    /**
     * Puts vertices in random places, all between (0,0) and (1,1).
     */
    public void placeRandomly(InternalNode [] entitiesToLayout) {
	    // If only one node in the data repository, put it in the middle
	    if(entitiesToLayout.length == 1) {
	    	// If only one node in the data repository, put it in the middle
	    	InternalNode layoutEntity = entitiesToLayout[0];
			layoutEntity.setDx(0.5);
			layoutEntity.setDy(0.5);
	    } else {
			for (int i = 0; i < entitiesToLayout.length; i++) {
				InternalNode layoutEntity = entitiesToLayout[i];
				if(i == 0) {
					layoutEntity.setDx(0.5);
					layoutEntity.setDy(0.5);
					
				} else if (i == 1) {
					layoutEntity.setDx(1.0);
					layoutEntity.setDy(1.0);
					
				}
				 else {
					 layoutEntity.setDx(Math.random());
					 layoutEntity.setDy(Math.random());
					 
				    
				}
		    }
	    }
    }
    
	/** Places nodes randomly on the screen **/
	private void placeRandomly (InternalNode [] entitiesToLayout, int width, int height, int node_width, int node_height) {
		
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
    		double x = Math.random() * width - node_width;
    		double y = Math.random() * height - node_height;
            node.setDx( x );
            node.setDy( y );
        }
	    
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode n1 = entitiesToLayout[i];
	    	DisplayIndependentPoint p1 = new DisplayIndependentPoint(n1.getDx(), n1.getDy());
            for (int j = i+1; j < entitiesToLayout.length; j++) {
                InternalNode n2 = entitiesToLayout[j];
	    		DisplayIndependentPoint p2 = new DisplayIndependentPoint(n1.getDx(), n2.getDy());
	    		if ( n1 != n2 && p1.equals(p2)) {
	    			throw new RuntimeException("Nodes with the same location: " +  "P1: " + p1 + " and p2 " + p2 + " -- ");
	    		}
	    	}
            try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	}


    
    public DisplayIndependentPoint getClusterForce(InternalNode layoutent, FadeCell fadenode)
    {
    	/** Need to do some calculations to get the scaled down distances etc... **/
    	//DisplayIndependentPoint nodelocat = (DisplayIndependentPoint)getTempLocation(layoutent);//.clone();
    	DisplayIndependentPoint nodelocat = new DisplayIndependentPoint(layoutent.getDx(), layoutent.getDy() );
    	double scaled_dx = nodelocat.x - (fadenode.GetLocation().x / fadenode.GetNumElements());
    	double scaled_dy = nodelocat.y - (fadenode.GetLocation().y / fadenode.GetNumElements()) ;
    	double scaled_distance = Math.sqrt(scaled_dx * scaled_dx + scaled_dy * scaled_dy);
    	
    	//System.out.println("Scaled Distance: " + scaled_distance);
    	
    	if (scaled_distance == 0.0)
    		scaled_distance = 100.0;
    	scaled_distance = Math.max(MIN_DISTANCE, scaled_distance);
    	double scaled_distance_squared = scaled_distance * scaled_distance;
    	

    	
    	double dx = layoutent.getDx() + CELL_WIDTH - fadenode.GetAverageX();
		double dy = layoutent.getDy() + CELL_HEIGHT - fadenode.GetAverageY();

		double distance = Math.sqrt(dx*dx + dy*dy);
		//make sure distance and distance squared not too small
		distance = Math.max(MIN_DISTANCE, distance);
		
		/** Barnes Hut Cell opening Criteria **/
		//if (fadenode.HasChildren())
		if ((fadenode.GetWidth()/distance>0.0) && (fadenode.HasChildren()))
		{
    		
    		DisplayIndependentPoint returnpoint = new DisplayIndependentPoint(0,0);
    		
    		/** Get Cluster Forces for all daughter cells of fadenode **/
    		FadeCell NWnode = (FadeCell)nodeVector.get(fadenode.getNW());
    		if (NWnode.IsFull() || (NWnode.HasChildren()))
    		{
    			DisplayIndependentPoint NWpoint = getClusterForce(layoutent,NWnode);//new DisplayIndependentPoint(0,0); //
    			returnpoint.x = returnpoint.x + NWpoint.x;
    			returnpoint.y = returnpoint.y + NWpoint.y;
    		}
    		
    		FadeCell NEnode = (FadeCell)nodeVector.get(fadenode.getNE());
    		if (NEnode.IsFull() || (NEnode.HasChildren()))
    		{
    			DisplayIndependentPoint NEpoint = getClusterForce(layoutent,NEnode);// new DisplayIndependentPoint(0,0);//
    			returnpoint.x = returnpoint.x + NEpoint.x;
    			returnpoint.y = returnpoint.y + NEpoint.y;
    		}
    		
    		FadeCell SEnode = (FadeCell)nodeVector.get(fadenode.getSE());
    		if (SEnode.IsFull() || (SEnode.HasChildren()))
    		{
    			DisplayIndependentPoint SEpoint = getClusterForce(layoutent,SEnode);//new DisplayIndependentPoint(0,0);//
    			returnpoint.x = returnpoint.x + SEpoint.x;
    			returnpoint.y = returnpoint.y + SEpoint.y;
    		}
    		
    		FadeCell SWnode = (FadeCell)nodeVector.get(fadenode.getSW());
    		if (SWnode.IsFull() || (SWnode.HasChildren()))
    		{
    			DisplayIndependentPoint SWpoint = getClusterForce(layoutent,SWnode);//new DisplayIndependentPoint(0,0);//
    			returnpoint.x = returnpoint.x + SWpoint.x;
    			returnpoint.y = returnpoint.y + SWpoint.y;
    		}
  
    		return returnpoint;
    	}
    	else
    	{
    		
    		/**The following calculates the force on layoutent due to the cluster called fadenode **/
    		//double distance_sq = distance*distance;
    		  		
    		// Method 1:
    		double f = (sprGravitation / scaled_distance_squared);
    		
    		//give the really close nodes an extra kick
//    		if (scaled_distance <= MIN_DISTANCE)
//    		{f = f *  400;
//    		System.out.println("distance very small");
//    		}
    		
    		DisplayIndependentPoint returnpoint = new DisplayIndependentPoint(f * scaled_dx/scaled_distance, f * scaled_dy/scaled_distance);
    		
    		//Method 2: 
    		//If we use this method, we can take out some code in the algorithm associated with method 1! - Its not perfect though.              
    		//double f =  sprGravitation / distance_sq;
    		//DisplayIndependentPoint returnpoint = new DisplayIndependentPoint(Maxwidth * f * dx/distance,Maxheight * f * dy/distance);
    		
    		return returnpoint;
    	}
    }
    
        ///////////////////////////////////////////////////////////////////
    /////                 Protected Methods                       /////
    ///////////////////////////////////////////////////////////////////

    /**
     * Computes the force for each node in this ShrimpSpringLayoutAlgorithm.
     * The computed force will be stored in the data repository
     */
    protected void computeForces(InternalNode [] entitiesToLayout, InternalRelationship [] relationshipsToConsider) {
	
		for (int i = 0; i < entitiesToLayout.length; i++)
    	{					    
		
			InternalNode sourceEntity = entitiesToLayout[i];			
			//setForce(sourceEntity, 0, 0);
			zeroForce(sourceEntity);
		
			DisplayIndependentPoint srcForce = getForce (sourceEntity);//.clone();
			double fx = srcForce.x; //force in x direction
			double fy = srcForce.y; //force in y direction
			
			/** For each entity we want to compute the force on it from all other cells
			 * We do this by calling GetClusterForce with the entity and the root cell
			 * in the vector of Fade Nodes objects... **/
			/** This computes non - edge forces **/
			DisplayIndependentPoint forcepoint = getClusterForce(sourceEntity,(FadeCell)nodeVector.get(0));//.clone();
			
			fx = fx + forcepoint.x;
			fy = fy + forcepoint.y;
			
			setForce (sourceEntity, fx, fy);
		}/** End of the For Loop **/
		setEdgeForces(relationshipsToConsider);
    }

    /**
     * Computes the position for each node in this ShrimpSpringLayoutAlgorithm.
     * The computed position will be stored in the data repository.
     * position = position + sprMove * force
     */
 
    
    protected void computePositions(InternalNode [] entitiesToLayout) {
    	
		for (int i = 0; i < entitiesToLayout.length; i++) {
			InternalNode layoutEntity = entitiesToLayout[i];
		    //if (true)//!isAnchor (layoutEntity))
			if ( !layoutEntity.hasPreferredLocation() )
		    	{
		        //DisplayIndependentPoint tempLocation = getTempLocation(layoutEntity);
		    	DisplayIndependentPoint tempLocation = new DisplayIndependentPoint( layoutEntity.getDx(), layoutEntity.getDy());
				double oldX = tempLocation.x;
				double oldY = tempLocation.y;
				double deltaX = sprMove * getForce (layoutEntity).x;
				double deltaY = sprMove * getForce (layoutEntity).y;
				
				// constrain movement, so that nodes don't shoot way off to the edge
				double maxMovement = 10.0d*sprMove;
				if (deltaX >= 0) {
					deltaX = Math.min (deltaX, maxMovement);
				} else {
					deltaX = Math.max (deltaX, -maxMovement);
				}
				if (deltaY >= 0) {
					deltaY = Math.min (deltaY, maxMovement);
				} else {
					deltaY = Math.max (deltaY, -maxMovement);
				}

				
				largestMovement = Math.max(largestMovement, Math.abs(deltaX));
				largestMovement = Math.max(largestMovement, Math.abs(deltaY));
				
				double newX = oldX + deltaX;
				double newY = oldY + deltaY;
				//setTempLocation(layoutEntity, new DisplayIndependentPoint (newX, newY));
				layoutEntity.setDx(newX);
				layoutEntity.setDy(newY);
				
				//layoutEntity.setInternalSize(newX, newY);
				//System.out.println("Set location: " + newX + " : " + newY);
			}
		}
		//largestX = maxX - minY;
		//largestY = maxY - minY;
		//System.out.println("largestMovement: " + largestMovement);
		//// if (DEBUG) System.out.println("\nlargestX: " + largestX);
		//// if (DEBUG) System.out.println("largestY: " + largestY);
    }

    /**
     * Converts the position for each node in this ShrimpSpringLayoutAlgorithm to
     * unit coordinates in double precision. The computed positions
     * will be still stored in the data repository.
     */
    protected void convertToUnitCoordinates(InternalNode [] entitiesToLayout, int width, int height, int node_width, int node_height) {
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity = entitiesToLayout[i];
			minX = Math.min(minX, layoutEntity.getDx());
			minY = Math.min(minY, layoutEntity.getDy());
			maxX = Math.max(maxX, layoutEntity.getDx());
			maxY = Math.max(maxY, layoutEntity.getDy());
		}
		
		double spanX = maxX - minX;
		double spanY = maxY - minY;
		double maxSpan = Math.max(spanX, spanY);
	
		if(maxSpan > EPSILON) {
            for (int i = 0; i < entitiesToLayout.length; i++) {
                InternalNode layoutEntity = entitiesToLayout[i];
				double x = (layoutEntity.getDx() - minX) / spanX;
				double y = (layoutEntity.getDy() - minY) / spanY;
				//setTempLocation(layoutEntity, new DisplayIndependentPoint (x,y));
				layoutEntity.setInternalLocation(x,y);
		    }
		} else {
		    placeRandomly(entitiesToLayout,width,height,node_width, node_height);
		    placeRandomly(entitiesToLayout);
		}
    }

    /**
     * Gets the specified node's force in this ShrimpSpringLayoutAlgorithm. 
     */
    private DisplayIndependentPoint getForce(InternalNode layoutEntity) {
		DisplayIndependentPoint force = (DisplayIndependentPoint)layoutEntity.getAttributeInLayout(ATTR_FORCE);
	    return force == null? new DisplayIndependentPoint(0, 0) : force;
	}
	
    private void setForce(InternalNode layoutEntity, double x, double y) {
		setForce (layoutEntity, new DisplayIndependentPoint(x, y));
	}
	
    
    HashMap oldForces = new HashMap();
    
    private void zeroForce( InternalNode layoutEntity ) {
    	layoutEntity.setAttributeInLayout(ATTR_FORCE, new DisplayIndependentPoint(0,0));
    }
    private void setForce(InternalNode layoutEntity, DisplayIndependentPoint force) {
    	DisplayIndependentPoint oldForce = (DisplayIndependentPoint)oldForces.get( layoutEntity );
    	//System.out.println("Force is: " + layoutEntity.hashCode() + " : " + force.x);
    	if ( oldForce != null ) {

    		if ( force.x * oldForce.x < 0 && ( Math.abs( force.x ) > 0.000001 )) {
    			//System.out.println("old: " + oldForce.x + " : " + force.x );
    			//force.x = oldForce.x / 100;
    			force.x = 0;
    		}
    		else if ( force.x * oldForce.x < 0 ) {
    			//System.out.println("newForce: " + force.x );
    		}
    		
    		if ( force.y * oldForce.y < 0 && ( Math.abs( force.y ) > 0.000001 )) {
    			//force.y = oldForce.y / 100;
    			force.y = 0;
    		}
    		else if ( force.y * oldForce.y < 0 ) {
    			//System.out.println("newForce: " + force.y );
    		}
    	}
    	oldForces.put(layoutEntity, force);
    	
	    layoutEntity.setAttributeInLayout(ATTR_FORCE, force);
	}
	
	/*
	protected DisplayIndependentPoint getTempLocation (InternalNode layoutEntity) {
	    DisplayIndependentPoint tempPosition = (DisplayIndependentPoint) layoutEntity.getAttributeInLayout(ATTR_TEMP_LOCATION);
	    return tempPosition == null ? new DisplayIndependentPoint (0,0) : tempPosition;
	}
	*/
	
	/*
	protected void setTempLocation (InternalNode layoutEntity, DisplayIndependentPoint tempLocation) {
	    layoutEntity.setAttributeInLayout(ATTR_TEMP_LOCATION, tempLocation);
	}
	*/


	
}// class FadeSpringLayoutAlgorithm





