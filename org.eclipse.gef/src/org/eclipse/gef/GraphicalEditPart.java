package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import org.eclipse.draw2d.*;

/**
 * Specialized EditPart for use with Graphical draw2d Figures.
 */
public interface GraphicalEditPart
	extends EditPart
{

/**
 * Returns the outer-most figure representing this EditPart.
 This is the figure that will be added into the parent's content pane.
 */
IFigure getFigure();

/**
 * Returns the connections for which this EditPart is the <B>source</B>.
 * This method should only be called internally or by helpers such as
 * edit policies.
 */
List getSourceConnections();

/**
 * Returns the connections for which this EditPart is the <B>target</B>.
 * This method should only be called internally or by helpers such as
 * edit policies.
 */
List getTargetConnections();

/**
The figure into which children will be placed. Should not return <code>null</code>,
but may return the same figure as <code>getFigure()</code>. An example would be
a GraphicalEditPart that uses a 
<a href = "../../../../../../../org.eclipse.draw2d.doc.isv/reference/api/org/eclipse/draw2d/ScrollPane.html">
<code>ScrollPane</code></a>, which looks like this:
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><font size="4">&#9516;</font></td>
    <td colspan="3">ScrollPane - returned by <code><b>getFigure()</b></code></td>
  </tr>
  <tr>
    <td><font size="4">&#9492;</font></td>
    <td><font size="4">&#9516;</font></td>
    <td colspan="2">Viewport - (internal, does the scrolling)</td>
  </tr>
  <tr>
    <td></td>
    <td><font size="4">&#9492;</font></td>
    <td><font size="4">&#9472;</font></td>
    <td><b>View</b> - returned by <code><b>getContentPane()</b></code></td>
  </tr>
</table>
<p>A ScrollPane already has a special child, so the GraphicalEditPart's children
will get added to the <b>View</b></p>
 */
IFigure getContentPane();

/**
 * A child can call this method to set the layout constraint for its figure.
 */
void setLayoutConstraint(EditPart child, IFigure figure, Object constraint);

}
