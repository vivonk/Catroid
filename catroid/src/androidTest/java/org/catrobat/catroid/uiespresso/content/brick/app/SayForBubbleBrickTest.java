/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

package org.catrobat.catroid.uiespresso.content.brick.app;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.bricks.SayForBubbleBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;

@RunWith(AndroidJUnit4.class)
public class SayForBubbleBrickTest {

	private int brickPositionOneSecond;
	private int brickPositionTwoSeconds;

	private String sayString = "say something";
	private float durationOne = 1;
	private float durationTwo = 2;

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		Script script = BrickTestUtils.createProjectAndGetStartScript("sayBubbleBrickTest");
		script.addBrick(new SayForBubbleBrick(sayString, durationOne));
		script.addBrick(new SayForBubbleBrick(sayString, durationTwo));
		brickPositionOneSecond = 1;
		brickPositionTwoSeconds = 2;
		baseActivityTestRule.launchActivity(null);
	}

	@Category({Cat.CatrobatLanguage.class, Level.Smoke.class})
	@Test
	public void sayForBubbleBrickTest() {
		onBrickAtPosition(brickPositionOneSecond)
				.checkShowsText(R.string.brick_say_bubble);

		onBrickAtPosition(brickPositionOneSecond).onFormulaTextField(R.id.brick_say_for_bubble_edit_text_text)
				.checkShowsText(sayString);

		onBrickAtPosition(brickPositionOneSecond)
				.checkShowsText(R.string.brick_think_say_for_text);

		onBrickAtPosition(brickPositionOneSecond).onFormulaTextField(R.id.brick_say_for_bubble_edit_text_duration)
				.check(matches(withText("1 ")));

		String second = UiTestUtils.getResources().getQuantityString(R.plurals.second_plural, (int) durationOne);

		onBrickAtPosition(brickPositionOneSecond)
				.checkShowsText(second);

		onBrickAtPosition(brickPositionTwoSeconds).onFormulaTextField(R.id.brick_say_for_bubble_edit_text_duration)
				.check(matches(withText("2 ")));

		String seconds = UiTestUtils.getResources().getQuantityString(R.plurals.second_plural, (int) durationTwo);

		onBrickAtPosition(brickPositionTwoSeconds)
				.checkShowsText(seconds);
	}
}
