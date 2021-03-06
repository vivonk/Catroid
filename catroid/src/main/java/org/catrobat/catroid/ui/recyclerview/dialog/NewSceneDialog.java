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

package org.catrobat.catroid.ui.recyclerview.dialog;

import android.app.Dialog;
import android.os.Bundle;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.ui.dialogs.TextDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;
import org.catrobat.catroid.ui.recyclerview.util.UniqueNameProvider;

import java.util.HashSet;
import java.util.Set;

public class NewSceneDialog extends TextDialog {

	public static final String TAG = NewSceneDialog.class.getSimpleName();

	private NewItemInterface<Scene> newItemInterface;
	private Project dstProject;

	public NewSceneDialog(NewItemInterface<Scene> newItemInterface, Project dstProject) {
		super(R.string.new_scene_dialog, R.string.scene_name, "", false);
		this.newItemInterface = newItemInterface;
		this.dstProject = dstProject;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.previousText = new UniqueNameProvider().getUniqueName(getString(R.string.default_scene_name, 1),
				getScope(dstProject));
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	protected boolean handlePositiveButtonClick() {
		String name = input.getText().toString().trim();

		if (name.isEmpty()) {
			input.setError(getString(R.string.name_consists_of_spaces_only));
			return false;
		}

		if (!getScope(dstProject).contains(name)) {
			newItemInterface.addItem(new Scene(getActivity(), name, dstProject));
			return true;
		} else {
			input.setError(getString(R.string.name_already_exists));
			return false;
		}
	}

	@Override
	protected void handleNegativeButtonClick() {
	}

	private Set<String> getScope(Project project) {
		Set<String> scope = new HashSet<>();
		for (Scene item : project.getSceneList()) {
			scope.add(item.getName());
		}
		return scope;
	}
}
