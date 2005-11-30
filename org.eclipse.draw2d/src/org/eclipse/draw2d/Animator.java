/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Animates the layout of a figure's children. The animator will capture the effects of a
 * layout manager, and then play back the placement of children using linear interpolation
 * for each child's start and end locations.
 * @since 3.2
 */
public class Animator extends LayoutListener.Stub {

static final Animator INSTANCE = new Animator();

/**
 * Constructs a new Animator. The singleton {@link #INSTANCE} can be used on all figures
 * being animated.
 * @since 3.2
 */
protected Animator() { }

/**
 * Captures the final state of the given figure. This method is called once after the
 * update manager has completed validation of all invalid figures.
 * @param container the container
 * @since 3.2
 */
public void capture(IFigure container) {
	recordFinalState(container);
}

/**
 * Returns an object encapsulating the placement of children in a container. This method
 * is called to capture both the initial and final states.
 * @param container the container figure
 * @return the current state
 * @since 3.2
 */
protected Object getCurrentState(IFigure container) {
	ArrayList locations = new ArrayList();
	List children = container.getChildren();
	for (int i = 0; i < children.size(); i++)
		locations.add(((IFigure)children.get(i)).getBounds().getCopy());
	return locations;
}

Animator getDefault() {
	return INSTANCE;
}

/**
 * Hooks invalidation in case animation is in progress. 
 * @see LayoutListener#invalidate(IFigure)
 */
public final void invalidate(IFigure container) {
	if (Animation.isInitialRecording())
		Animation.hookAnimator(container, this);
}

/**
 * Hooks layout in case animation is in progress.
 * @see org.eclipse.draw2d.LayoutListener#layout(org.eclipse.draw2d.IFigure)
 */
public final boolean layout(IFigure container) {
	if (Animation.isAnimating()) {
		return playback(container);
	}
	return false;
}

/**
 * Plays back the animation for the given container and returns <code>true</code> if
 * successful.
 * @param container the container being animated
 * @return <code>true</code> if playback was successful
 * @since 3.2
 */
protected boolean playback(IFigure container) {
	List initial = (List) Animation.getInitialState(container);
	List ending = (List) Animation.getFinalState(container);
	if (initial == null)
		return false;
	List children = container.getChildren();
	if (initial.size() != ending.size() || children.size() != ending.size())
		throw new IllegalStateException("children added/removed during animation"); //$NON-NLS-1$

	float progress = Animation.getProgress();
	float ssergorp = 1 - progress;

	Rectangle rect1, rect2;
	
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure) children.get(i);
		rect1 = (Rectangle)initial.get(i);
		rect2 = (Rectangle)ending.get(i);
		child.setBounds(new Rectangle(
			Math.round(progress * rect2.x + ssergorp * rect1.x),
			Math.round(progress * rect2.y + ssergorp * rect1.y),
			Math.round(progress * rect2.width + ssergorp * rect1.width),
			Math.round(progress * rect2.height + ssergorp * rect1.height)
		));
	}
	return true;
}

/**
 * Sent as playback is starting for a given figure.
 * @param figure the figure
 * @since 3.2
 */
public void playbackStarting(IFigure figure) { }

/**
 * Hooks post layout in case animation is in progress.
 * @see org.eclipse.draw2d.LayoutListener#postLayout(org.eclipse.draw2d.IFigure)
 */
public final void postLayout(IFigure container) {
	if (Animation.isFinalRecording())
		Animation.hookPostLayout(container, this);
}

/**
 * Records the final state information for the given figure.
 * @param container the container
 * @since 3.2
 */
protected void recordFinalState(IFigure container) {
	Animation.putFinalState(container, getCurrentState(container));
}

/**
 * Records initial state information for the given figure.
 * @param container the container.
 * @since 3.2
 */
protected void recordInitialState(IFigure container) {
	Animation.putInitialState(container, getCurrentState(container));
}

/**
 * Sets up the animator for the given figure to be animated. This method is called exactly
 * once time prior to any layouts happening. The animator can capture the figure's current
 * state, and set any animation-time settings for the figure. Changes made to the figure
 * should be reverted in {@link #tearDown(IFigure)}.
 * @param figure the animated figure
 * @since 3.2
 */
public void init(IFigure figure) {
	recordInitialState(figure);
}

/**
 * Reverts any temporary changes made to the figure during animation. This method is
 * called exactly once after all animation has been completed. Subclasses should extend
 * this method to revert any changes.
 * @param figure the animated figure
 * @since 3.2
 * @see #init(IFigure)
 */
public void tearDown(IFigure figure) { }

}
