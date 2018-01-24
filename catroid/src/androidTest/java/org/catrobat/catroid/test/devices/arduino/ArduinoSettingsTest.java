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

package org.catrobat.catroid.test.devices.arduino;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.ArduinoSendPWMValueBrick;
import org.catrobat.catroid.exceptions.CompatibilityProjectException;
import org.catrobat.catroid.exceptions.LoadingProjectException;
import org.catrobat.catroid.exceptions.OutdatedVersionProjectException;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.test.utils.LegacyFileUtils;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.utils.Utils;

import java.io.IOException;

public class ArduinoSettingsTest extends InstrumentationTestCase {

	Context context = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		context = this.getInstrumentation().getTargetContext();
		SettingsActivity.setArduinoSharedPreferenceEnabled(context, false);
	}

	@Override
	protected void tearDown() throws Exception {
		SettingsActivity.setArduinoSharedPreferenceEnabled(context, false);
		super.tearDown();
	}

	public void testIfArduinoBricksAreEnabledIfItItUsedInAProgram() throws IOException, CompatibilityProjectException,
			OutdatedVersionProjectException, LoadingProjectException, InterruptedException {

		createProjectArduino();

		assertFalse("By default Arduino should be disabled",
				SettingsActivity.isArduinoSharedPreferenceEnabled(context));

		ProjectManager.getInstance().loadProject(LegacyFileUtils.DEFAULT_TEST_PROJECT_NAME, context);

		assertTrue("After loading a project which needs Arduino it should be enabled",
				SettingsActivity.isArduinoSharedPreferenceEnabled(context));

		StorageHandler.deleteDir(Utils.buildProjectPath(LegacyFileUtils.DEFAULT_TEST_PROJECT_NAME));
	}

	private void createProjectArduino() throws InterruptedException {
		Project projectArduino = new Project(InstrumentationRegistry.getTargetContext(),
				LegacyFileUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite sprite = new SingleSprite("Arduino");

		StartScript startScript = new StartScript();
		ArduinoSendPWMValueBrick arduinoArduinoSendPWMValueBrick = new ArduinoSendPWMValueBrick(3, 255);
		startScript.addBrick(arduinoArduinoSendPWMValueBrick);
		sprite.addScript(startScript);
		projectArduino.getDefaultScene().addSprite(sprite);

		ProjectManager.getInstance().setProject(projectArduino);
		ProjectManager.getInstance().saveProject(context);
		Thread.sleep(100);
		ProjectManager.getInstance().setProject(null);
	}
}
