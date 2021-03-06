/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.content.script;

import com.badlogic.gdx.scenes.scene2d.Action;

import org.catrobat.catroid.content.ActionFactory;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.CollisionScript;
import org.catrobat.catroid.content.RaspiInterruptScript;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenBackgroundChangesScript;
import org.catrobat.catroid.content.WhenClonedScript;
import org.catrobat.catroid.content.WhenConditionScript;
import org.catrobat.catroid.content.WhenGamepadButtonScript;
import org.catrobat.catroid.content.WhenNfcScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.WhenTouchDownScript;
import org.catrobat.catroid.content.actions.EventThread;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.content.bricks.Brick;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class CommentOutScriptTest {

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{BroadcastScript.class.getSimpleName(), BroadcastScript.class},
				{CollisionScript.class.getSimpleName(), CollisionScript.class},
				{RaspiInterruptScript.class.getSimpleName(), RaspiInterruptScript.class},
				{StartScript.class.getSimpleName(), StartScript.class},
				{WhenBackgroundChangesScript.class.getSimpleName(), WhenBackgroundChangesScript.class},
				{WhenClonedScript.class.getSimpleName(), WhenClonedScript.class},
				{WhenConditionScript.class.getSimpleName(), WhenConditionScript.class},
				{WhenGamepadButtonScript.class.getSimpleName(), WhenGamepadButtonScript.class},
				{WhenNfcScript.class.getSimpleName(), WhenNfcScript.class},
				{WhenScript.class.getSimpleName(), WhenScript.class},
				{WhenTouchDownScript.class.getSimpleName(), WhenTouchDownScript.class},
		});
	}

	@Parameterized.Parameter
	public String name;

	@Parameterized.Parameter(1)
	public Class<Script> scriptClass;

	private Script script;

	@Before
	public void setUp() throws IllegalAccessException, InstantiationException {
		script = scriptClass.newInstance();
	}

	@Test
	public void testCommentOutBrickInScript() {
		Brick brick = mock(Brick.class);

		script.addBrick(brick);
		script.setCommentedOut(true);

		verify(brick).setCommentedOut(true);
	}

	@Test
	public void testRunScript() {
		Sprite sprite = mock(Sprite.class);
		Brick brick = mock(Brick.class);

		script.addBrick(brick);
		script.setCommentedOut(true);

		EventThread sequence = (EventThread) ActionFactory.createEventThread(script);
		doAnswer(invocation -> {
			((ScriptSequenceAction) invocation.getArgument(0)).addAction(mock(Action.class));
			return null;
		}).when(brick).addActionToSequence(sprite, sequence);

		script.run(sprite, sequence);

		assertEquals(0, sequence.getActions().size);
	}
}
