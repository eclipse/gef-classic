package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.parts.*;
import org.eclipse.gef.internal.GEFMessages;

public class GraphicalRootEditPartWithOverview 
	extends GraphicalRootEditPart 
{

private Thumbnail thumbnail;

protected IFigure createFigure(){
	LayeredPane topLayers = new LayeredPane();
	
	Layer scrollPaneLayer = new Layer();
	ScrollPane pane = (ScrollPane)super.createFigure();
	scrollPaneLayer.setLayoutManager( new StackLayout() );
	scrollPaneLayer.add( pane );
	topLayers.add( scrollPaneLayer );
	
	Layer dockLayer = new Layer();
	dockLayer.setLayoutManager( new DelegatingLayout() );
	thumbnail = new ScrollableThumbnail( pane.getViewport() );
	thumbnail.setSource( getLayer(CONNECTION_LAYER) );
	Dock dock = new Dock( pane.getViewport(), thumbnail );
	dock.setTitle( GEFMessages.GraphicalRootEditPart_Overview_Title );
	dockLayer.add( dock, new DockLocator() );
	topLayers.add( dockLayer );
	
	return topLayers;
}

public void deactivate() {
	thumbnail.deactivate();
	super.deactivate();
}

}