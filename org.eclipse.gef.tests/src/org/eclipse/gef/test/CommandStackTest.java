package org.eclipse.gef.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.junit.Assert;

public class CommandStackTest extends TestCase {

	public void testCommandStackEventListenerNotifications() {
		final List commandStackEvents = new ArrayList();

		// capture all notifications in an event list
		CommandStack stack = new CommandStack();
		stack.addCommandStackEventListener(new CommandStackEventListener() {
			public void stackChanged(CommandStackEvent event) {
				commandStackEvents.add(event);
			}
		});

		Command c = new Command() {
		};

		// test execution pre and post events
		Assert.assertEquals(0, commandStackEvents.size());
		stack.execute(c);

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(0)).getCommand());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(0))
				.isPreChangeEvent());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(0))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(0)).getSource());
		Assert.assertEquals(CommandStack.PRE_EXECUTE,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail());
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.POST_MASK);

		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(1)).getCommand());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(1))
				.isPreChangeEvent());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(1))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(1)).getSource());
		Assert.assertEquals(CommandStack.POST_EXECUTE,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail());
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.POST_MASK);

		// // test undo pre and post events
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.undo();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(0)).getCommand());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(0))
				.isPreChangeEvent());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(0))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(0)).getSource());
		Assert.assertEquals(CommandStack.PRE_UNDO,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail());
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.POST_MASK);

		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(1)).getCommand());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(1))
				.isPreChangeEvent());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(1))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(1)).getSource());
		Assert.assertEquals(CommandStack.POST_UNDO,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail());
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.POST_MASK);

		// // test redo pre and post events
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.redo();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(0)).getCommand());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(0))
				.isPreChangeEvent());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(0))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(0)).getSource());
		Assert.assertEquals(CommandStack.PRE_REDO,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail());
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.POST_MASK);

		Assert.assertEquals(c,
				((CommandStackEvent) commandStackEvents.get(1)).getCommand());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(1))
				.isPreChangeEvent());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(1))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(1)).getSource());
		Assert.assertEquals(CommandStack.POST_REDO,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail());
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.POST_MASK);

		// test flush event
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.flush();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(null,
				((CommandStackEvent) commandStackEvents.get(0)).getCommand());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(0))
				.isPreChangeEvent());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(0))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(0)).getSource());
		Assert.assertEquals(CommandStack.PRE_FLUSH,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail());
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.POST_MASK);

		Assert.assertEquals(null,
				((CommandStackEvent) commandStackEvents.get(1)).getCommand());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(1))
				.isPreChangeEvent());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(1))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(1)).getSource());
		Assert.assertEquals(CommandStack.POST_FLUSH,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail());
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.POST_MASK);

		// test mark save location event
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.redo();
		stack.markSaveLocation();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(null,
				((CommandStackEvent) commandStackEvents.get(0)).getCommand());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(0))
				.isPreChangeEvent());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(0))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(0)).getSource());
		Assert.assertEquals(CommandStack.PRE_MARK_SAVE,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail());
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(0)).getDetail()
						& CommandStack.POST_MASK);

		Assert.assertEquals(null,
				((CommandStackEvent) commandStackEvents.get(1)).getCommand());
		Assert.assertFalse(((CommandStackEvent) commandStackEvents.get(1))
				.isPreChangeEvent());
		Assert.assertTrue(((CommandStackEvent) commandStackEvents.get(1))
				.isPostChangeEvent());
		Assert.assertEquals(stack,
				((CommandStackEvent) commandStackEvents.get(1)).getSource());
		Assert.assertEquals(CommandStack.POST_MARK_SAVE,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail());
		Assert.assertEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.PRE_MASK);
		Assert.assertNotEquals(0,
				((CommandStackEvent) commandStackEvents.get(1)).getDetail()
						& CommandStack.POST_MASK);

	}
}
