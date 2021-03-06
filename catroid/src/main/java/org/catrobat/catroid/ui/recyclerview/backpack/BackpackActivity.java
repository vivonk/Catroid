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

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.BaseActivity;

public class BackpackActivity extends BaseActivity {

	public static final String TAG = BackpackActivity.class.getSimpleName();

	public static final String EXTRA_FRAGMENT_POSITION = "FRAGMENT_POSITION";
	public static final int FRAGMENT_SCENES = 0;
	public static final int FRAGMENT_SPRITES = 1;
	public static final int FRAGMENT_LOOKS = 2;
	public static final int FRAGMENT_SOUNDS = 3;
	public static final int FRAGMENT_SCRIPTS = 4;

	private int fragmentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setTitle(R.string.backpack_title);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#83B3C7")));

		setContentView(R.layout.activity_backpack);

		if (savedInstanceState == null) {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				fragmentPosition = bundle.getInt(EXTRA_FRAGMENT_POSITION, FRAGMENT_SCENES);
			}
		}

		switchToFragment(fragmentPosition);
	}

	private void switchToFragment(int fragmentTag) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		switch (fragmentTag) {
			case FRAGMENT_SCENES:
				fragmentTransaction.replace(R.id.fragment_container, new BackpackSceneFragment(),
						BackpackSceneFragment.TAG);
				break;
			case FRAGMENT_SPRITES:
				fragmentTransaction.replace(R.id.fragment_container, new BackpackSpriteFragment(),
						BackpackSpriteFragment.TAG);
				break;
			case FRAGMENT_LOOKS:
				fragmentTransaction.replace(R.id.fragment_container, new BackpackLookFragment(),
						BackpackLookFragment.TAG);
				break;
			case FRAGMENT_SOUNDS:
				fragmentTransaction.replace(R.id.fragment_container, new BackpackSoundFragment(),
						BackpackSoundFragment.TAG);
				break;
			case FRAGMENT_SCRIPTS:
				fragmentTransaction.replace(R.id.fragment_container, new BackpackScriptFragment(),
						BackpackScriptFragment.TAG);
				break;
			default:
				return;
		}

		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_backpack_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		BackpackRecyclerViewFragment currentFragment = ((BackpackRecyclerViewFragment) getFragmentManager()
				.findFragmentById(R.id
				.fragment_container));

		menu.findItem(R.id.show_details).setVisible(currentFragment.hasDetails);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
}
