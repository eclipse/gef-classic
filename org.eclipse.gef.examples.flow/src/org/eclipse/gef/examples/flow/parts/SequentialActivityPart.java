package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.graph.*;

import org.eclipse.gef.EditPart;

import org.eclipse.gef.examples.flow.figures.*;

/**
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SequentialActivityPart
	extends StructuredActivityPart
{

static final MarginBorder MARGIN_BORDER = new MarginBorder(0, 8, 0, 0);

static final PointList ARROW = new PointList(3); {
	ARROW.addPoint(0,0);
	ARROW.addPoint(0, 9);
	ARROW.addPoint(5,5);
}


/**
 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#createFigure()
 */
protected IFigure createFigure() {
	ARROW.removeAllPoints();
	ARROW.addPoint(0,0);
	ARROW.addPoint(10,0);
	ARROW.addPoint(5,5);
	SubgraphFigure f = new SubgraphFigure(new StartTag(""), new EndTag("")) {
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);
			graphics.setBackgroundColor(ColorConstants.button);
			Rectangle r = getBounds();
			graphics.fillRectangle(r.x + 13, r.y + 10, 8, r.height - 18);
//			graphics.fillPolygon(ARROW);
//			graphics.drawPolygon(ARROW);
		}
	};
	f.setBorder(MARGIN_BORDER);
	f.setOpaque(true);
	return f;
}

/**
 * @see ActivityPart#contributeEdgesToGraph(org.eclipse.graph.CompoundDirectedGraph, 
 * 											java.util.Map)
 */
public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map) {
	super.contributeEdgesToGraph(graph, map);
	Node node, prev = null;
	EditPart a;
	List members = getChildren();
	for (int n = 0; n < members.size(); n++) {
		a = (EditPart)members.get(n);
		node = (Node)map.get(a);
		if (prev != null) {
			Edge e = new Edge(prev, node);
			e.weight = 50;
			graph.edges.add(e);
		}
		prev = node;
	}
}

}
