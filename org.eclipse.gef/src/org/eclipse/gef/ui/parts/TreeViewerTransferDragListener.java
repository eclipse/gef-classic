/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;

class TreeViewerTransferDragListener extends AbstractTransferDragSourceListener {

	private List<Object> modelSelection;

	public TreeViewerTransferDragListener(EditPartViewer viewer) {
		super(viewer, TreeViewerTransfer.getInstance());
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public TreeViewerTransferDragListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = getViewer().getSelectedEditParts();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		TreeViewerTransfer.getInstance().setViewer(getViewer());
		List<? extends EditPart> selection = getViewer().getSelectedEditParts();
		TreeViewerTransfer.getInstance().setObject(selection);
		saveModelSelection(selection);
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		TreeViewerTransfer.getInstance().setObject(null);
		TreeViewerTransfer.getInstance().setViewer(null);
		if (event.doit) {
			revertModelSelection();
		} else {
			modelSelection = null;
		}
	}

	protected void revertModelSelection() {
		List<EditPart> list = modelSelection.stream().map(m -> getViewer().getEditPartForModel(m)).toList();
		getViewer().setSelection(new StructuredSelection(list));
		modelSelection = null;
	}

	protected void saveModelSelection(List<? extends EditPart> editPartSelection) {
		modelSelection = editPartSelection.stream().map(EditPart::getModel).toList();
	}

}
