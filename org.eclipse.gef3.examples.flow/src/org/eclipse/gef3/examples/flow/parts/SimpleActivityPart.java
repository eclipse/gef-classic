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
package org.eclipse.gef3.examples.flow.parts;

import java.util.Map;

import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.Label;
import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.graph.CompoundDirectedGraph;
import org.eclipse.draw2dl.graph.Node;
import org.eclipse.draw2dl.graph.Subgraph;
import org.eclipse.gef3.examples.flow.FlowImages;
import org.eclipse.gef3.examples.flow.figures.SimpleActivityLabel;
import org.eclipse.gef3.editparts.AbstractEditPart;
import org.eclipse.gef3.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author hudsonr Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart {

	public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s,
			Map map) {
		Node n = new Node(this, s);
		n.outgoingOffset = getAnchorOffset();
		n.incomingOffset = getAnchorOffset();
		n.width = getFigure().getPreferredSize().width;
		n.height = getFigure().getPreferredSize().height;
		n.setPadding(new Insets(10, 8, 10, 12));
		map.put(this, n);
		graph.nodes.add(n);
	}

	/**
	 * @see AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Label l = new SimpleActivityLabel();
		l.setLabelAlignment(PositionConstants.LEFT);
		l.setIcon(FlowImages.GEAR);
		return l;
	}

	int getAnchorOffset() {
		return 9;
	}

	protected void performDirectEdit() {
		if (manager == null) {
			Label l = (Label) getFigure();
			manager = new ActivityDirectEditManager(this, TextCellEditor.class,
					new ActivityCellEditorLocator(l), l);
		}
		manager.show();
	}

	/**
	 * @see AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		((Label) getFigure()).setText(getActivity().getName());
	}

}
