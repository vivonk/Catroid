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

import com.parrot.freeflight.drone.DroneProxy.ARDRONE_LED_ANIMATION;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.bricks.DronePlayLedAnimationBrick;
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
public class ARDronePlayLedAnimationBrickTest {
	private int brickPosition;

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		brickPosition = 1;

		BrickTestUtils.createProjectAndGetStartScript("ARDronePlayLedAnimationBrickTest")
				.addBrick(new DronePlayLedAnimationBrick(ARDRONE_LED_ANIMATION.ARDRONE_LED_ANIMATION_BLINK_GREEN_RED));
		baseActivityTestRule.launchActivity(null);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.Gadgets.class})
	@Test
	@Flaky
	public void testARDronePlayLedAnimationBrick() {

		onBrickAtPosition(0).checkShowsText(R.string.brick_when_started);
		onBrickAtPosition(brickPosition).checkShowsText(R.string.brick_drone_play_led_animation);

		onBrickAtPosition(brickPosition).onSpinner(R.id.brick_drone_play_led_animation_spinner)
				.checkShowsText(R.string.drone_animation_blinkgreenred);

		List<Integer> spinnerValuesResourceIds = Arrays.asList(
				R.string.drone_animation_blinkgreenred,
				R.string.drone_animation_blinkgreen,
				R.string.drone_animation_blinkred,
				R.string.drone_animation_blinkorange,
				R.string.drone_animation_snakegreenred,
				R.string.drone_animation_fire,
				R.string.drone_animation_standard,
				R.string.drone_animation_red,
				R.string.drone_animation_green,
				R.string.drone_animation_redsnake,
				R.string.drone_animation_blank,
				R.string.drone_animation_rightmissile,
				R.string.drone_animation_leftmissile,
				R.string.drone_animation_doublemissile,
				R.string.drone_animation_frontleftgreenothersred,
				R.string.drone_animation_frontrightgreenothersred,
				R.string.drone_animation_rearrightgreenothersred,
				R.string.drone_animation_rearleftgreenothersred,
				R.string.drone_animation_leftgreenrightred,
				R.string.drone_animation_leftredrightgreen,
				R.string.drone_animation_blinkstandard);

		onBrickAtPosition(brickPosition).onSpinner(R.id.brick_drone_play_led_animation_spinner)
				.checkValuesAvailable(spinnerValuesResourceIds);
	}
}
