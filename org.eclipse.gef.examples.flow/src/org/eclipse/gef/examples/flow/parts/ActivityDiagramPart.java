package org.eclipse.gef.examples.flow.parts;

import java.util.*;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.examples.flow.policies.ActivityContainerEditPolicy;
import org.eclipse.graph.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * Created on Jul 16, 2003
 */
public class ActivityDiagramPart extends StructuredActivityPart {

class GraphLayoutManager extends AbstractLayout {
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		container.validate();
		List children = container.getChildren();
		Rectangle result =
			new Rectangle().setLocation(container.getClientArea().getLocation());
		for (int i = 0; i < children.size(); i++)
			result.union(((IFigure)children.get(i)).getBounds());
		result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());
		return result.getSize();
	}
	public void layout(IFigure container) {
		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		Map partsToNodes = new HashMap();
		contributeNodesToGraph(graph, null, partsToNodes);
		contributeEdgesToGraph(graph, partsToNodes);
		new CompoundDirectedGraphLayout().visit(graph);
		applyGraphResults(graph, partsToNodes);
	}
}

protected void applyOwnResults(CompoundDirectedGraph graph, Map map) { }

/**
 * @see org.eclipse.gef.examples.flow.parts.ActivityPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.NODE_ROLE, null);
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new ActivityContainerEditPolicy());
}


protected IFigure createFigure() {
	Figure f = new Figure() {
		public void setBounds(Rectangle rect) {
			int x = bounds.x,
			    y = bounds.y;
		
			boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height),
				  translate = (rect.x != x) || (rect.y != y);
		
			if (isVisible() && (resize || translate))
				erase();
			if (translate) {
				int dx = rect.x - x;
				int dy = rect.y - y;
				primTranslate(dx, dy);
			}
			bounds.width = rect.width;
			bounds.height = rect.height;
			if (resize || translate) {
				fireMoved();
				repaint();
			}
		}
	};
	f.setLayoutManager(new GraphLayoutManager());
	return f;
}
/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
 */
public boolean isSelectable() {
	return false;
}

}
