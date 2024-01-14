/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides a {@link Connection} with an orthogonal route between the
 * Connection's source and target anchors.
 */
public final class ManhattanConnectionRouter extends AbstractRouter {

	private final Map<Integer, Integer> rowsUsed = new HashMap<>();
	private final Map<Integer, Integer> colsUsed = new HashMap<>();
	private final Map<Connection, ReservedInfo> reservedInfo = new HashMap<>();

	private class ReservedInfo {
		public List<Integer> reservedRows = new ArrayList<>(2);
		public List<Integer> reservedCols = new ArrayList<>(2);
	}

	private static final Ray UP = new Ray(0, -1);
	private static final Ray DOWN = new Ray(0, 1);
	private static final Ray LEFT = new Ray(-1, 0);
	private static final Ray RIGHT = new Ray(1, 0);

	/**
	 * @see ConnectionRouter#invalidate(Connection)
	 */
	@Override
	public void invalidate(Connection connection) {
		removeReservedLines(connection);
	}

	private int getColumnNear(Connection connection, int r, int n, int x) {
		int min = Math.min(n, x), max = Math.max(n, x);
		if (min > r) {
			max = min;
			min = r - (min - r);
		}
		if (max < r) {
			min = max;
			max = r + (r - max);
		}
		int proximity = 0;
		int direction = -1;
		if (r % 2 == 1) {
			r--;
		}
		Integer i;
		while (proximity < r) {
			i = Integer.valueOf(r + proximity * direction);
			if (!colsUsed.containsKey(i)) {
				colsUsed.put(i, i);
				reserveColumn(connection, i);
				return i.intValue();
			}
			int j = i.intValue();
			if (j <= min) {
				return j + 2;
			}
			if (j >= max) {
				return j - 2;
			}
			if (direction == 1) {
				direction = -1;
			} else {
				direction = 1;
				proximity += 2;
			}
		}
		return r;
	}

	/**
	 * Returns the direction the point <i>p</i> is in relation to the given
	 * rectangle. Possible values are LEFT (-1,0), RIGHT (1,0), UP (0,-1) and DOWN
	 * (0,1).
	 *
	 * @param r the rectangle
	 * @param p the point
	 * @return the direction from <i>r</i> to <i>p</i>
	 */
	@SuppressWarnings("static-method")
	protected Ray getDirection(Rectangle r, Point p) {
		int distance = Math.abs(r.x - p.x);
		Ray direction;

		direction = LEFT;

		int i = Math.abs(r.y - p.y);
		if (i <= distance) {
			distance = i;
			direction = UP;
		}

		i = Math.abs(r.bottom() - p.y);
		if (i <= distance) {
			distance = i;
			direction = DOWN;
		}

		i = Math.abs(r.right() - p.x);
		if (i < distance) {
			direction = RIGHT;
		}

		return direction;
	}

	protected Ray getEndDirection(Connection conn) {
		ConnectionAnchor anchor = conn.getTargetAnchor();
		Point p = getEndPoint(conn);
		Rectangle rect;
		if (anchor.getOwner() == null) {
			rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
		} else {
			rect = conn.getTargetAnchor().getOwner().getBounds().getCopy();
			conn.getTargetAnchor().getOwner().translateToAbsolute(rect);
		}
		return getDirection(rect, p);
	}

	protected int getRowNear(Connection connection, int r, int n, int x) {
		int min = Math.min(n, x), max = Math.max(n, x);
		if (min > r) {
			max = min;
			min = r - (min - r);
		}
		if (max < r) {
			min = max;
			max = r + (r - max);
		}

		int proximity = 0;
		int direction = -1;
		if (r % 2 == 1) {
			r--;
		}
		Integer i;
		while (proximity < r) {
			i = Integer.valueOf(r + proximity * direction);
			if (!rowsUsed.containsKey(i)) {
				rowsUsed.put(i, i);
				reserveRow(connection, i);
				return i.intValue();
			}
			int j = i.intValue();
			if (j <= min) {
				return j + 2;
			}
			if (j >= max) {
				return j - 2;
			}
			if (direction == 1) {
				direction = -1;
			} else {
				direction = 1;
				proximity += 2;
			}
		}
		return r;
	}

	protected Ray getStartDirection(Connection conn) {
		ConnectionAnchor anchor = conn.getSourceAnchor();
		Point p = getStartPoint(conn);
		Rectangle rect;
		if (anchor.getOwner() == null) {
			rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
		} else {
			rect = conn.getSourceAnchor().getOwner().getBounds().getCopy();
			conn.getSourceAnchor().getOwner().translateToAbsolute(rect);
		}
		return getDirection(rect, p);
	}

	protected void processPositions(Ray start, Ray end, List<Integer> positions, boolean horizontal, Connection conn) {
		removeReservedLines(conn);

		int[] pos = new int[positions.size() + 2];
		if (horizontal) {
			pos[0] = start.x;
		} else {
			pos[0] = start.y;
		}
		int i;
		for (i = 0; i < positions.size(); i++) {
			pos[i + 1] = positions.get(i).intValue();
		}
		if (horizontal == (positions.size() % 2 == 1)) {
			i++;
			pos[i] = end.x;
		} else {
			i++;
			pos[i] = end.y;
		}

		PointList points = new PointList();
		points.addPoint(new Point(start.x, start.y));
		Point p;
		boolean adjust;
		for (i = 2; i < pos.length - 1; i++) {
			horizontal = !horizontal;
			int prev = pos[i - 1];
			int current = pos[i];

			adjust = (i != pos.length - 2);
			if (horizontal) {
				if (adjust) {
					int min = pos[i - 2];
					int max = pos[i + 2];
					pos[i] = current = getRowNear(conn, current, min, max);
				}
				p = new Point(prev, current);
			} else {
				if (adjust) {
					int min = pos[i - 2];
					int max = pos[i + 2];
					pos[i] = current = getColumnNear(conn, current, min, max);
				}
				p = new Point(current, prev);
			}
			points.addPoint(p);
		}
		points.addPoint(new Point(end.x, end.y));
		conn.setPoints(points);
	}

	/**
	 * @see ConnectionRouter#remove(Connection)
	 */
	@Override
	public void remove(Connection connection) {
		removeReservedLines(connection);
	}

	protected void removeReservedLines(Connection connection) {
		ReservedInfo rInfo = reservedInfo.get(connection);
		if (rInfo == null) {
			return;
		}

		for (Object element : rInfo.reservedRows) {
			rowsUsed.remove(element);
		}
		for (Object element : rInfo.reservedCols) {
			colsUsed.remove(element);
		}
		reservedInfo.remove(connection);
	}

	protected void reserveColumn(Connection connection, Integer column) {
		ReservedInfo info = reservedInfo.computeIfAbsent(connection, dummy -> new ReservedInfo());
		info.reservedCols.add(column);
	}

	protected void reserveRow(Connection connection, Integer row) {
		ReservedInfo info = reservedInfo.computeIfAbsent(connection, dummy -> new ReservedInfo());
		info.reservedRows.add(row);
	}

	/**
	 * @see ConnectionRouter#route(Connection)
	 */
	@Override
	public void route(Connection conn) {
		if ((conn.getSourceAnchor() == null) || (conn.getTargetAnchor() == null)) {
			return;
		}
		int i;
		Point startPoint = getStartPoint(conn);
		conn.translateToRelative(startPoint);
		Point endPoint = getEndPoint(conn);
		conn.translateToRelative(endPoint);

		Ray start = new Ray(startPoint);
		Ray end = new Ray(endPoint);
		Ray average = start.getAveraged(end);

		Ray direction = new Ray(start, end);
		Ray startNormal = getStartDirection(conn);
		Ray endNormal = getEndDirection(conn);

		List<Integer> positions = new ArrayList<>(5);
		boolean horizontal = startNormal.isHorizontal();
		if (horizontal) {
			positions.add(Integer.valueOf(start.y));
		} else {
			positions.add(Integer.valueOf(start.x));
		}
		horizontal = !horizontal;

		if (startNormal.dotProduct(endNormal) == 0) {
			if ((startNormal.dotProduct(direction) >= 0) && (endNormal.dotProduct(direction) <= 0)) {
				// 0
			} else {
				// 2
				if (startNormal.dotProduct(direction) < 0) {
					i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
				} else {
					if (horizontal) {
						i = average.y;
					} else {
						i = average.x;
					}
				}
				positions.add(Integer.valueOf(i));
				horizontal = !horizontal;

				if (endNormal.dotProduct(direction) > 0) {
					i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
				} else {
					if (horizontal) {
						i = average.y;
					} else {
						i = average.x;
					}
				}
				positions.add(Integer.valueOf(i));
				horizontal = !horizontal;
			}
		} else {
			if (startNormal.dotProduct(endNormal) > 0) {
				// 1
				if (startNormal.dotProduct(direction) >= 0) {
					i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
				} else {
					i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
				}
				positions.add(Integer.valueOf(i));
				horizontal = !horizontal;
			} else {
				// 3 or 1
				if (startNormal.dotProduct(direction) < 0) {
					i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
					positions.add(Integer.valueOf(i));
					horizontal = !horizontal;
				}

				if (horizontal) {
					i = average.y;
				} else {
					i = average.x;
				}
				positions.add(Integer.valueOf(i));
				horizontal = !horizontal;

				if (startNormal.dotProduct(direction) < 0) {
					i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
					positions.add(Integer.valueOf(i));
					horizontal = !horizontal;
				}
			}
		}
		if (horizontal) {
			positions.add(Integer.valueOf(end.y));
		} else {
			positions.add(Integer.valueOf(end.x));
		}

		processPositions(start, end, positions, startNormal.isHorizontal(), conn);
	}

}
