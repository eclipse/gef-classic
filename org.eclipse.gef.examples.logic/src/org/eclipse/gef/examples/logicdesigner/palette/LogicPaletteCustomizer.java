package org.eclipse.gef.examples.logicdesigner.palette;

import org.eclipse.gef.palette.PaletteCategory;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteToolEntry;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gef.ui.palette.customize.ReadOnlyEntryPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Pratik Shah
 */
public class LogicPaletteCustomizer 
	extends PaletteCustomizer 
{
	
/**
 * Tools cannot be deleted
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canDelete(PaletteEntry)
 */
public boolean canDelete(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	}
	
	return super.canDelete(entry);
}

/**
 * Tools cannot be moved down
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canMoveDown(PaletteEntry)
 */
public boolean canMoveDown(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	} else if( entry instanceof PaletteGroup ){
		if( entry.getLabel().equals("Control Group") ){
			return false;
		}
	}
	
	return super.canMoveDown(entry);
}

/**
 * Tools cannot be moved up
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canMoveUp(PaletteEntry)
 */
public boolean canMoveUp(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	} else if( entry instanceof PaletteGroup ){
		if( entry.getLabel().equals("Control Group") ){
			return false;
		}
	}
	
	if( entry instanceof PaletteContainer && entry.getParent().getChildren().indexOf(entry) == 1){
		return false;
	} else if( entry.getParent().getChildren().indexOf(entry) == 0 && 
		entry.getParent().getParent().getChildren().indexOf(entry.getParent()) == 1 ){
		return false;
	}
	return super.canMoveUp(entry);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#getPropertiesPage(PaletteEntry)
 */
public EntryPage getPropertiesPage(PaletteEntry entry) {
	if( entry instanceof PaletteSeparator || entry instanceof PaletteToolEntry || 
	    entry instanceof PaletteGroup ){
		return new ReadOnlyEntryPage();
	} else if (entry instanceof PaletteCategory) {
		return new CategoryEntryPage();
	} else {
		return new LogicEntryPage();
	}
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#revertToSaved()
 */
public void revertToSaved() {
}


/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#dialogClosed(PaletteEntry)
 */
public void save() {
}

private class CategoryEntryPage extends LogicEntryPage {
	private Button b, pinOption;
	
	public void createControl(Composite parent, PaletteEntry entry) {
		super.createControl(parent, entry);
		
		b = new Button(panel, SWT.CHECK);
		b.setFont(panel.getFont());
		b.setText(PaletteMessages.EXPAND_LABEL);
		b.setSelection(getCategory().isInitiallyOpen());
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleExpandSelected(((Button)e.getSource()).getSelection());
			}
		});
		
		pinOption = new Button(panel, SWT.CHECK);
		pinOption.setFont(panel.getFont());
		pinOption.setText(PaletteMessages.CATEGORY_PIN);
		GridData data = new GridData();
		data.horizontalIndent = 15;
		pinOption.setLayoutData(data);
		pinOption.setEnabled(b.getSelection());
		pinOption.setSelection(getCategory().isInitiallyPinned());
		pinOption.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handlePinSelected(((Button)e.getSource()).getSelection());
			}
		});
	}
	
	private PaletteCategory getCategory(){
		return (PaletteCategory)entry;
	}
	
	private void handleExpandSelected(boolean selection) {
		int status = selection ? PaletteCategory.INITIAL_STATUS_EXPANDED
		                       : PaletteCategory.INITIAL_STATUS_COLLAPSED;
		getCategory().setInitialStatus(status);
		pinOption.setEnabled(selection);
		if (!selection) {
			pinOption.setSelection(false);
		}
	}
	
	private void handlePinSelected(boolean selection) {
		int status = selection ? PaletteCategory.INITIAL_STATUS_PINNED_OPEN
		                       : PaletteCategory.INITIAL_STATUS_EXPANDED;
		getCategory().setInitialStatus(status);
	}
}

}
