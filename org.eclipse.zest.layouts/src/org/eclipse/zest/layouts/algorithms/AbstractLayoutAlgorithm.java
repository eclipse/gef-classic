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

package org.eclipse.mylar.zest.layouts.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylar.zest.layouts.Filter;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.Stoppable;

import org.eclipse.mylar.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.mylar.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.mylar.zest.layouts.dataStructures.InternalNode;
import org.eclipse.mylar.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.mylar.zest.layouts.progress.ProgressListener;



/**
 * Handles common elements in all layout algorithms
 * [irbull] Refactored into a template pattern.  ApplyLayout now delegates the
 * task to ApplyLayoutInternal
 * 
 * [irbull] Included asynchronous layouts
 * 
 * @version 1.0
 * @author Casey Best
 * @author irbull
 * @author ccallendar
 * @author Rob Lintern
 */
public abstract class AbstractLayoutAlgorithm implements LayoutAlgorithm, Stoppable {
	
	public final static int MIN_ENTITY_SIZE = 5;
	private final static int MIN_TIME_DELAY_BETWEEN_PROGRESS_EVENTS = 1;

	protected Comparator comparator;
	protected Filter filter;
	private List progressListeners;
	private Calendar lastProgressEventFired;
	private double widthToHeightRatio;
	
	
	class InternalComparator implements Comparator {
		Comparator externalComparator = null;
		public InternalComparator( Comparator externalComparator ) {
			this.externalComparator = externalComparator;
		}
		public int compare(Object o1, Object o2 ) {
			InternalNode internalNode1 = (InternalNode) o1;
			InternalNode internalNode2 = (InternalNode) o2;
			
			return this.externalComparator.compare(internalNode1.getLayoutEntity(), internalNode2.getLayoutEntity());
		}
		
	}
	
	/*
	 * Internal Nodes.   
	 */
	private InternalNode [] _internalNodes;
	private InternalRelationship [] _internalRelationships;
	private double _x;
	private double _y;
	private double _width;
	private double _height;

	/*
	 * A queue of entities and relationships to add or remove.  Each layout 
	 * algorithm should check these and update their internal lists.
	 */
	
	/** A list of LayoutEntity objects to be removed from the layout. */
	private List entitiesToRemove;
	/** A list of LayoutRelationship objects to be removed. */
	private List relationshipsToRemove;
	/** A list of LayoutEntity objects to be added to the layout. */	
	private List entitiesToAdd;
	/** A list of LayoutRelationship objects to be added. */	
	private List relationshipsToAdd;
	
	//protected boolean cancelled = false;
	protected boolean layoutStopped = true;
	private boolean isLayoutPaused = false;
	protected boolean runContinuously = false;


	private Object lock = new Object();
	

	/**
	 * Initializes the abstract layout algorithm.
	 */
	public AbstractLayoutAlgorithm() {
		progressListeners = new ArrayList();
		lastProgressEventFired = Calendar.getInstance();
		widthToHeightRatio = 1.0;
		
		entitiesToRemove = new ArrayList();
		relationshipsToRemove = new ArrayList();
		entitiesToAdd = new ArrayList();
		relationshipsToAdd = new ArrayList();
	}
	
	
	public abstract void setLayoutArea(double x, double y, double width, double height);
	
	/**
	 * Determines if the configuration is valid for this layout
	 * @param asynchronous
	 * @param continuous
	 */
	abstract boolean isValidConfiguration( boolean asynchronous, boolean continuous );
	
	/**
	 * Apply the layout to the given entities.  The entities will be moved and resized based
	 * on the algorithm.
	 * 
	 * @param entitiesToLayout Apply the algorithm to these entities
	 * @param relationshipsToConsider Only consider these relationships when applying the algorithm.
	 * @param x The left side of the bounds in which the layout can place the entities.
	 * @param y The top side of the bounds in which the layout can place the entities.
	 * @param width The width of the bounds in which the layout can place the entities.
	 * @param height The height of the bounds in which the layout can place the entities.
	 */
	abstract protected void applyLayoutInternal( InternalNode[] entitiesToLayout, 
			                                     InternalRelationship[] relationshipsToConsider, 
			                                     double boundsX, 
			                                     double boundsY, 
			                                     double boundsWidth, 
			                                     double boundsHeight );
	
	
	/**
	 * Updates the given array of entities checking if any need to be removed or added.
	 * @param entities the current entities
	 * @return the updated entities array
	 */
    protected InternalNode[] updateEntities(InternalNode[] entities) {
    	if ((entitiesToRemove.size() > 0) || (entitiesToAdd.size() > 0)) {
            List internalNodesList = new ArrayList (Arrays.asList(entities));

            // remove nodes
            for (Iterator iter = entitiesToRemove.iterator(); iter.hasNext();) {
				LayoutEntity entity = (LayoutEntity) iter.next();
				if (entity.getLayoutInformation() != null) {
                    internalNodesList.remove(entity.getLayoutInformation());
				}
			}
            
            // Also remove from _internalNodes
            ArrayList updatedEntities = new ArrayList(_internalNodes.length - entitiesToRemove.size() + entitiesToAdd.size());
            for (int i = 0; i < _internalNodes.length; i++) {
            	InternalNode node = _internalNodes[i];
            	if (entitiesToRemove.contains(node.getLayoutEntity())) {
            		entitiesToRemove.remove(node.getLayoutEntity());
            	} else {
            		updatedEntities.add(node);
            	}
            }                   
	    	entitiesToRemove.clear();

    		// Add any new nodes
            LayoutEntity[] entitiesArray = new LayoutEntity[entitiesToAdd.size()];
            entitiesArray = (LayoutEntity [])entitiesToAdd.toArray(entitiesArray);
    		InternalNode [] newNodes = createInternalNodes(entitiesArray);
            for (int i = 0; i < newNodes.length; i++) {
                internalNodesList.add(newNodes[i]);
                updatedEntities.add(newNodes[i]);
			}
        	entitiesToAdd.clear();
            
            entities = new InternalNode [internalNodesList.size()];
            entities = (InternalNode[])internalNodesList.toArray(entities);
            
            _internalNodes = new InternalNode[updatedEntities.size()];
            _internalNodes = (InternalNode[])updatedEntities.toArray(_internalNodes);
    	}
    	
    	return entities;
    }
	
	/**
	 * Updates the given array of relationships checking if any need to be removed or added.
	 * Also updates the original array of relationships.
	 * @param relationships the current relationships
	 * @return the update relationships array
	 */
    protected InternalRelationship[] updateRelationships(InternalRelationship[] relationships) {
    	if ((relationshipsToRemove.size() > 0) || (relationshipsToAdd.size() > 0)) {
            List internalRelsList = new ArrayList(Arrays.asList(relationships));

            // remove relationships
            if (relationshipsToRemove.size() > 0) {
	    		for (Iterator iter = relationshipsToRemove.iterator(); iter.hasNext();) {
	    			LayoutRelationship relation = (LayoutRelationship) iter.next();
	    			if (relation.getLayoutInformation() != null) {
	                    internalRelsList.remove(relation.getLayoutInformation());
	    			}
				}
            }

            // Also remove from _internalRelationships
            ArrayList updatedRelationships = new ArrayList(_internalRelationships.length - relationshipsToRemove.size() + relationshipsToAdd.size());
            for (int i = 0; i < _internalRelationships.length; i++) {
            	InternalRelationship relation = _internalRelationships[i];
            	if (relationshipsToRemove.contains(relation.getLayoutRelationship())) {
            		relationshipsToRemove.remove(relation.getLayoutRelationship());
            	} else {
            		updatedRelationships.add(relation);
            	}
            }            
            relationshipsToRemove.clear();
            
    		// add relationships
            if (relationshipsToAdd.size() > 0) {
	            LayoutRelationship[] relsArray = new LayoutRelationship[relationshipsToAdd.size()];
	            relsArray = (LayoutRelationship[])relationshipsToAdd.toArray(relsArray);
	    		InternalRelationship [] newRelationships = createInternalRelationships(relsArray);
	            for (int i = 0; i < newRelationships.length; i++) {
	                internalRelsList.add(newRelationships[i]);
	                updatedRelationships.add(newRelationships[i]);
				}
            }
            relationshipsToAdd.clear();
            
            relationships = new InternalRelationship [internalRelsList.size()];
            relationships = (InternalRelationship[])internalRelsList.toArray(relationships);

            _internalRelationships = new InternalRelationship[updatedRelationships.size()];
            _internalRelationships = (InternalRelationship[])updatedRelationships.toArray(_internalRelationships);
    	}
    	
    	return relationships;
    }
    
	/**
	 * Queues up the given entity (if it isn't in the list) to be added to the algorithm.
	 * @param entity
	 */
	public void addEntity(LayoutEntity entity) {
		if ((entity != null) && !entitiesToAdd.contains(entity)) {
			entitiesToAdd.add(entity);
		}
	}
	
	/**
	 * Queues up the given relationshp (if it isn't in the list) to be added to the algorithm.
	 * @param relationship
	 */
	public void addRelationship(LayoutRelationship relationship) {
		if ((relationship != null) && !relationshipsToAdd.contains(relationship)) {
			relationshipsToAdd.add(relationship);
		}
	}
	
	/**
	 * Queues up the given entity to be removed from the algorithm next time it runs.
	 * @param entity The entity to remove
	 */
	public void removeEntity(LayoutEntity entity) {
		if ((entity != null) && !entitiesToRemove.contains(entity)) {
			entitiesToRemove.add(entity);
		}
	}
	
	/**
	 * Queues up the given relationship to be removed from the algorithm next time it runs.
	 * @param relationship	The relationship to remove.
	 */
	public void removeRelationship(LayoutRelationship relationship) {
		if ((relationship != null) && !relationshipsToRemove.contains(relationship)) {
			relationshipsToRemove.add(relationship);
		}
	}
	
	/**
	 * Queues up all the relationships in the list to be removed.
	 * @param relationships
	 */
	public void removeRelationships(List relationships) {
		// note we don't check if the relationshipsToRemove contains
		// any of the objects in relationships.
		relationshipsToRemove.addAll(relationships);
	}
	
	/**
	 * Moves all the entities by the given amount.  
	 * @param dx	the amount to shift the entities in the x-direction 
	 * @param dy	the amount to shift the entities in the y-direction
	 */
	public void moveAllEntities(double dx, double dy) {
		if ((dx != 0) || (dy != 0)) {
			synchronized (_internalNodes) {
                for (int i = 0; i < _internalNodes.length; i++) {
                    InternalNode node = _internalNodes[i];
					node.setInternalLocation(node.getInternalX()+dx, node.getInternalY()+dy);
					node.setLocation(node.getX()+dx, node.getY()+dy);
				}
			}
		}
	}
	
	/**
	 * Returns true if the layout algorithm is running
	 * @return boolean if the layout algorithm is running
	 */
	public synchronized boolean isRunning() {
		return !layoutStopped;
	}
	
	
	/**
	 * Stops the current layout from running.
	 * All layout algorithms should constantly check isLayoutRunning
	 */
	public synchronized void stop() {
		isLayoutPaused = false;
		layoutStopped = true;
		postLayoutAlgorithm(_internalNodes, _internalRelationships );
		fireProgressEnded( getTotalNumberOfLayoutSteps() );
	}
	
	/**
	 * Checks if the layout algorithm is paused.
	 * @return boolean
	 */
	public synchronized boolean isPaused() {
		return isLayoutPaused;
	}
	/**
	 * Pauses the layout algorithm.  All algorithms that extend this class
	 * should be checking the isPaused() method.
	 */
	public synchronized void pause() {
		isLayoutPaused = true;
	}
	
	/**
	 * Resumes the layout algorithm.
	 */
	public synchronized void resume() {
		isLayoutPaused = false;
	}
	
	/**
	 * Sleeps while the algorithm is paused.
	 */
	protected void sleepWhilePaused() {
		// do nothing while the algorithm is paused
		boolean wasPaused = false;
		while (isPaused()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
			wasPaused = true;
		}
		// update the node positions (they might have been moved while paused)
		if (wasPaused) {
            for (int i = 0; i < _internalNodes.length; i++) {
                InternalNode node = _internalNodes[i];
				node.setInternalLocation(node.getPreferredX(), node.getPreferredY());
			}
		}
	}
		
	
	private void setupLayout( LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider, 
							  double x, double y, double width, double height ) {
		_x = x;
		_y = y;
		_height = height;
		_width = width;
		// Filter all the unwanted entities and relationships
        entitiesToLayout = (LayoutEntity []) filterUnwantedObjects(entitiesToLayout);
        relationshipsToConsider = (LayoutRelationship []) filterUnwantedObjects(relationshipsToConsider);

		// Check that the input is valid
		if (!verifyInput(entitiesToLayout, relationshipsToConsider)) {
			throw new RuntimeException(
					"The relationships in relationshipsToConsider don't contain the entities in entitiesToLayout");
		}

		// Create the internal nodes and relationship
		_internalNodes = createInternalNodes(entitiesToLayout);
		_internalRelationships = createInternalRelationships(relationshipsToConsider);
        
        
		
	}
	
	
	
	public synchronized Stoppable  getLayoutThread( LayoutEntity[] entitiesToLayout, 
			                                  LayoutRelationship[] relationshipsToConsider, 
			                                  double x, double y, 
			                                  double width, double height  )  {
		//setupLayout( entitiesToLayout, relationshipsToConsider, x, y, width, height );
		setupLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height );
		preLayoutAlgorithm( _internalNodes, _internalRelationships, _x, _y, _width, _height );
		fireProgressStarted( getTotalNumberOfLayoutSteps() );
		return this;
	}
	
	/**
	 * Code called before the layout algorithm starts
	 */
	protected abstract void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height);
	
	
	/**
	 * Code called after the layout algorithm ends
	 */
	protected abstract void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider );
	
	/**
	 *  Gets the total number of steps in this layout
	 */
	protected abstract int getTotalNumberOfLayoutSteps();

	/**
	 * Gets the current layout step
	 * @return
	 */
	protected abstract int getCurrentLayoutStep();





	/**
	 * This actually applies the layout
	 */
	public synchronized void applyLayout(LayoutEntity[] entitiesToLayout,
			LayoutRelationship[] relationshipsToConsider, double x, double y, double width,
			double height, boolean asynchronous, boolean continuous) throws InvalidLayoutConfiguration {
		
		if ( !isValidConfiguration( asynchronous, continuous )) throw new InvalidLayoutConfiguration();
		
		synchronized (lock) {
			if (layoutStopped == false) {
				System.out.println("Layout Already Running! ");
				return;
			}
			layoutStopped = false;
		}

		runContinuously = continuous;


		// when an algorithm starts, reset the progress event
		lastProgressEventFired = Calendar.getInstance();
		if (asynchronous) {
			Thread thread = new Thread(getLayoutThread(entitiesToLayout, relationshipsToConsider, x, y, width, height ));
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		} else {
			
			// If we are running synchronously then we have to stop this at some
			// point? right?
		    setupLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height );
		    preLayoutAlgorithm( _internalNodes, _internalRelationships, _x, _y, _width, _height );
		    fireProgressStarted( getTotalNumberOfLayoutSteps() );
			
			applyLayoutInternal(_internalNodes, _internalRelationships,
					_x, _y, _width, _height);
			stop();
		}
		

	}

	public void run() {
		layoutStopped = false;
		isLayoutPaused = false;
		applyLayoutInternal(_internalNodes, _internalRelationships, _x,
				_y, _width, _height);
		stop();
		layoutStopped = true;
		isLayoutPaused = false;
	}

	
	/**
	 * Creates a list of InternalNode objects from the list of LayoutEntity objects the user
	 * wants layed out. Sets the internal nodes' positions and sizes from the
	 * external entities.
	 */
	private InternalNode[] createInternalNodes( LayoutEntity[] nodes ) {
        InternalNode [] internalNodes = new InternalNode [nodes.length];
	    for (int i = 0; i < nodes.length; i++) {
            LayoutEntity externalNode = nodes[i];
			InternalNode internalNode = new InternalNode( externalNode );
			internalNode.setInternalLocation(externalNode.getXInLayout(), externalNode.getYInLayout());
			internalNode.setInternalSize(externalNode.getWidthInLayout(), externalNode.getHeightInLayout());
            internalNodes[i] = internalNode;
		} // end of for
		return internalNodes;
	}
	
	/**
	 * Creates a list of InternalRelationship objects from the given list of LayoutRelationship objects.
	 * @param rels
	 * @return List of internal relationships
	 */
	private InternalRelationship[] createInternalRelationships( LayoutRelationship[] rels ) {
		ArrayList listOfInternalRelationships = new ArrayList( rels.length );
		for (int i = 0; i < rels.length; i++) {
            LayoutRelationship relation = rels[i];
			InternalNode src = (InternalNode) relation.getSourceInLayout().getLayoutInformation();
			InternalNode dest = (InternalNode) relation.getDestinationInLayout().getLayoutInformation();
			if ((src != null) && (dest != null)) {
				InternalRelationship internalRelationship = new InternalRelationship(relation, src, dest);
				listOfInternalRelationships.add( internalRelationship );
			} else {
				System.out.println("Error creating internal relationship, one of the nodes is null: src=" + src + ", dest=" + dest);
			}
		}
        InternalRelationship [] internalRelationships = new InternalRelationship [listOfInternalRelationships.size()];
        listOfInternalRelationships.toArray(internalRelationships);
		return internalRelationships;	
	}	
		
	
	/**
	 * Removes any objects that are currently filtered
	 */
	private Object [] filterUnwantedObjects (Object[] objects) {
		// first remove any entities or relationships that are filtered.
		List unfilteredObjsList = new ArrayList();
		if (filter != null) {
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
				if (!filter.isObjectFiltered(object)) {
                    unfilteredObjsList.add(object);
				}
			}
            Object [] unfilteredObjs = new Object [unfilteredObjsList.size()];
            unfilteredObjsList.toArray(unfilteredObjs);
            return unfilteredObjs;
		}
        return objects;
	}
	
	/**
	 * Filters the entities and relationships to apply the layout on
	 */
	public void setFilter (Filter filter) {
		this.filter = filter;
	}
	
	/**
	 * Determines the order in which the objects should be displayed.
	 * Note: Some algorithms force a specific order.
	 */
	public void setComparator (Comparator comparator) {
		this.comparator = new InternalComparator( comparator );
	}
	
	/**
	 * Verifies the endpoints of the relationships are entities in the entitiesToLayout list.
	 * Allows other classes in this package to use this method to verify the input
	 */
	public static boolean verifyInput (LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider) {
		boolean stillValid = true;
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            LayoutRelationship relationship = relationshipsToConsider[i];
 			LayoutEntity source = relationship.getSourceInLayout();
			LayoutEntity destination = relationship.getDestinationInLayout();
            boolean containsSrc = false;
            boolean containsDest = false;
            int j = 0;
            while (j < entitiesToLayout.length && !(containsSrc && containsDest)) {
                if (entitiesToLayout[j].equals(source)) {
                    containsSrc = true;
                }
                if (entitiesToLayout[j].equals(destination)) {
                    containsDest = true;
                }
                j++;
            }
            stillValid = containsSrc && containsDest;
		}
		return stillValid;
	}
	
	/**
	 * Find an appropriate size for the given nodes, then fit them into the given bounds.
	 * The relative locations of the nodes to each other must be preserved.
	 */
	protected void defaultFitWithinBounds(InternalNode[] entitiesToLayout, DisplayIndependentRectangle realBounds) {
		// Set the layout bounds.
		double borderWidth = Math.min(realBounds.width, realBounds.height)/10.0; // use 10% for the border - 5% on each side
		DisplayIndependentRectangle screenBounds = new DisplayIndependentRectangle (realBounds.x + borderWidth/2.0, realBounds.y + borderWidth/2.0, realBounds.width - borderWidth, realBounds.height - borderWidth);

		// find and set the node size - shift the nodes to the right and down to make room for the width and height
		convertNodePositionsToPercentage (entitiesToLayout, false);
		
		
		double nodeSize = getNodeSize(entitiesToLayout);
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
			
			node.setInternalSize( nodeSize, nodeSize );
			node.setInternalLocation( node.getInternalX() + nodeSize / 2, node.getInternalY() + nodeSize / 2 );
		}
		
		// convert all positions and sizes to percent
		convertNodePositionsToPercentage (entitiesToLayout, true); // for the nodes within 100% of the screen bounds
		
		// Convert the bounds to the area to use
		// NOTE: ALL OF THE POSITIONS OF NODES UNTIL NOW WERE FOR THE CENTER OF THE NODE - Convert it to the left top corner.
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
 			double width = node.getInternalWidth() * screenBounds.width;
			double height = node.getInternalHeight() * screenBounds.height;
			double x = screenBounds.x + node.getInternalX() * screenBounds.width - width / 2;
			double y = screenBounds.y + node.getInternalY() * screenBounds.height - height / 2;
			
			node.setInternalLocation( x , y );

//			node.setSizeInLayout(Math.max(width, MIN_ENTITY_SIZE), Math.max(height, MIN_ENTITY_SIZE));
			double widthUsingHeight = height * widthToHeightRatio;
			if (widthToHeightRatio <= 1.0 && widthUsingHeight <= width) {
				double widthToUse = height * widthToHeightRatio;
				double leftOut = width - widthToUse;
				node.setInternalSize( Math.max( height * widthToHeightRatio, MIN_ENTITY_SIZE ), Math.max(height, MIN_ENTITY_SIZE) );
				node.setInternalLocation( node.getInternalX() + leftOut / 2, node.getInternalY() );
				
			} else {
				double heightToUse = height / widthToHeightRatio;
				double leftOut = height - heightToUse;
				
				node.setInternalSize( Math.max(width, MIN_ENTITY_SIZE), Math.max(width / widthToHeightRatio, MIN_ENTITY_SIZE) );
				node.setInternalLocation( node.getInternalX(), node.getInternalY() + leftOut / 2 );
			}
		}
	}

	/**
	 * Convert all node positions into a percentage of the screen
	 * @param entitiesToLayout
	 */
	private void convertNodePositionsToPercentage (InternalNode[] entitiesToLayout, boolean includeNodeSize) {
		DisplayIndependentRectangle layoutBounds = getLayoutBounds(entitiesToLayout, includeNodeSize);
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity = entitiesToLayout[i];
			
			double x = (layoutBounds.width == 0) ? 0 : (layoutEntity.getInternalX() - layoutBounds.x) / layoutBounds.width;
			double y = (layoutBounds.height == 0) ? 0 : (layoutEntity.getInternalY() - layoutBounds.y) / layoutBounds.height;
			
			layoutEntity.setInternalLocation( x, y );
			
			if (includeNodeSize) {
				double width = layoutEntity.getInternalWidth() / layoutBounds.width;
				double height = layoutEntity.getInternalHeight() / layoutBounds.height;
				layoutEntity.setInternalSize( width, height );
			}
			if ( layoutEntity.getInternalX() < 0 ) {
				System.out.println("We have nodes less than 0 here!");
			}

		}
	}
	

	
	/**
	 * Returns the node size in the current coord system.
	 */
	private double getNodeSize (InternalNode[] entitiesToLayout) {
		double width, height;
		if (entitiesToLayout.length == 1) {
			width = 0.8;
			height = 0.8;
		} else {
			DisplayIndependentDimension minimumDistance = getMinimumDistance (entitiesToLayout);
			width = 0.8 * minimumDistance.width;
			height = 0.8 * minimumDistance.height;
		}
		return Math.max (width, height);
	}
		
	/**
	 * Find the bounds in which the nodes are located.  Using the bounds against the real bounds
	 * of the screen, the nodes can proportionally be placed within the real bounds.
	 * The bounds can be determined either including the size of the nodes or not.  If the size
	 * is not included, the bounds will only be guaranteed to include the center of each node.
	 */
	protected DisplayIndependentRectangle getLayoutBounds (InternalNode[] entitiesToLayout, boolean includeNodeSize) {
		double rightSide = Double.MIN_VALUE;
		double bottomSide = Double.MIN_VALUE;
		double leftSide = Double.MAX_VALUE;
		double topSide = Double.MAX_VALUE;
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode entity = entitiesToLayout[i];
			if (includeNodeSize) {
				leftSide = Math.min (entity.getInternalX() - entity.getInternalWidth() / 2, leftSide);
				topSide = Math.min (entity.getInternalY() - entity.getInternalHeight() / 2, topSide);
				rightSide = Math.max (entity.getInternalX() + entity.getInternalWidth() / 2, rightSide);
				bottomSide = Math.max (entity.getInternalY() + entity.getInternalHeight() / 2, bottomSide);
			} else {
				leftSide = Math.min (entity.getInternalX(), leftSide);
				topSide = Math.min (entity.getInternalY(), topSide);
				rightSide = Math.max (entity.getInternalX(), rightSide);
				bottomSide = Math.max (entity.getInternalY(), bottomSide);
			}
		}
		return new DisplayIndependentRectangle (leftSide, topSide, rightSide - leftSide, bottomSide - topSide);
	}
	
	/** 
	 * minDistance is the closest that any two points are together.
	 * These two points become the center points for the two closest nodes, 
	 * which we wish to make them as big as possible without overlapping.
	 * This will be the maximum of minDistanceX and minDistanceY minus a bit, lets say 20%
	 * 
	 * We make the recommended node size a square for convenience.
	 * 
	 * 
	 *    _______
	 *   |       | 
	 *   |       |
	 *   |   +   |
	 *   |   |\  |
	 *   |___|_\_|_____
	 *       | | \     |
	 *       | |  \    |
	 *       +-|---+   |
	 *         |       |
	 *         |_______|
	 * 
	 *  
	 * 
	*/
	private DisplayIndependentDimension getMinimumDistance (InternalNode[] entitiesToLayout) {
		DisplayIndependentDimension horAndVertdistance = new DisplayIndependentDimension (Double.MAX_VALUE, Double.MAX_VALUE);
		double minDistance = Double.MAX_VALUE; // the minimum distance between all the nodes
		//TODO: Very Slow!
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity1 = entitiesToLayout[i];
			double x1 = layoutEntity1.getInternalX();
			double y1 = layoutEntity1.getInternalY();
            for (int j = i+1; j < entitiesToLayout.length; j++) {
                InternalNode layoutEntity2 = entitiesToLayout[j];
				double x2 = layoutEntity2.getInternalX();
				double y2 = layoutEntity2.getInternalY();
				double distanceX = Math.abs(x1-x2);
				double distanceY = Math.abs(y1-y2);
				double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
			    
			    if (distance < minDistance) {
			    	minDistance = distance;
			    	horAndVertdistance.width = distanceX;
			    	horAndVertdistance.height = distanceY;
			    }
			}
		}
		return horAndVertdistance;
	}
	
	/**
	 * Set the width to height ratio you want the entities to use
	 */
	public void setEntityAspectRatio(double ratio) {
		widthToHeightRatio = ratio;
	}
	
	/**
	 * Returns the width to height ratio this layout will use to set the size of the entities.
	 */
	public double getEntityAspectRatio () {
		return widthToHeightRatio;
	}

	/**
	 * A layout algorithm could take an uncomfortable amout of time to complete.  To relieve some of
	 * the mystery, the layout algorithm will notify each ProgressListener of its progress. 
	 */
	public void addProgressListener (ProgressListener listener) {
		if (!progressListeners.contains(listener))
			progressListeners.add (listener);
	}
	
	/**
	 * Removes the given progress listener, preventing it from receiving any more updates.
	 */
	public void removeProgressListener (ProgressListener listener) {
		if (progressListeners.contains (listener))
			progressListeners.remove (listener);
	}
	
	/**
	 * Updates the layout locations so the external nodes know about the new locations
	 */
	protected void updateLayoutLocations(InternalNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            InternalNode node = nodes[i];
			if ( !node.hasPreferredLocation() ) { 
				node.setLocation( node.getInternalX(), node.getInternalY() );
				node.setSize( node.getInternalWidth(), node.getInternalHeight() );
			}
		}
	}
	
	
	protected void fireProgressStarted( int totalNumberOfSteps ) {
		ProgressEvent event = new ProgressEvent(0, totalNumberOfSteps);
		for (int i = 0; i < progressListeners.size(); i++) {
			ProgressListener listener = (ProgressListener)progressListeners.get(i);
			
			listener.progressStarted(event);
		}
	}
	
	protected void fireProgressEnded( int totalNumberOfSteps ) {
		ProgressEvent event = new ProgressEvent(totalNumberOfSteps, totalNumberOfSteps);
		for (int i = 0; i < progressListeners.size(); i++) {
			ProgressListener listener = (ProgressListener)progressListeners.get(i);
			listener.progressEnded(event);
		}
		
	}
	
	/**
	 * Fires an event to notify all of the registered ProgressListeners that another step
	 * has been completed in the algorithm.
	 * @param currentStep The current step completed.
	 * @param totalNumberOfSteps The total number of steps in the algorithm.
	 */
	
	private long timerValues2[] = new long[25];
	private int timerCounter2 = 0;
	protected void fireProgressEvent (int currentStep, int totalNumberOfSteps) {
		
		// Update the layout locations to the external nodes
		
		long startTime = System.currentTimeMillis();
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND, -MIN_TIME_DELAY_BETWEEN_PROGRESS_EVENTS);
		
		if (now.after(lastProgressEventFired) || currentStep == totalNumberOfSteps) {
			ProgressEvent event = new ProgressEvent(currentStep, totalNumberOfSteps);
			
			
			for (int i = 0; i < progressListeners.size(); i++) {
				
				ProgressListener listener = (ProgressListener)progressListeners.get(i);
				listener.progressUpdated(event);
			}
			lastProgressEventFired = Calendar.getInstance();
		}
		long endTime = System.currentTimeMillis();
		timerValues2[timerCounter2++] = endTime - startTime;
		if ( timerCounter2 >= timerValues2.length ) {
			timerCounter2 = 0;
			long sum = 0;
			for ( int i = 0; i < timerValues2.length; i++ ) {
				sum+=timerValues2[i];
			}
			//System.out.println("Average Time in Prgoress: " + (sum / timerValues2.length) );
		}
	}
	
	public int getNumberOfProgressListeners() {
		return progressListeners.size();
	}

	
}
