package com.ibm.etools.draw2d.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public interface Draw2dMessages {

static class Helper{
	public static String getString(String key){
		org.eclipse.core.runtime.IPluginDescriptor desc = org.eclipse.core.runtime.Platform.getPlugin("com.ibm.etools.draw2d").getDescriptor();//$NON-NLS-1$
		try{
			return desc.getResourceString(key);
		}catch(MissingResourceException e){
			return key;
		}
	}
	
	public static String[] getStrings(String[] keys){
		int length = keys.length;
		String[] result = new String[length];
		for(int i=0;i<length;i++)
			result[i] = getString(keys[i]);
		return result;
	}
}


public String ERR_Figure_Add_Exception_OutOfBounds=Helper.getString("%Figure.Add.Exception.OutOfBounds_EXC_");				//$NON-NLS-1$
public String ERR_Figure_Add_Exception_IllegalArgument=Helper.getString("%Figure.Add.Exception.IllegalArgument_EXC_");		//$NON-NLS-1$
public String ERR_Figure_Remove_Exception_IllegalArgument=Helper.getString("%Figure.Remove.Exception.IllegalArgument_EXC_");	//$NON-NLS-1$
public String ERR_Figure_SetConstraint_Exception_IllegalArgument=Helper.getString("%Figure.SetConstraint.Exception.IllegalArgument_EXC_");	//$NON-NLS-1$
public String ERR_PointList_InsertPoint_Exception_IndexOutOfBounds=Helper.getString("%PointList.InsertPoint.Exception.IndexOutOfBounds_EXC_");	//$NON-NLS-1$
public String ERR_PointList_RemovePoint_Exception_IndexOutOfBounds=Helper.getString("%PointList.RemovePoint.Exception.IndexOutOfBounds_EXC_");	//$NON-NLS-1$
public String ERR_SWTEventDispatcher_SetControl_Exception_Runtime=Helper.getString("%SWTEventDispatcher.SetControl.Exception.Runtime_EXC_");	//$NON-NLS-1$

}