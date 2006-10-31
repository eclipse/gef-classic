/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * 
 * @author Ian Bull
 */
public interface IDAGContentProvider extends IStructuredContentProvider {
	
	
	public boolean hasAdjacent( Object element );
	
	public Object[] getAdjacent( Object element );
	
	
}
