/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.draw2d.examples.cg;

import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.ColorConstants;
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
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * since 3.0
 */
public class ObstructionExample extends AbstractExample {

static class DragFigure extends RectangleFigure {
	private static Dimension offset = new Dimension();
	DragFigure() {
		setBackgroundColor(ColorConstants.green);
		addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent event) {
				event.consume();
				offset.width = event.x - getLocation().x;
				offset.height = event.y - getLocation().y;
			}
			public void mouseReleased(MouseEvent event) {
				offset.width = 0;
				offset.height = 0;
			}
		});
		addMouseMotionListener(new MouseMotionListener.Stub() {
			public void mouseDragged(MouseEvent event) {
				Rectangle rect = getBounds().getCopy();
				rect.x = event.x - offset.width;
				rect.y = event.y - offset.height;
				setBounds(rect);
				getParent().repaint();//REPAINT THE visibility graph
			}
		});
	}
}

class TestFigure extends Figure {
	
	Point source, target;
	
	{
		source = new Point(15, 111);
		target = new Point(800, 300);
		DragFigure f;
		Random r = new Random(0);
		int COUNT = 30;
		int rowSize = (int)Math.sqrt(COUNT);
		for (int i = 0; i < COUNT; i++) {
			add(f = new DragFigure());
			f.setBounds(new Rectangle(
					(i / rowSize) * 101 + (i) % 3 * 10 + 100, i % rowSize * 101 + (i % 7) * 6,
					50, 50 + (int)(r.nextDouble() * 10)));
		}
/*		add(f = new DragFigure());
		f.setBounds(new Rectangle(200, 100, 50, 70));
		add(f = new DragFigure());
		f.setBounds(new Rectangle(0, 110, 50, 120));
		add(f = new DragFigure());
		f.setBounds(new Rectangle(120, 0, 50, 50));
		add(f = new DragFigure());
		f.setBounds(new Rectangle(190, 200, 80, 50));
*/	}
	protected void paintBorder(Graphics g) {
	
		ShortestPathRouting routing = new ShortestPathRouting();
		List children = getChildren();
		Rectangle obstacles[] = new Rectangle[children.size()];
		for (int i = 0; i < obstacles.length; i++)
			obstacles[i] = ((IFigure)children.get(i)).getBounds();
		routing.setObstacles(obstacles);
		routing.setGoals(source, target);
		
		g.setForegroundColor(ColorConstants.blue);
		g.setLineWidth(2);
		//g.setXORMode(true);
		List segs = routing.segments;

		for (int i = 0; i < segs.size(); i++) {
			ShortestPathRouting.Segment seg = (ShortestPathRouting.Segment)segs.get(i);
			g.drawLine(seg.u, seg.v);
		}
		
		g.setForegroundColor(ColorConstants.black);
		g.setLineWidth(1);
		segs = routing.visibility;
		for (int i = 0; i < segs.size(); i++) {
			ShortestPathRouting.Segment seg = (ShortestPathRouting.Segment)segs.get(i);
			g.drawLine(seg.u, seg.v);
		}
		
		g.setBackgroundColor(ColorConstants.darkBlue);
		g.fillOval(source.x - 10, source.y - 5, 20, 20);
		g.fillOval(target.x - 10, target.y - 5, 20, 20);
/*		Rectangle clip = new Rectangle();
		g.getClip(clip);
		ShortestPathRouting.Segment seg = new ShortestPathRouting.Segment();
		seg.x1 = 80;
		seg.x2 = 200;
		seg.y1 = 170;
		seg.y2 = 165;
		int x1 = 80, y1 = 10;
		for(int x = clip.x; x < clip.right(); x++)
			for (int y = clip.y; y < clip.bottom(); y++)
				if (seg.intersects(x1, y1, x, y))
					g.drawPoint(x, y);
*/
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
