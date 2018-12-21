package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.ImageDTO;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserManager;

public class PhotoListActivity extends AppCompatActivity {

	public static final String EXTRA_PHOTO_LIST_TYPE = "ProfileActivity.EXTRA_PHOTO_LIST_TYPE";
	public static final String EXTRA_USER_ID = "ProfileActivity.EXTRA_USER_ID";
	public static final int EXTRA_TYPE_MY_UPLOAD = 1001;
	public static final int EXTRA_TYPE_SOLD_PHOTO = 1002;

	private static Intent prepare(Context context, int type) {
		return new Intent(context, ProfileActivity.class)
				.putExtra(EXTRA_PHOTO_LIST_TYPE, type);
	}

	static Integer extractPhotoListType(Intent intent) {
		return intent.getIntExtra(EXTRA_PHOTO_LIST_TYPE, 1001);
	}

	static String extractUserId(Intent intent) {
		return intent.getStringExtra(EXTRA_USER_ID);
	}

	@BindView(R.id.listView)
	ListView imageListView;

	private UserManager userManager = new UserManager();
	private GalleryManager galleryManager = new GalleryManager(this);
	private List<Bitmap> finalImageList = new ArrayList<Bitmap>();

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		ButterKnife.bind(this);

		String userid = extractUserId(getIntent());

		if (extractPhotoListType(getIntent()) == EXTRA_TYPE_SOLD_PHOTO) {
			userManager.getSoldPhotos(userid)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(x ->
									updateList(x)
							, error ->
									Log.e("Error", "")
					);
		} else if (extractPhotoListType(getIntent()) == EXTRA_TYPE_MY_UPLOAD) {
			userManager.getUserUploads(userid)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(x ->
									updateList(x)
							, error ->
									Log.e("Error", "")
					);
		}
	}

	@SuppressLint("CheckResult")
	private void updateList(List<ImageDTO> imageList) {
		for (ImageDTO img : imageList) {
			galleryManager.getImage(img.filename)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(params -> {
								finalImageList.add(params);
								updateAdapter(finalImageList);
							}
							, error ->
									Log.e("Error", "")
					);
		}

	}

	private void updateAdapter(List<Bitmap> imgList) {
		PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, imgList);
		imageListView.setAdapter(photoListAdapter);
	}
}
