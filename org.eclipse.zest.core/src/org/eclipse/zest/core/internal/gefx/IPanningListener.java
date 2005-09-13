/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;

/**
 * An interface used for handling panning events.
 * 
 * @author Chris Callendar
 */
public interface IPanningListener {

    /**
     * Indicates that panning has started.    
     */
    public void panningStart();
    
    /**
     * Handles a change in position due to panning.  
     * @param dx	the change in x position
     * @param dy	the change in y position
     */
    public void panning(int dx, int dy);
    
    /**
     * Indicates that panning has ceased.  
     */
    public void panningEnd();
    
}
