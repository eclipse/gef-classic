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

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.dot.DotAst.Layout;
import org.eclipse.zest.dot.DotAst.Style;
import org.eclipse.zest.internal.dot.parser.dot.a_list;
import org.eclipse.zest.internal.dot.parser.dot.attr_stmt;
import org.eclipse.zest.internal.dot.parser.dot.attributetype;
import org.eclipse.zest.internal.dot.parser.dot.edgeRHS_node;
import org.eclipse.zest.internal.dot.parser.dot.edge_stmt_node;
import org.eclipse.zest.internal.dot.parser.dot.graph;
import org.eclipse.zest.internal.dot.parser.dot.graphtype;
import org.eclipse.zest.internal.dot.parser.dot.node_id;
import org.eclipse.zest.internal.dot.parser.dot.node_stmt;
import org.eclipse.zest.internal.dot.parser.dot.stmt;
import org.eclipse.zest.internal.dot.parser.dot.util.DotSwitch;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * Create a Zest graph instance from a DOT string by interpreting the AST of the
 * parsed DOT.
 * 
 * @author Fabian Steeg (fsteeg)
 */
final class GraphCreatorInterpreter extends DotSwitch<Object> implements
		IGraphCreator {

	private Map<String, GraphNode> nodes = new HashMap<String, GraphNode>();
	private Graph graph;
	private String globalEdgeStyle;
	private String globalEdgeLabel;
	private String globalNodeLabel;
	private int style;
	private Composite parent;
	private String currentEdgeStyleValue;
	private String currentEdgeLabelValue;
	private String currentEdgeSourceNodeId;
	private boolean gotSource;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.dot.IGraphCreator#create(org.eclipse.swt.widgets.Composite,
	 *      int, java.lang.String)
	 */
	public Graph create(Composite parent, int style, DotAst dotAst) {
		this.parent = parent;
		this.style = style;
		TreeIterator<Object> contents = EcoreUtil.getAllProperContents(
				dotAst.resource(), false);
		while (contents.hasNext()) {
			doSwitch((EObject) contents.next());
		}
		if (graph == null) {
			throw new IllegalStateException(String.format(
					DotMessages.GraphCreatorInterpreter_0 + ": %s, DOT: %s",
					dotAst.errors().toString(), dotAst));
		}
		return graph;
	}

	@Override
	public Object casegraph(graph object) {
		createGraph(object);
		return super.casegraph(object);
	}

	@Override
	public Object caseattr_stmt(attr_stmt object) {
		createAttributes(object);
		return super.caseattr_stmt(object);
	}

	@Override
	public Object casenode_stmt(node_stmt object) {
		createNode(object);
		return super.casenode_stmt(object);
	}

	@Override
	public Object caseedge_stmt_node(edge_stmt_node object) {
		currentEdgeLabelValue = getAttributeValue(object, "label"); //$NON-NLS-1$
		currentEdgeStyleValue = getAttributeValue(object, "style"); //$NON-NLS-1$
		return super.caseedge_stmt_node(object);
	}

	@Override
	public Object casenode_id(node_id object) {
		if (!gotSource) {
			currentEdgeSourceNodeId = object.getName();
			gotSource = true;
		} else {
			String targetNodeId = object.getName();
			if (currentEdgeSourceNodeId != null && targetNodeId != null) {
				GraphConnection graphConnection = new GraphConnection(graph,
						SWT.NONE, node(currentEdgeSourceNodeId),
						node(targetNodeId));
				/* Set the optional label, if set in the DOT input: */
				if (currentEdgeLabelValue != null) {
					graphConnection.setText(currentEdgeLabelValue);
				} else if (globalEdgeLabel != null) {
					graphConnection.setText(globalEdgeLabel);
				}
				/* Set the optional style, if set in the DOT input: */
				if (currentEdgeStyleValue != null) {
					Style v = Style.valueOf(Style.class,
							currentEdgeStyleValue.toUpperCase());
					graphConnection.setLineStyle(v.style);
				} else if (globalEdgeStyle != null) {
					Style v = Style.valueOf(Style.class,
							globalEdgeStyle.toUpperCase());
					graphConnection.setLineStyle(v.style);
				}
			}
			gotSource = false;
		}
		return super.casenode_id(object);
	}

	@Override
	public Object caseedgeRHS_node(edgeRHS_node object) {
		// Set the flag for the node_id case handled above
		gotSource = true;
		return super.caseedgeRHS_node(object);
	}

	// private implementation of the cases above

	private void createGraph(graph object) {
		graph = new Graph(parent, style);
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		graphtype graphType = object.getType();
		graph.setConnectionStyle(graphType == graphtype.DIGRAPH ? ZestStyles.CONNECTIONS_DIRECTED
				: ZestStyles.CONNECTIONS_SOLID);
	}

	private void createAttributes(final attr_stmt attrStmt) {
		attributetype type = attrStmt.getType();
		switch (type) {
		case EDGE: {
			globalEdgeStyle = getAttributeValue(attrStmt, "style"); //$NON-NLS-1$
			globalEdgeLabel = getAttributeValue(attrStmt, "label"); //$NON-NLS-1$
			break;
		}
		case NODE: {
			globalNodeLabel = getAttributeValue(attrStmt, "label"); //$NON-NLS-1$
			break;
		}
		case GRAPH: {
			String graphLayout = getAttributeValue(attrStmt, "layout"); //$NON-NLS-1$
			if (graphLayout != null) {
				Layout layout = Layout.valueOf(Layout.class,
						graphLayout.toUpperCase());
				graph.setLayoutAlgorithm(layout.algorithm, true);
			}
			break;
		}
		}
	}

	private void createNode(final node_stmt eStatementObject) {
		String nodeId = eStatementObject.getName();
		GraphNode node = new GraphNode(graph, SWT.NONE, nodeId);
		node.setText(nodeId);
		String value = getAttributeValue(eStatementObject, "label"); //$NON-NLS-1$
		if (value != null) {
			node.setText(value);
		} else if (globalNodeLabel != null) {
			node.setText(globalNodeLabel);
		}
		nodes.put(nodeId, node);
	}

	private GraphNode node(String id) {
		if (!nodes.containsKey(id)) {
			nodes.put(id, new GraphNode(graph, SWT.NONE, id));
		}
		return nodes.get(id);
	}

	/**
	 * @param eStatementObject
	 *            The statement object, e.g. the object corresponding to
	 *            "node[label="hi"]"
	 * @param attributeName
	 *            The name of the attribute to get the value for, e.g. "label"
	 * @return The value of the given attribute, e.g. "hi"
	 */
	private String getAttributeValue(final stmt eStatementObject,
			final String attributeName) {
		Iterator<EObject> nodeContents = eStatementObject.eContents()
				.iterator();
		while (nodeContents.hasNext()) {
			EObject nodeContentElement = nodeContents.next();
			if (nodeContentElement.eClass().getName().equals("attr_list")) { //$NON-NLS-1$
				Iterator<EObject> attributeContents = nodeContentElement
						.eContents().iterator();
				while (attributeContents.hasNext()) {
					EObject next = attributeContents.next();
					if (next instanceof a_list) {
						a_list attributeElement = (a_list) next;
						if (attributeElement.getName().equals(attributeName)) {
							String label = attributeElement.getValue()
									.replaceAll("\"", ""); //$NON-NLS-1$//$NON-NLS-2$
							return label;
						}
					}
				}
			}
		}
		return null;
	}
}