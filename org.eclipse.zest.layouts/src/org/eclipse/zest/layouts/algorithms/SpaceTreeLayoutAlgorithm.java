/*******************************************************************************
 * Copyright (c) 2005-2010, 2024 The Chisel Group and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group - initial API and implementation
 *               Mateusz Matela
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.interfaces.ContextListener;
import org.eclipse.zest.layouts.interfaces.ExpandCollapseManager;
import org.eclipse.zest.layouts.interfaces.LayoutContext;
import org.eclipse.zest.layouts.interfaces.LayoutListener;
import org.eclipse.zest.layouts.interfaces.NodeLayout;
import org.eclipse.zest.layouts.interfaces.SubgraphLayout;

/**
 * Layout algorithm implementing SpaceTree. It assumes that nodes in the layout
 * context make a tree structure.
 *
 * It expands and collapses nodes to optimize use of available space. In order
 * to keep the tree structure clearly visible, it also keeps track of the nodes'
 * positions to makes sure they stay in their current layer and don't overlap
 * with each other.
 *
 * It's recommended to set an <code>ExpandCollapseManger</code> returned by
 * {@link #getExpandCollapseManager()} in the <code>LayoutContext</code> that
 * will be used with this layout algorithm. Other
 * <code>ExpandCollapseManager</code>s could also work, but the algorithm's
 * functionality would be very limited.
 *
 * @since 2.0
 */
public class SpaceTreeLayoutAlgorithm extends AbstractLayoutAlgorithm {

	/**
	 * Tree direction constant for which root is placed at the top and branches
	 * spread downwards
	 */
	public final static int TOP_DOWN = 1;

	/**
	 * Tree direction constant for which root is placed at the bottom and branches
	 * spread upwards
	 */
	public final static int BOTTOM_UP = 2;

	/**
	 * Tree direction constant for which root is placed at the left and branches
	 * spread to the right
	 */
	public final static int LEFT_RIGHT = 3;

	/**
	 * Tree direction constant for which root is placed at the right and branches
	 * spread to the left
	 */
	public final static int RIGHT_LEFT = 4;

	private class SpaceTreeNode extends TreeLayoutObserver.TreeNode {
		public SubgraphLayout subgraph = null;

		public boolean expanded = false;
		public double positionInLayer;

		public SpaceTreeNode(NodeLayout node, TreeLayoutObserver owner) {
			super(node, owner);
		}

		@Override
		protected void addChild(TreeLayoutObserver.TreeNode child2) {
			super.addChild(child2);

			SpaceTreeNode child = (SpaceTreeNode) child2;
			child.expanded = false;
			child.setSubgraph(null);

			if (child.depth >= 0) {
				spaceTreeLayers.get(child.depth).removeNode(child);
			}

			if (expanded) {
				child.depth = this.depth + 1;

				SpaceTreeLayer childLayer;
				if (child.depth < spaceTreeLayers.size()) {
					childLayer = (spaceTreeLayers.get(child.depth));
				} else {
					spaceTreeLayers.add(childLayer = new SpaceTreeLayer(child.depth));
				}

				if (childLayer.nodes.isEmpty()) {
					child.order = 0;
				} else {
					child.order = childLayer.nodes.get(childLayer.nodes.size() - 1).order + 1;
				}
				childLayer.addNodes(Arrays.asList(child));
			}
		}

		@Override
		public void precomputeTree() {
			super.precomputeTree();
			if (this == owner.getSuperRoot()) {
				expanded = true;
				while (spaceTreeLayers.size() <= this.height) {
					spaceTreeLayers.add(new SpaceTreeLayer(spaceTreeLayers.size()));
				}

				if (treeObserver != null) {
					refreshLayout(true);
				}
			}
		}

		public SubgraphLayout collapseAllChildrenIntoSubgraph(SubgraphLayout subgraph, boolean includeYourself) {
			expanded = false;
			List<NodeLayout> allChildren = new ArrayList<>();
			LinkedList<SpaceTreeNode> nodesToVisit = new LinkedList<>();
			nodesToVisit.addLast(this);
			while (!nodesToVisit.isEmpty()) {
				SpaceTreeNode currentNode = nodesToVisit.removeFirst();
				for (SpaceTreeNode child : currentNode.getChildren()) {
					allChildren.add(child.node);
					child.setSubgraph(null);
					child.expanded = false;
					nodesToVisit.addLast(child);
				}
			}
			if (includeYourself) {
				allChildren.add(this.node);
			}
			if (allChildren.isEmpty()) {
				setSubgraph(null);
				return null;
			}
			NodeLayout[] childrenArray = allChildren.toArray(new NodeLayout[allChildren.size()]);
			if (subgraph == null) {
				subgraph = context.createSubgraph(childrenArray);
				subgraph.setDirection(getSubgraphDirection());
			} else {
				subgraph.addNodes(childrenArray);
			}
			if (!includeYourself) {
				setSubgraph(subgraph);
			}
			return subgraph;
		}

		public void setSubgraph(SubgraphLayout subgraph) { // !
			if (this.subgraph != subgraph) {
				this.subgraph = subgraph;
				refreshSubgraphLocation();
			}
		}

		/**
		 * Moves the node back to its layer, as close as possible to given preferred
		 * location
		 *
		 * @param preferredLocation
		 */
		public void adjustPosition(DisplayIndependentPoint preferredLocation) { // !
			protectedNode = (SpaceTreeNode) owner.getSuperRoot();

			double newPositionInLayer = (direction == BOTTOM_UP || direction == TOP_DOWN) ? preferredLocation.x
					: preferredLocation.y;
			if (getParent().expanded) {
				spaceTreeLayers.get(depth).moveNode(this, newPositionInLayer);
				centerParentsTopDown();
			}
		}

		public void refreshSubgraphLocation() {
			if (subgraph != null && subgraph.isGraphEntity()) {
				DisplayIndependentPoint nodeLocation = node.getLocation();
				DisplayIndependentDimension nodeSize = node.getSize();
				DisplayIndependentDimension subgraphSize = subgraph.getSize();
				double x = 0, y = 0;
				switch (direction) {
				case TOP_DOWN:
					x = nodeLocation.x;
					y = nodeLocation.y + (nodeSize.height + subgraphSize.height) / 2;
					break;
				case BOTTOM_UP:
					x = nodeLocation.x;
					y = nodeLocation.y - (nodeSize.height + subgraphSize.height) / 2;
					break;
				case LEFT_RIGHT:
					x = nodeLocation.x + (nodeSize.width + subgraphSize.width) / 2;
					y = nodeLocation.y;
					break;
				case RIGHT_LEFT:
					x = nodeLocation.x - (nodeSize.width + subgraphSize.height) / 2;
					y = nodeLocation.y;
					break;
				}
				subgraph.setLocation(x, y);
			}
			spaceTreeLayers.get(depth).refreshThickness();
		}

		public double spaceRequiredForNode() {
			if (node == null) {
				return 0;
			}
			switch (direction) {
			case TOP_DOWN:
			case BOTTOM_UP:
				return node.getSize().width;
			case LEFT_RIGHT:
			case RIGHT_LEFT:
				return node.getSize().height;
			}
			throw new RuntimeException("invalid direction"); //$NON-NLS-1$
		}

		public double spaceRequiredForChildren() {
			if (children.isEmpty()) {
				return 0;
			}
			double result = 0;
			for (SpaceTreeNode child : getChildren()) {
				result += child.spaceRequiredForNode();
			}
			result += leafGap * (children.size() - 1);
			return result;
		}

		/**
		 * Checks if nodes in given list have proper positions according to their
		 * children (a parent's position cannot be smaller than its first child's
		 * position nor bigger than its last child's position). If not, it tries to fix
		 * them.
		 *
		 * @param nodesToCheck
		 * @return true if all locations are correct or could be corrected while
		 *         checking.
		 */
		public boolean childrenPositionsOK(List<SpaceTreeNode> nodesToCheck) {
			for (SpaceTreeNode node : nodesToCheck) {
				if (node.depth < 0 || node.children.isEmpty()) {
					continue;
				}
				SpaceTreeNode child = (node.getChildren().get(0));
				if (child.positionInLayer > node.positionInLayer) {
					spaceTreeLayers.get(node.depth).moveNode(node, child.positionInLayer);
					if (child.positionInLayer > node.positionInLayer) {
						spaceTreeLayers.get(child.depth).moveNode(child, node.positionInLayer);
						if (child.positionInLayer > node.positionInLayer) {
							return false;
						}
					}
				}
				child = node.getChildren().get(node.children.size() - 1);
				if (child.positionInLayer < node.positionInLayer) {
					spaceTreeLayers.get(node.depth).moveNode(node, child.positionInLayer);
					if (child.positionInLayer < node.positionInLayer) {
						spaceTreeLayers.get(child.depth).moveNode(child, node.positionInLayer);
						if (child.positionInLayer < node.positionInLayer) {
							return false;
						}
					}
				}
			}
			return true;
		}

		public void centerParentsBottomUp() {
			if (!children.isEmpty() && expanded) {
				for (SpaceTreeNode child : getChildren()) {
					child.centerParentsBottomUp();
				}

				if (depth >= 0) {
					SpaceTreeNode firstChild = getChildren().get(0);
					SpaceTreeNode lastChild = getChildren().get(children.size() - 1);
					SpaceTreeLayer layer = spaceTreeLayers.get(depth);
					layer.moveNode(this, (firstChild.positionInLayer + lastChild.positionInLayer) / 2);
				}
			}
		}

		public void centerParentsTopDown() {
			if (this == owner.getSuperRoot()) {
				this.positionInLayer = getAvailableSpace() / 2;
			}
			if (!children.isEmpty() && expanded) {
				SpaceTreeNode firstChild = getChildren().get(0);
				SpaceTreeNode lastChild = getChildren().get(children.size() - 1);
				double offset = this.positionInLayer - (firstChild.positionInLayer + lastChild.positionInLayer) / 2;
				if (firstChild.positionInLayer - firstChild.spaceRequiredForNode() / 2 + offset < 0) {
					offset = -firstChild.positionInLayer + firstChild.spaceRequiredForNode() / 2;
				}
				double availableSpace = getAvailableSpace();
				if (lastChild.positionInLayer + lastChild.spaceRequiredForNode() / 2 + offset > availableSpace) {
					offset = availableSpace - lastChild.positionInLayer - lastChild.spaceRequiredForNode() / 2;
				}
				SpaceTreeLayer layer = spaceTreeLayers.get(depth + 1);
				layer.fitNodesWithinBounds(getChildren(), firstChild.positionInLayer + offset,
						lastChild.positionInLayer + offset);

				for (SpaceTreeNode child : getChildren()) {
					child.centerParentsTopDown();
				}
			}
		}

		public void flushExpansionChanges() {
			if (node != null) {
				node.prune(null);
			}
			if (this.expanded) {
				setSubgraph(null);
				for (SpaceTreeNode child : getChildren()) {
					child.flushExpansionChanges();
				}
			}
			if (!this.expanded && subgraph == null) {
				collapseAllChildrenIntoSubgraph(null, false);
			}
		}

		public boolean flushCollapseChanges() {
			if (!expanded) {
				int numberOfChildrenInSubgraph = subgraph == null ? 0 : subgraph.countNodes();
				collapseAllChildrenIntoSubgraph(subgraph, false);
				int newNumberOfChildrenInSubgraph = (subgraph == null ? 0 : subgraph.countNodes());
				if (numberOfChildrenInSubgraph != newNumberOfChildrenInSubgraph && newNumberOfChildrenInSubgraph > 0) {
					refreshSubgraphLocation();
				}
				return numberOfChildrenInSubgraph != newNumberOfChildrenInSubgraph;
			}
			if (expanded && subgraph == null) {
				boolean madeChagnes = false;
				for (SpaceTreeNode child : getChildren()) {
					madeChagnes = child.flushCollapseChanges() || madeChagnes;
				}
				return madeChagnes;
			}
			return false;
		}

		/**
		 * Sets locations of nodes in the graph depending on their current layer and
		 * position in layer.
		 *
		 * @param thicknessSoFar sum of thicknesses and gaps for all layers 'above' this
		 *                       node (should be 0 if called on superRoot)
		 * @return true if location of at least one node has changed
		 */
		public boolean flushLocationChanges(double thicknessSoFar) {
			boolean madeChanges = false;
			if (node != null) {
				DisplayIndependentDimension nodeSize = node.getSize();
				double x = 0, y = 0;
				switch (direction) {
				case TOP_DOWN:
					x = bounds.x + positionInLayer;
					y = thicknessSoFar + nodeSize.height / 2;
					break;
				case BOTTOM_UP:
					x = bounds.x + positionInLayer;
					y = bounds.y + bounds.height - thicknessSoFar - nodeSize.height / 2;
					break;
				case LEFT_RIGHT:
					x = thicknessSoFar + nodeSize.height / 2;
					y = bounds.y + positionInLayer;
					break;
				case RIGHT_LEFT:
					x = bounds.x + bounds.width - thicknessSoFar - nodeSize.height / 2;
					y = bounds.y + positionInLayer;
					break;
				}
				DisplayIndependentPoint currentLocation = node.getLocation();
				if (currentLocation.x != x || currentLocation.y != y) {
					node.setLocation(x, y);
					refreshSubgraphLocation();
					madeChanges = true;
				}
			}
			if (expanded && subgraph == null) {
				thicknessSoFar += (depth >= 0 ? spaceTreeLayers.get(depth).thickness : 0) + layerGap;
				for (SpaceTreeNode child : getChildren()) {
					madeChanges = child.flushLocationChanges(thicknessSoFar) || madeChanges;
				}
			}
			return madeChanges;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < depth; i++) {
				sb.append(" "); //$NON-NLS-1$
			}
			if (node != null) {
				sb.append(node.toString());
			}
			sb.append("|" + this.order); //$NON-NLS-1$
			sb.append('\n');
			for (SpaceTreeNode child : getChildren()) {
				sb.append(child.toString());
			}
			return sb.toString();
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<SpaceTreeNode> getChildren() {
			return (List<SpaceTreeNode>) super.getChildren();
		}

		@Override
		public SpaceTreeNode getParent() {
			return (SpaceTreeNode) super.getParent();
		}
	}

	private final TreeLayoutObserver.TreeNodeFactory spaceTreeNodeFactory = new TreeLayoutObserver.TreeNodeFactory() {
		@Override
		public TreeLayoutObserver.TreeNode createTreeNode(NodeLayout nodeLayout, TreeLayoutObserver observer) {
			return new SpaceTreeNode(nodeLayout, observer);
		};
	};

	private class SpaceTreeLayer {
		public List<SpaceTreeNode> nodes = new ArrayList<>();
		private final int depth;
		public double thickness = 0;

		public SpaceTreeLayer(int depth) {
			this.depth = depth;
		}

		public void addNodes(List<SpaceTreeNode> nodesToAdd) {
			ListIterator<SpaceTreeNode> layerIterator = nodes.listIterator();
			SpaceTreeNode previousNode = null;
			for (SpaceTreeNode nodeToAdd : nodesToAdd) {

				SpaceTreeNode nodeInLayer = null;
				while (layerIterator.hasNext()) {
					nodeInLayer = layerIterator.next();
					if (nodeInLayer.order >= nodeToAdd.order) {
						break;
					}
					double expectedPostion = (previousNode == null) ? 0
							: previousNode.positionInLayer + expectedDistance(previousNode, nodeInLayer);
					nodeInLayer.positionInLayer = Math.max(nodeInLayer.positionInLayer, expectedPostion);
					previousNode = nodeInLayer;
				}

				if (nodeInLayer == null) {
					layerIterator.add(nodeToAdd);
				} else if (nodeInLayer.order == nodeToAdd.order) {
					layerIterator.set(nodeToAdd);
				} else {
					if (nodeInLayer.order > nodeToAdd.order) {
						layerIterator.previous();
					}
					layerIterator.add(nodeToAdd);
				}
				layerIterator.previous();
			}
			// move the rest of nodes so that they don't overlap
			while (layerIterator.hasNext()) {
				SpaceTreeNode nodeInLayer = layerIterator.next();
				double expectedPostion = (previousNode == null) ? 0
						: previousNode.positionInLayer + expectedDistance(previousNode, nodeInLayer);
				nodeInLayer.positionInLayer = Math.max(nodeInLayer.positionInLayer, expectedPostion);
				previousNode = nodeInLayer;
			}

			refreshThickness();
		}

		public void removeNode(SpaceTreeNode node) {
			if (nodes.remove(node)) {
				spaceTreeLayers.get(depth + 1).removeNodes(node.getChildren());
				refreshThickness();
			}
		}

		public void removeNodes(List<SpaceTreeNode> nodesToRemove) {
			if (this.nodes.removeAll(nodesToRemove)) {
				SpaceTreeLayer nextLayer = (spaceTreeLayers.get(depth + 1));
				for (SpaceTreeNode nodeToRemove : nodesToRemove) {
					nextLayer.removeNodes(nodeToRemove.getChildren());
				}
				refreshThickness();
			}
		}

		public void checkThickness(SpaceTreeNode node) {
			double nodeThickness = 0;
			DisplayIndependentDimension size = node.node.getSize();
			nodeThickness = (direction == TOP_DOWN || direction == BOTTOM_UP) ? size.height : size.width;
			if (node.subgraph != null && node.subgraph.isGraphEntity()) {
				size = node.subgraph.getSize();
				nodeThickness += (direction == TOP_DOWN || direction == BOTTOM_UP) ? size.height : size.width;
			}
			this.thickness = Math.max(this.thickness, nodeThickness);
		}

		public void refreshThickness() {
			this.thickness = 0;
			for (SpaceTreeNode node : nodes) {
				checkThickness(node);
			}
		}

		public void fitNodesWithinBounds(List<SpaceTreeNode> nodeList, double startPosition, double endPosition) {
			NodeSnapshot[][] snapShot = takeSnapShot();
			SpaceTreeNode[] nodes = nodeList.toArray(new SpaceTreeNode[nodeList.size()]);
			double initialStartPosition = nodes[0].positionInLayer;
			double initialNodesBredth = nodes[nodes.length - 1].positionInLayer - initialStartPosition;
			double[] desiredPositions = new double[nodes.length];
			// calculate desired positions for every node, regarding their
			// initial initial proportions
			for (int i = 0; i < nodes.length; i++) {
				double initialPositionAsPercent = (initialNodesBredth > 0)
						? (nodes[i].positionInLayer - initialStartPosition) / initialNodesBredth
						: 0;
				desiredPositions[i] = initialPositionAsPercent * (endPosition - startPosition);
			}
			// make sure there's proper distance between each pair of
			// consecutive nodes
			for (int i = 1; i < nodes.length; i++) {
				SpaceTreeNode node = nodes[i];
				SpaceTreeNode previousNode = nodes[i - 1];
				double expectedDistance = expectedDistance(previousNode, node);
				if (desiredPositions[i] - desiredPositions[i - 1] < expectedDistance) {
					desiredPositions[i] = desiredPositions[i - 1] + expectedDistance;
				}
			}
			// if the above operation caused some nodes to fall out of requested
			// bounds, push them back
			if (desiredPositions[nodes.length - 1] > (endPosition - startPosition)) {
				desiredPositions[nodes.length - 1] = (endPosition - startPosition);
				for (int i = nodes.length - 1; i > 0; i--) {
					SpaceTreeNode node = nodes[i];
					SpaceTreeNode previousNode = nodes[i - 1];
					double expectedDistance = expectedDistance(previousNode, node);
					if (desiredPositions[i] - desiredPositions[i - 1] >= expectedDistance) {
						break;
					}
					desiredPositions[i - 1] = desiredPositions[i] - expectedDistance;
				}
			}

			for (int i = 0; i < nodeList.size(); i++) {
				SpaceTreeNode node = nodeList.get(i);
				double desiredPosition = startPosition + desiredPositions[i];
				moveNode(node, desiredPosition);
				if (Math.abs(node.positionInLayer - desiredPosition) > 0.5) {
					startPosition += (node.positionInLayer - desiredPosition);
					i = -1;
					revertToShanpshot(snapShot);
				}
			}
		}

		public void moveNode(SpaceTreeNode node, double newPosition) {
			Collections.sort(nodes, (arg0, arg1) -> arg0.order - arg1.order);
			double positionInLayerAtStart = node.positionInLayer;
			if (newPosition >= positionInLayerAtStart) {
				moveNodeForward(node, newPosition);
			}
			if (newPosition <= positionInLayerAtStart) {
				moveNodeBackward(node, newPosition);
			}
		}

		/**
		 * Tries to increase node's position in layer. It can move a node only if it
		 * doesn't cause nodes to fall out of available space (see
		 * {@link SpaceTreeLayoutAlgorithm#getAvailableSpace()}. If there's not enough
		 * space available, some nodes may be collapsed to increase it as long as it
		 * doesn't cause {@link SpaceTreeLayoutAlgorithm#protectedNode} or any of its
		 * descendants to be collapsed.
		 *
		 * @param nodeToMove
		 * @param newPosition
		 */
		private void moveNodeForward(SpaceTreeNode nodeToMove, double newPosition) {
			int nodeIndex = nodes.indexOf(nodeToMove);
			if (nodeIndex == -1) {
				throw new IllegalArgumentException("node not on this layer"); //$NON-NLS-1$
			}
			// move forward -> check space to the 'right'
			NodeSnapshot[][] snapShot = takeSnapShot();
			boolean firstRun = true;
			mainLoop: while (firstRun || nodeToMove.positionInLayer < newPosition) {
				firstRun = false;
				double requiredSpace = 0;
				SpaceTreeNode previousNode = nodeToMove;
				for (int i = nodeIndex + 1; i < nodes.size(); i++) {
					SpaceTreeNode nextNode = nodes.get(i);
					requiredSpace += expectedDistance(previousNode, nextNode);
					previousNode = nextNode;
				}
				requiredSpace += previousNode.spaceRequiredForNode() / 2;
				if (requiredSpace > getAvailableSpace() - newPosition) {
					// find nodes to remove
					boolean removed = false;
					for (int i = nodeIndex; i < nodes.size(); i++) {
						SpaceTreeNode nextNode = (nodes.get(i));
						if (protectedNode == null || (!protectedNode.isAncestorOf(nextNode)
								&& !nextNode.parent.isAncestorOf(protectedNode))) {
							collapseNode(nextNode.getParent());
							if (nextNode.parent == nodeToMove.parent) {
								break mainLoop;
							}
							removed = true;
							break;
						}
					}
					if (!removed) {
						// not enough space, but we can't collapse anything...
						newPosition = getAvailableSpace() - requiredSpace;
						revertToShanpshot(snapShot);
						continue mainLoop;
					}
				}
				// move the node and all its neighbors to the 'right'
				SpaceTreeNode currentNodeToMove = nodeToMove;
				double newPositionForCurrent = newPosition;
				for (int i = nodeIndex; i < nodes.size(); i++) {
					currentNodeToMove.positionInLayer = newPositionForCurrent;
					// move parent if moved node is its first child
					if (currentNodeToMove.firstChild) {
						SpaceTreeNode parent = currentNodeToMove.getParent();
						if (depth > 0 && parent.positionInLayer <= newPositionForCurrent) {
							SpaceTreeLayer parentLayer = spaceTreeLayers.get(depth - 1);
							parentLayer.moveNodeForward(parent, newPositionForCurrent);
							if (parent.positionInLayer < newPositionForCurrent) {
								double delta = newPositionForCurrent - parent.positionInLayer;
								newPosition -= delta;
								revertToShanpshot(snapShot);
								continue mainLoop;
							}
						}
					}
					// move children if necessary
					if (currentNodeToMove.expanded && !currentNodeToMove.children.isEmpty()) {
						SpaceTreeNode lastChild = currentNodeToMove.getChildren()
								.get(currentNodeToMove.children.size() - 1);
						if (lastChild.positionInLayer < newPositionForCurrent) {
							// try to move all the children, that is move the
							// first child and the rest will be pushed
							SpaceTreeNode firstChild = currentNodeToMove.getChildren().get(0);
							SpaceTreeLayer childLayer = spaceTreeLayers.get(depth + 1);
							double expectedDistanceBetweenChildren = currentNodeToMove.spaceRequiredForChildren()
									- firstChild.spaceRequiredForNode() / 2 - lastChild.spaceRequiredForNode() / 2;
							childLayer.moveNodeForward(firstChild,
									newPositionForCurrent - expectedDistanceBetweenChildren);
							if (currentNodeToMove.expanded && lastChild.positionInLayer < newPositionForCurrent) {
								// the previous attempt failed -> try to move
								// only the last child
								childLayer.moveNodeForward(lastChild, newPositionForCurrent);
								if (lastChild.positionInLayer < newPositionForCurrent) {
									// child couldn't be moved as far as needed
									// -> move current node back to the position
									// over the child
									double delta = newPositionForCurrent - lastChild.positionInLayer;
									newPosition -= delta;
									revertToShanpshot(snapShot);
									continue mainLoop;
								}
							}
						}
					}

					if (i < nodes.size() - 1) {
						SpaceTreeNode nextNode = nodes.get(i + 1);
						newPositionForCurrent += expectedDistance(currentNodeToMove, nextNode);
						currentNodeToMove = nextNode;
						if (currentNodeToMove.positionInLayer > newPositionForCurrent) {
							break;
						}
					}
				}
			}
		}

		/**
		 * Method complementary to {@link #moveNodeForward(SpaceTreeNode, double)}.
		 * Decreases node's position in layer.
		 *
		 * @param nodeToMove
		 * @param newPosition
		 */
		private void moveNodeBackward(SpaceTreeNode nodeToMove, double newPosition) {
			int nodeIndex = nodes.indexOf(nodeToMove);
			if (nodeIndex == -1) {
				throw new IllegalArgumentException("node not on this layer"); //$NON-NLS-1$
			}
			// move backward -> check space to the 'left'
			// move and collapse until there's enough space
			NodeSnapshot[][] snapShot = takeSnapShot();
			boolean firstRun = true;
			mainLoop: while (firstRun || nodeToMove.positionInLayer > newPosition) {
				firstRun = false;
				double requiredSpace = 0;
				SpaceTreeNode previousNode = nodeToMove;
				for (int i = nodeIndex - 1; i >= 0; i--) {
					SpaceTreeNode nextNode = nodes.get(i);
					requiredSpace += expectedDistance(previousNode, nextNode);
					previousNode = nextNode;
				}
				requiredSpace += previousNode.spaceRequiredForNode() / 2;
				if (requiredSpace > newPosition) {
					// find nodes to remove
					boolean removed = false;
					for (int i = nodeIndex; i >= 0; i--) {
						SpaceTreeNode nextNode = (nodes.get(i));
						if (protectedNode == null || (!protectedNode.isAncestorOf(nextNode)
								&& !nextNode.parent.isAncestorOf(protectedNode))) {
							collapseNode(nextNode.getParent());
							if (nextNode.parent == nodeToMove.parent) {
								break mainLoop;
							}
							nodeIndex -= nextNode.parent.children.size();
							removed = true;
							break;
						}
					}
					if (!removed) {
						// not enough space, but we can't collapse anything...
						newPosition = requiredSpace;
						revertToShanpshot(snapShot);
						continue mainLoop;
					}
				}
				// move the node and all its neighbors to the 'left'
				SpaceTreeNode currentNodeToMove = nodeToMove;
				double newPositionForCurrent = newPosition;
				for (int i = nodeIndex; i >= 0; i--) {
					currentNodeToMove.positionInLayer = newPositionForCurrent;
					// move parent if moved node is its last child
					if (currentNodeToMove.lastChild) {
						SpaceTreeNode parent = currentNodeToMove.getParent();
						if (depth > 0 && parent.positionInLayer >= newPositionForCurrent) {
							SpaceTreeLayer parentLayer = spaceTreeLayers.get(depth - 1);
							parentLayer.moveNodeBackward(parent, newPositionForCurrent);
							if (parent.positionInLayer > newPositionForCurrent) {
								double delta = parent.positionInLayer - newPositionForCurrent;
								newPosition += delta;
								revertToShanpshot(snapShot);
								continue mainLoop;
							}
						}
					}
					// move children if necessary
					if (currentNodeToMove.expanded && !currentNodeToMove.children.isEmpty()) {
						SpaceTreeNode firstChild = currentNodeToMove.getChildren().get(0);
						if (firstChild.positionInLayer > newPositionForCurrent) {
							// try to move all the children, that is move the
							// last child and the rest will be pushed
							SpaceTreeNode lastChild = currentNodeToMove.getChildren()
									.get(currentNodeToMove.children.size() - 1);
							SpaceTreeLayer childLayer = spaceTreeLayers.get(depth + 1);
							double expectedDistanceBetweenChildren = currentNodeToMove.spaceRequiredForChildren()
									- firstChild.spaceRequiredForNode() / 2 - lastChild.spaceRequiredForNode() / 2;
							childLayer.moveNodeBackward(lastChild,
									newPositionForCurrent + expectedDistanceBetweenChildren);
							if (currentNodeToMove.expanded && firstChild.positionInLayer > newPositionForCurrent) {
								// the previous attempt failed -> try to move
								// only the first child
								childLayer.moveNodeBackward(firstChild, newPositionForCurrent);
								if (firstChild.positionInLayer > newPositionForCurrent) {
									// child couldn't be moved as far as needed
									// -> move current node back to the position
									// over the child
									double delta = firstChild.positionInLayer - newPositionForCurrent;
									newPosition += delta;
									revertToShanpshot(snapShot);
									continue mainLoop;
								}
							}
						}
					}
					if (i > 0) {
						SpaceTreeNode nextNode = nodes.get(i - 1);
						newPositionForCurrent -= expectedDistance(currentNodeToMove, nextNode);
						currentNodeToMove = nextNode;
						if (currentNodeToMove.positionInLayer < newPositionForCurrent) {
							break;
						}
					}
				}
			}
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Layer ").append(depth).append(": "); //$NON-NLS-1$//$NON-NLS-2$
			for (SpaceTreeNode node : nodes) {
				buffer.append(node.node).append(", "); //$NON-NLS-1$
			}
			return buffer.toString();
		}

		private void collapseNode(SpaceTreeNode node) {
			node.expanded = false;
			SpaceTreeLayer layer = spaceTreeLayers.get(node.depth + 1);
			layer.removeNodes(node.getChildren());
			for (SpaceTreeNode child : node.getChildren()) {
				if (child.expanded) {
					collapseNode(child);
				}
			}
		}
	}

	private class SpaceTreeExpandCollapseManager implements ExpandCollapseManager {
		@Override
		public void initExpansion(LayoutContext context) {
			// do nothing - initialization performed in #setLayoutContext()
		}

		@Override
		public void setExpanded(LayoutContext context, NodeLayout node, boolean expanded) {
			SpaceTreeNode spaceTreeNode = (SpaceTreeNode) treeObserver.getTreeNode(node);
			if (expanded) {
				maximizeExpansion(spaceTreeNode);
				refreshLayout(true);
			} else if (spaceTreeNode.expanded) {
				spaceTreeNode.expanded = false;
				spaceTreeLayers.get(spaceTreeNode.depth + 1).removeNodes(spaceTreeNode.getChildren());
				refreshLayout(true);
			}
		}

		@Override
		public boolean canExpand(LayoutContext context, NodeLayout node) {
			SpaceTreeNode spaceTreeNode = (SpaceTreeNode) treeObserver.getTreeNode(node);
			if (spaceTreeNode != null) {
				// we don't check if node is expanded because it can be expanded
				// again
				return !spaceTreeNode.children.isEmpty();
			}
			return false;
		}

		@Override
		public boolean canCollapse(LayoutContext context, NodeLayout node) {
			SpaceTreeNode spaceTreeNode = (SpaceTreeNode) treeObserver.getTreeNode(node);
			if (spaceTreeNode != null) {
				return spaceTreeNode.expanded && !spaceTreeNode.children.isEmpty();
			}
			return false;
		}

		public void maximizeExpansion(SpaceTreeNode nodeToExpand) {
			protectedNode = nodeToExpand;
			double availableSpace = getAvailableSpace();
			double requiredSpace = 0;

			spaceTreeLayers.get(nodeToExpand.depth + 1).removeNodes(nodeToExpand.getChildren());

			List<SpaceTreeNode> nodesInThisLayer = null;
			List<SpaceTreeNode> nodesInNextLayer = new ArrayList<>();
			nodesInNextLayer.add(nodeToExpand);
			double spaceRequiredInNextLayer = nodeToExpand.spaceRequiredForNode();
			for (int layer = 0; !nodesInNextLayer.isEmpty(); layer++) {
				NodeSnapshot[][] snapShot = takeSnapShot();
				requiredSpace = Math.max(requiredSpace, spaceRequiredInNextLayer);
				spaceRequiredInNextLayer = 0;

				nodesInThisLayer = nodesInNextLayer;
				nodesInNextLayer = new ArrayList<>();

				int numOfNodesWithChildren = 0;
				for (SpaceTreeNode element : nodesInThisLayer) {
					SpaceTreeNode node = element;
					if (!node.children.isEmpty()) {
						node.expanded = true;
						spaceRequiredInNextLayer += node.spaceRequiredForChildren();
						nodesInNextLayer.addAll(node.getChildren());
						numOfNodesWithChildren++;
					}
				}

				for (SpaceTreeNode node : nodesInNextLayer) {
					node.expanded = false;
				}

				if (numOfNodesWithChildren == 0) {
					break;
				}

				spaceRequiredInNextLayer += branchGap * (numOfNodesWithChildren - 1);

				boolean addedNewLayer = false;
				if ((spaceRequiredInNextLayer <= requiredSpace || spaceRequiredInNextLayer <= availableSpace
						|| (layer < 1 && nodeToExpand.depth + layer < 1)) && !nodesInNextLayer.isEmpty()) {
					// add next layer and center its nodes

					SpaceTreeLayer childLayer = spaceTreeLayers.get(nodeToExpand.depth + layer + 1);
					childLayer.addNodes(nodesInNextLayer);
					SpaceTreeNode firstChild = (nodesInNextLayer.get(0));
					SpaceTreeNode lastChild = (nodesInNextLayer.get(nodesInNextLayer.size() - 1));
					double boundsWidth = spaceRequiredInNextLayer - firstChild.spaceRequiredForNode() / 2
							- lastChild.spaceRequiredForNode() / 2;
					double startPosition = Math.max((availableSpace - boundsWidth) / 2,
							firstChild.spaceRequiredForNode() / 2);
					setAvailableSpace(spaceRequiredInNextLayer);
					childLayer.fitNodesWithinBounds(nodesInNextLayer, startPosition, startPosition + boundsWidth);
					setAvailableSpace(0);
					if (nodeToExpand.childrenPositionsOK(nodesInThisLayer) || layer == 0
							|| nodeToExpand.depth + layer < 1) {
						addedNewLayer = true;
					}
				}
				if (!addedNewLayer) {
					revertToShanpshot(snapShot);
					break;
				}
			}
			nodeToExpand.centerParentsBottomUp();
			nodeToExpand.centerParentsTopDown();
		}
	}

	private final SpaceTreeExpandCollapseManager expandCollapseManager = new SpaceTreeExpandCollapseManager();

	private final ContextListener contextListener = new ContextListener.Stub() {
		@Override
		public boolean boundsChanged(LayoutContext context) {
			boolean previousBoundsWrong = (bounds == null || bounds.width * bounds.height <= 0);
			bounds = context.getBounds();
			if (bounds.width * bounds.height > 0 && previousBoundsWrong) {
				expandCollapseManager.maximizeExpansion((SpaceTreeNode) treeObserver.getSuperRoot());
				refreshLayout(false);
			}
			return false;
		}
	};

	private final LayoutListener layoutListener = new LayoutListener() {

		@Override
		public boolean subgraphResized(LayoutContext context, SubgraphLayout subgraph) {
			return defaultSubgraphHandle(context, subgraph);
		}

		@Override
		public boolean subgraphMoved(LayoutContext context, SubgraphLayout subgraph) {
			return defaultSubgraphHandle(context, subgraph);
		}

		@Override
		public boolean nodeResized(LayoutContext context, NodeLayout node) {
			setAvailableSpace(
					getAvailableSpace() + ((SpaceTreeNode) treeObserver.getTreeNode(node)).spaceRequiredForNode());
			boolean result = defaultNodeHandle(context, node);
			setAvailableSpace(0);
			return result;
		}

		@Override
		public boolean nodeMoved(LayoutContext context, NodeLayout node) {
			return defaultNodeHandle(context, node);
		}

		/**
		 * Finds a root of given subgraph, updates the root position and moves the
		 * subgraph to proper position
		 *
		 * @param context
		 * @param subgraph
		 * @return
		 */
		private boolean defaultSubgraphHandle(LayoutContext context, SubgraphLayout subgraph) {
			SpaceTreeNode node = (SpaceTreeNode) treeObserver.getTreeNode(subgraph.getNodes()[0]);
			while (node != null && node.node != null && node.node.getSubgraph() == subgraph) {
				node = node.getParent();
			}
			if (node != null && node.subgraph == subgraph) {
				node.adjustPosition(subgraph.getLocation());
				if (context.isBackgroundLayoutEnabled()) {
					((SpaceTreeNode) treeObserver.getSuperRoot()).flushLocationChanges(0);
					node.refreshSubgraphLocation();
					context.flushChanges(false);
				}
			}
			return false;
		}

		private boolean defaultNodeHandle(LayoutContext context, NodeLayout node) {
			if (bounds.width * bounds.height <= 0) {
				return false;
			}
			SpaceTreeNode spaceTreeNode = (SpaceTreeNode) treeObserver.getTreeNode(node);
			spaceTreeNode.adjustPosition(node.getLocation());
			if (context.isBackgroundLayoutEnabled()) {
				((SpaceTreeNode) treeObserver.getSuperRoot()).flushLocationChanges(0);
				spaceTreeNode.refreshSubgraphLocation();
				context.flushChanges(false);
			}
			return false;
		}
	};

	private int direction = TOP_DOWN;

	private final double leafGap = 15;
	private final double branchGap = leafGap + 5;
	private final double layerGap = 20;

	private boolean directionChanged = false;

	private DisplayIndependentRectangle bounds;

	private TreeLayoutObserver treeObserver;

	private double availableSpace;

	private final List<SpaceTreeLayer> spaceTreeLayers = new ArrayList<>();

	/**
	 * If not null, this node and all of its children shall not be collapsed during
	 * node movements.
	 */
	private SpaceTreeNode protectedNode = null;

	/**
	 * Constructs an instance of <code>SpaceTreeLayoutAlgorithm</code> that places
	 * the root of a tree at the top of the graph.
	 */
	public SpaceTreeLayoutAlgorithm() {
	}

	/**
	 * Constructs an instance of <code>SpaceTreeLayoutAlgorithm</code> that places
	 * the root of a tree according to given direction
	 *
	 * @param direction direction of the tree, sould be one of the following:
	 *                  {@link #TOP_DOWN}, {@link #BOTTOM_UP}, {@link #LEFT_RIGHT},
	 *                  {@link #RIGHT_LEFT}.
	 */
	public SpaceTreeLayoutAlgorithm(int direction) {
		setDirection(direction);
	}

	/**
	 *
	 * @return current direction (placement) of the tree
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets direction (placement) of the tree
	 *
	 * @param direction direction of the tree, sould be one of the following:
	 *                  {@link #TOP_DOWN}, {@link #BOTTOM_UP}, {@link #LEFT_RIGHT},
	 *                  {@link #RIGHT_LEFT}.
	 */
	public void setDirection(int direction) {
		if (direction == this.direction) {
			return;
		}
		if ((direction != TOP_DOWN) && (direction != BOTTOM_UP) && (direction != LEFT_RIGHT)
				&& (direction != RIGHT_LEFT)) {
			throw new IllegalArgumentException("Invalid direction: " + direction); //$NON-NLS-1$
		}
		this.direction = direction;
		directionChanged = true;
		if (context.isBackgroundLayoutEnabled()) {
			checkPendingChangeDirection();
		}
	}

	@Override
	public void applyLayout(boolean clean) {
		bounds = context.getBounds();

		if (bounds.width * bounds.height == 0) {
			return;
		}

		if (clean) {
			treeObserver.recomputeTree();
			expandCollapseManager.maximizeExpansion((SpaceTreeNode) treeObserver.getSuperRoot());
		}
		SpaceTreeNode superRoot = ((SpaceTreeNode) treeObserver.getSuperRoot());
		superRoot.flushExpansionChanges();
		superRoot.flushLocationChanges(0);
		checkPendingChangeDirection();
	}

	@Override
	public void setLayoutContext(LayoutContext context) {
		if (this.context != null) {
			this.context.removeContextListener(contextListener);
			this.context.removeLayoutListener(layoutListener);
			treeObserver.stop();
		}
		super.setLayoutContext(context);
		context.addContextListener(contextListener);
		context.addLayoutListener(layoutListener);
		treeObserver = new TreeLayoutObserver(context, spaceTreeNodeFactory);

		bounds = context.getBounds();
	}

	/**
	 *
	 * @return <code>ExpandCollapseManager</code> that can (and should) be used on
	 *         layout context managed by this layout algorithm.
	 */
	public ExpandCollapseManager getExpandCollapseManager() {
		return expandCollapseManager;
	}

	private void checkPendingChangeDirection() {
		if (directionChanged) {
			SubgraphLayout[] subgraphs = context.getSubgraphs();
			int subgraphDirection = getSubgraphDirection();
			for (SubgraphLayout subgraph : subgraphs) {
				subgraph.setDirection(subgraphDirection);
			}
			directionChanged = false;
		}
	}

	private int getSubgraphDirection() {
		switch (direction) {
		case TOP_DOWN:
			return SubgraphLayout.TOP_DOWN;
		case BOTTOM_UP:
			return SubgraphLayout.BOTTOM_UP;
		case LEFT_RIGHT:
			return SubgraphLayout.LEFT_RIGHT;
		case RIGHT_LEFT:
			return SubgraphLayout.RIGHT_LEFT;
		}
		throw new RuntimeException();
	}

	protected void refreshLayout(boolean animation) {
		if (!context.isBackgroundLayoutEnabled()) {
			return;
		}
		SpaceTreeNode superRoot = (SpaceTreeNode) treeObserver.getSuperRoot();
		if (animation && superRoot.flushCollapseChanges()) {
			context.flushChanges(true);
		}
		if (superRoot.flushLocationChanges(0) && animation) {
			context.flushChanges(true);
		}
		superRoot.flushExpansionChanges();
		superRoot.flushLocationChanges(0);
		context.flushChanges(animation);
	}

	/**
	 * Available space is the biggest of the following values:
	 * <ul>
	 * <li>Space provided by current context bounds</li>
	 * <li>Space already taken by the widest layer</li>
	 * <li>Value set with {@link #setAvailableSpace(double)}</li>
	 * </ul>
	 *
	 * @return
	 */
	private double getAvailableSpace() {
		double result = (direction == TOP_DOWN || direction == BOTTOM_UP) ? bounds.width : bounds.height;
		result = Math.max(result, this.availableSpace);
		for (Object spaceTreeLayer : spaceTreeLayers) {
			SpaceTreeLayer layer = (SpaceTreeLayer) spaceTreeLayer;
			if (layer.nodes.isEmpty()) {
				break;
			}
			SpaceTreeNode first = layer.nodes.get(0);
			SpaceTreeNode last = layer.nodes.get(layer.nodes.size() - 1);
			result = Math.max(result, last.positionInLayer - first.positionInLayer
					+ (first.spaceRequiredForNode() + last.spaceRequiredForNode()) / 2);
		}
		return result;
	}

	/**
	 * This method allows to reserve more space than actual layout bounds provide or
	 * nodes currently occupy.
	 *
	 * @param availableSpace
	 */
	private void setAvailableSpace(double availableSpace) {
		this.availableSpace = availableSpace;
	}

	private double expectedDistance(SpaceTreeNode node, SpaceTreeNode neighbor) {
		double expectedDistance = (node.spaceRequiredForNode() + neighbor.spaceRequiredForNode()) / 2;
		expectedDistance += (node.parent == neighbor.parent) ? leafGap : branchGap;
		return expectedDistance;
	}

	private class NodeSnapshot {
		SpaceTreeNode node;
		double position;
		boolean expanded;
	}

	/**
	 * Stores current expansion state of tree nodes and their position in layers
	 *
	 * @return array containing state of all unpruned nodes
	 */
	private NodeSnapshot[][] takeSnapShot() {
		NodeSnapshot[][] result = new NodeSnapshot[spaceTreeLayers.size()][];
		for (int i = 0; i < result.length; i++) {
			SpaceTreeLayer layer = spaceTreeLayers.get(i);
			result[i] = new NodeSnapshot[layer.nodes.size()];
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = new NodeSnapshot();
				result[i][j].node = (layer.nodes.get(j));
				result[i][j].position = result[i][j].node.positionInLayer;
				result[i][j].expanded = result[i][j].node.expanded;
			}
		}
		return result;
	}

	/**
	 * Restores tree nodes' expansion state and position in layers
	 *
	 * @param snapShot state obtained with {@link #takeSnapShot()}
	 */
	private void revertToShanpshot(NodeSnapshot[][] snapShot) {
		for (int i = 0; i < snapShot.length; i++) {
			SpaceTreeLayer layer = spaceTreeLayers.get(i);
			layer.nodes.clear();
			for (int j = 0; j < snapShot[i].length; j++) {
				snapShot[i][j].node.positionInLayer = snapShot[i][j].position;
				snapShot[i][j].node.expanded = snapShot[i][j].expanded;
				layer.nodes.add(snapShot[i][j].node);
			}
		}
	}
}
