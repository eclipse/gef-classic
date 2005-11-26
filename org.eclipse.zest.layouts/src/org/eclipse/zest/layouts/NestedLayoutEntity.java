package org.eclipse.mylar.zest.layouts;


import java.util.List;

public interface NestedLayoutEntity extends LayoutEntity {

	public NestedLayoutEntity getParent();
	
	public List getChildren();
}
