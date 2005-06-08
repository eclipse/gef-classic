/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     E.D.Willink
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

public abstract class ModelPropertySource 
	extends AbstractPropertySource
{
	
private Object model;

public ModelPropertySource(Object model) {
	this.model = model;
}

public Object getModel() {
	return model;
}

}