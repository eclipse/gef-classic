/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.model;

/**
 * @author hudsonr
 * Created on Jun 30, 2003
 */
public class Transition extends FlowElement {

public Activity source, target;

public Transition(Activity source, Activity target) {
	this.source = source;
	this.target = target;

	source.addOutput(this);
	target.addInput(this);
}

}
