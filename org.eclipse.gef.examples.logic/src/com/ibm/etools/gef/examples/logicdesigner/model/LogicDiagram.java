package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.swt.graphics.Image;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class LogicDiagram
	extends LogicSubpart
{
static final long serialVersionUID = 1;

private static int count;
private static Image LOGIC_ICON = new Image (null,
	LogicDiagram.class.getResourceAsStream("icons/circuit16.gif")); //$NON-NLS-1$

protected List children = new ArrayList();
public static String ID_ROUTER = "router";	//$NON-NLS-1$
public static Integer ROUTER_MANUAL = new Integer(0);
public static Integer ROUTER_MANHATTAN = new Integer(1);
protected Integer connectionRouter = null;

public LogicDiagram() {
	size.width = 100;
	size.height= 100;
	location.x = 20;
	location.y = 20;
}

public void addChild(LogicElement child){
	addChild(child, -1);
}

public void addChild(LogicElement child, int index){
	if (index >= 0)
		children.add(index,child);
	else
		children.add(child);
	fireStructureChange(CHILDREN, child);
}

public List getChildren(){
	return children;
}

public Integer getConnectionRouter(){
	if(connectionRouter==null)
		connectionRouter = ROUTER_MANUAL;
	return connectionRouter;
}

public Image getIconImage() {
	return LOGIC_ICON;
}

public String getNewID() {
	return Integer.toString(count++);
}

/**
 * Returns <code>null</code> for this model. Returns
 * normal descriptors for all subclasses.
 *
 * @return  Array of property descriptors.
 */
public IPropertyDescriptor[] getPropertyDescriptors() {
	if(getClass().equals(LogicDiagram.class)){
		ComboBoxPropertyDescriptor cbd = new ComboBoxPropertyDescriptor(
				ID_ROUTER, 
				LogicResources.getString("PropertyDescriptor.LogicDiagram.ConnectionRouter"), //$NON-NLS-1$
				new String[]{
					LogicResources.getString("PropertyDescriptor.LogicDiagram.Manual"),  //$NON-NLS-1$
					LogicResources.getString("PropertyDescriptor.LogicDiagram.Manhattan")});//$NON-NLS-1$
		cbd.setLabelProvider(new ConnectionRouterLabelProvider());
		return new IPropertyDescriptor[]{cbd};
	}
	return super.getPropertyDescriptors();
}

public Object getPropertyValue(Object propName) {
	if(propName.equals(ID_ROUTER))
		return connectionRouter;
	return super.getPropertyValue(propName);
}

public void removeChild(LogicElement child){
	children.remove(child);
	fireStructureChange(CHILDREN, child);
}

public void setConnectionRouter(Integer router){
	Integer oldConnectionRouter = connectionRouter;
	connectionRouter = router;
	firePropertyChange(ID_ROUTER, oldConnectionRouter, connectionRouter);
}

public void setPropertyValue(Object id, Object value){
	if(ID_ROUTER.equals(id))
		setConnectionRouter((Integer)value);
	else super.setPropertyValue(id,value);
}

public String toString(){
	return LogicResources.getString("LogicDiagram.LabelText"); //$NON-NLS-1$
}

private class ConnectionRouterLabelProvider 
	extends org.eclipse.jface.viewers.LabelProvider{

	public ConnectionRouterLabelProvider(){
		super();
	}
	public String getText(Object element){
		if(element instanceof Integer){
			Integer integer = (Integer)element;
			if(ROUTER_MANUAL.intValue()==integer.intValue())
				return LogicResources.getString("PropertyDescriptor.LogicDiagram.Manual");//$NON-NLS-1$
			if(ROUTER_MANHATTAN.intValue()==integer.intValue())
				return LogicResources.getString("PropertyDescriptor.LogicDiagram.Manhattan");//$NON-NLS-1$;
		}
		return super.getText(element);
	}

}

}
