/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.util.HashMap;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * Base class that can be used for model factories. Offers facilities to 
 * style the items that have been created by the factory.
 * @author Del Myers
 *
 */
public abstract class AbstractStylingModelFactory {
	/**
	 * 
	 * Class to keep track of the number of connections there are between two nodes.
	 * @author Del Myers
	 *
	 */
	//@tag bug(114452-MultipleArcs) : resolution
	class ConnectionCounter {
		Object source;
		Object dest;
		public ConnectionCounter(Object source, Object dest) {
			this.source = source;
			this.dest = dest;
		}
		public boolean equals(Object that) {
			return (
				this.source.equals(((ConnectionCounter)that).source) &&
				this.dest.equals(((ConnectionCounter)that).dest)
			);
		}
		public int hashCode() {
			return 0;
		}
	}
	
	
	/** Counts the number of connections between two nodes **/
	private HashMap counters;
	private Object input;
	
	/**
	 * 
	 */
	public AbstractStylingModelFactory() {
		this.counters = new HashMap();
	}
	
//	@tag bug(154412-ClearStatic(fix)) : the styling factory needs to clear the counters if the input is new.
	public final GraphModel createModelFromContentProvider(Object input, int nodeStyle, int connectionStyle) {
		if (input != getInput()) {
			counters.clear();
//			@tag bug(154412-ClearStatic(fix)) : save the input so that it can be checked to see whether or not it is new.
			this.input = input;
		}
		return doCreateModelFromContentProvider(input, nodeStyle, connectionStyle);
	}
	
	
	/**
	 * 
	 * @return the input for this factory.
	 */
	public final Object getInput() {
		return this.input;
	}

	protected abstract GraphModel doCreateModelFromContentProvider(Object input, int nodeStyle, int connectionStyle);

	public void styleConnection(IGraphModelConnection conn) {
		ConnectionCounter key = new ConnectionCounter(
				conn.getSource().getExternalNode(),
				conn.getDestination().getExternalNode()
		);
		Integer count = (Integer) counters.get(key);
		if (count == null) {
			count = new Integer(1);
			counters.put(key, count);
		} else {
			count = new Integer(count.intValue() + 1);
			counters.put(key, count);
		}
		int scale = 3;
		if (conn.getSource() == conn.getDestination()) {
			scale = 5;
		}
		//even if the connection isn't curved in the style, the edit part
		//may decide that it should be curved if source and dest are equal.
		//@tag drawing(arcs) : check here if arcs are too close when being drawn. Adjust the constant.
		conn.setCurveDepth(count.intValue()*(scale+conn.getLineWidth()));

//		@tag bug(152530-Bezier(fix)) : set the angles, etc based on the count.
		//limit the angle to 90 degrees.
		conn.setStartAngle(90.0 - 85.0/Math.pow(count.doubleValue(), 1.0/9.0));
		conn.setEndAngle(85.0/Math.pow(count.doubleValue(), 1.0/9.0) - 90.0);
		//limit the length to 1
		conn.setStartLength(.75 - .25/(Math.sqrt(count.doubleValue())));
		conn.setEndLength(.75 - .25/(Math.sqrt(count.doubleValue())));
		//allow the item styler to override the style.
		GraphItemStyler.styleItem(conn, getLabelProvider());
	}
	
	public void styleItem(IGraphItem item) {
		if (item instanceof IGraphModelConnection) styleConnection((IGraphModelConnection) item);
		GraphItemStyler.styleItem(item, getLabelProvider());
	}

	protected abstract ILabelProvider getLabelProvider();
	
}
