/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Pratik Shah
 * @since 3.1
 * @model
 */
public interface Diagram extends EObject{

/**
 * @model type="org.eclipse.emf.ecore.EPackage"
 */
EList getImports();

/**
 * @model containment="true" type="Node" opposite="diagram"
 */
EList getContents();

}