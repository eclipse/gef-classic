package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class TemplateTransfer extends ByteArrayTransfer {

private Object template;
private long startTime;
private static TemplateTransfer instance = new TemplateTransfer();

private static final String TYPE_NAME = "Template transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

protected TemplateTransfer(){}

public static TemplateTransfer getInstance() {
	return instance;
}

public Object getTemplate() {
	return template;
}

protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

public void javaToNative(Object object, TransferData transferData) {
	startTime = System.currentTimeMillis();
	if (transferData != null)
		super.javaToNative(String.valueOf(startTime).getBytes(), transferData);
}

/**
 * This decodes the time of the transfer and returns the recorded the object 
 * if the recorded time and the decoded time match.
 */
public Object nativeToJava(TransferData transferData) {
	byte bytes[] = (byte[])super.nativeToJava(transferData);
	long startTime = Long.parseLong(new String(bytes));
	return this.startTime == startTime ? template : null;
}

public void setTemplate(Object template) {
	this.template = template;
}

}