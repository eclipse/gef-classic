package org.eclipse.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Hideable, dock-like Figure. 
 * This Figure is meant to "dock" itself inside a larger Figure.
 * The Dock will hide/show its contents upon a mouse click.
 * 
 * Typical application is an "overview" window, in which the Dock's 
 * contents are a Figure's entire contents displayed much smaller than 
 * their actual size. This effect can be acheived with the use of a
 * {@link ScrollableThumbnail} as the Dock's overview in the
 * constructor Dock(IFigure port, IFigure overview).
 */
final public class Dock 
	extends Figure 
{
	
private IFigure host;
private IFigure contents, resizeFigure;
private AnimationModel animation;
private Dimension oldSize;
private Border contentBorder;

boolean isCollapsing;

public Dock(){
	contentBorder = new CompoundBorder(
		new AbstractBorder(){
			public Insets getInsets(IFigure fig){
				return new Insets(1,1,0,0);
			}
			public void paint(IFigure fig, Graphics g, Insets insets){
				Rectangle rect = getPaintRectangle(fig, insets);
				g.setForegroundColor(ColorConstants.button);
				g.drawLine(rect.x+2, rect.y, rect.right()-1, rect.y);
				g.drawLine(rect.x, rect.y+2, rect.x, rect.bottom()-1);
				g.drawLine(rect.x+1, rect.y+1, rect.x+1, rect.y+1);
			}
			public boolean isOpaque(){return false;}
		},
		new SchemeBorder(new SchemeBorder.Scheme(
			new Color[] { ColorConstants.buttonLightest,
					  ColorConstants.button, ColorConstants.buttonDarker },
			new Color[] { ColorConstants.buttonDarkest, ColorConstants.button,
					  ColorConstants.buttonLightest, ColorConstants.button}
		))
	); 
	resizeFigure = new ResizeFigure();
	setLayoutManager(new StackLayout());
	setBorder(new TabBorder());
	setTitle(""); //$NON-NLS-1$
	setRequestFocusEnabled(true);
	addMouseListener(new MouseListener.Stub(){
		public void mousePressed(MouseEvent me){
			requestFocus();
			if (contents.getParent() == Dock.this)
				minimizeDockIfMaximized();
			else
				maximizeDockIfMinimized();
			me.consume();
		}
	});
	addFocusListener(new FocusListener.Stub(){
		public void focusLost(FocusEvent fe){
			minimizeDockIfMaximized();
		}
	});
}

public Dock(IFigure port, IFigure overview){
	this();
	setHost(port);
	setContents(overview);
}

void animate(){
	if (animation != null)
		return;
	animation = new AnimationModel(250);
	animation.start();
	new Thread(new Runnable(){
		public void run(){
			boolean loop;
			do {
				float value = animation.getValue();
				loop = (value < 1.0);
				Display.getDefault().syncExec(new Runnable(){
					public void run(){	
						revalidate();
					}
				});
				if (!loop)
					animation = null;
			} while (loop);
			oldSize = null;		
			if (isCollapsing){
				Display.getDefault().syncExec(new Runnable(){
					public void run(){	
						remove(contents);
						remove(resizeFigure);
					}
				});

				isCollapsing = false;
			}
		}
	}).start();
}

Dimension calculatePreferredSize(int wHint, int hHint){
	Dimension result = new Dimension();
	List children = getChildren();
	if (! isCollapsing)
		for(int i = 0; i < children.size(); i++){
			IFigure child = (IFigure)children.get(i);
			result.union(child.getPreferredSize(wHint, hHint));
		}
	Dimension borderSize = ((AbstractLabeledBorder)getBorder()).getPreferredSize(this);
	result.width = Math.max(result.width, borderSize.width);
	result.expand(0, borderSize.height);
	return result;
}

public boolean containsPoint(int x, int y){
	return ((TabBorder)getBorder()).containsPoint(x, y) ||
		 getClientArea().contains(x, y);
}

public IFigure findMouseEventTargetAt(int x, int y){
	if (((TabBorder)getBorder()).containsPoint(x, y))
		return this;
	else
		return super.findMouseEventTargetAt(x, y);
}

public IFigure getHost(){
	return host;
}

public Dimension getPreferredSize(int wHint, int hHint){
	if (animation == null)
		return calculatePreferredSize(wHint, hHint);
	float value = animation.getValue();
	Dimension size = new Dimension(calculatePreferredSize(wHint, hHint));
	size.width = Math.max(size.width, oldSize.width);
	size.height *= value;
	size.height += oldSize.height * (1.0f-value);
	return size;
}

void maximizeDockIfMinimized(){
	if (contents.getParent() != Dock.this){
		oldSize = getPreferredSize();
		setCursor(Cursors.APPSTARTING);
		add(contents);
		add(resizeFigure);
		// Asking the thumbnail to paint before animating it allows
		// smooth animation.  Otherwise, the paint can be time-consuming
		// (as the image might have to be scaled again), and it seems
		// like there is no animation.
		Image image = new Image(Display.getDefault(), getSize().width, getSize().height);
		GC gc = new GC(image);
		contents.paint(new SWTGraphics(gc));
		gc.dispose();
		image.dispose();
		setCursor(Cursors.ARROW);
		animate();
	}
}

void minimizeDockIfMaximized(){
	if (contents.getParent() == Dock.this){
		oldSize = getPreferredSize();
		isCollapsing = true;
		animate();
	}
}

public void setContents(IFigure fig){
	contents = fig;
	contents.setBorder(contentBorder);
	contents.setPreferredSize(new Dimension(150,150));
	if (getHost() != null)
		contents.setMaximumSize(getHost().getSize().getScaled(0.5f));
}

public void setTitle(String title){
	((AbstractLabeledBorder)getBorder()).setLabel(title);
}

public void setHost(IFigure port){
	host = port;
	if (contents != null)
		contents.setMaximumSize(host.getSize().getScaled(0.5f));
	port.addFigureListener(new FigureListener(){
		public void figureMoved(IFigure ignored){
			contents.setMaximumSize(getHost().getSize().getScaled(0.5f));
			revalidate();
		}
	});
}

private class ResizeFigure
	extends Figure
{
	
	private Color scheme[] = new Color[]{
		ColorConstants.buttonDarker,
		ColorConstants.buttonDarker,
		ColorConstants.buttonLightest,
		ColorConstants.button
	};
	// Change this number to make the resize area larger or smaller -- just keep
	// it an even number
	private int squareSize = 16;

	public ResizeFigure(){
		ResizeListener listener = new ResizeListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		setCursor(Cursors.SIZENW);
	}

	public boolean containsPoint(int x, int y){
		if (!super.containsPoint(x, y))
			return false;
		Point p = getLocation();
		p.negate().translate(x,y);
		return p.x + p.y <= squareSize;
	}
	
	public Dimension getPreferredSize(int wHint, int hHint){
		return new Dimension();
	}
	
	public void paintFigure(Graphics g){
		Rectangle resizeIcon = new Rectangle();
		int offset = Math.min(contents.getInsets().left - 1, 
					     contents.getInsets().top - 1);
		resizeIcon.setLocation(this.getLocation().getTranslated(offset, offset));
		offset *= 2;
		resizeIcon.setSize(new Dimension(squareSize - offset, squareSize - offset));
		Point p1 = resizeIcon.getTopRight();
		Point p2 = resizeIcon.getBottomLeft();
		g.setLineStyle(Graphics.LINE_SOLID);
		for(int count = 0; count <= resizeIcon.width; count++){
			g.setForegroundColor(scheme[count % scheme.length]);
			g.drawLine(p1,p2);
			p1.x--;
			p2.y--;
		}
		// This does not belong here.  This is part of making a rounded-off top-left
		// corner for the border of the contents.
		g.setForegroundColor(ColorConstants.button);
		Point hack = getLocation().getTranslated(1, 1);
		g.drawLine(hack, hack);
	}

	private class ResizeListener
		extends MouseMotionListener.Stub
		implements MouseListener
	{	
		Point startLocation;
		Dimension originalSize;
		public void mouseDragged(MouseEvent me){
			Dimension newSize = originalSize.getExpanded(
						me.getLocation().getDifference(startLocation).getNegated());
			newSize.width = Math.max(newSize.width, 
				((TabBorder)Dock.this.getBorder()).getPreferredSize(Dock.this).width);
			contents.setPreferredSize(newSize);
			me.consume();
		}
		public void mousePressed(MouseEvent me){
			startLocation = me.getLocation();
			originalSize = contents.getSize().getCopy();
			me.consume();
		}
		public void mouseReleased(MouseEvent me){
		}
		public void mouseDoubleClicked(MouseEvent me){
		}
	}

}


}