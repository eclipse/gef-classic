/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal;

import java.util.*;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;

public interface Draw2dMessages {

static class Helper {
	public static String getString(String key) {
		IPluginDescriptor desc = 
			Platform.getPlugin("org.eclipse.draw2d").getDescriptor();//$NON-NLS-1$
		try {
			return desc.getResourceString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	public static String[] getStrings(String[] keys) {
		int length = keys.length;
		String[] result = new String[length];
		for (int i = 0; i < length; i++)
			result[i] = getString(keys[i]);
		return result;
	}
}

public String ERR_Figure_Add_Exception_OutOfBounds = Helper.getString("%Figure.Add.Exception.OutOfBounds_EXC_");				//$NON-NLS-1$
public String ERR_Figure_Add_Exception_IllegalArgument = Helper.getString("%Figure.Add.Exception.IllegalArgument_EXC_");		//$NON-NLS-1$
public String ERR_Figure_Remove_Exception_IllegalArgument = Helper.getString("%Figure.Remove.Exception.IllegalArgument_EXC_");	//$NON-NLS-1$
public String ERR_Figure_SetConstraint_Exception_IllegalArgument = Helper.getString("%Figure.SetConstraint.Exception.IllegalArgument_EXC_");	//$NON-NLS-1$
public String ERR_PointList_InsertPoint_Exception_IndexOutOfBounds = Helper.getString("%PointList.InsertPoint.Exception.IndexOutOfBounds_EXC_");	//$NON-NLS-1$
public String ERR_PointList_RemovePoint_Exception_IndexOutOfBounds = Helper.getString("%PointList.RemovePoint.Exception.IndexOutOfBounds_EXC_");	//$NON-NLS-1$
public String ERR_SWTEventDispatcher_SetControl_Exception_Runtime = Helper.getString("%SWTEventDispatcher.SetControl.Exception.Runtime_EXC_");	//$NON-NLS-1$

}