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

package org.catrobat.catroid.ui.recyclerview.fragment;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.controller.PocketPaintExchangeHandler;
import org.catrobat.catroid.ui.recyclerview.adapter.LookAdapter;
import org.catrobat.catroid.ui.recyclerview.backpack.BackpackActivity;
import org.catrobat.catroid.ui.recyclerview.controller.LookController;
import org.catrobat.catroid.ui.recyclerview.dialog.NewLookDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.RenameItemDialog;
import org.catrobat.catroid.utils.SnackbarUtil;
import org.catrobat.catroid.utils.ToastUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.catrobat.catroid.common.Constants.POCKET_PAINT_PACKAGE_NAME;

public class LookListFragment extends RecyclerViewFragment<LookData> {

	public static final String TAG = LookListFragment.class.getSimpleName();

	public static final int POCKET_PAINT = 0;
	public static final int LIBRARY = 1;
	public static final int FILE = 2;
	public static final int CAMERA = 3;
	public static final int DRONE = 4;

	private LookController lookController = new LookController();

	@Override
	protected void initializeAdapter() {
		SnackbarUtil.showHintSnackbar(getActivity(), R.string.hint_looks);
		sharedPreferenceDetailsKey = "showDetailsLookList";
		hasDetails = true;
		List<LookData> items = ProjectManager.getInstance().getCurrentSprite().getLookList();
		adapter = new LookAdapter(items);
		emptyView.setText(R.string.fragment_look_text_description);
		onAdapterReady();
	}

	@Override
	public void handleAddButton() {
		NewLookDialog dialog = new NewLookDialog(
				this, ProjectManager.getInstance().getCurrentScene(), ProjectManager.getInstance().getCurrentSprite());
		dialog.show(getFragmentManager(), NewLookDialog.TAG);
	}

	@Override
	public void addItem(LookData item) {
		if (ProjectManager.getInstance().getCurrentSprite().hasCollision()) {
			item.getCollisionInformation().calculate();
		}
		adapter.add(item);
	}

	@Override
	protected void packItems(List<LookData> selectedItems) {
		finishActionMode();
		try {
			for (LookData item : selectedItems) {
				lookController.pack(item, ProjectManager.getInstance().getCurrentScene());
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.packed_looks,
					selectedItems.size(),
					selectedItems.size()));

			switchToBackpack();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	protected boolean isBackpackEmpty() {
		return BackPackListManager.getInstance().getBackPackedLooks().isEmpty();
	}

	@Override
	protected void switchToBackpack() {
		Intent intent = new Intent(getActivity(), BackpackActivity.class);
		intent.putExtra(BackpackActivity.EXTRA_FRAGMENT_POSITION, BackpackActivity.FRAGMENT_LOOKS);
		startActivity(intent);
	}

	@Override
	protected void copyItems(List<LookData> selectedItems) {
		finishActionMode();
		Scene currentScene = ProjectManager.getInstance().getCurrentScene();
		Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
		for (LookData item : selectedItems) {
			try {
				lookController.copy(item, currentScene, currentScene, currentSprite);
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.copied_looks,
				selectedItems.size(),
				selectedItems.size()));
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_looks;
	}

	@Override
	protected void deleteItems(List<LookData> selectedItems) {
		finishActionMode();
		for (LookData item : selectedItems) {
			try {
				lookController.delete(item, ProjectManager.getInstance().getCurrentScene());
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			adapter.remove(item);
		}
		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_looks,
				selectedItems.size(),
				selectedItems.size()));
	}

	@Override
	protected void showRenameDialog(List<LookData> selectedItems) {
		String name = selectedItems.get(0).getName();
		RenameItemDialog dialog = new RenameItemDialog(R.string.rename_look_dialog, R.string.look_name_label, name, this);
		dialog.show(getFragmentManager(), RenameItemDialog.TAG);
	}

	@Override
	public boolean isNameUnique(String name) {
		Set<String> scope = new HashSet<>();
		for (LookData item : adapter.getItems()) {
			scope.add(item.getName());
		}

		return !scope.contains(name);
	}

	@Override
	public void renameItem(String name) {
		LookData item = adapter.getSelectedItems().get(0);
		if (!item.getName().equals(name)) {
			item.setName(name);
		}
		finishActionMode();
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_looks_title,
				selectedItemCnt,
				selectedItemCnt));
	}

	@Override
	public void onItemClick(LookData item) {
		if (actionModeType != NONE) {
			return;
		}

		Intent intent = new Intent("android.intent.action.MAIN");
		intent.setComponent(new ComponentName(POCKET_PAINT_PACKAGE_NAME,
				Constants.POCKET_PAINT_INTENT_ACTIVITY_NAME));

		Bundle bundle = new Bundle();
		bundle.putString(Constants.EXTRA_PICTURE_PATH_POCKET_PAINT, item.getAbsolutePath());
		intent.putExtras(bundle);
		intent.addCategory("android.intent.category.LAUNCHER");

		if (PocketPaintExchangeHandler.isPocketPaintInstalled(getActivity(), intent)) {
			startActivityForResult(intent, POCKET_PAINT);
		} else {
			BroadcastReceiver receiver = createPocketPaintBroadcastReceiver(intent, POCKET_PAINT);
			PocketPaintExchangeHandler.installPocketPaintAndRegister(receiver, getActivity());
		}
	}

	private BroadcastReceiver createPocketPaintBroadcastReceiver(final Intent paintroidIntent, final int
			requestCode) {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				String packageName = intent.getData().getEncodedSchemeSpecificPart();
				if (!packageName.equals(POCKET_PAINT_PACKAGE_NAME)) {
					return;
				}

				getActivity().unregisterReceiver(this);

				if (PocketPaintExchangeHandler.isPocketPaintInstalled(getActivity(), paintroidIntent)) {
					ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context
							.ACTIVITY_SERVICE);
					activityManager.moveTaskToFront(getActivity().getTaskId(), 0);
					startActivityForResult(paintroidIntent, requestCode);
				}
			}
		};
	}
}
