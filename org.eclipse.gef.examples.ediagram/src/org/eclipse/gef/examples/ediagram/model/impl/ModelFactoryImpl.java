/**
 * <copyright>
 *******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************
 * </copyright>
 *
 * $Id: ModelFactoryImpl.java,v 1.2 2004/12/07 19:07:09 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.impl;

import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.ModelFactory;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.StickyNote;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = ""; //$NON-NLS-1$

	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case ModelPackage.DIAGRAM: return createDiagram();
			case ModelPackage.REFERENCE_VIEW: return createReferenceView();
			case ModelPackage.LINK: return createLink();
			case ModelPackage.NAMED_ELEMENT_VIEW: return createNamedElementView();
			case ModelPackage.STICKY_NOTE: return createStickyNote();
			case ModelPackage.INHERITANCE_VIEW: return createInheritanceView();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue)
	{
		switch (eDataType.getClassifierID())
		{
			case ModelPackage.POINT:
				return createPointFromString(eDataType, initialValue);
			case ModelPackage.DIMENSION:
				return createDimensionFromString(eDataType, initialValue);
			case ModelPackage.ABSOLUTE_BENDPOINT:
				return createAbsoluteBendpointFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue)
	{
		switch (eDataType.getClassifierID())
		{
			case ModelPackage.POINT:
				return convertPointToString(eDataType, instanceValue);
			case ModelPackage.DIMENSION:
				return convertDimensionToString(eDataType, instanceValue);
			case ModelPackage.ABSOLUTE_BENDPOINT:
				return convertAbsoluteBendpointToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diagram createDiagram()
	{
		DiagramImpl diagram = new DiagramImpl();
		return diagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceView createReferenceView()
	{
		ReferenceViewImpl referenceView = new ReferenceViewImpl();
		return referenceView;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Link createLink()
	{
		LinkImpl link = new LinkImpl();
		return link;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedElementView createNamedElementView()
	{
		NamedElementViewImpl namedElementView = new NamedElementViewImpl();
		return namedElementView;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StickyNote createStickyNote()
	{
		StickyNoteImpl stickyNote = new StickyNoteImpl();
		return stickyNote;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InheritanceView createInheritanceView()
	{
		InheritanceViewImpl inheritanceView = new InheritanceViewImpl();
		return inheritanceView;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public Point createPointFromString(EDataType eDataType, String initialValue)
	{
		if (initialValue == null || initialValue.length() == 0)
			return new Point();
		
		initialValue = initialValue.trim();
		StringTokenizer tokenizer = new StringTokenizer(initialValue, ","); //$NON-NLS-1$
		int x = 0, y = 0;
		try {
			x = new Integer(tokenizer.nextToken().trim()).intValue();
			y = new Integer(tokenizer.nextToken().trim()).intValue();
		} catch (NumberFormatException nfe) {}
		return new Point(x, y);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String convertPointToString(EDataType eDataType, Object instanceValue)
	{
		if (instanceValue == null)
			return "0,0";
		Point p = (Point)instanceValue;
		return p.x + "," + p.y;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public Dimension createDimensionFromString(EDataType eDataType, String initialValue)
	{
		Point p = createPointFromString(null, initialValue);
		return new Dimension(p.x, p.y);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String convertDimensionToString(EDataType eDataType, Object instanceValue)
	{
		Dimension d = (Dimension)instanceValue;
		return convertPointToString(null, new Point(d.width, d.height));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public AbsoluteBendpoint createAbsoluteBendpointFromString(EDataType eDataType, String initialValue)
	{
		return new AbsoluteBendpoint(createPointFromString(null, initialValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public String convertAbsoluteBendpointToString(EDataType eDataType, Object instanceValue)
	{
		return convertPointToString(null, ((Bendpoint)instanceValue).getLocation());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPackage getModelPackage()
	{
		return (ModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static ModelPackage getPackage()
	{
		return ModelPackage.eINSTANCE;
	}

} //ModelFactoryImpl
