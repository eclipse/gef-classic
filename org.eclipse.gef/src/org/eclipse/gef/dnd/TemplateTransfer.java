package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * 
 * @author ebordeau
 * @since 2.1 */
public class TemplateTransfer
	extends ByteArrayTransfer
{

private Object template;
private long startTime;
private static TemplateTransfer instance = new TemplateTransfer();

private static final String TYPE_NAME = "Template transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

/**
 * Constructs a new TemplateTransfer
 * @see java.lang.Object#Object() */
protected TemplateTransfer() { }

/**
 * Returns the singleton instance
 * @return the singleton
 */
public static TemplateTransfer getInstance() {
	return instance;
}

/**
 * Returns the <i>template</i> object.
 * @return the template */
public Object getTemplate() {
	return template;
}

/** * @see Transfer#getTypeIds() */
protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

/** * @see Transfer#getTypeNames() */
protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

/**
 * The template objects is not converted to bytes. It is held onto in a static field.
 * Instead, a checksum is written out to prevent unwanted drags across mulitple running
 * copies of Eclipse.
 * @see Transfer#javaToNative(Object, TransferData) */
public void javaToNative(Object object, TransferData transferData) {
	startTime = System.currentTimeMillis();
	if (transferData != null)
		super.javaToNative(String.valueOf(startTime).getBytes(), transferData);
}

/**
 * The template objects is not converted to bytes. It is held onto in a static field.
 * Instead, a checksum is written out to prevent unwanted drags across mulitple running.
 * copies of Eclipse.
 * @see Transfer#nativeToJava(TransferData)
 */
public Object nativeToJava(TransferData transferData) {
	byte bytes[] = (byte[])super.nativeToJava(transferData);
	long startTime = Long.parseLong(new String(bytes));
	return (this.startTime == startTime)
		? template
		: null;
}

/**
 * Sets the <i>template</template> Object.
 * @param template the template */
public void setTemplate(Object template) {
	this.template = template;
}

}