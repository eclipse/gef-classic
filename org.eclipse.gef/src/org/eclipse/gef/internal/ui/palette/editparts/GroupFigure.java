/*
 * Created on Feb 14, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MarginBorder;

/**
 * @author hudsonr
 */
public class GroupFigure extends Figure {

private static final Border BORDER = new MarginBorder(2, 1, 2, 1);

{
	setBorder(new RaisedBorder(3,3,3,3));
//	setOpaque(true);
}

}
