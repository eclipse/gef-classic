/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.dot;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This wizard page previews the Zest graph generated from the input in the
 * first page.
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestGraphWizardPageCustomize extends WizardPage {
    private Composite composite;
    private Text inputText;

    /**
     * Constructor for ZestGraphWizardPage.
     */
    public ZestGraphWizardPageCustomize() {
        super("wizardPage");
        setTitle("Zest Graph Customization");
        setDescription("Customize the DOT representation of the Zest graph to generate below. "
                + "To select a different template switch back to the first page.");
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.DialogPage#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        composite.dispose();
        inputText.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(final Composite parent) {
        composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 9;
        createTemplateRow(composite);
        setControl(composite);
    }

    /**
     * Updates the customization text field from the DOT text stored centrally
     * in the wizard
     */
    void updateTextFromWizard() {
        if (inputText != null) {
            inputText.setText(((ZestGraphWizard) getWizard()).getDotText());
        }
    }

    private void createTemplateRow(final Composite composite) {
        GridData gd = new GridData(GridData.FILL_BOTH);
        inputText = new Text(composite, SWT.BORDER | SWT.MULTI);
        inputText.setText("");
        inputText.setLayoutData(gd);
        inputText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                updateWizard();
                updateFistPage();
            }
            private void updateWizard() {
                ZestGraphWizard wizard = (ZestGraphWizard) getWizard();
                wizard.setDotText(inputText.getText());
            }
            private void updateFistPage() {
                ZestGraphWizardPageTemplateSelection page = getFirstPage();
                page.validateContent();
                page.updatePreview();
            }
        });
        String[] templates = ZestGraphTemplate.availableTemplateContents();
        inputText.setText(templates[0]);
        ZestGraphWizard wizard = (ZestGraphWizard) getWizard();
        if (wizard.getDotText() != null) {
            inputText.setText(wizard.getDotText());
        }
    }

    private ZestGraphWizardPageTemplateSelection getFirstPage() {
        ZestGraphWizardPageTemplateSelection previewPage = (ZestGraphWizardPageTemplateSelection) getWizard()
                .getPreviousPage(this);
        return previewPage;
    }
}
