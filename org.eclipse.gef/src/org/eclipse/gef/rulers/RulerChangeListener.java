/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.rulers;

/**
 * @author Pratik Shah
 */
public interface RulerChangeListener {
	
public void notifyUnitsChanged(int newUnit);
public void notifyGuideReparented(Object guide);
public void notifyGuideMoved(Object guide);
public void notifyPartAttachmentChanged(Object part, Object guide);

public class Stub implements RulerChangeListener {
	public void notifyUnitsChanged(int newUnit) {
	}
	public void notifyGuideReparented(Object guide) {
	}
	public void notifyGuideMoved(Object guide) {
	}
	public void notifyPartAttachmentChanged(Object part, Object guide) {
	}
}

}
