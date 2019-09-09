package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;

		if (getArguments() != null) {
			listType = (PhotoListType) getArguments().getSerializable(ARG_PHOTO_LIST_TYPE);
			userId = (String) getArguments().get(ARG_USER_ID);
		} else
			listType = PhotoListType.UPLOADED;
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
					gridview.setOnItemClickListener((parent, v, position, id) ->
							startActivityForResult(PhotoDetailActivity.prepare(getContext(), imageList.get(position), listType), HomeFragment.REQUEST_IMAGE_DELETE));
				}, error -> Log.e("Error", ""));
	}
}
