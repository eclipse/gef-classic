package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public class LogicResources{
	public static String getString(String key){
		org.eclipse.core.runtime.IPluginDescriptor desc = org.eclipse.core.runtime.Platform.getPluginRegistry().getPluginDescriptor("com.ibm.etools.gef.examples.logicdesigner");//$NON-NLS-1$
		try{
			return desc.getResourceString("%"+key);//$NON-NLS-1$
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