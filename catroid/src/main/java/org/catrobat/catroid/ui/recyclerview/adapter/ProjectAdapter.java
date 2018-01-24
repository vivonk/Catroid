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

package org.catrobat.catroid.ui.recyclerview.adapter;

import android.content.Context;
import android.view.View;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.ProjectData;
import org.catrobat.catroid.io.ProjectAndSceneScreenshotLoader;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.utils.UtilFile;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ProjectAdapter extends RecyclerViewAdapter<ProjectData> {

	public ProjectAdapter(List<ProjectData> items) {
		super(items);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);

		Context context = holder.itemView.getContext();
		ProjectAndSceneScreenshotLoader loader = new ProjectAndSceneScreenshotLoader(context);
		ProjectData item = items.get(position);
		String sceneName = StorageHandler.getInstance().getFirstSceneName(item.projectName);

		holder.name.setText(item.projectName);
		loader.loadAndShowScreenshot(item.projectName, sceneName, false, holder.image);

		if (showDetails) {
			holder.details.setVisibility(View.VISIBLE);

			Date lastModified = new Date(item.lastUsed);
			String lastAccess = DateFormat.getDateInstance(DateFormat.MEDIUM).format(lastModified);

			holder.leftTopDetails.setText(context.getString(R.string.last_used));
			holder.rightTopDetails.setText(lastAccess);

			holder.leftBottomDetails.setText(context.getString(R.string.size));
			String size = UtilFile.getSizeAsString(new File(Utils.buildProjectPath(item.projectName)), context);
			holder.rightBottomDetails.setText(size);
		} else {
			holder.details.setVisibility(View.GONE);
		}
	}
}
