package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.*;

import org.eclipse.gef.*;

import org.eclipse.gef.examples.flow.FlowImages;
import org.eclipse.gef.examples.flow.figures.SimpleActivityLabel;

/**
 * @author hudsonr
 * Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart {

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Node n = new Node(this, s);
	n.outgoingOffset = getAnchorOffset();
	n.incomingOffset = getAnchorOffset();
	n.width = getFigure().getPreferredSize().width;
	n.height = getFigure().getPreferredSize().height;
	n.setPadding(new Insets(10,8,10,12));
	map.put(this, n);
	graph.nodes.add(n);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Label l = new SimpleActivityLabel();
	l.setLabelAlignment(PositionConstants.LEFT);
	l.setIcon(FlowImages.gear);
	return l;
}

int getAnchorOffset() {
	return 9;
}

protected void performDirectEdit() {
	if (manager == null) {
		Label l = (Label)getFigure();
		manager =
			new ActivityDirectEditManager(
				this,
				TextCellEditor.class,
				new ActivityCellEditorLocator(l), l);
	}
	manager.show();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	((Label)getFigure()).setText(getActivity().getName());
}

}
