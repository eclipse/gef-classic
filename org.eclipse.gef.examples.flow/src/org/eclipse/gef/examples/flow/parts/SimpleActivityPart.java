package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.examples.flow.FlowImages;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author hudsonr
 * Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart implements NodeEditPart {

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Node n = new Node(this, s);
	n.outgoingOffset = 5;
	n.incomingOffset = 5;
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
	Label l = new Label();
	l.setLabelAlignment(PositionConstants.LEFT);
	l.setIcon(FlowImages.gear);
	return l;
}

static class TopAnchor extends AbstractConnectionAnchor {
	TopAnchor (IFigure source) {
		super(source);
	}
	public Point getLocation(Point reference) {
		return getOwner().getBounds().getLocation().translate(5,0);
	}
}

static class BottomAnchor extends AbstractConnectionAnchor {
	BottomAnchor (IFigure source) {
		super(source);
	}
	public Point getLocation(Point reference) {
		return getOwner().getBounds().getBottomLeft().translate(5,-1);
	}
}

/**
 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return new BottomAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

/**
 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return new TopAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
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
