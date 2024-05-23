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

package org.eclipse.zest.tests.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.core.runtime.Assert;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.HideNodeHelper;

import org.eclipse.draw2d.Clickable;

/**
 * Utility class to simulate user events (clicks, etc.) on a {@link Graph}.
 */
public class GraphicalRobot {
	private final Graph graph;

	public GraphicalRobot(Graph graph) {
		this.graph = graph;
	}

	/**
	 * This method simulates a {@link SWT#KeyDown} event using the given
	 * {@code character}. Events are sent to the current {@link #graph} instance.
	 *
	 * @param character The character that has been typed.
	 */
	public final void keyDown(char character) {
		keyDown(0, character);
	}

	/**
	 * This method simulates a {@link SWT#KeyDown} event using the given
	 * {@code character} and {@code state mask}. Events are sent to the current
	 * {@link #graph} instance.
	 *
	 * @param stateMask The (optional) keyboard modified that has been pressed.
	 * @param character The character that has been typed.
	 */
	public void keyDown(int stateMask, char character) {
		Event event = createKeyEvent(SWT.KeyDown, stateMask, character);
		graph.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates a {@link SWT#MouseHover} event using the given
	 * {@code x} and {@code y} coordinates. Events are sent to the current
	 * {@link #graph} instance. The coordinates must be inside the graph.
	 *
	 * @param x The x coordinate of the simulated mouse cursor.
	 * @param y The y coordinate of the simulated mouse cursor.
	 */
	public void mouseHover(int x, int y) {
		Assert.isTrue(graph.getBounds().contains(x, y), "Coordinates are not inside the graph."); //$NON-NLS-1$
		Event event = createMouseEvent(SWT.MouseHover, x, y);
		graph.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates a {@link SWT#MouseMove} event using the given {@code x}
	 * and {@code y} coordinates. Events are sent to the current {@link #graph}
	 * instance. The coordinates must be inside the graph.
	 *
	 * @param x The x coordinate of the simulated mouse cursor.
	 * @param y The y coordinate of the simulated mouse cursor.
	 */
	public void mouseMove(int x, int y) {
		Assert.isTrue(graph.getBounds().contains(x, y), "Coordinates are not inside the graph."); //$NON-NLS-1$
		Event event = createMouseEvent(SWT.MouseMove, x, y);
		graph.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates a {@link SWT#MouseDown} event using the given {@code x}
	 * and {@code y} coordinates. Events are sent to the current {@link #graph}
	 * instance. The coordinates must be inside the graph.
	 *
	 * @param x The x coordinate of the simulated mouse cursor.
	 * @param y The y coordinate of the simulated mouse cursor.
	 */
	public void mouseDown(int x, int y) {
		Assert.isTrue(graph.getBounds().contains(x, y), "Coordinates are not inside the graph."); //$NON-NLS-1$
		Event event = createMouseEvent(SWT.MouseDown, x, y);
		graph.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates a {@link SWT#MouseDoubleClick} event using the given
	 * {@code x} and {@code y} coordinates. Events are sent to the current
	 * {@link #graph} instance. The coordinates must be inside the graph.
	 *
	 * @param x The x coordinate of the simulated mouse cursor.
	 * @param y The y coordinate of the simulated mouse cursor.
	 */
	public void mouseDoubleClick(int x, int y) {
		Assert.isTrue(graph.getBounds().contains(x, y), "Coordinates are not inside the graph."); //$NON-NLS-1$
		Event event = createMouseEvent(SWT.MouseDoubleClick, x, y);
		graph.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates the selection of the given graph node. It first selects
	 * the given {@code node}, followed by sending an {@link SWT#Selection} event on
	 * the current {@link #graph}.
	 *
	 * @param node The graph node to select.
	 */
	public void select(GraphNode node) {
		graph.setSelection(new GraphItem[] { node });
		select(graph);
	}

	/**
	 * This method simulates a {@link SWT#Selection} event using the given
	 * {@code widget}. Events are sent to the argument.
	 *
	 * @param widget The widget to select.
	 */
	@SuppressWarnings("static-method")
	public void select(Widget widget) {
		Event event = createMouseEvent(SWT.Selection, 1, 1);
		widget.notifyListeners(event.type, event);
	}

	/**
	 * This method simulates a {@link SWT#FocusOut} event using the given
	 * {@code widget}. Events are sent to the argument.
	 *
	 * @param widget The widget whose focus has been lost.
	 */
	@SuppressWarnings("static-method")
	public void focusOut(Widget widget) {
		widget.notifyListeners(SWT.FocusOut, new Event());
	}

	/**
	 * This method simulates clicking the {@code hide} button of the
	 * {@link HideNodeHelper} figure, corresponding to the given node. The
	 * hide-nodes feature must be enabled on the current {@link #graph}.
	 *
	 * @param node The node to hide.
	 */
	@SuppressWarnings("static-method")
	public void clickHide(GraphNode node) {
		Assert.isTrue(node.getGraphModel().getHideNodesEnabled(), "Hide-feature is not enabled"); //$NON-NLS-1$
		HideNodeHelper helper = node.getHideNodeHelper();
		Clickable hideButton = (Clickable) helper.getChildren().get(0);
		hideButton.doClick();
	}

	/**
	 * This method simulates clicking the {@code reveal} button of the
	 * {@link HideNodeHelper} figure, corresponding to the given node. The
	 * hide-nodes feature must be enabled on the current {@link #graph}.
	 *
	 * @param node The node to reveal.
	 */
	@SuppressWarnings("static-method")
	public void clickReveal(GraphNode node) {
		Assert.isTrue(node.getGraphModel().getHideNodesEnabled(), "Hide-feature is not enabled"); //$NON-NLS-1$
		HideNodeHelper helper = node.getHideNodeHelper();
		Clickable revealButton = (Clickable) helper.getChildren().get(1);
		revealButton.doClick();
	}

	private static Event createMouseEvent(int type, int x, int y) {
		Event event = new Event();
		event.x = x;
		event.y = y;
		event.type = type;
		return event;
	}

	private static Event createKeyEvent(int type, int stateMask, char character) {
		Event event = new Event();
		event.type = type;
		event.stateMask = stateMask;
		event.keyCode = character;
		event.character = character;
		return event;
	}
}
