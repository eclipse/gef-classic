package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * 
 * Created on :Sep 30, 2002
 * @author hudsonr
 * @since 2.0
 */
abstract public class AbstractHintLayout
	extends AbstractLayout
{

private Dimension minimumSize = null;
private Dimension cachedHint = new Dimension(-1, -1);

final protected Dimension calculatePreferredSize(IFigure container) {
	throw new RuntimeException("Unreachable code");
}

protected abstract Dimension calculateMinimumSize(IFigure container);

final public Dimension getMinimumSize(IFigure container) {
	if (minimumSize == null)
		minimumSize = calculateMinimumSize(container);
	return minimumSize;
}

final public Dimension getPreferredSize(IFigure figure, int w, int h){ 
	if (cachedHint.width != w
	  || cachedHint.height != h)
	{
		invalidate();
		cachedHint.width = w;
		cachedHint.height = h;
	}
	return super.getPreferredSize(figure, w, h);
}

public void invalidate(){
	minimumSize = null;
	super.invalidate();
}

}
