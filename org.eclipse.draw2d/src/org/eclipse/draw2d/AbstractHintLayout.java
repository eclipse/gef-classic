package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * 
 * Created on :Sep 30, 2002
 * @author hudsonr
 * @since 2.0
 */
public abstract class AbstractHintLayout
	extends AbstractLayout
{

private Dimension minimumSize = null;
private Dimension cachedHint = new Dimension(-1, -1);

protected final Dimension calculatePreferredSize(IFigure container) {
	throw new RuntimeException("Unreachable code"); //$NON-NLS-1$
}

protected abstract Dimension calculateMinimumSize(IFigure container);

public final Dimension getMinimumSize(IFigure container) {
	if (minimumSize == null)
		minimumSize = calculateMinimumSize(container);
	return minimumSize;
}

public final Dimension getPreferredSize(IFigure figure, int w, int h) { 
	if (cachedHint.width != w || cachedHint.height != h) {
		invalidate();
		cachedHint.width = w;
		cachedHint.height = h;
	}
	return super.getPreferredSize(figure, w, h);
}

public void invalidate() {
	minimumSize = null;
	super.invalidate();
}

}
