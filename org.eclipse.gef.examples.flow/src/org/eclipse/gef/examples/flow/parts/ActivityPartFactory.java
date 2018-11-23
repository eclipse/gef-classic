/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.ActivityDiagram;
import org.eclipse.gef.examples.flow.model.ParallelActivity;
import org.eclipse.gef.examples.flow.model.SequentialActivity;
import org.eclipse.gef.examples.flow.model.Transition;

/**
 * @author hudsonr Created on Jul 16, 2003
 */
public class ActivityPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		if (model instanceof ActivityDiagram)
			part = new ActivityDiagramPart();
		else if (model instanceof ParallelActivity)
			part = new ParallelActivityPart();
		else if (model instanceof SequentialActivity)
			part = new SequentialActivityPart();
		else if (model instanceof Activity)
			part = new SimpleActivityPart();
		else if (model instanceof Transition)
			part = new TransitionPart();
		part.setModel(model);
		return part;
	}

}
