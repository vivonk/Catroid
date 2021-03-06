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
import org.catrobat.catroid.content.bricks.LegoEv3MotorTurnAngleBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.annotations.Flaky;
import org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;

@RunWith(AndroidJUnit4.class)
public class LegoEv3MotorTurnAngleBrickTest {

	private static int brickPosition;

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		brickPosition = 1;
		int startAngle = 180;
		BrickTestUtils.createProjectAndGetStartScript("LegoEv3MotorTurnAngleBrickTest")
				.addBrick(new LegoEv3MotorTurnAngleBrick(LegoEv3MotorTurnAngleBrick.Motor.MOTOR_A, startAngle));
		baseActivityTestRule.launchActivity(null);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.Gadgets.class})
	@Test
	@Flaky
	public void legoEv3MotorTurnAngleBrickTest() {
		int testAngle = 100;

		onBrickAtPosition(0).checkShowsText("When program starts");
		onBrickAtPosition(brickPosition).checkShowsText("Turn EV3 motor");

		onBrickAtPosition(brickPosition).onSpinner(R.id.lego_ev3_motor_turn_angle_spinner)
				.checkShowsText(R.string.ev3_motor_a);

		List<Integer> spinnerValuesResourceIds = Arrays.asList(
				R.string.ev3_motor_a,
				R.string.ev3_motor_b,
				R.string.ev3_motor_c,
				R.string.ev3_motor_d,
				R.string.ev3_motor_b_and_c);

		onBrickAtPosition(brickPosition).onSpinner(R.id.lego_ev3_motor_turn_angle_spinner)
				.checkValuesAvailable(spinnerValuesResourceIds);

		onBrickAtPosition(brickPosition).onFormulaTextField(R.id.ev3_motor_turn_angle_edit_text)
				.performEnterNumber(testAngle)
				.checkShowsNumber(testAngle);
	}
}
