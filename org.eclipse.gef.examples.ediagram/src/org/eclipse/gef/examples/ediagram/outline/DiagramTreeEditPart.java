/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.outline;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;


public class DiagramTreeEditPart extends BaseTreeEditPart {
	public DiagramTreeEditPart(Diagram model) {
		super(model);
	}
	private Diagram getDiagram() {
		return (Diagram)getModel();
	}
	protected List getModelChildren() {
		return getDiagram().getImports();
	}
	protected void handlePropertyChanged(Notification msg) {
		switch (msg.getFeatureID(Diagram.class)) {
			case ModelPackage.DIAGRAM__IMPORTS:
				refreshChildren();
		}
	}
}
