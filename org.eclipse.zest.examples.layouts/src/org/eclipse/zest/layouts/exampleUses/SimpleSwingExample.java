/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria,
 *                      BC, Canada and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleUses;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.BoxLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.exampleStructures.SimpleGraph;
import org.eclipse.zest.layouts.exampleStructures.SimpleNode;
import org.eclipse.zest.layouts.exampleStructures.SimpleRelationship;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * @author Rob Lintern
 * @author Chris Bennett
 *
 *         A simple example of using layout algorithms with a Swing application.
 */
public class SimpleSwingExample {
	private static final Color NODE_NORMAL_COLOR = new Color(225, 225, 255);
	private static final Color NODE_SELECTED_COLOR = new Color(255, 125, 125);
	private static final Color BORDER_NORMAL_COLOR = new Color(0, 0, 0);
	private static final Color BORDER_SELECTED_COLOR = new Color(255, 0, 0);
	private static final Stroke BORDER_NORMAL_STROKE = new BasicStroke(1.0f);
	private static final Stroke BORDER_SELECTED_STROKE = new BasicStroke(2.0f);
	private static final Color RELATIONSHIP_NORMAL_COLOR = Color.BLUE;

	public static SpringLayoutAlgorithm SPRING = new SpringLayoutAlgorithm();
	public static TreeLayoutAlgorithm TREE_VERT = new TreeLayoutAlgorithm();
	public static TreeLayoutAlgorithm TREE_HORIZ = new TreeLayoutAlgorithm(TreeLayoutAlgorithm.LEFT_RIGHT);
	public static RadialLayoutAlgorithm RADIAL = new RadialLayoutAlgorithm();
	public static GridLayoutAlgorithm GRID = new GridLayoutAlgorithm();
	public static BoxLayoutAlgorithm HORIZ = new BoxLayoutAlgorithm(SWT.HORIZONTAL);
	public static BoxLayoutAlgorithm VERT = new BoxLayoutAlgorithm(SWT.VERTICAL);

	private final List<LayoutAlgorithm> algorithms = new ArrayList<>();
	private final List<String> algorithmNames = new ArrayList<>();

	private static final int INITIAL_PANEL_WIDTH = 700;
	private static final int INITIAL_PANEL_HEIGHT = 500;

	private static final boolean RENDER_HIGH_QUALITY = true;

	private static final double INITIAL_NODE_WIDTH = 20;
	private static final double INITIAL_NODE_HEIGHT = 20;
	private static final int ARROW_HALF_WIDTH = 4;
	private static final int ARROW_HALF_HEIGHT = 6;
	private static final Shape ARROW_SHAPE = new Polygon(
			new int[] { -ARROW_HALF_HEIGHT, ARROW_HALF_HEIGHT, -ARROW_HALF_HEIGHT },
			new int[] { -ARROW_HALF_WIDTH, 0, ARROW_HALF_WIDTH }, 3);
	private static final Stroke ARROW_BORDER_STROKE = new BasicStroke(0.5f);
	private static final Color ARROW_HEAD_FILL_COLOR = new Color(125, 255, 125);
	private static final Color ARROW_HEAD_BORDER_COLOR = Color.BLACK;

	public static final String DEFAULT_NODE_SHAPE = "oval"; //$NON-NLS-1$

	private JFrame mainFrame;
	private JPanel mainPanel;
	private SimpleGraph graph;
	private JToolBar toolBar;

	private LayoutAlgorithm currentLayoutAlgorithm;
	protected String currentLayoutAlgorithmName;
	protected SimpleNode selectedEntity;
	protected Point mouseDownPoint;
	protected Point selectedEntityPositionAtMouseDown;
	private long idCount;
	protected String currentNodeShape = DEFAULT_NODE_SHAPE; // e.g., oval, rectangle

	protected void addAlgorithm(LayoutAlgorithm algorithm, String name, boolean animate) {
		algorithms.add(algorithm);
		algorithmNames.add(name);
	}

	public void start() {

		mainFrame = new JFrame(Messages.getString("SimpleSwingExample.Title")); //$NON-NLS-1$
		toolBar = new JToolBar();
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);

		createMainPanel();
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainFrame.dispose();
			}
		});

		JButton btnCreateGraph = new JButton(Messages.getString("SimpleSwingExample.NewGraph")); //$NON-NLS-1$
		btnCreateGraph.addActionListener(e -> createGraph(true));
		toolBar.add(btnCreateGraph);
		JButton btnCreateTree = new JButton(Messages.getString("SimpleSwingExample.NewTree")); //$NON-NLS-1$
		btnCreateTree.addActionListener(e -> createGraph(false));
		toolBar.add(btnCreateTree);

		createGraph(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation((int) (screenSize.getWidth() - INITIAL_PANEL_WIDTH) / 2,
				(int) (screenSize.getHeight() - INITIAL_PANEL_HEIGHT) / 2);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.repaint();

		try {
			SwingUtilities.invokeAndWait(() -> {
				// initialize layouts
				SPRING.setResizing(true);
				TREE_VERT.setResizing(true);
				TREE_HORIZ.setResizing(true);
				RADIAL.setResizing(true);
				GRID.setRowPadding(20);
				GRID.setResizing(true);
				HORIZ.setResizing(true);
				VERT.setResizing(true);
				addAlgorithm(SPRING, Messages.getString("SimpleSwingExample.SpringLayout"), false); //$NON-NLS-1$
				addAlgorithm(TREE_VERT, Messages.getString("SimpleSwingExample.TreeVLayout"), false); //$NON-NLS-1$
				addAlgorithm(TREE_HORIZ, Messages.getString("SimpleSwingExample.TreeHLayout"), false); //$NON-NLS-1$
				addAlgorithm(RADIAL, Messages.getString("SimpleSwingExample.RadialLayout"), false); //$NON-NLS-1$
				addAlgorithm(GRID, Messages.getString("SimpleSwingExample.GridLayout"), false); //$NON-NLS-1$
				addAlgorithm(HORIZ, Messages.getString("SimpleSwingExample.HorizontalLayout"), false); //$NON-NLS-1$
				addAlgorithm(VERT, Messages.getString("SimpleSwingExample.VerticalLayout"), false); //$NON-NLS-1$

				for (int i = 0; i < algorithms.size(); i++) {
					final LayoutAlgorithm algorithm = algorithms.get(i);
					final String algorithmName = algorithmNames.get(i);
					JButton algorithmButton = new JButton(algorithmName);
					algorithmButton.addActionListener(e -> {
						currentLayoutAlgorithm = algorithm;
						currentLayoutAlgorithmName = algorithmName;
						performLayout();
					});
					toolBar.add(algorithmButton);
				}
			});
		} catch (InterruptedException | InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	protected void performLayout() {
		placeRandomly();

		Dimension size = mainPanel.getSize();
		Point location = mainPanel.getLocation();
		graph.setBounds(location.x, location.y, size.width, size.height);

		SwingUtilities.invokeLater(() -> {
			currentLayoutAlgorithm.setLayoutContext(graph);
			currentLayoutAlgorithm.applyLayout(true);
			updateGUI();
		});
		// reset
		currentNodeShape = DEFAULT_NODE_SHAPE;
	}

	private void createMainPanel() {

		mainPanel = new MainPanel(); // see below for class definition
		mainPanel.setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(null);
		mainFrame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);

		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selectedEntity = null;
				for (SimpleNode entity : graph.getEntities()) {
					double x = entity.getX();
					double y = entity.getY();
					double w = entity.getWidth();
					double h = entity.getHeight();
					Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
					if (rect.contains(e.getX(), e.getY())) {
						selectedEntity = entity;
						break;
					}
				}
				if (selectedEntity != null) {
					mouseDownPoint = e.getPoint();
					selectedEntityPositionAtMouseDown = new Point((int) selectedEntity.getX(),
							(int) selectedEntity.getY());
				} else {
					mouseDownPoint = null;
					selectedEntityPositionAtMouseDown = null;
				}
				updateGUI();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				selectedEntity = null;
				mouseDownPoint = null;
				selectedEntityPositionAtMouseDown = null;
				updateGUI();
			}
		});

		mainPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// if (selectedEntity != null) {
				// //TODO: Add mouse moving
				// //selectedEntity.setLocationInLayout(selectedEntityPositionAtMouseDown.x +
				// dx, selectedEntityPositionAtMouseDown.y + dy);
				// updateGUI();
				// }
			}
		});

		mainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (currentLayoutAlgorithm != null) {
					performLayout();
				}
			}
		});
	}

	private void createGraph(boolean addNonTreeRels) {
		graph = new SimpleGraph();
		selectedEntity = null;

		createTreeGraph(2, 4, 2, 5, true, true, addNonTreeRels);
		// createCustomGraph();

		placeRandomly();
		mainPanel.repaint();
	}

	/**
	 *
	 * @param maxLevels         Max number of levels wanted in tree
	 * @param maxChildren       Max number of children for each node in the tree
	 * @param randomNumChildren Whether or not to pick random number of levels (from
	 *                          1 to maxLevels) and random number of children (from
	 *                          1 to maxChildren)
	 */
	private void createTreeGraph(int minChildren, int maxChildren, int minLevels, int maxLevels,
			boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
		SimpleNode currentParent = new SimpleNode(getNextID(), graph);
		graph.addEntity(currentParent);
		createTreeGraphRecursive(currentParent, minChildren, maxChildren, minLevels, maxLevels, 1, randomNumChildren,
				randomLevels, addNonTreeRels);
	}

	private void createTreeGraphRecursive(SimpleNode currentParentNode, int minChildren, int maxChildren, int minLevel,
			int maxLevel, int level, boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
		if (level > maxLevel) {
			return;
		}
		if (randomLevels) {
			if (level > minLevel) {
				double zeroToOne = Math.random();
				if (zeroToOne < 0.75) {
					return;
				}
			}
		}
		int numChildren = randomNumChildren ? Math.max(minChildren, (int) (Math.random() * maxChildren + 1))
				: maxChildren;
		for (int i = 0; i < numChildren; i++) {
			SimpleNode newNode = new SimpleNode(getNextID(), graph);
			graph.addEntity(newNode);
			if (addNonTreeRels && graph.getNodes().length % 5 == 0) {
				int index = (int) (Math.random() * graph.getNodes().length);
				SimpleRelationship rel = new SimpleRelationship(graph.getNodes()[index], newNode, false);
				graph.addRelationship(rel);
			}
			SimpleRelationship rel = new SimpleRelationship(currentParentNode, newNode, false);
			graph.addRelationship(rel);
			createTreeGraphRecursive(newNode, minChildren, maxChildren, minLevel, maxLevel, level + 1,
					randomNumChildren, randomLevels, addNonTreeRels);
		}
	}

	/**
	 * Call this from createGraph in place of createTreeGraph this for debugging and
	 * testing.
	 */
	/*
	 * private void createCustomGraph() { LayoutEntity A = createSimpleNode("1");
	 * LayoutEntity B = createSimpleNode("10"); LayoutEntity _1 =
	 * createSimpleNode("100"); entities.add(A); entities.add(B); entities.add(_1);
	 * relationships.add(new SimpleRelationship (A, B, false));
	 * relationships.add(new SimpleRelationship (A, _1, false));
	 * relationships.add(new SimpleRelationship (_1, A, false)); }
	 */
	private String getNextID() {
		String id = Long.toString(idCount);
		idCount++;
		return id;
	}

	/** Places nodes randomly on the screen **/
	private void placeRandomly() {
		for (SimpleNode simpleNode : graph.getNodes()) {
			double x = Math.random() * INITIAL_PANEL_WIDTH - INITIAL_NODE_WIDTH;
			double y = Math.random() * INITIAL_PANEL_HEIGHT - INITIAL_NODE_HEIGHT;
			simpleNode.setLocation(x, y);
			simpleNode.setSize(INITIAL_NODE_WIDTH, INITIAL_NODE_HEIGHT);
		}
	}

	private void updateGUI() {
		mainPanel.paintImmediately(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
	}

	private static Point2D.Double getEllipseIntersectionPoint(double theta, double ellipseWidth, double ellipseHeight) {
		double nhalfw = ellipseWidth / 2.0; // half elllipse width
		double nhalfh = ellipseHeight / 2.0; // half ellipse height
		double tanTheta = Math.tan(theta);

		double a = nhalfw;
		double b = nhalfh;
		double x = (a * b) / Math.sqrt(Math.pow(b, 2) + Math.pow(a, 2) * Math.pow(tanTheta, 2));
		if ((theta > Math.PI / 2.0 && theta < 1.5 * Math.PI) || (theta < -Math.PI / 2.0 && theta > -1.5 * Math.PI)) {
			x = -x;
		}
		double y = tanTheta * x;
		return new Point2D.Double(x, y);
	}

	public static void main(String[] args) {
		(new SimpleSwingExample()).start();
	}

	/**
	 * A JPanel that provides entity and relationship rendering Instead of letting
	 * Swing paint all the JPanels for us, we will just do our own painting here
	 */
	private class MainPanel extends JPanel {

		private static final long serialVersionUID = 1;

		@Override
		protected void paintChildren(Graphics g) {
			if (g instanceof Graphics2D && RENDER_HIGH_QUALITY) {
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
						RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			}

			// paint the nodes
			for (SimpleNode element : graph.getNodes()) {
				paintEntity(element, g);
			}

			// paint the relationships
			for (SimpleRelationship relationship : graph.getConnections()) {
				paintRelationship(relationship, g);
			}
		}

		private void paintEntity(SimpleNode entity, Graphics g) {
			boolean isSelected = selectedEntity != null && selectedEntity.equals(entity);
			g.setColor(isSelected ? NODE_SELECTED_COLOR : NODE_NORMAL_COLOR);
			if (currentNodeShape.equals("rectangle")) { //$NON-NLS-1$
				g.fillRect((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
			} else { // default
				g.fillOval((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
			}
			g.setColor(isSelected ? BORDER_SELECTED_COLOR : BORDER_NORMAL_COLOR);
			String name = entity.toString();
			Rectangle2D nameBounds = g.getFontMetrics().getStringBounds(name, g);
			g.drawString(name, (int) (entity.getX() + entity.getWidth() / 2.0 - nameBounds.getWidth() / 2.0),
					(int) (entity.getY() + entity.getHeight() / 2.0 + nameBounds.getHeight() / 2.0));// -
																										// nameBounds.getHeight()
																										// -
																										// nameBounds.getY()));
			if (g instanceof Graphics2D) {
				((Graphics2D) g).setStroke(isSelected ? BORDER_SELECTED_STROKE : BORDER_NORMAL_STROKE);
			}
			if (currentNodeShape.equals("rectangle")) { //$NON-NLS-1$
				g.drawRect((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
			} else { // default
				g.drawOval((int) entity.getX(), (int) entity.getY(), (int) entity.getWidth(), (int) entity.getHeight());
			}
		}

		private void paintRelationship(SimpleRelationship rel, Graphics g) {

			SimpleNode src = rel.getSource();
			SimpleNode dest = rel.getTarget();

			double srcX = src.getX() + src.getWidth() / 2.0;
			double srcY = src.getY() + src.getHeight() / 2.0;
			double destX = dest.getX() + dest.getWidth() / 2.0;
			double destY = dest.getY() + dest.getHeight() / 2.0;
			double dx = getLength(srcX, destX);
			double dy = getLength(srcY, destY);
			double theta = Math.atan2(dy, dx);
			drawRelationship(src, dest, theta, srcX, srcY, destX, destY, g);

			// draw an arrow in the middle of the line
			drawArrow(theta, srcX, srcY, dx, dy, g);
		}

		/**
		 * Draw a line from the edge of the src node to the edge of the destination node
		 */
		private void drawRelationship(SimpleNode src, SimpleNode dest, double theta, double srcX, double srcY,
				double destX, double destY, Graphics g) {

			double reverseTheta = theta > 0.0d ? theta - Math.PI : theta + Math.PI;

			Point2D.Double srcIntersectionP = getEllipseIntersectionPoint(theta, src.getWidth(), src.getHeight());

			Point2D.Double destIntersectionP = getEllipseIntersectionPoint(reverseTheta, dest.getWidth(),
					dest.getHeight());

			drawRelationship(srcX + srcIntersectionP.getX(), srcY + srcIntersectionP.getY(),
					destX + destIntersectionP.getX(), destY + destIntersectionP.getY(), g);
		}

		/**
		 * Draw a line from specified source to specified destination
		 */
		private void drawRelationship(double srcX, double srcY, double destX, double destY, Graphics g) {
			g.setColor(RELATIONSHIP_NORMAL_COLOR);
			g.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
		}

		private void drawArrow(double theta, double srcX, double srcY, double dx, double dy, Graphics g) {
			AffineTransform tx = new AffineTransform();
			double arrX = srcX + (dx) / 2.0;
			double arrY = srcY + (dy) / 2.0;
			tx.translate(arrX, arrY);
			tx.rotate(theta);
			Shape arrowTx = tx.createTransformedShape(ARROW_SHAPE);
			if (g instanceof Graphics2D) {
				g.setColor(ARROW_HEAD_FILL_COLOR);
				((Graphics2D) g).fill(arrowTx);
				((Graphics2D) g).setStroke(ARROW_BORDER_STROKE);
				g.setColor(ARROW_HEAD_BORDER_COLOR);
				((Graphics2D) g).draw(arrowTx);
			}
		}

		/**
		 * Get the length of a line ensuring it is not too small to render
		 *
		 * @param start
		 * @param end
		 * @return
		 */
		private double getLength(double start, double end) {
			double length = end - start;
			// make sure dx is not zero or too small
			if (length < 0.01 && length > -0.01) {
				if (length > 0) {
					length = 0.01;
				} else if (length < 0) {
					length = -0.01;
				}
			}
			return length;
		}
	}
}
