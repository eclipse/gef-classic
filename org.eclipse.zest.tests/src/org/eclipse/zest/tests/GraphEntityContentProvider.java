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

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.mylar.zest.core.viewers.IGraphEntityContentProvider;;


/**
 * 
 * 
 * @author Ian Bull
 */
public class GraphEntityContentProvider implements IGraphEntityContentProvider {

	private Random random = null;
	private ArrayList al = null;

	public GraphEntityContentProvider() {
		random = new Random();		
		al = new ArrayList();
		al.add( "Ian" );
		al.add( "Chris" );
		al.add( "Peter" );
		al.add( "Peggy" );
		al.add( "Tricia" );
		al.add( "Rob" );
	}


	public Object[] getConnectedTo(Object entity) {
		// TODO Auto-generated method stub
		if ( al.indexOf(entity) == 3 ) {
			return new Object[] { al.get( 0 ), al.get( 4 ) }; 
		}
		return new Object[] {al.get( (al.indexOf( entity )  + 1 ) % al.size() )};
	}

	public Object[] getElements(Object inputElement) {
		return al.toArray(new Object[ al.size() ] );
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	public double getWeight(Object entity1, Object entity2) {
		return random.nextDouble();
	}
	
}
