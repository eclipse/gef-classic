package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.graph.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.tools.DirectEditManager;

import org.eclipse.gef.examples.flow.FlowImages;

import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author hudsonr
 * Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart implements NodeEditPart {

private DirectEditManager manager;

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Node n = new Node(this, s);
	n.outgoingOffset = 5;
	n.incomingOffset = 5;
	n.width = getFigure().getPreferredSize().width;
	n.height = getFigure().getPreferredSize().height;
	map.put(this, n);
	graph.nodes.add(n);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Label l = new Label();
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

private void performDirectEdit() {
	if (manager == null)
		manager =
			new ActivityDirectEditManager(
				this,
				TextCellEditor.class,
				new ActivityCellEditorLocator((Label) getFigure()));
	manager.show();
}

/**
 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
 */
public void performRequest(Request request) {
	if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		performDirectEdit();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	((Label)getFigure()).setText(getActivity().getName());
}

}
