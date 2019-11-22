package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.PhotoListType;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserManager;

public class PhotoListFragment extends Fragment {

	public static final String ARG_PHOTO_LIST_TYPE = "PhotoListFragment.ARG_PHOTO_LIST_TYPE";
	public static final String ARG_USER_ID = "PhotoListFragment.ARG_USER_ID";

	UserManager userManager;
	GalleryManager galleryManager;
	Context context;
	PhotoListType listType;
	String userId;

	boolean isMultiSelect = false;
	ActionMode mActionMode;
	ActionMode.Callback mActionModeCallback;
	ArrayList<Long> multiSelectionList = new ArrayList<Long>();

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;

		if (getArguments() != null) {
			listType = (PhotoListType) getArguments().getSerializable(ARG_PHOTO_LIST_TYPE);
			userId = (String) getArguments().get(ARG_USER_ID);
		} else
			listType = PhotoListType.UPLOADED;

		mActionModeCallback = new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
				MenuInflater inflater = actionMode.getMenuInflater();
				inflater.inflate(R.menu.menu_multi_select, menu);
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
				switch (menuItem.getItemId()) {
					case R.id.action_delete: {
						Log.d("ACTION DELETE", "" + multiSelectionList.size());
					}
				}
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode actionMode) {
				isMultiSelect = false;
				mActionMode = null;
				multiSelectionList = new ArrayList<Long>();
			}
		};
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (listType == PhotoListType.EMPTY)
			return inflater.inflate(R.layout.empty_photo_list, container, false);
		return inflater.inflate(R.layout.activity_photo_list_fragment, container, false);
	}

	@SuppressLint("CheckResult")
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		userManager = new UserManager();
		galleryManager = new GalleryManager(context);

		if (listType == PhotoListType.SOLD) {
			userManager.getSoldPhotos(userId)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
					.subscribe(list ->
									setupAdapter(view, list, listType)
							, error ->
									Log.e("Error", "")
					);
		} else if (listType == PhotoListType.UPLOADED) {
			userManager.getUserUploads(userId)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
					.subscribe(list ->
									setupAdapter(view, list, listType)
							, error ->
									Log.e("Error", "")
					);
		}
	}

	void multiSelect(Long imageId) {
		if (multiSelectionList.contains(imageId)) {
			multiSelectionList.remove(imageId);
		} else {
			multiSelectionList.add(imageId);
		}
		if (multiSelectionList.size() > 1) {
			mActionMode.setTitle(multiSelectionList.size() + "items");
		} else {
			mActionMode.setTitle("1 item");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (getParentFragment() != null)
			((HomeFragment) getParentFragment()).onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("CheckResult")
	private void setupAdapter(View view, List<ImageDTO> imageList, PhotoListType listType) {

		GridView gridview = view.findViewById(R.id.gridview);

		Flowable.fromIterable(imageList)
				.concatMapSingle(x -> galleryManager.getImage(x.filename))
				.toList()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(getContext()))
				.subscribe(bitmap -> {
					gridview.setAdapter(new PhotoListAdapter(context, bitmap));
					gridview.setOnItemClickListener((parent, v, position, id) -> {
						if (isMultiSelect) {
							multiSelect(id);
						} else {
							startActivityForResult(PhotoDetailActivity.prepare(getContext(), imageList.get(position), listType), HomeFragment.REQUEST_IMAGE_DELETE);
						}
					});
					gridview.setOnItemLongClickListener((parent, v, position, id) -> {
						if (!isMultiSelect) {
							isMultiSelect = true;
							if (mActionMode == null) {
								mActionMode = getActivity().startActionMode(mActionModeCallback);
							}
							multiSelect(id);
						}
						return true;
					});
				}, error -> Log.e("Error", ""));
	}
}