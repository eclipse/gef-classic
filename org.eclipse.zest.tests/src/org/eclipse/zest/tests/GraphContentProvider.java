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
package org.eclipse.mylar.zest.tests;

import java.util.Random;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylar.zest.core.viewers.IGraphContentProvider;

/**
 * The content provider class is responsible for providing objects to the view. It can wrap existing 
 * objects in adapters or simply return objects as-is. These objects may be sensitive to the current 
 * input of the view, or ignore it and always show the same content (like Task List, for example).
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */	
public class GraphContentProvider implements IGraphContentProvider {
	
	
//	public static final String[] ALLNAMES = new String[] { "Peggy", "Rob", "Ian", "Chris", "Simon", "Wendy", "Steven", "Kim", "Maleh", "Del",  
//															"Dave", "John", "Suzanne", "Jody", "Casey", "Bjorn", "Peter", "Erin", "Lisa", 
//															"Jennie", "Liz", "Bert", "Ryan", "Nick", "Amy", "Lee", "Me", "You", "Max", 
//															"NCI", "OWL", "Ed", "Jamie", "Protege", "Matt", "Brian", "Pete", "Sam", 
//															"Bob", "Katie", "Bill", "Josh", "Davor", "Ken", "Jacob", "Norm", "Jim", "Maya",
//															"Jill", "Kit", "Jo", "Joe", "Andrew", "Charles", "Pat", "Patrick", "Jeremy", 
//															"Mike", "Michael", "Patricia", "Marg", "Terry", "Emily", "Ben", "Holly", "Joanna", 
//															"Joanne", "Evan", "Tom", "Dan", "Eric", "Corey", "Meghan", "Kevin", "Nina", 
//															"Ron", "Daniel", "David", "Jeff", "Nathan", "Amanda", "Phil", "Tricia", "Steph", 
//															"Stewart", "Stuart", "Bull", "Lintern", "Callendar", "Thompson", "Rigby",
//															"Adam", "Judith", "Cynthia", "Sarah", "Sara", "Roger", "Andy", "Kris", 
//															"Mark", "Shane", "Spence", 	"Ivy", "Ivanna", "Julie", "Justin", "Emile",
//															"Toby", "Robin", "Rich", "Kathy", "Cathy", "Nicky", "Ricky", "Danny", "Anne",
//															"Ann", "Jen", "Robert", "Calvin", "Alvin", "Scott", "Neil" };
	
	public static final String[] NAMES = new String[] { "Peggy", "Rob", "Ian", "Chris", "Suzanne", "Peter", "Del",
														"Kim", "Davor", "Jody", "Casey", "Tricia", "Maleh" };
	
	public static final String[] NEWNAMES = new String[] { "Simon", "Wendy", "Steven", "Bjorn", "John", "Dave", "Erin", "Lisa",
															"Jennie", "Liz", "Bert", "Ryan", "Nick", "Amy", "Lee", "Me", "You", "Max", 
															"NCI", "OWL", "Ed", "Jamie", "Protege", "Matt", "Brian", "Pete", "Sam", 
															"Bob", "Katie", "Bill", "Josh", "Ken", "Jacob", "Norm", "Jim", "Maya",
															"Jill", "Kit", "Jo", "Joe", "Andrew", "Charles", "Pat", "Patrick", "Jeremy", 
															"Mike", "Michael", "Patricia", "Marg", "Terry", "Emily", "Ben", "Holly", "Joanna", 
															"Joanne", "Evan", "Tom", "Dan", "Eric", "Corey", "Meghan", "Kevin", "Nina", 
															"Ron", "Daniel", "David", "Jeff", "Nathan", "Amanda", "Phil", "Steph", 
															"Stewart", "Stuart", "Bull", "Lintern", "Callendar", "Thompson", "Rigby",
															"Adam", "Judith", "Cynthia", "Sarah", "Sara", "Roger", "Andy", "Kris", 
															"Mark", "Shane", "Spence", 	"Ivy", "Ivanna", "Julie", "Justin", "Emile",
															"Toby", "Robin", "Rich", "Kathy", "Cathy", "Nicky", "Ricky", "Danny", "Anne",
															"Ann", "Jen", "Robert", "Calvin", "Alvin", "Scott", "Neil" };
		
	
	private Random random = null;
	
	public GraphContentProvider() {
		this.random = new Random();
	}
	
	/**
	 * Gets the destination object.
	 * @param rel
	 */
	public Object getDestination(Object rel) {
		Object dest = null;
		if (rel != null) {
			int relNumber = Integer.parseInt( rel.toString() );
			dest = NAMES[relNumber];
		}
		return dest;
	}


	public Object[] getElements( Object o )  {
		Object[] rels = new String[ NAMES.length -1];
		for ( int i = 1; i <= rels.length; i++ ) {
			rels[i-1] = "" + i;
		}
		return rels;
	}

	public Object getSource(Object rel) {
		Object src = null;
		if (rel != null) {
			int relNumber = Integer.parseInt( rel.toString() );
			src = NAMES[relNumber /3];
		}
		return src;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	
	}
	
	/**
	 * Returns the weight between 0.0 and 1.0 inclusively.
	 * Currently it returns a random weight between 0 and 1.
	 * @see org.eclipse.mylar.zest.core.viewers.IGraphContentProvider#getWeight(java.lang.Object)
	 */
	public double getWeight(Object connection) {
		return random.nextDouble();
	}
	
}
