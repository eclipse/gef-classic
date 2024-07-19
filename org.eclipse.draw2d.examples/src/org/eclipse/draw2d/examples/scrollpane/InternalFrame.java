/*******************************************************************************
 * Copyright (c) 2005, 2024 IBM Corporation and others.
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
package org.eclipse.draw2d.examples.scrollpane;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.CursorProvider;
import org.eclipse.draw2d.CursorProviders;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.LabeledContainer;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class InternalFrame extends LabeledContainer {

	static class InternalFrameBorder extends CompoundBorder {
		InternalFrameBorder() {
			TitleBarBorder titlebar = new TitleBarBorder();
			titlebar.setTextColor(ColorConstants.white);
			titlebar.setBackgroundColor(ColorConstants.darkGray);
			inner = new CompoundBorder(
					new LineBorder(FigureUtilities.mixColors(ColorConstants.buttonDarker, ColorConstants.button), 3),
					new SchemeBorder(SchemeBorder.SCHEMES.LOWERED));
			outer = new CompoundBorder(new SchemeBorder(SchemeBorder.SCHEMES.RAISED), titlebar);
		}
	}

	public InternalFrame() {
		super(new InternalFrameBorder());
		setLayoutManager(new StackLayout());
		setOpaque(true);
		setRequestFocusEnabled(true);
		addListeners();
	}

	public InternalFrame(String title) {
		this();
		setLabel(title);
	}

	protected void addListeners() {
		DragTracker tracker = new DragTracker();
		addMouseMotionListener(tracker);
		addMouseListener(tracker);
	}

	private class DragTracker extends MouseMotionListener.Stub implements MouseListener {
		private boolean move;
		private Point startLocation;
		private Rectangle bounds;
		private int position = PositionConstants.NONE;

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			if (position == PositionConstants.NONE) {
				return;
			}
			int dx = 0, dy = 0, dw = 0, dh = 0;
			Rectangle rect = bounds.getCopy();
			Dimension diff = me.getLocation().getDifference(startLocation);
			if ((position & PositionConstants.NORTH) == PositionConstants.NORTH) {
				dy = diff.height;
				if (move) {
					dx = diff.width;
				} else {
					dh = -diff.height;
				}
			}
			if ((position & PositionConstants.WEST) == PositionConstants.WEST) {
				dx = diff.width;
				dx = (rect.x + dx) > ((rect.x + rect.width) - 100) ? rect.width - 100 : dx;
				dw = -diff.width;
			}
			if ((position & PositionConstants.EAST) == PositionConstants.EAST) {
				dw = diff.width;
			}
			if ((position & PositionConstants.SOUTH) == PositionConstants.SOUTH) {
				dh = diff.height;
			}
			rect.x += dx;
			rect.y += dy;
			rect.width += dw;
			rect.width = rect.width < 100 ? 100 : rect.width;
			rect.height += dh;
			rect.height = rect.height < 22 ? 22 : rect.height;
			setBounds(rect);
			revalidate();
		}

		@Override
		public void mouseMoved(MouseEvent me) {
			Rectangle figBounds = getBounds();
			Point mouseLocation = me.getLocation();
			if (!getBounds().contains(mouseLocation)) {
				System.out.println("Error"); //$NON-NLS-1$
			}
			if (!figBounds.contains(mouseLocation)) {
				return;
			}
			position = getClientArea().getCopy().shrink(4, 4).getPosition(mouseLocation);
			move = false;
			CursorProvider newCursor = null;

			switch (position) {
			case PositionConstants.NONE:
				break;
			case PositionConstants.SOUTH_EAST:
				newCursor = CursorProviders.SIZESE;
				break;
			case PositionConstants.SOUTH:
				newCursor = CursorProviders.SIZES;
				break;
			case PositionConstants.EAST:
				newCursor = CursorProviders.SIZEE;
				break;
			case PositionConstants.NORTH_EAST:
				newCursor = CursorProviders.SIZENE;
				break;
			case PositionConstants.NORTH_WEST:
				newCursor = CursorProviders.SIZENW;
				break;
			case PositionConstants.SOUTH_WEST:
				newCursor = CursorProviders.SIZESW;
				break;
			case PositionConstants.NORTH:
				if (mouseLocation.y > (getBounds().y + getInsets().left)) {
					// Special case: MOVE and NOT RESIZE
					move = true;
				} else {
					newCursor = CursorProviders.SIZEN;
				}
				break;
			case PositionConstants.WEST:
				newCursor = CursorProviders.SIZEW;
				break;
			}
			setCursorProvider(newCursor);
		}

		@Override
		public void mousePressed(MouseEvent me) {
			// Request focus
			requestFocus();
			// Bring this frame to the top
			// getParent().add(InternalFrame.this);
			me.consume();

			startLocation = me.getLocation();
			bounds = getBounds().getCopy();
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			revalidate();
			startLocation = null;
			position = PositionConstants.NONE;
			move = false;
		}

		@Override
		public void mouseExited(MouseEvent me) {
			setCursorProvider((CursorProvider) null);
		}
	}

}