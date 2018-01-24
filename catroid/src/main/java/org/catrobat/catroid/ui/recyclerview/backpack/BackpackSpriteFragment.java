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

package org.catrobat.catroid.ui.recyclerview.backpack;

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.adapter.SpriteAdapter;
import org.catrobat.catroid.ui.recyclerview.controller.SpriteController;
import org.catrobat.catroid.utils.ToastUtil;

import java.io.IOException;
import java.util.List;

public class BackpackSpriteFragment extends BackpackRecyclerViewFragment<Sprite> {

	public static final String TAG = BackpackSpriteFragment.class.getSimpleName();

	private SpriteController spriteController = new SpriteController();

	@Override
	protected void initializeAdapter() {
		sharedPreferenceDetailsKey = "showDetailsSpriteList";
		hasDetails = true;
		List<Sprite> items = BackPackListManager.getInstance().getBackPackedSprites();
		adapter = new SpriteAdapter(items);
		onAdapterReady();
	}

	@Override
	protected void unpackItems(List<Sprite> selectedItems) {
		finishActionMode();
		try {
			for (Sprite item : selectedItems) {
				spriteController.unpack(item, ProjectManager.getInstance().getCurrentScene());
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.unpacked_sprites,
					selectedItems.size(),
					selectedItems.size()));
			getActivity().finish();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_sprites;
	}

	@Override
	protected void deleteItems(List<Sprite> selectedItems) {
		finishActionMode();
		for (Sprite item : selectedItems) {
			//spriteController.delete(item);
			adapter.remove(item);
		}
		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_sprites,
				selectedItems.size(),
				selectedItems.size()));

		BackPackListManager.getInstance().saveBackpack();
		if (adapter.getItems().isEmpty()) {
			getActivity().finish();
		}
	}

	@Override
	protected String getItemName(Sprite item) {
		return item.getName();
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_sprites_title,
				selectedItemCnt,
				selectedItemCnt));
	}
}
