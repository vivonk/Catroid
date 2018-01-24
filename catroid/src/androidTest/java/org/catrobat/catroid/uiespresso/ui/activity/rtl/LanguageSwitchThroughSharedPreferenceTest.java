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

package org.catrobat.catroid.uiespresso.ui.activity.rtl;

import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.rules.DontGenerateDefaultProjectActivityInstrumentationRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.catrobat.catroid.uiespresso.util.UiTestUtils.getResources;

@RunWith(AndroidJUnit4.class)
public class LanguageSwitchThroughSharedPreferenceTest {
	private static final Locale ARABICLOCALE = new Locale("ar");
	private static final Locale DEUTSCHLOCALE = Locale.GERMAN;
	@Rule
	public DontGenerateDefaultProjectActivityInstrumentationRule<MainMenuActivity> baseActivityTestRule = new
			DontGenerateDefaultProjectActivityInstrumentationRule<>(MainMenuActivity.class);
	private Configuration conf = getResources().getConfiguration();
	private IdlingResource idlingResource;

	@After
	public void tearDown() {
		SettingsActivity.removeLanguageSharedPreference(InstrumentationRegistry.getTargetContext());
		IdlingRegistry.getInstance().unregister(idlingResource);
	}

	@Test
	public void testSetLanguageToArabic() throws Exception {
		SettingsActivity.setLanguageSharedPreference(InstrumentationRegistry.getTargetContext(), "ar");
		baseActivityTestRule.launchActivity(null);

		MainMenuActivity mainMenuActivity = (MainMenuActivity) UiTestUtils.getCurrentActivity();
		idlingResource = mainMenuActivity.getIdlingResource();
		IdlingRegistry.getInstance().register(idlingResource);

		assertEquals(Locale.getDefault().getDisplayLanguage(), ARABICLOCALE.getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		assertEquals(View.LAYOUT_DIRECTION_RTL, conf.getLayoutDirection());
	}

	@Test
	public void testSetLanguageToGerman() throws Exception {
		SettingsActivity.setLanguageSharedPreference(InstrumentationRegistry.getTargetContext(), "de");
		baseActivityTestRule.launchActivity(null);

		MainMenuActivity mainMenuActivity = (MainMenuActivity) UiTestUtils.getCurrentActivity();
		idlingResource = mainMenuActivity.getIdlingResource();
		IdlingRegistry.getInstance().register(idlingResource);

		assertEquals(Locale.getDefault().getDisplayLanguage(), DEUTSCHLOCALE.getDisplayLanguage());
		assertFalse(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		assertEquals(View.LAYOUT_DIRECTION_LTR, conf.getLayoutDirection());
	}
}
