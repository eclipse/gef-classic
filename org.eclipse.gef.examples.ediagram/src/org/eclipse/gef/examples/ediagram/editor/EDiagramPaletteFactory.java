/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.editor;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.ediagram.EDiagramImages;
import org.eclipse.gef.examples.ediagram.model.ModelFactory;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * @author Pratik Shah
 */
public class EDiagramPaletteFactory {

public static final PaletteRoot createPalette() {
	PaletteRoot root = new PaletteRoot();
	PaletteGroup group = new PaletteGroup("Control Group");
	PaletteDrawer drawer = new PaletteDrawer("UML");
	
	ToolEntry selectionTool = new PanningSelectionToolEntry();
	group.add(selectionTool);
	root.setDefaultEntry(selectionTool);
	
	group.add(new MarqueeToolEntry());
	
	group.add(new PaletteSeparator());

	ImageDescriptor img = EDiagramImages.getImageDescriptor(EDiagramImages.REFERENCE);
	group.add(new ConnectionCreationToolEntry("Reference", 
			"Create a reference between two classes in the eDiagram",
			new CreationFactory() {
				public Object getNewObject() {
					ReferenceView refView = ModelFactory.eINSTANCE.createReferenceView();
					refView.setEReference(EcoreFactory.eINSTANCE.createEReference());
					return refView;
				}
				public Object getObjectType() {
					return null;
				}
			}, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.TWO_WAY_REFERENCE);
	group.add(new ConnectionCreationToolEntry("Two-way Reference",
			"Create opposite references",
			new CreationFactory() {
				public Object getNewObject() {
					EReference origRef = EcoreFactory.eINSTANCE.createEReference();
					EReference oppRef = EcoreFactory.eINSTANCE.createEReference();
					origRef.setEOpposite(oppRef);
					oppRef.setEOpposite(origRef);
					ReferenceView refView = ModelFactory.eINSTANCE.createReferenceView();
					refView.setEReference(origRef);
					refView.setOppositeShown(true);
					return refView;
				}
				public Object getObjectType() {
					return null;
				}
			}, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.INHERITANCE);
	group.add(new ConnectionCreationToolEntry("Inheritance", 
			"Create a hierarchy",
			new CreationFactory() {
				public Object getNewObject() {
					return ModelFactory.eINSTANCE.createInheritanceView();
				}
				public Object getObjectType() {
					return null;
				}
			}, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.CONNECTION);
	group.add(new ConnectionCreationToolEntry("Note Link", 
			"Associate a note/comment with another node", 
			new CreationFactory() {
				public Object getNewObject() {
					return ModelFactory.eINSTANCE.createLink();
				}
				public Object getObjectType() {
					return null;
				}
			}, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.PACKAGE);
	CreationFactory factory = new CreationFactory() {
		public Object getNewObject() {
			NamedElementView packageView = 
					ModelFactory.eINSTANCE.createNamedElementView();
			packageView.setENamedElement(EcoreFactory.eINSTANCE.createEPackage());
			return packageView;
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Package", "Create a new package or " +
			"sub-package", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.CLASS);
	factory = new CreationFactory() {
		public Object getNewObject() {
			NamedElementView classView = 
					ModelFactory.eINSTANCE.createNamedElementView();
			classView.setENamedElement(EcoreFactory.eINSTANCE.createEClass());
			return classView;
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Class", 
			"Create a new class or interface", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.DATATYPE);
	factory = new CreationFactory() {
		public Object getNewObject() {
			NamedElementView dataTypeView = 
					ModelFactory.eINSTANCE.createNamedElementView();
			dataTypeView.setENamedElement(EcoreFactory.eINSTANCE.createEDataType());
			return dataTypeView;
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("DataType", 
			"Define a new EDataType", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.ENUM);
	factory = new CreationFactory() {
		public Object getNewObject() {
			NamedElementView dataTypeView = 
					ModelFactory.eINSTANCE.createNamedElementView();
			dataTypeView.setENamedElement(EcoreFactory.eINSTANCE.createEEnum());
			return dataTypeView;
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Enum", 
			"Create a new Enum type", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.STICKY_NOTE);
	factory = new CreationFactory() {
		public Object getNewObject() {
			return ModelFactory.eINSTANCE.createStickyNote();
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Sticky Note", 
			"Can be used to add comments and other notes to the diagram", factory,
			factory, img, img));
	
	drawer.add(new PaletteSeparator());
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.ATTRIBUTE);
	factory = new CreationFactory() {
		public Object getNewObject() {
			return EcoreFactory.eINSTANCE.createEAttribute();
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Attribute", 
			"Add a new attribute to an existing class", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.OPERATION);
	factory = new CreationFactory() {
		public Object getNewObject() {
			return EcoreFactory.eINSTANCE.createEOperation();
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Operation", 
			"Add a new operation to an existing class", factory, factory, img, img));
	
	img = EDiagramImages.getImageDescriptor(EDiagramImages.ENUM_LITERAL);
	factory = new CreationFactory() {
		public Object getNewObject() {
			return EcoreFactory.eINSTANCE.createEEnumLiteral();
		}
		public Object getObjectType() {
			return null;
		}
	};
	drawer.add(new CombinedTemplateCreationEntry("Enum Literal", 
			"Add a new value to an Enum type", factory, factory, img, img));
	
	root.add(group);
	root.add(drawer);
	return root;
}
	
}
