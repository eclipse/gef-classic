/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.dot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * Create a Zest graph instance from a DOT string by interpreting the AST of the
 * parsed DOT.
 * 
 * @author Fabian Steeg (fsteeg)
 */
final class GraphCreatorInterpreter implements IGraphCreator {
	private Map<String, GraphNode> nodes = new HashMap<String, GraphNode>();
	private Graph graph = null;
	private String globalEdgeStyle;
	private String globalEdgeLabel;
	private String globalNodeLabel;
	private static final int STYLE = LayoutStyles.NO_LAYOUT_NODE_RESIZING;

	/** Edge style attributes in the DOT input and their Zest/SWT styles. */
	private enum Style {
		DASHED(SWT.LINE_DASH), DOTTED(SWT.LINE_DOT), SOLID(SWT.LINE_SOLID), DASHDOT(
				SWT.LINE_DASHDOT), DASHDOTDOT(SWT.LINE_DASHDOTDOT);
		private int style;

		Style(final int style) {
			this.style = style;
		}
	}

	/**
	 * Graph layout attributes in the DOT input and their Zest layout
	 * algorithms.
	 */
	private enum Layout {
		TREE(new TreeLayoutAlgorithm(STYLE)), GRID(new GridLayoutAlgorithm(
				STYLE)), RADIAL(new RadialLayoutAlgorithm(STYLE)), SPRING(
				new SpringLayoutAlgorithm(STYLE));
		private AbstractLayoutAlgorithm algorithm;

		Layout(final AbstractLayoutAlgorithm algorithm) {
			this.algorithm = algorithm;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.dot.IGraphCreator#create(org.eclipse.swt.widgets.Composite,
	 *      int, java.lang.String)
	 */
	public Graph create(final Composite parent, final int style,
			final String dot) {
		DotImport importer = new DotImport(dot);
		graph = new Graph(parent, style);
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		EObject eGraph = importer.getDotAst().graph();
		String graphType = DotAst.getValue(eGraph, "type"); //$NON-NLS-1$
		graph.setConnectionStyle(graphType.equals("digraph") ? ZestStyles.CONNECTIONS_DIRECTED //$NON-NLS-1$
				: ZestStyles.CONNECTIONS_SOLID);
		addContent(eGraph);
		return graph;
	}

	private void addContent(final EObject eGraph) {
		EList<EObject> eContents = eGraph.eContents();
		for (EObject eStatementObject : eContents) {
			String eStatementClassName = eStatementObject.eClass().getName();
			if (eStatementClassName.equals("attr_stmt")) { //$NON-NLS-1$
				createAttributes(eStatementObject);
			} else if (eStatementClassName.equals("node_stmt")) { //$NON-NLS-1$
				createNode(eStatementObject);
			} else if (eStatementClassName.equals("edge_stmt_node")) { //$NON-NLS-1$
				createEdge(eStatementObject);
			}
		}
	}

	private void createAttributes(final EObject eStatementObject) {
		String type = DotAst.getValue(eStatementObject, "type"); //$NON-NLS-1$
		if (type.equals("edge")) { //$NON-NLS-1$
			globalEdgeStyle = DotAst.getAttributeValue(eStatementObject,
					"style"); //$NON-NLS-1$
			globalEdgeLabel = DotAst.getAttributeValue(eStatementObject,
					"label"); //$NON-NLS-1$
		} else if (type.equals("node")) { //$NON-NLS-1$
			globalNodeLabel = DotAst.getAttributeValue(eStatementObject,
					"label"); //$NON-NLS-1$
		} else if (type.equals("graph")) { //$NON-NLS-1$
			String graphLayout = DotAst.getAttributeValue(eStatementObject,
					"layout"); //$NON-NLS-1$
			if (graphLayout != null) {
				Layout layout = Layout.valueOf(Layout.class,
						graphLayout.toUpperCase());
				graph.setLayoutAlgorithm(layout.algorithm, true);
			}
		}
	}

	private void createEdge(final EObject eStatementObject) {
		Iterator<EObject> eEdgeContents = eStatementObject.eContents()
				.iterator();
		String sourceNodeId = null;
		String labelValue = DotAst.getAttributeValue(eStatementObject, "label"); //$NON-NLS-1$
		String styleValue = DotAst.getAttributeValue(eStatementObject, "style"); //$NON-NLS-1$
		while (eEdgeContents.hasNext()) {
			EObject eEdgeContentElement = eEdgeContents.next();
			String eClassName = eEdgeContentElement.eClass().getName();
			if (eClassName.equals("node_id")) { //$NON-NLS-1$
				sourceNodeId = DotAst.getValue(eEdgeContentElement, "name"); //$NON-NLS-1$
			} else if (eClassName.equals("edgeRHS_node")) { //$NON-NLS-1$
				createConnectionsForRhs(sourceNodeId, eEdgeContentElement,
						labelValue, styleValue);
			}
		}
	}

	private void createConnectionsForRhs(final String sourceNodeId,
			final EObject edgeRhsElement, final String labelValue,
			final String styleValue) {
		String targetNodeId;
		Iterator<EObject> eRhsContents = edgeRhsElement.eContents().iterator();
		while (eRhsContents.hasNext()) {
			EObject eRhsContentElement = eRhsContents.next();
			if (eRhsContentElement.eClass().getName().equals("node_id")) { //$NON-NLS-1$
				targetNodeId = DotAst.getValue(eRhsContentElement, "name"); //$NON-NLS-1$
				if (sourceNodeId != null && targetNodeId != null) {
					GraphConnection graphConnection = new GraphConnection(
							graph, SWT.NONE, nodes.get(sourceNodeId),
							nodes.get(targetNodeId));
					/* Set the optional label, if set in the DOT input: */
					if (labelValue != null) {
						graphConnection.setText(labelValue);
					} else if (globalEdgeLabel != null) {
						graphConnection.setText(globalEdgeLabel);
					}
					/* Set the optional style, if set in the DOT input: */
					if (styleValue != null) {
						Style v = Style.valueOf(Style.class,
								styleValue.toUpperCase());
						graphConnection.setLineStyle(v.style);
					} else if (globalEdgeStyle != null) {
						Style v = Style.valueOf(Style.class,
								globalEdgeStyle.toUpperCase());
						graphConnection.setLineStyle(v.style);
					}
				}
			}
		}
	}

	private void createNode(final EObject eStatementObject) {
		String nodeId = DotAst.getValue(eStatementObject, "name"); //$NON-NLS-1$
		GraphNode node = new GraphNode(graph, SWT.NONE, nodeId);
		node.setText(nodeId);
		String value = DotAst.getAttributeValue(eStatementObject, "label"); //$NON-NLS-1$
		if (value != null) {
			node.setText(value);
		} else if (globalNodeLabel != null) {
			node.setText(globalNodeLabel);
		}
		nodes.put(nodeId, node);
	}
}
