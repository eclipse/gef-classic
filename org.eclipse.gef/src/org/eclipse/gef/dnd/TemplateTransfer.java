package org.eclipse.gef.dnd;

/**
 * 
 * @author ebordeau
 * @since 2.1 */
public class TemplateTransfer
	extends SimpleObjectTransfer
{

private static final TemplateTransfer instance = new TemplateTransfer();
private static final String TYPE_NAME = "Template transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

private TemplateTransfer() { }

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
	return getObject();
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
 * Sets the <i>template</template> Object.
 * @param template the template */
public void setTemplate(Object template) {
	setObject(template);
}

}