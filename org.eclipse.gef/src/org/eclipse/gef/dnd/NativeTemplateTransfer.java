package org.eclipse.gef.dnd;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * 
 * @author Eric Bordeau
 */
public class NativeTemplateTransfer extends ByteArrayTransfer {

private static final NativeTemplateTransfer instance = new NativeTemplateTransfer();
private static final String TYPE_NAME = "Native Template transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

private Object template;

/**
 * Constructor for NativeTemplateTransfer.
 */
private NativeTemplateTransfer() {
	super();
}

/**
 * Returns the singleton instance
 * @return the singleton
 */
public static NativeTemplateTransfer getInstance() {
	return instance;
}

/**
 * Returns the <i>template</i> object.
 * @return the template
 */
public Object getTemplate() {
	return template;
}

/**
 * @see Transfer#getTypeIds()
 */
protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

/**
 * @see Transfer#getTypeNames()
 */
protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

/**
 * @see org.eclipse.swt.dnd.Transfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
 */
protected void javaToNative(Object object, TransferData transferData) {
	try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(object);

		oos.close();
		baos.close();
		byte[] bytes = baos.toByteArray();
		super.javaToNative(bytes, transferData);
	} catch (IOException e) {
		//it's best to send nothing if there were problems
	}
}

/**
 * @see org.eclipse.swt.dnd.Transfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
 */
protected Object nativeToJava(TransferData transferData) {
	byte[] bytes = (byte[]) super.nativeToJava(transferData);
	if (bytes == null)
		return null;
	try {
		ObjectInputStream dis = new ObjectInputStream(new ByteArrayInputStream(bytes));
		Object object = dis.readObject();
		return object;
	} catch (Exception e) {
		return null;
	}
}

/**
 * Sets the <i>template</template> Object.
 * @param template the template
 */
public void setTemplate(Object template) {
	this.template = template;
}

}
