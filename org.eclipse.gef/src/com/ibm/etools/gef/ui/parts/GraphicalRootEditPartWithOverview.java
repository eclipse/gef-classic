package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.parts.*;
import com.ibm.etools.gef.internal.GEFMessages;

public class GraphicalRootEditPartWithOverview 
	extends GraphicalRootEditPart 
{

protected IFigure createFigure(){
	LayeredPane topLayers = new LayeredPane();
	
	Layer scrollPaneLayer = new Layer();
	ScrollPane pane = (ScrollPane)super.createFigure();
	scrollPaneLayer.setLayoutManager( new StackLayout() );
	scrollPaneLayer.add( pane );
	topLayers.add( scrollPaneLayer );
	
	Layer dockLayer = new Layer();
	dockLayer.setLayoutManager( new DelegatingLayout() );
	ScrollableThumbnail overview = new ScrollableThumbnail( pane.getViewport() );
	overview.setSource( getLayer(CONNECTION_LAYER) );
	Dock dock = new Dock( pane.getViewport(), overview );
	dock.setTitle( GEFMessages.GraphicalRootEditPart_Overview_Title );
	dockLayer.add( dock, new DockLocator() );
	topLayers.add( dockLayer );
	
	return topLayers;
}

}