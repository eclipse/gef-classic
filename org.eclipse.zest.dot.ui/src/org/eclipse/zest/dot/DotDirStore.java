/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Store and access the path to the 'dot' executable in the preference store. The path can be set by the user,
 * using a directory selection dialog. The selected location is stored in the bundle's preferences and
 * available from there after the initial setting.
 * @author Fabian Steeg (fsteeg)
 */
final class DotDirStore {

    private DotDirStore() {/* Enforce non-instantiability */}

    static final String DOTPATH_KEY = DotUiActivator.PLUGIN_ID + ".dotpath";

    // TODO externalize
    private enum Caption {
        DOT_SELECT_SHORT("Where is 'dot'?"), /***/
        DOT_SELECT_LONG("Please specify the folder that contains 'dot' (http://www.graphviz.org/)"), /***/
        NOT_FOUND_LONG("The application 'dot' was not found in the specified directory"), /***/
        NOT_FOUND_SHORT("Not found");
        private String s;

        Caption(final String s) {
            this.s = s;
        }
    }

    /** @return The path to the folder containing the local 'dot' executable */
    static String getDotDirPath() {
        if (dotPathFromPreferences().length() == 0) {
            setDotDirPath(); // set the preferences
        }
        return dotPathFromPreferences();
    }

    /** Sets the path to the local 'dot' executable based on user selection. */
    static void setDotDirPath() {
        IWorkbenchWindow parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
        dialog.setMessage(Caption.DOT_SELECT_LONG.s);
        dialog.setText(Caption.DOT_SELECT_SHORT.s);
        processUserInput(parent, dialog);
    }

    private static void processUserInput(final IWorkbenchWindow parent, final DirectoryDialog dialog) {
        String selectedPath = dialog.open();
        if (selectedPath != null) {
            if (!containsDot(new File(selectedPath))) {
                MessageDialog.openError(parent.getShell(), Caption.NOT_FOUND_SHORT.s,
                        Caption.NOT_FOUND_LONG.s);
            } else {
                DotUiActivator.getDefault().getPreferenceStore().setValue(DOTPATH_KEY,
                        selectedPath + File.separator);
            }
        }
    }

    private static boolean containsDot(final File folder) {
        String[] files = folder.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].equals("dot") || files[i].equals("dot.exe")) {
                return true;
            }
        }
        return false;
    }

    private static String dotPathFromPreferences() {
        return DotUiActivator.getDefault().getPreferenceStore().getString(DOTPATH_KEY);
    }
}
