/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.commands;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class LinkCreationCommand extends Command {

	private Link link;
	private Node source, target;

	public LinkCreationCommand(Link newObj, Node src) {
		super("Link Creation");
		link = newObj;
		source = src;
	}

	public boolean canExecute() {
		boolean result = source != null && target != null && link != null;
		if (link instanceof InheritanceView) {
			result = result
					&& !((EClass) ((NamedElementView) source)
							.getENamedElement()).getESuperTypes().contains(
							((NamedElementView) target).getENamedElement());
		}
		return result;
	}

	public void execute() {
		if (link instanceof ReferenceView) {
			ReferenceView refView = (ReferenceView) link;
			EClass srcClass = (EClass) ((NamedElementView) source)
					.getENamedElement();
			EClass targetClass = (EClass) ((NamedElementView) target)
					.getENamedElement();
			if (refView.isOppositeShown()
					&& refView.getEReference().getEOpposite() != null) {
				EReference oppRef = refView.getEReference().getEOpposite();
				oppRef.setEType(srcClass);
				targetClass.getEStructuralFeatures().add(oppRef);
			}
			refView.getEReference().setEType(targetClass);
			srcClass.getEStructuralFeatures().add(refView.getEReference());
		} else if (link instanceof InheritanceView) {
			((EClass) ((NamedElementView) source).getENamedElement())
					.getESuperTypes().add(
							(EClass) ((NamedElementView) target)
									.getENamedElement());
		}
		if (source == target) {
			link.getBendpoints().add(
					new AbsoluteBendpoint(source.getLocation().getTranslated(
							-10, 10)));
			link.getBendpoints().add(
					new AbsoluteBendpoint(source.getLocation().getTranslated(
							-10, -10)));
			link.getBendpoints().add(
					new AbsoluteBendpoint(source.getLocation().getTranslated(
							10, -10)));
		}
		link.setSource(source);
		link.setTarget(target);
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public void undo() {
		link.setSource(null);
		link.setTarget(null);
		if (source == target) {
			link.getBendpoints().clear();
		}
		if (link instanceof ReferenceView) {
			ReferenceView refView = (ReferenceView) link;
			EClass srcClass = (EClass) ((NamedElementView) source)
					.getENamedElement();
			EClass targetClass = (EClass) ((NamedElementView) target)
					.getENamedElement();
			srcClass.getEStructuralFeatures().remove(refView.getEReference());
			refView.getEReference().setEType(null);
			if (refView.isOppositeShown()
					&& refView.getEReference().getEOpposite() != null) {
				EReference oppRef = refView.getEReference().getEOpposite();
				targetClass.getEStructuralFeatures().remove(oppRef);
				oppRef.setEType(null);
			}
		} else if (link instanceof InheritanceView) {
			((EClass) ((NamedElementView) source).getENamedElement())
					.getESuperTypes().remove(
							((NamedElementView) target).getENamedElement());
		}
	}

}
