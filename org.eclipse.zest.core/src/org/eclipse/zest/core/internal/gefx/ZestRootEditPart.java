/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Chisel Group, University of Victoria - Adapted for XY Scaled Graphics
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;



/**
 * All root edit parts should enable this.  This allows the GEF Factory
 * to set the top level model element with the root edit part.
 * @author Ian Bull
 */
public interface ZestRootEditPart {
	
	public void setModelRootEditPart( Object modelRootEditPart );

}
