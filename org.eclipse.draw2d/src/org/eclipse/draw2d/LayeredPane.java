package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * LayeredPane - Figure capable of holding any number of layers.
 * Only layers can be added to this figure. Layers are added
 * to this figure with thier respective keys, which are used to
 * identify them.
 */
public class LayeredPane
	extends Layer
{

private List layerKeys = new ArrayList();

/**
 * Constructs a new layered pane with no layers in it.
 */
public LayeredPane() {
	setLayoutManager(new StackLayout());
}

/**
 * Adds the given layer figure, identifiable with the given key,
 * at the specified index. While adding the layer, it informs the
 * surrounding layers of the addition.
 *
 * @param figure  Figure of the layer to be added.
 * @param layerKey  Key for identifying the layer.
 * @param index  Index where the layer should be added.
 * @since 2.0
 */
public void add(IFigure figure, Object layerKey, int index) {
	if (index == -1) index = layerKeys.size();
	super.add(figure, null, index);
	layerKeys.add(index, layerKey);
}

/**
 * Adds the given layer, identifiable with the given key, under the 
 * <it>after</it> layer provided in the input.
 *
 * @param layer  Layer to be added.
 * @param key  Key to identify the input layer.
 * @param after  Layer under which the input layer should be added.
 * @see  #addLayerBefore(Layer, Object, Object)
 * @since 2.0
 */
public void addLayerAfter(Layer layer, Object key, Object after) {
	int index = layerKeys.indexOf(after);
	add(layer, key, ++index);
}

/**
 * Adds the given layer, identifiable with the given key, above the
 * <it>before</it> layer provided in the input.
 *
 * @param layer  Layer to be added.
 * @param key  Key to identify the input layer.
 * @param after  Layer above which the input layer should be added.
 * @see  #addLayerAfter(Layer, Object, Object)
 * @since 2.0
 */
public void addLayerBefore(Layer layer, Object key, Object before) {
	int index = layerKeys.indexOf(before);
	add(layer, key, index);
}

/**
 * Returns the layer identified by the key given in the input.
 *
 * @param key  Key to identify the desired layer.
 * @return  The desired layer.
 * @see  #addLayerBefore(Layer, Object, Object)
 * @see  #addLayerAfter(Layer, Object, Object)
 * @see  #removeLayer(IFigure)
 * @see  #removeLayer(Object)
 * @since 2.0
 */
public Layer getLayer(Object key) {
	int index = layerKeys.indexOf(key);
	if (index == -1)
		return null;
	return (Layer)getChildren().get(index);
}

/**
 * Returns the layer at the specified index in this pane.
 *
 * @param index  Location of the desired layer.
 * @return  The layer desired.
 * @since 2.0
 */
protected Layer getLayer(int index) {
	return (Layer)getChildren().get(index);
}

/**
 * Returns the opaque state of this pane containing layers.
 * 
 * @return  Returns <code>true</code> if any of the child layers
 *          is opaque, else returns <code>false</code>.
 * @since 2.0
 */
public boolean isOpaque() {
	if (super.isOpaque())
		return true;
	return false;
}

/**
 * Removes the layer identified by the given key from this 
 * layerepane.
 *
 * @param key  Key identifying the layer.
 * @since 2.0
 */
public void removeLayer(Object key) {
	removeLayer(layerKeys.indexOf(key));
}

/**
 * Removes the given layer from the layers in this figure.
 *
 * @deprecated call remove(IFigure)
 * @param layer  Layer to be removed.
 * @see  #removeLayer(Object)
 * @since 2.0
 */
public void removeLayer(IFigure layer) {
	remove(layer);
}

/**
 * Removes the layer at the specified index from the list of 
 * layers in this layered pane. It collapses the layers, occupying
 * the space vacated by the removed layer.
 *
 * @param index  Index of the layer to be removed.
 * @see  #removeLayer(IFigure)
 * @since 2.0
 */
protected void removeLayer(int index) {
	Layer removeLayer = getLayer(index);
	remove(removeLayer);
	layerKeys.remove(index);	 	
}

}