/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.test.performance;

import org.eclipse.test.performance.PerformanceTestCase;

public class BasePerformanceTestCase 
	extends PerformanceTestCase
{
	
public static final String OUTLINE_VIEW_ID = "org.eclipse.ui.views.ContentOutline"; //$NON-NLS-1$
public static final String RESOURCE_PERSPECTIVE_ID= "org.eclipse.ui.resourcePerspective"; //$NON-NLS-1$

protected int getWarmupRuns() {
	return 4;
}

protected int getMeasuredRuns() {
	return 4;
}

}