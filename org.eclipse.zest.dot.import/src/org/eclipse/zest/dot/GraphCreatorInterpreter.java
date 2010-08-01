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
import org.eclipse.zest.internal.dot.parser.dot.AList;
import org.eclipse.zest.internal.dot.parser.dot.AttrList;
import org.eclipse.zest.internal.dot.parser.dot.AttrStmt;
import org.eclipse.zest.internal.dot.parser.dot.Attribute;
import org.eclipse.zest.internal.dot.parser.dot.AttributeType;
import org.eclipse.zest.internal.dot.parser.dot.DotGraph;
import org.eclipse.zest.internal.dot.parser.dot.EdgeRhsNode;
import org.eclipse.zest.internal.dot.parser.dot.EdgeStmtNode;
import org.eclipse.zest.internal.dot.parser.dot.GraphType;
import org.eclipse.zest.internal.dot.parser.dot.NodeId;
import org.eclipse.zest.internal.dot.parser.dot.NodeStmt;
import org.eclipse.zest.internal.dot.parser.dot.Stmt;
import org.eclipse.zest.internal.dot.parser.dot.util.DotSwitch;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

/**
 * Create a Zest graph instance from a DOT string by interpreting the AST of the
 * parsed DOT.
 * 
 * @author Fabian Steeg (fsteeg)
 */
final class GraphCreatorInterpreter extends DotSwitch<Object> {

	private Map<String, GraphNode> nodes = new HashMap<String, GraphNode>();
	private Graph graph;
	private String globalEdgeStyle;
	private String globalEdgeLabel;
	private String globalNodeLabel;
	private String currentEdgeStyleValue;
	private String currentEdgeLabelValue;
	private String currentEdgeSourceNodeId;
	private boolean gotSource;

	Graph create(Composite parent, int style, DotAst dotAst) {
		return create(dotAst, new Graph(parent, style));
	}

	Graph create(DotAst dotAst, Graph graph) {
		if (dotAst.errors().size() > 0) {
			throw new IllegalArgumentException(String.format(
					DotMessages.GraphCreatorInterpreter_0 + ": %s", dotAst
							.errors().toString()));
		}
		this.graph = graph;
		TreeIterator<Object> contents = EcoreUtil.getAllProperContents(
				dotAst.resource(), false);
		while (contents.hasNext()) {
			doSwitch((EObject) contents.next());
		}
		return graph;
	}

	@Override
	public Object caseDotGraph(DotGraph object) {
		createGraph(object);
		return super.caseDotGraph(object);
	}

	@Override
	public Object caseAttribute(Attribute object) {
		/*
		 * Convenience for common 'rankdir=LR' attribute: use
		 * HorizontalTreeLayoutAlgorithm if nothing else is specified
		 */
		if (object.getName().equals("rankdir") //$NON-NLS-1$
				&& object.getValue().equals("LR")) { //$NON-NLS-1$
			graph.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(
					LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		}
		return super.caseAttribute(object);
	}

	@Override
	public Object caseAttrStmt(AttrStmt object) {
		createAttributes(object);
		return super.caseAttrStmt(object);
	}

	@Override
	public Object caseNodeStmt(NodeStmt object) {
		createNode(object);
		return super.caseNodeStmt(object);
	}

	@Override
	public Object caseEdgeStmtNode(EdgeStmtNode object) {
		currentEdgeLabelValue = getAttributeValue(object, "label"); //$NON-NLS-1$
		currentEdgeStyleValue = getAttributeValue(object, "style"); //$NON-NLS-1$
		return super.caseEdgeStmtNode(object);
	}

	@Override
	public Object caseNodeId(NodeId object) {
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
		return super.caseNodeId(object);
	}

	@Override
	public Object caseEdgeRhsNode(EdgeRhsNode object) {
		// Set the flag for the node_id case handled above
		gotSource = true;
		return super.caseEdgeRhsNode(object);
	}

	// private implementation of the cases above

	private void createGraph(DotGraph object) {
		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		GraphType graphType = object.getType();
		graph.setConnectionStyle(graphType == GraphType.DIGRAPH ? ZestStyles.CONNECTIONS_DIRECTED
				: ZestStyles.CONNECTIONS_SOLID);
	}

	private void createAttributes(final AttrStmt attrStmt) {
		AttributeType type = attrStmt.getType();
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

	private void createNode(final NodeStmt eStatementObject) {
		String nodeId = eStatementObject.getName();
		GraphNode node = new GraphNode(graph, SWT.NONE, nodeId);
		node.setText(nodeId);
		node.setData(nodeId);
		String value = getAttributeValue(eStatementObject, "label"); //$NON-NLS-1$
		if (value != null) {
			node.setText(value);
		} else if (globalNodeLabel != null) {
			node.setText(globalNodeLabel);
		}
		nodes.put(nodeId, node);
	}

	private GraphNode node(String id) {
		if (!nodes.containsKey(id)) { // undeclared node, as in "graph{1->2}"
			GraphNode node = new GraphNode(graph, SWT.NONE,
					globalNodeLabel != null ? globalNodeLabel : id);
			node.setData(id);
			nodes.put(id, node);
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
	private String getAttributeValue(final Stmt eStatementObject,
			final String attributeName) {
		Iterator<EObject> nodeContents = eStatementObject.eContents()
				.iterator();
		while (nodeContents.hasNext()) {
			EObject nodeContentElement = nodeContents.next();
			if (nodeContentElement instanceof AttrList) {
				Iterator<EObject> attributeContents = nodeContentElement
						.eContents().iterator();
				while (attributeContents.hasNext()) {
					EObject next = attributeContents.next();
					if (next instanceof AList) {
						AList attributeElement = (AList) next;
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