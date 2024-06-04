/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.zest.tests.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.core.runtime.Assert;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.core.widgets.internal.GraphLabel;
import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.tests.utils.GraphicalRobot;
import org.eclipse.zest.tests.utils.Snippet;
import org.eclipse.zest.tests.utils.WidgetVisitor;

import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PopUpHelper;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.draw2d.ToolTipHelper;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

/**
 * Abstract base class for all tests related to the Zest examples.
 */
public abstract class AbstractGraphTest {
	protected Graph graph;
	protected GraphicalRobot robot;

	@Rule
	public TestRule rule = (base, description) -> new Statement() {
		@Override
		public void evaluate() throws Throwable {
			Snippet annotation = description.getAnnotation(Snippet.class);
			Objects.requireNonNull(annotation, "Test is missing @Snippet annotation."); //$NON-NLS-1$
			doTest(annotation, base);
		}
	};

	/**
	 * Wrapper method to handle the instantiation of the example class and the
	 * execution of the unit test. Each example must satisfy the following
	 * requirements:
	 * <ul>
	 * <li>It must have a static main(String[]) method.</li>
	 * <li>It must not create a display.</li>
	 * <li>It must store the created viewer in a static {@code viewer} variable.
	 * <ul>
	 *
	 * @param clazz     The example to instantiate.
	 * @param statement The test to execute once the example has been created.
	 * @throws Throwable If the example could not be instantiated.
	 */
	private void doTest(Snippet annotation, Statement statement) throws Throwable {
		Class<?> clazz = annotation.type();

		Semaphore lock = new Semaphore(0);
		AtomicReference<Throwable> throwable = new AtomicReference<>();

		Lookup lookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
		MethodType type = MethodType.methodType(void.class, String[].class);
		MethodHandle methodHandle = lookup.findStatic(clazz, "main", type); //$NON-NLS-1$

		// Fail early, otherwise the example might block indefinitely
		Assert.isTrue(hasGraph(lookup, annotation), "Graph object not found for " + clazz); //$NON-NLS-1$

		// The actual test has to be executed asynchronously, so that it is run as part
		// of the readAndDispatch() call. Otherwise we end up in a deadlock, as both
		// snippet and test run in the UI thread.
		Display.getCurrent().asyncExec(() -> {
			Shell shell = null;
			try {
				graph = getGraph(lookup, annotation);

				// Make sure the layout is reproducible
				if (graph.getLayoutAlgorithm() instanceof SpringLayoutAlgorithm.Zest1) {
					graph.setLayoutAlgorithm(new GridLayoutAlgorithm.Zest1(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				}

				robot = new GraphicalRobot(graph);
				shell = graph.getShell();
				// Wait for layout to be applied
				waitEventLoop(0);
				// Run the actual test
				statement.evaluate();
			} catch (Throwable e) {
				throwable.set(e);
			} finally {
				// Close the snippet
				if (shell != null) {
					shell.dispose();
				}
				lock.release();
			}
		});

		methodHandle.invoke(null);
		// Wait for asynchronous test execution
		lock.acquire();
		// Propagate any errors
		if (throwable.get() != null) {
			throw throwable.get();
		}
	}

	/**
	 * Checks whether the example class can be tested.
	 *
	 * @param lookup  The lookup object on the example class.
	 * @param snippet The annotation of the executed test.
	 * @return {@code true}, if the graph instance of the example is accessible.
	 * @throws ReflectiveOperationException If the graph could not
	 */
	protected abstract boolean hasGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException;

	/**
	 * Returns the graph instance created by the current Zest example. This instance
	 * is expected to be stored in a static variable.
	 *
	 * @param lookup  The lookup object on the example class.
	 * @param snippet The annotation of the executed test.
	 * @return The graph object created by the example.
	 * @throws ReflectiveOperationException If the graph could not
	 */
	protected abstract Graph getGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException;

	/**
	 * The nodes used by the layout algorithm might be less than the nodes in the
	 * graph when a {@link Filter} is used.
	 *
	 * @return All nodes considers by the current layout algorithm.
	 */
	protected InternalNode[] getInternalNodes() throws ReflectiveOperationException {
		LayoutAlgorithm layoutAlgorithm = graph.getLayoutAlgorithm();
		Field field = AbstractLayoutAlgorithm.class.getDeclaredField("internalNodes"); //$NON-NLS-1$
		boolean isAccessible = field.canAccess(layoutAlgorithm);
		try {
			field.setAccessible(true);
			return (InternalNode[]) field.get(layoutAlgorithm);
		} finally {
			field.setAccessible(isAccessible);
		}
	}

	/**
	 * The connections used by the layout algorithm might be less than the nodes in
	 * the graph when a {@link Filter} is used.
	 *
	 * @return All connections considers by the current layout algorithm.
	 */
	protected InternalRelationship[] getInternalRelationships() throws ReflectiveOperationException {
		LayoutAlgorithm layoutAlgorithm = graph.getLayoutAlgorithm();
		Field field = AbstractLayoutAlgorithm.class.getDeclaredField("internalRelationships"); //$NON-NLS-1$
		boolean isAccessible = field.canAccess(layoutAlgorithm);
		try {
			field.setAccessible(true);
			return (InternalRelationship[]) field.get(layoutAlgorithm);
		} finally {
			field.setAccessible(isAccessible);
		}
	}

	/**
	 * The distance is defined as {@code sqrt(x^2 + y^2) } where {@code x} and
	 * {@code y} are the coordinates of the vector pointing from the {@code source}
	 * to the {@code destination} node.
	 *
	 * @return The Euclidean length of the given {@code connection}.
	 */
	protected static double getLength(GraphConnection connection) {
		Point c1 = getCenter(connection.getSource());
		Point c2 = getCenter(connection.getDestination());
		int x = c1.x - c2.x;
		int y = c1.y - c2.y;
		Point vec = new Point(x, y);
		return getLength(vec);
	}

	/**
	 * The length of a vector is defined as {@code sqrt(x^2 + y^2) }.
	 *
	 * @return The Euclidean length of the given {@code vector}.
	 */
	protected static double getLength(Point vec) {
		return Math.sqrt(vec.x * vec.x + vec.y * vec.y);
	}

	/**
	 * The dot product of two vectors is defined as {@code x1 * x2 + y1 * y2 }.
	 *
	 * @return The dot product of the given {@code vectors}.
	 */
	protected static double getDotProduct(Point vec1, Point vec2) {
		return vec1.x * vec2.x + vec1.y * vec2.y;
	}

	/**
	 * Calculates the arc (in degrees) that is spanned by the given graph
	 * connection. The arc is calculated using the cosine of the vector from the
	 * start to the end point and the vector from the start to the mid point.
	 *
	 * @param connection The arc that is spanned by the given connection.
	 */
	private static double getArc(PolylineConnection connection) {
		PointList points = connection.getPoints();
		Point start = points.getFirstPoint();
		Point center = points.getMidpoint();
		Point end = points.getLastPoint();

		int x1 = start.x - center.x;
		int y1 = start.y - center.y;
		Point vec1 = new Point(x1, y1);

		int x2 = start.x - end.x;
		int y2 = start.y - end.y;
		Point vec2 = new Point(x2, y2);

		double cos = getDotProduct(vec1, vec2) / (getLength(vec1) * getLength(vec2));
		return Math.acos(cos) * 360 / (2 * Math.PI);
	}

	/**
	 * The center is defined as {@code (x + width / 2, y + height / 2)}.
	 *
	 * @return The center of the given {@code node}.
	 */
	protected static Point getCenter(GraphNode node) {
		Point location = node.getLocation();
		Dimension size = node.getSize();
		return new Rectangle(location, size).getCenter();
	}

	/**
	 * Returns the tooltip that is shown for the given figure. The tooltip is
	 * accessed via reflection by first going through the {@link EventDispatcher},
	 * followed by the {@link ToolTipHelper}.
	 *
	 * @param figure The node figure beneath the mouse cursor.
	 * @return The tooltip of the node figure that is currently being shown.
	 */
	protected static IFigure getToolTip(IFigure figure) throws Throwable {
		EventDispatcher eventDispatcher = figure.internalGetEventDispatcher();

		Lookup lookup1 = MethodHandles.privateLookupIn(SWTEventDispatcher.class, MethodHandles.lookup());
		MethodHandle getter1 = lookup1.findGetter(SWTEventDispatcher.class, "toolTipHelper", ToolTipHelper.class); //$NON-NLS-1$
		ToolTipHelper toolTipHelper = (ToolTipHelper) getter1.invoke(eventDispatcher);

		Lookup lookup2 = MethodHandles.privateLookupIn(PopUpHelper.class, MethodHandles.lookup());
		MethodHandle getter2 = lookup2.findGetter(PopUpHelper.class, "lws", LightweightSystem.class); //$NON-NLS-1$
		LightweightSystem lws = (LightweightSystem) getter2.invoke(toolTipHelper);

		return lws.getRootFigure().getChildren().get(0);
	}

	/**
	 * Returns the fish-eye figure at the given coordinates. Note that those figures
	 * are on a separate layer.
	 *
	 * @param x The x coordinate of the fish-eye figure.
	 * @param y The x coordinate of the fish-eye figure.
	 */
	protected GraphLabel getFishEyeFigure(int x, int y) {
		IFigure fishEyeLayer = graph.getRootLayer().getChildren().get(1);
		return (GraphLabel) fishEyeLayer.findFigureAt(x, y);
	}

	/**
	 * Asserts that the given graph node has the expected name.
	 *
	 * @param node The graph node to validate.
	 * @param text The expected name of the graph node.
	 */
	protected static void assertNode(GraphNode node, String text) {
		assertEquals(node.getText(), text);
	}

	/**
	 * Asserts that the given {@code connection} has the expected source and
	 * destination nodes.
	 *
	 * @param connection  The graph connection to validate.
	 * @param source      The name of the expected source node.
	 * @param destination The name of the expected destination node.
	 */
	protected static void assertConnection(GraphConnection connection, String source, String destination) {
		assertEquals(connection.getSource().getText(), source);
		assertEquals(connection.getDestination().getText(), destination);
	}

	/**
	 * Asserts that the given {@code connection} uses a {@link PolylineConnection}
	 * with given {@code arc}. The arc of the connection is calculated using the
	 * points of the polyline. The test passes if this value is within 5° of the
	 * expected value.
	 *
	 * @param connection The graph connection to validate.
	 * @param arc        The expected arc of the
	 */
	protected static void assertArc(GraphConnection connection, double arc) {
		PolylineConnection connectionFigure = (PolylineConnection) connection.getConnectionFigure();
		assertEquals(getArc(connectionFigure), arc, 5); // tolerance to account for OS differences
	}

	/**
	 * Asserts that no graph nodes in the given {@link IContainer} intersect with
	 * one another. This method doesn't check nested containers.
	 *
	 * @param container The {@link IContainer} to validate.
	 */
	protected static void assertNoOverlap(IContainer container) {
		@SuppressWarnings("unchecked")
		List<? extends GraphNode> nodes = container.getNodes();
		for (int i = 0; i < nodes.size(); ++i) {
			for (int j = i + 1; j < nodes.size(); ++j) {
				GraphNode node1 = nodes.get(i);
				Rectangle bounds1 = new Rectangle(node1.getLocation(), node1.getSize());
				GraphNode node2 = nodes.get(j);
				Rectangle bounds2 = new Rectangle(node2.getLocation(), node2.getSize());
				assertFalse(bounds1.intersects(bounds2));
			}
		}
	}

	/**
	 * Asserts that the given {@code object} is of type {@code class}. Subclasses
	 * are allowed.
	 *
	 * @param object The object to validate.
	 * @param clazz  The expected class of the describing {@link IFigure}.
	 */
	protected static void assertInstanceOf(Object object, Class<?> clazz) {
		assertTrue(clazz.isAssignableFrom(object.getClass()));
	}

	/**
	 * @return the {@link Button} with given text.
	 */
	protected static Button findButtonByName(Shell shell, String text) {
		return findWidget(shell, Button.class, b -> text.equals(b.getText()));
	}

	/**
	 * @return the first {@link Text} in the given {@link Shell}.
	 */
	protected static Text findText(Shell shell) {
		return findWidget(shell, Text.class, t -> true);
	}

	/**
	 * @return the first {@link Canvas} in the given {@link Shell}.
	 */
	protected static Canvas findCanvas(Shell shell) {
		return findWidget(shell, Canvas.class, t -> true);
	}

	/**
	 * Convenience method for finding a given {@link Widget} in a {@link Shell}.
	 * type.
	 *
	 * @return The first widget of type {@code T}, matching the predicate. Returns
	 *         {@code null} if no such widget is found.
	 */
	private static <T extends Widget> T findWidget(Shell shell, Class<T> clazz, Predicate<T> predicate) {
		AtomicReference<T> ref = new AtomicReference<>();
		new WidgetVisitor() {
			@Override
			public boolean visit(Widget w) {
				if (clazz.isAssignableFrom(w.getClass())) {
					T widget = clazz.cast(w);
					if (predicate.test(widget)) {
						ref.set(widget);
						return false;
					}
				}
				return true;
			}
		}.traverse(shell);
		return ref.get();
	}

	/**
	 * Pumps the event loop for the given number of milliseconds. At least one
	 * events loop will be executed.
	 */
	protected static void waitEventLoop(int time) {
		long start = System.currentTimeMillis();
		do {
			while (Display.getCurrent().readAndDispatch()) {
				// do nothing
			}
		} while (System.currentTimeMillis() - start < time);
	}
}
