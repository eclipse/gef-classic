/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Plugin class used by the flow editor.
 */
public class FlowPlugin extends AbstractUIPlugin {
	
/**
 * Creates a new FlowPlugin with the given descriptor
 * @param descriptor the descriptor
 */
public FlowPlugin(IPluginDescriptor descriptor) {
	super(descriptor);
}

}
