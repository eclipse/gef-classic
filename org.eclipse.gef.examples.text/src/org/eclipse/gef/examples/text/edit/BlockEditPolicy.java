/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.jface.util.Assert;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.Block;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.InlineContainer;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.model.commands.ApplyBooleanStyle;
import org.eclipse.gef.examples.text.model.commands.CompoundEditCommand;
import org.eclipse.gef.examples.text.model.commands.ConvertElementCommand;
import org.eclipse.gef.examples.text.model.commands.InsertString;
import org.eclipse.gef.examples.text.model.commands.MergeWithPrevious;
import org.eclipse.gef.examples.text.model.commands.MiniEdit;
import org.eclipse.gef.examples.text.model.commands.NestElementCommand;
import org.eclipse.gef.examples.text.model.commands.ProcessMacroCommand;
import org.eclipse.gef.examples.text.model.commands.PromoteElementCommand;
import org.eclipse.gef.examples.text.model.commands.RemoveRange;
import org.eclipse.gef.examples.text.model.commands.RemoveText;
import org.eclipse.gef.examples.text.model.commands.SingleEditCommand;
import org.eclipse.gef.examples.text.model.commands.SubdivideElement;
import org.eclipse.gef.examples.text.requests.TextRequest;

/**
 * @since 3.1
 */
public class BlockEditPolicy extends GraphicalEditPolicy {

/**
 * @since 3.1
 * @param location
 * @return
 */
private Command checkForConversion(TextLocation location) {
	TextRun run = (TextRun)location.part.getModel();
	String prefix = run.getText().substring(0, location.offset);
	if (prefix.endsWith("<b>")) {
		Container converted = new InlineContainer(Container.TYPE_INLINE);
		converted.getStyle().setBold(true);
		TextRun boldText = new TextRun("BOLD");
		converted.add(boldText);
		ProcessMacroCommand command = new ProcessMacroCommand(run, location.offset - 3,
				location.offset, converted, new ModelLocation(boldText, 0));
		command.setEndLocation(new ModelLocation(boldText, 1));
		return command;
	} else if (prefix.equals("()")) {
		ConvertElementCommand command;
		Container list = new Block(Container.TYPE_BULLETED_LIST);
		TextRun bullet = new TextRun("", TextRun.TYPE_BULLET);
		list.add(bullet);
		command = new ConvertElementCommand(run, 0, 2, list, new ModelLocation(bullet, 0));
		return command;
	} else if (prefix.equals("import")) {
		ConvertElementCommand command;
		Container imports = new Block(Container.TYPE_IMPORT_DECLARATIONS);
		TextRun statement = new TextRun("", TextRun.TYPE_IMPORT);
		imports.add(statement);
		command = new ConvertElementCommand(run, 0, 6, imports, new ModelLocation(statement,
				0));
		return command;
	}
	return null;
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getBackspaceCommand(TextRequest request) {
	TextLocation where = request.getSelectionRange().begin;
	
	CompoundEditCommand command = (CompoundEditCommand)request.getPreviousCommand();
	if (command == null)
		command = new CompoundEditCommand("Backspace");

	TextRun run = (TextRun)where.part.getModel();
	MiniEdit remove;

	if (where.offset == 0) {
		remove = getMergeBackspaceEdit(request);
		command.setBeginLocation(new ModelLocation(run, where.offset));
		command.setEndLocation(new ModelLocation(run, where.offset));
	} else {
		remove = new RemoveText(run, where.offset - 1, where.offset);
		command.setBeginLocation(new ModelLocation(run, where.offset - 1));
		command.setEndLocation(new ModelLocation(run, where.offset));
	}
	
	command.pendEdit(remove);
	return command;
}

public Command getCommand(Request request) {
	if (TextRequest.REQ_STYLE == request.getType())
		return getTextStyleApplication((TextRequest)request);
	if (TextRequest.REQ_INSERT == request.getType())
		return getTextInsertionCommand((TextRequest)request);
	if (TextRequest.REQ_BACKSPACE == request.getType())
		return getBackspaceCommand((TextRequest)request);
	if (TextRequest.REQ_DELETE == request.getType())
		return getDeleteCommand((TextRequest)request);
	if (TextRequest.REQ_REMOVE_RANGE == request.getType())
		return getRangeRemovalCommand((TextRequest)request);
	if (TextRequest.REQ_NEWLINE == request.getType())
		return getNewlineCommand((TextRequest)request);
	if (TextRequest.REQ_UNINDENT == request.getType())
		return getUnindentCommand((TextRequest)request);
	if (TextRequest.REQ_INDENT == request.getType())
		return getIndentCommand((TextRequest)request);
	return null;
}

/**
 * @param request
 * @return
 * @since 3.1
 */
private Command getTextStyleApplication(TextRequest request) {
	SelectionRange range = request.getSelectionRange();
	if (range.isEmpty())
		return null;
	
	ModelLocation start = new ModelLocation((TextRun)range.begin.part.getModel(), range.begin.offset);
	ModelLocation end = new ModelLocation((TextRun)range.end.part.getModel(), range.end.offset);
	ApplyBooleanStyle style = new ApplyBooleanStyle(start, end,
			request.getStyleKeys(), request.getStyleValues());
	
	return new SingleEditCommand(style, start, end);
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getIndentCommand(TextRequest request) {
	SelectionRange range = request.getSelectionRange();
	return new NestElementCommand(range.begin.part, range.begin.offset);
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getUnindentCommand(TextRequest request) {
	SelectionRange range = request.getSelectionRange();
	return new PromoteElementCommand(range.begin.part, range.begin.offset);
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getDeleteCommand(TextRequest request) {
	TextLocation where = request.getSelectionRange().begin;
	if (where.offset == where.part.getLength())
		return null;
	TextRun run = (TextRun)where.part.getModel();
	MiniEdit remove = new RemoveText(run, where.offset, where.offset + 1);
	CompoundEditCommand command = (CompoundEditCommand)request.getPreviousCommand();
	if (command == null) {
		command = new CompoundEditCommand("Delete");
		command.setBeginLocation(new ModelLocation(run, where.offset));
		command.setEndLocation(new ModelLocation(run, where.offset + 1));
	}
	command.pendEdit(remove);
	return command;
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private MiniEdit getMergeBackspaceEdit(TextRequest request) {
	TextualEditPart part = request.getSelectionRange().begin.part;
	MergeWithPrevious edit = new MergeWithPrevious(part);
	if (edit.canApply())
		return edit;
	return null;
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getNewlineCommand(TextRequest request) {
	TextLocation where = request.getSelectionRange().end;
	TextRun run = (TextRun)where.part.getModel();
	SubdivideElement edit = new SubdivideElement(run, where.offset);

	CompoundEditCommand command = null;
	if (request.getPreviousCommand() instanceof CompoundEditCommand)
		command = (CompoundEditCommand)request.getPreviousCommand();
	else
		command = new CompoundEditCommand("typing");
	command.pendEdit(edit);
	return command;
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getRangeRemovalCommand(TextRequest request) {
	return getTextInsertionCommand(request);
}

public EditPart getTargetEditPart(Request request) {
	if (request instanceof TextRequest)
		return getHost();
	return null;
}

/**
 * @since 3.1
 * @param request
 * @return
 */
private Command getTextInsertionCommand(TextRequest request) {
	CompoundEditCommand command = null;
	if (request.getPreviousCommand() instanceof CompoundEditCommand)
		command = (CompoundEditCommand)request.getPreviousCommand();

	SelectionRange range = request.getSelectionRange();

	if (range.isEmpty() && request.getText().equals(" ")) {
		Command result = checkForConversion(request.getSelectionRange().begin);
		if (result != null)
			return result;
	}
	TextRun rangeBegin = (TextRun)range.begin.part.getModel();
	if (command == null) {
		TextRun rangeEnd = (TextRun)range.end.part.getModel();
		command = new CompoundEditCommand("typing");
		command.setBeginLocation(new ModelLocation(rangeBegin, range.begin.offset));

		if (!range.isEmpty()) {
			RemoveRange remove;
			command.setEndLocation(new ModelLocation(rangeEnd, range.end.offset));
			remove = new RemoveRange(rangeBegin, range.begin.offset, rangeEnd,
					range.end.offset);
			command.pendEdit(remove);
		}
	} else {
		//The range should be empty any time there is a previous command
		Assert.isTrue(range.isEmpty());
	}
	
	if (request.getText() != null) {
		InsertString insert = new InsertString(rangeBegin, request.getText(), range.begin.offset);
		command.pendEdit(insert);
	}
	return command;
}

}