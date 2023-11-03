/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr Created on Apr 28, 2003
 */
public class Animation {

	static final long DURATION = 210;

	static long current;
	static double progress;
	static long start = -1;
	static long finish;
	static Viewport viewport;
	static IFigure trackMe;
	static IFigure showMe;
	static Point trackLocation;

	static boolean PLAYBACK;
	static boolean RECORDING;

	static Map<IFigure, List<Rectangle>> initialStates;
	static Map<IFigure, List<Rectangle>> finalStates;

	static void end() {
		initialStates.keySet().forEach(IFigure::revalidate);
		initialStates = null;
		finalStates = null;
		PLAYBACK = false;
		trackMe = null;
		showMe = null;
		viewport = null;
	}

	static void mark(IFigure figure) {
		trackMe = figure;
		trackLocation = trackMe.getBounds().getLocation();
		while (!(figure instanceof Viewport)) {
			figure = figure.getParent();
		}
		viewport = (Viewport) figure;

		initialStates = new HashMap<>();
		finalStates = new HashMap<>();
		start = System.currentTimeMillis();
		finish = start + DURATION;
		current = start + 20;
	}

	static void captureLayout(IFigure root) {
		RECORDING = true;
		while (root.getParent() != null) {
			root = root.getParent();
		}

		root.validate();
		initialStates.keySet().forEach(Animation::recordFinalStates);

		RECORDING = false;
		PLAYBACK = true;
	}

	static boolean playbackState(IFigure container) {
		if (!PLAYBACK) {
			return false;
		}
		List<Rectangle> initial = initialStates.get(container);
		if (initial == null) {
			System.out.println("Error playing back state"); //$NON-NLS-1$
			return false;
		}
		List<Rectangle> target = finalStates.get(container);
		List<? extends IFigure> children = container.getChildren();

		for (int i = 0; i < children.size(); i++) {
			IFigure child = children.get(i);
			Rectangle rect1 = initial.get(i);
			Rectangle rect2 = target.get(i);
			child.setBounds(new Rectangle((int) Math.round((progress * rect2.x) + ((1 - progress) * rect1.x)),
					(int) Math.round((progress * rect2.y) + ((1 - progress) * rect1.y)),
					(int) Math.round((progress * rect2.width) + ((1 - progress) * rect1.width)),
					(int) Math.round((progress * rect2.height) + ((1 - progress) * rect1.height))));
		}
		return true;
	}

	static void recordFinalStates(IFigure container) {
		recordStates(container, finalStates);
	}

	static void recordInitialState(IFigure container) {
		if (!RECORDING) {
			return;
		}
		List<Rectangle> list = initialStates.get(container);
		if (list != null) {
			return;
		}
		recordStates(container, initialStates);
	}

	private static void recordStates(IFigure container, Map<IFigure, List<Rectangle>> state) {
		final List<Rectangle> newList = new ArrayList<>();
		state.put(container, newList);
		container.getChildren().forEach(child -> newList.add(child.getBounds().getCopy()));
	}

	static void swap() {
		var temp = finalStates;
		finalStates = initialStates;
		initialStates = temp;
	}

	static boolean step() {
		current = System.currentTimeMillis() + 30;
		progress = (double) (current - start) / (finish - start);
		progress = Math.min(progress, 0.999);

		initialStates.keySet().forEach(IFigure::revalidate);
		viewport.validate();

		Point loc = viewport.getViewLocation();
		loc.translate(trackMe.getBounds().getLocation().getDifference(trackLocation));
		viewport.setViewLocation(loc);
		trackLocation = trackMe.getBounds().getLocation();

		return current < finish;
	}

}
