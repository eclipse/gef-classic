package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * This class is the ILabelProvider for the {@link org.eclipse.jface.viewers.TreeViewer}
 * used in {@link org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog}.
 * 
 * @author Pratik Shah
 */
class PaletteLabelProvider implements ILabelProvider {

private TreeViewer treeviewer;
private Image extraImg;
	
/**
 * Constructor
 * 
 * @param viewer	The TreeViewer for which this instance is a LabelProvider
 */
public PaletteLabelProvider(TreeViewer viewer) {
	treeviewer = viewer;
	//@TODO:Pratik
	// Change this to the folder icon from the workbench
	extraImg = new Image(treeviewer.getTree().getDisplay(),
	                     WorkbenchImages.getImageDescriptor(
	                         IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE_HOVER)
	                         .getImageData());
}

/**
 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(Object)
 */
public Image getImage(Object element) {
	PaletteEntry entry = (PaletteEntry)element;
	Image img = entry.getSmallIcon();
	if (img == null && (entry instanceof PaletteContainer)) {
		img = extraImg;
	}
	
	return img;
}

/**
 * @see org.eclipse.jface.viewers.ILabelProvider#getText(Object)
 */
public String getText(Object element) {
	PaletteEntry entry = (PaletteEntry)element;
	String text = entry.getLabel();
	if (!entry.isVisible()) {
		text = text + "**"; //$NON-NLS-1$
	}
	return text;
}

/**
 * Not implemented
 * 
 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(ILabelProviderListener)
 */
public void addListener(ILabelProviderListener listener) {
}

/**
 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
 */
public void dispose() {
	if (extraImg != null && !extraImg.isDisposed()) {
		extraImg.dispose();
	}
}

/**
 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(Object, String)
 */
public boolean isLabelProperty(Object element, String property) {
	return false;
}

/**
 * Not implemented
 * 
 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(ILabelProviderListener)
 */
public void removeListener(ILabelProviderListener listener) {
}

}
