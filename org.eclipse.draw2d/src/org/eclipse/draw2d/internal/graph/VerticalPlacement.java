package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.*;

/**
 * 
 * @author hudsonr
 * Created on Jul 17, 2003
 */
public class VerticalPlacement extends GraphVisitor {

public void visit(DirectedGraph g) {
	Insets pad;
	int currentY = 0;
	for (int row = 0; row < g.ranks.size(); row++) {
		int rowHeight = 0, rowAscent = 0, rowDescent = 0;
		Rank rank = g.ranks.getRank(row);
		for (int n = 0; n < rank.size(); n++) {
			Node node = rank.getNode(n);
			pad = g.getPadding(node);
			rowHeight = Math.max(node.height, rowHeight);
			rowAscent = Math.max(pad.top, rowAscent);
			rowDescent = Math.max(pad.bottom, rowDescent);
		}
		currentY += rowAscent;
		for (int n = 0; n < rank.size(); n++) {
			Node node = rank.getNode(n);
			node.y = currentY;
			node.height = rowHeight;
		}
		currentY += rowHeight + rowDescent;
	}
}

}
