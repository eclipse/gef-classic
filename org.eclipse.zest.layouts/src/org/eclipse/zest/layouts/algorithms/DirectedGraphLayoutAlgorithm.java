package org.eclipse.mylyn.zest.layouts.algorithms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.mylyn.zest.layouts.dataStructures.InternalNode;
import org.eclipse.mylyn.zest.layouts.dataStructures.InternalRelationship;

public class DirectedGraphLayoutAlgorithm extends AbstractLayoutAlgorithm {

	class ExtendedDirectedGraphLayout extends DirectedGraphLayout {

		public void visit(DirectedGraph graph) {
			Field field;
			try {
				field = DirectedGraphLayout.class.getDeclaredField("steps");
				field.setAccessible(true);
				Object object = field.get(this);
				List steps = (List) object;
				steps.remove(10);
				steps.remove(9);
				steps.remove(8);
				steps.remove(2);
				field.setAccessible(false);
				super.visit(graph);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	public DirectedGraphLayoutAlgorithm(int styles) {
		super(styles);
	}

	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		HashMap mapping = new HashMap(entitiesToLayout.length);
		DirectedGraph graph = new DirectedGraph();
		for (int i = 0; i < entitiesToLayout.length; i++) {
			InternalNode internalNode = entitiesToLayout[i];
			Node node = new Node(internalNode);
			node.setSize(new Dimension(10, 10));
			mapping.put(internalNode, node);
			graph.nodes.add(node);
		}
		for (int i = 0; i < relationshipsToConsider.length; i++) {
			InternalRelationship relationship = relationshipsToConsider[i];
			Node source = (Node) mapping.get(relationship.getSource());
			Node dest = (Node) mapping.get(relationship.getDestination());
			Edge edge = new Edge(relationship, source, dest);
			graph.edges.add(edge);
		}
		DirectedGraphLayout directedGraphLayout = new ExtendedDirectedGraphLayout();
		directedGraphLayout.visit(graph);

		for (Iterator iterator = graph.nodes.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			InternalNode internalNode = (InternalNode) node.data;
			internalNode.setInternalLocation(node.x, node.y);
		}
		updateLayoutLocations(entitiesToLayout);
	}

	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected int getTotalNumberOfLayoutSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		// TODO Auto-generated method stub
		return true;
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
		// TODO Auto-generated method stub

	}

	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
		// TODO Auto-generated method stub

	}

	public void setLayoutArea(double x, double y, double width, double height) {
		// TODO Auto-generated method stub

	}

}
