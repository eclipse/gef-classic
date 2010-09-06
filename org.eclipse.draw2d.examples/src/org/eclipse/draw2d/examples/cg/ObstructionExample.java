/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.examples.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.examples.AbstractExample;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;

/**
 * @since 3.0
 */
public class ObstructionExample extends AbstractExample {

	static ShortestPathRouter router = new ShortestPathRouter();

	static int COUNT = 0;
	static List sourceList = new ArrayList();
	static List targetList = new ArrayList();
	static Map obstacleMap = new HashMap();
	static List paths = new ArrayList();

	static class EllipseDragFigure extends Ellipse {

		protected Point loc;
		protected EllipseDragFigure oFigure;
		protected EllipseDragFigure thisFigure;
		protected boolean isSource;
		protected Path path;

		private static Dimension offset = new Dimension();

		public EllipseDragFigure(Point location, boolean isItSource) {
			loc = location;
			thisFigure = this;
			this.isSource = isItSource;
			setBackgroundColor(ColorConstants.darkBlue);

			addMouseListener(new MouseListener.Stub() {
				public void mousePressed(MouseEvent event) {
					event.consume();
					offset.setWidth(event.x - getLocation().x());
					offset.setHeight(event.y - getLocation().y());
				}

				public void mouseReleased(MouseEvent event) {
					offset.setWidth(0);
					offset.setHeight(0);
					if (event.button == 3) {
						getParent().remove(oFigure);
						getParent().remove(thisFigure);

						if (isSource) {
							sourceList.remove(loc);
							targetList.remove(oFigure.loc);
							router.removePath(path);
							paths.remove(path);
						} else {
							sourceList.remove(oFigure.loc);
							targetList.remove(loc);
							router.removePath(path);
							paths.remove(path);
						}

					}
				}
			});
			addMouseMotionListener(new MouseMotionListener.Stub() {
				public void mouseDragged(MouseEvent event) {
					Rectangle rect = getBounds().getCopy();
					rect.setX(event.x - offset.width());
					rect.setY(event.y - offset.height());
					setBounds(rect);
					int index = 0;
					if (isSource) {
						index = sourceList.indexOf(loc);

						router.removePath(path);
						paths.remove(path);
						sourceList.remove(loc);
						loc = new Point(rect.x() + 10, rect.y() + 10);
						sourceList.add(index, loc);
						path = new Path(loc, (Point) targetList.get(index));
						router.addPath(path);
						paths.add(path);
						oFigure.path = path;
					} else {
						index = targetList.indexOf(loc);

						router.removePath(path);
						paths.remove(path);
						targetList.remove(loc);
						loc = new Point(rect.x() + 10, rect.y() + 10);
						targetList.add(index, loc);
						path = new Path((Point) sourceList.get(index), loc);
						router.addPath(path);
						paths.add(path);
						oFigure.path = path;
					}
					getParent().repaint();

				}
			});

		}

		public void addOtherFigure(EllipseDragFigure figure) {
			this.oFigure = figure;
		}
	}

	static class DragFigure extends RectangleFigure {
		private static Dimension offset = new Dimension();
		private DragFigure thisFigure;

		public DragFigure() {
			thisFigure = this;

			setBackgroundColor(ColorConstants.green);

			addMouseListener(new MouseListener.Stub() {
				public void mousePressed(MouseEvent event) {
					event.consume();
					offset.setWidth(event.x - getLocation().x());
					offset.setHeight(event.y - getLocation().y());
				}

				public void mouseReleased(MouseEvent event) {
					offset.setWidth(0);
					offset.setHeight(0);
					if (event.button == 3) {
						getParent().remove(thisFigure);
						router.removeObstacle((Rectangle) ObstructionExample.obstacleMap
								.get(thisFigure));
						ObstructionExample.obstacleMap.remove(thisFigure);
					}
				}
			});
			addMouseMotionListener(new MouseMotionListener.Stub() {
				public void mouseDragged(MouseEvent event) {

					Rectangle rect = getBounds().getCopy();
					rect.setX(event.x - offset.width());
					rect.setY(event.y - offset.height());
					router.updateObstacle(
							(Rectangle) ObstructionExample.obstacleMap
									.get(thisFigure), rect);
					ObstructionExample.obstacleMap.put(thisFigure, rect);
					setBounds(rect);
					getParent().repaint();
				}
			});
		}

		public void paint(Graphics graphics) {
			super.paint(graphics);
			Rectangle b = getBounds();
			Point p = b.getCenter().translate(-22, -15);
			graphics.drawString("y:" + b.y() + "h:" + b.height(), p);
			p.setY(p.y() + 12);
			graphics.drawString("x:" + b.x() + "w:" + b.width(), p);
		}
	}

	class TestFigure extends Figure {

		Map obstacleMap = new HashMap();
		List obstaclesList = new ArrayList();
		boolean showSegs = false;

		public TestFigure() {
			router = new ShortestPathRouter();

			addMouseListener(new MouseListener.Stub() {
				private Point pressPoint;
				private boolean firstPointCreated = false;
				private EllipseDragFigure figure;
				private Point pPoint;

				public void mousePressed(MouseEvent event) {
					event.consume();
					pPoint = event.getLocation();
				}

				public void mouseReleased(MouseEvent event) {
					if (event.button == 1) {
						if (Math.abs(pPoint.x() - event.getLocation().x()) > 10) {
							DragFigure f = new DragFigure();
							add(f);

							f.setBounds(new Rectangle(pPoint, event
									.getLocation()));
							Rectangle bounds = new Rectangle(pPoint,
									event.getLocation());

							router.addObstacle(bounds);
							ObstructionExample.obstacleMap.put(f, bounds);

						}
					} else if (event.button == 3) {
						if (firstPointCreated) {
							targetList.add(event.getLocation());
							EllipseDragFigure eFigure = new EllipseDragFigure(
									event.getLocation(), false);
							eFigure.setBounds(new Rectangle(event.getLocation()
									.x() - 10, event.getLocation().y() - 10,
									20, 20));
							add(eFigure);
							firstPointCreated = false;
							figure.addOtherFigure(eFigure);
							eFigure.addOtherFigure(figure);
							Path path = new Path(pressPoint,
									event.getLocation());
							router.addPath(path);
							paths.add(path);
							eFigure.path = path;
							figure.path = path;
							figure = null;
							pressPoint = null;
							path = null;
						} else {
							sourceList.add(event.getLocation());
							pressPoint = event.getLocation();
							EllipseDragFigure eFigure = new EllipseDragFigure(
									event.getLocation(), true);
							eFigure.setBounds(new Rectangle(event.getLocation()
									.x() - 10, event.getLocation().y() - 10,
									20, 20));
							add(eFigure);
							figure = eFigure;
							firstPointCreated = true;
						}
					} else
						showSegs = !showSegs;
					getParent().repaint();
				}
			});

			DragFigure f;
			Random r = new Random(0);
			int rowSize = (int) Math.sqrt(COUNT);
			for (int i = 0; i < COUNT; i++) {
				add(f = new DragFigure());
				f.setBounds(new Rectangle((i / rowSize) * 101 + (i) % 3 * 10
						+ 100, i % rowSize * 101 + (i % 7) * 6, 50,
						80 + (int) (r.nextDouble() * 10)));
			}

			for (int i = 0; i < COUNT; i++) {
				Rectangle bounds = ((IFigure) obstaclesList.get(i)).getBounds()
						.getCopy();
				ObstructionExample.obstacleMap
						.put(obstaclesList.get(i), bounds);
				router.addObstacle(bounds);
			}
		}

		public void add(IFigure figure, Object constraint, int index) {
			if (figure instanceof DragFigure)
				obstaclesList.add(figure);

			super.add(figure, constraint, index);
		}

		protected void paintBorder(Graphics g) {
			router.solve();
			g.setLineWidth(1);

			g.setForegroundColor(ColorConstants.blue);
			g.setBackgroundColor(ColorConstants.button);

			// draw paths
			Path path = null;
			for (int n = 0; n < paths.size(); n++) {
				path = (Path) paths.get(n);
				PointList pList = path.getPoints();

				for (int i = 1; i < pList.size(); i++)
					g.drawLine(pList.getPoint(i - 1), pList.getPoint(i));
			}
		}

		public void remove(IFigure figure) {
			if (figure instanceof DragFigure) {
				obstaclesList.remove(figure);
			}

			super.remove(figure);
			repaint();
		}

	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
	 */
	protected IFigure getContents() {
		Figure f = new TestFigure();
		f.setPreferredSize(900, 700);
		return f;
	}

	public static void main(String[] args) {
		new ObstructionExample().run();
	}

}
