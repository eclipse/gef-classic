/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.outline;

import org.eclipse.emf.ecore.EClass;


public class InheritanceModel {
	private EClass superType;
	private EClass subType;
	public InheritanceModel(EClass superClass, EClass subClass) {
		superType = superClass;
		subType = subClass;
	}
	public EClass getSuperType() {
		return superType;
	}
	public EClass getSubType() {
		return subType;
	}
}
