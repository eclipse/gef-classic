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
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * @author irbull
 */
public interface ITreeGraphContentProvider extends IStructuredContentProvider, ITreeContentProvider, IGraphEntityContentProvider {

	
	public Object[] getGraphElements( Object inputElement );
	
	public Object[] getChildren( Object element );
	
	public Object[] getConnectedTo( Object element );
	
	public Object[] getEdge( Object element1, Object element2 );
	
}
