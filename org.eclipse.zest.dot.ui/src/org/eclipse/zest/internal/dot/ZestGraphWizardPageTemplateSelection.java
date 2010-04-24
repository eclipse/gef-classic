/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.internal.dot;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotImport;

/**
 * This wizard page allows setting the container for the new Zest graph and the DOT input.
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestGraphWizardPageTemplateSelection extends WizardPage {
    // TODO externalize
    private static final String WIZARD_DESCRIPTION =
            "This wizard creates a new Zest graph, " + "based on a template. "
                    + "The DOT representation of the selected template can be customized on the next page.";
    private static final String NEW_ZEST_GRAPH = "New Zest Graph";
    private static final String EXTENSION_MUST_BE_JAVA = "File extension must be \"java\"";
    private static final String NAME_MUST_BE_VALID = "File name must be valid";
    private static final String NAME_MUST_BE_SPECIFIED = "File name must be specified";
    private static final String MUST_BE_WRITABLE = "Project must be writable";
    private static final String CONTAINER_MUST_EXIST = "File container must exist";
    private static final String CONTAINER_MUST_BE_SPECIFIED = "File container must be specified";
    private static final String FILE_CONTAINER = "Select new file container";
    private static final String JAVA = "java";
    private static final String TEMPLATE = "&Template:";
    private static final String BROWSE = "Browse...";
    private static final String CONTAINER = "&Container:";
    private static final String GRAPH_MUST_NOT_EXIST = "File already exists: ";
    private Text containerText;
    private ISelection selection;
    private Combo combo;
    private Composite composite;
    private Graph previewGraph;

    /**
     * Constructor for ZestGraphWizardPage.
     * @param selection The current selection
     * @param previewPage
     */
    public ZestGraphWizardPageTemplateSelection(final ISelection selection) {
        super("wizardPage");
        setTitle(NEW_ZEST_GRAPH);
        setDescription(WIZARD_DESCRIPTION);
        this.selection = selection;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(final Composite parent) {
        composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        createContainerRow(composite);
        createComboRow(composite);
        createPreviewRow(composite);
        validateSelection();
        validateContent();
        setControl(composite);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.DialogPage#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        composite.dispose();
        combo.dispose();
        containerText.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        return true; // we don't want to get stuck with invalid input on page 2
    }

    /**
     * Updates the preview graph based on the wizard's DOT content
     */
    void updatePreview() {
        if (previewGraph != null) {
            previewGraph.dispose();
        }
        if (composite != null) {
            previewGraph = new DotImport(getDotText()).newGraphInstance(composite, SWT.BORDER);
            setupLayout();
            composite.layout();
        }
    }

    /**
     * Validates the page's container selection and the selected DOT template, displays errors happening
     * during parsing of the DOT representation.
	 * @return True, if validation was successful
     */
    boolean validateContent() {
        IResource container =
                ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
        String fileName = getFileName();
        if (getContainerName().length() == 0) {
            updateStatus(CONTAINER_MUST_BE_SPECIFIED);
        } else if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
            updateStatus(CONTAINER_MUST_EXIST);
        } else if (!container.isAccessible()) {
            updateStatus(MUST_BE_WRITABLE);
        } else if (((IContainer) container).findMember(fileName) != null) {
            updateStatus(GRAPH_MUST_NOT_EXIST + fileName);
        } else if (fileName.length() == 0) {
            updateStatus(NAME_MUST_BE_SPECIFIED);
        } else if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
            updateStatus(NAME_MUST_BE_VALID);
        } else if (fileName.contains(".")
                && !fileName.substring(fileName.lastIndexOf('.') + 1).equalsIgnoreCase(JAVA)) {
            updateStatus(EXTENSION_MUST_BE_JAVA);
        } else {
            DotImport dotImport = new DotImport(getDotText());
            List<String> errors = dotImport.getErrors();
            if (errors.size() > 0) {
                updateStatus(errors.get(0));
            } else {
                updateStatus(null); 
                return true;
            }
        }
        return false;
    }

    /**
     * @return The text of the container field
     */
    String getContainerName() {
        return containerText.getText();
    }

    /**
     * @return The name of the file to create
     */
    String getFileName() {
        return new DotImport(getDotText()).getName() + "." + JAVA;
    }

    /**
     * @return The wizard's DOT text
     */
    String getDotText() {
        return ((ZestGraphWizard) getWizard()).getDotText();
    }

    /**
     * For testing purpose.
     * @return The name of the graph to generate
     */
    String getGraphName() {
        return combo.getItem(combo.getSelectionIndex());
    }

    /**
     * For testing purpose.
     * @param containerName The name of the container to use
     */
    void setContainerText(final String containerName) {
        containerText.setText(containerName);
    }

    private void createComboRow(final Composite composite) {
        Label label = new Label(composite, SWT.NULL);
        label.setText(TEMPLATE);
        combo = new Combo(composite, SWT.READ_ONLY);
        combo.setItems(ZestGraphTemplate.availableTemplateNames());
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                if (e.getSource() instanceof Combo) {
                    Combo combo = ((Combo) e.getSource());
                    updateWizard(combo);
                    updatePreview();
                    updateSecondPage();
                }
            }
            private void updateSecondPage() {
                ZestGraphWizardPageCustomize dotPage = getDotPage();
                if (dotPage != null) {
                    dotPage.updateTextFromWizard();
                }
            }
            private void updateWizard(final Combo combo) {
                ZestGraphWizard wizard = (ZestGraphWizard) getWizard();
                wizard
                        .setDotText((ZestGraphTemplate.availableTemplateContents()[(combo.getSelectionIndex())]));
            }
        });
        combo.select(0);
        label = new Label(composite, SWT.NULL);
        label.setText("");
    }

    private void createContainerRow(final Composite composite) {
        Label label = new Label(composite, SWT.NULL);
        label.setText(CONTAINER);
        containerText = new Text(composite, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        containerText.setLayoutData(gd);
        setContainerFromSelection();
        containerText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                validateContent();
            }
        });
        Button button = new Button(composite, SWT.PUSH);
        button.setText(BROWSE);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                handleBrowse();
            }
        });
    }

    private void createPreviewRow(final Composite composite) {
        Label label = new Label(composite, SWT.NULL);
        label.setText("&Preview:");
        previewGraph = new DotImport(getDotText()).newGraphInstance(composite, SWT.BORDER);
        setupLayout();
    }

    private void setupLayout() {
        if (previewGraph != null) {
            GridData gd = new GridData(GridData.FILL_BOTH);
            previewGraph.setLayout(new GridLayout());
            previewGraph.setLayoutData(gd);
        }
    }

    private ZestGraphWizardPageCustomize getDotPage() {
        ZestGraphWizardPageCustomize previewPage =
                (ZestGraphWizardPageCustomize) getWizard().getNextPage(this);
        return previewPage;
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    private void validateSelection() {
        if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1) {
                return;
            }
            Object obj = ssel.getFirstElement();
            if (obj instanceof IResource) {
                IContainer container;
                container = (obj instanceof IContainer) ? (IContainer) obj : ((IResource) obj).getParent();
                containerText.setText(container.getFullPath().toString());
            }
        }
    }

    private void setContainerFromSelection() {
        if (selection != null) {
            Object o = ((IStructuredSelection) selection).getFirstElement();
            if (o instanceof IPackageFragmentRoot) {
                containerText.setText(((IPackageFragmentRoot) o).getPath().toString());
            } else if (o instanceof IPackageFragment) {
                containerText.setText(((IPackageFragment) o).getPath().toString());
            } else if (o instanceof IResource) {
                containerText.setText(((IResource) o).getFullPath().toString());
            } else {
                containerText.setText("");
            }
        }
    }

    /**
     * Uses the standard container selection dialog to choose the new value for the container field.
     */
    private void handleBrowse() {
        ContainerSelectionDialog dialog =
                new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
                        FILE_CONTAINER);
        if (dialog.open() == ContainerSelectionDialog.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                containerText.setText(((Path) result[0]).toString());
            }
        }
    }

    private void updateStatus(final String message) {
        updatePageStatus(message, this);
        updatePageStatus(message, getDotPage());

    }

    private void updatePageStatus(final String message, final WizardPage page) {
        page.setErrorMessage(message);
        page.setPageComplete(message == null);
    }
}
