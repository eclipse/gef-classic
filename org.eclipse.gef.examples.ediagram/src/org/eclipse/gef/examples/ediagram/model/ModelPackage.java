/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackage.java,v 1.1 2004/11/11 06:03:50 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.gef.examples.ediagram.model.ModelFactory
 * @generated
 */
public interface ModelPackage extends EPackage{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = ""; //$NON-NLS-1$

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	String eNS_URI = "http:///gef.eDiagram";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	String eNS_PREFIX = "gef.eDiagram.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.NodeImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.DiagramImpl <em>Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.DiagramImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getDiagram()
	 * @generated
	 */
	int DIAGRAM = 0;

	/**
	 * The feature id for the '<em><b>Imports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__IMPORTS = 0;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__CONTENTS = 1;

	/**
	 * The number of structural features of the the '<em>Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.LinkImpl <em>Link</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.LinkImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getLink()
	 * @generated
	 */
	int LINK = 2;

	/**
	 * The feature id for the '<em><b>Source</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Bendpoints</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK__BENDPOINTS = 2;

	/**
	 * The number of structural features of the the '<em>Link</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.ReferenceViewImpl <em>Reference View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ReferenceViewImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getReferenceView()
	 * @generated
	 */
	int REFERENCE_VIEW = 1;

	/**
	 * The feature id for the '<em><b>Source</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW__SOURCE = LINK__SOURCE;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW__TARGET = LINK__TARGET;

	/**
	 * The feature id for the '<em><b>Bendpoints</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW__BENDPOINTS = LINK__BENDPOINTS;

	/**
	 * The feature id for the '<em><b>EReference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW__EREFERENCE = LINK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Opposite Shown</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW__OPPOSITE_SHOWN = LINK_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Reference View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_VIEW_FEATURE_COUNT = LINK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DIAGRAM = 1;

	/**
	 * The feature id for the '<em><b>Incoming Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__INCOMING_CONNECTIONS = 2;

	/**
	 * The feature id for the '<em><b>Outgoing Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__OUTGOING_CONNECTIONS = 3;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__WIDTH = 4;

	/**
	 * The number of structural features of the the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.NamedElementViewImpl <em>Named Element View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.NamedElementViewImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getNamedElementView()
	 * @generated
	 */
	int NAMED_ELEMENT_VIEW = 3;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__LOCATION = NODE__LOCATION;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__DIAGRAM = NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Incoming Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS = NODE__INCOMING_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Outgoing Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS = NODE__OUTGOING_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__WIDTH = NODE__WIDTH;

	/**
	 * The feature id for the '<em><b>ENamed Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW__ENAMED_ELEMENT = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Named Element View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_VIEW_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.StickyNoteImpl <em>Sticky Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.StickyNoteImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getStickyNote()
	 * @generated
	 */
	int STICKY_NOTE = 5;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__LOCATION = NODE__LOCATION;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__DIAGRAM = NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Incoming Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__INCOMING_CONNECTIONS = NODE__INCOMING_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Outgoing Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__OUTGOING_CONNECTIONS = NODE__OUTGOING_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__WIDTH = NODE__WIDTH;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE__TEXT = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Sticky Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STICKY_NOTE_FEATURE_COUNT = NODE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.gef.examples.ediagram.model.impl.InheritanceViewImpl <em>Inheritance View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.gef.examples.ediagram.model.impl.InheritanceViewImpl
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getInheritanceView()
	 * @generated
	 */
	int INHERITANCE_VIEW = 6;

	/**
	 * The feature id for the '<em><b>Source</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INHERITANCE_VIEW__SOURCE = LINK__SOURCE;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INHERITANCE_VIEW__TARGET = LINK__TARGET;

	/**
	 * The feature id for the '<em><b>Bendpoints</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INHERITANCE_VIEW__BENDPOINTS = LINK__BENDPOINTS;

	/**
	 * The number of structural features of the the '<em>Inheritance View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INHERITANCE_VIEW_FEATURE_COUNT = LINK_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '<em>Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.geometry.Point
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getPoint()
	 * @generated
	 */
	int POINT = 7;


	/**
	 * The meta object id for the '<em>Dimension</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.geometry.Dimension
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getDimension()
	 * @generated
	 */
	int DIMENSION = 8;


	/**
	 * The meta object id for the '<em>Absolute Bendpoint</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.AbsoluteBendpoint
	 * @see org.eclipse.gef.examples.ediagram.model.impl.ModelPackageImpl#getAbsoluteBendpoint()
	 * @generated
	 */
	int ABSOLUTE_BENDPOINT = 9;


	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Diagram
	 * @generated
	 */
	EClass getDiagram();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gef.examples.ediagram.model.Diagram#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Imports</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Diagram#getImports()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_Imports();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gef.examples.ediagram.model.Diagram#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Contents</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Diagram#getContents()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_Contents();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.ReferenceView <em>Reference View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference View</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.ReferenceView
	 * @generated
	 */
	EClass getReferenceView();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gef.examples.ediagram.model.ReferenceView#getEReference <em>EReference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>EReference</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.ReferenceView#getEReference()
	 * @see #getReferenceView()
	 * @generated
	 */
	EReference getReferenceView_EReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gef.examples.ediagram.model.ReferenceView#isOppositeShown <em>Opposite Shown</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Opposite Shown</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.ReferenceView#isOppositeShown()
	 * @see #getReferenceView()
	 * @generated
	 */
	EAttribute getReferenceView_OppositeShown();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.Link <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Link</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Link
	 * @generated
	 */
	EClass getLink();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.gef.examples.ediagram.model.Link#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Source</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Link#getSource()
	 * @see #getLink()
	 * @generated
	 */
	EReference getLink_Source();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gef.examples.ediagram.model.Link#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Link#getTarget()
	 * @see #getLink()
	 * @generated
	 */
	EReference getLink_Target();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.gef.examples.ediagram.model.Link#getBendpoints <em>Bendpoints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bendpoints</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Link#getBendpoints()
	 * @see #getLink()
	 * @generated
	 */
	EAttribute getLink_Bendpoints();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.NamedElementView <em>Named Element View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element View</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.NamedElementView
	 * @generated
	 */
	EClass getNamedElementView();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.gef.examples.ediagram.model.NamedElementView#getENamedElement <em>ENamed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>ENamed Element</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.NamedElementView#getENamedElement()
	 * @see #getNamedElementView()
	 * @generated
	 */
	EReference getNamedElementView_ENamedElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gef.examples.ediagram.model.Node#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node#getLocation()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Location();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.gef.examples.ediagram.model.Node#getDiagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Diagram</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node#getDiagram()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Diagram();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.gef.examples.ediagram.model.Node#getIncomingConnections <em>Incoming Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Incoming Connections</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node#getIncomingConnections()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_IncomingConnections();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.gef.examples.ediagram.model.Node#getOutgoingConnections <em>Outgoing Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Outgoing Connections</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node#getOutgoingConnections()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_OutgoingConnections();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gef.examples.ediagram.model.Node#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.Node#getWidth()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Width();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.StickyNote <em>Sticky Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sticky Note</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.StickyNote
	 * @generated
	 */
	EClass getStickyNote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.gef.examples.ediagram.model.StickyNote#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.StickyNote#getText()
	 * @see #getStickyNote()
	 * @generated
	 */
	EAttribute getStickyNote_Text();

	/**
	 * Returns the meta object for class '{@link org.eclipse.gef.examples.ediagram.model.InheritanceView <em>Inheritance View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inheritance View</em>'.
	 * @see org.eclipse.gef.examples.ediagram.model.InheritanceView
	 * @generated
	 */
	EClass getInheritanceView();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.geometry.Point <em>Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Point</em>'.
	 * @see org.eclipse.draw2d.geometry.Point
	 * @model instanceClass="org.eclipse.draw2d.geometry.Point"
	 * @generated
	 */
	EDataType getPoint();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.geometry.Dimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Dimension</em>'.
	 * @see org.eclipse.draw2d.geometry.Dimension
	 * @model instanceClass="org.eclipse.draw2d.geometry.Dimension"
	 * @generated
	 */
	EDataType getDimension();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.AbsoluteBendpoint <em>Absolute Bendpoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Absolute Bendpoint</em>'.
	 * @see org.eclipse.draw2d.AbsoluteBendpoint
	 * @model instanceClass="org.eclipse.draw2d.AbsoluteBendpoint"
	 * @generated
	 */
	EDataType getAbsoluteBendpoint();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

} //ModelPackage
