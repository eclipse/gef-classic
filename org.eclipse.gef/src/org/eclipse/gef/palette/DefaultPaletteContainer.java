package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import java.util.ArrayList;
import org.eclipse.swt.graphics.Image;

public class DefaultPaletteContainer 
	extends DefaultPaletteEntry
	implements PaletteContainer{
		
	protected List children = new ArrayList();
	
	public DefaultPaletteContainer(){}

	public DefaultPaletteContainer(String label){
		super(label);
	}
	
	public DefaultPaletteContainer(String label, Object type){
		super(label, null, type);
	}	
	
	public DefaultPaletteContainer(String label, List children, Object type){
		super(label, null, type);
		setChildren(children);
	}

	public DefaultPaletteContainer(String label, Object type, Image smallIcon, Image largeIcon){
		super(label,null,type,smallIcon,largeIcon);
	}
	
	public DefaultPaletteContainer(String label, List children, Object type, String description, Image smallIcon, Image largeIcon){
		super(label, description, smallIcon, largeIcon, type);
		setChildren(children);
	}
	
	public List getChildren(){
		return children;
	}
	
	public void setChildren(List children){
		this.children = children;
	}

	public String toString() {
		return "Palette Container (" + (getLabel() != null ? getLabel(): "") + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}
}