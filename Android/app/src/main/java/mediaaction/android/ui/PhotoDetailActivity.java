package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.PhotoListType;
import mediaaction.android.logic.RxUtils;

public class PhotoDetailActivity extends AppCompatActivity {

	public static final String EXTRA_IMAGE_DATA = "PhotoDetailActivity.EXTRA_IMAGE_DATA";
	public static final String EXTRA_IMAGE_TYPE = "PhotoDetailActivity.EXTRA_IMAGE_TYPE";

	public static Intent prepare(Context context, ImageDTO image, PhotoListType listType) {
		return new Intent(context, PhotoDetailActivity.class)
				.putExtra(EXTRA_IMAGE_DATA, image)
				.putExtra(EXTRA_IMAGE_TYPE, listType);
	}

	private static ImageDTO extractImageData(Intent intent) {
		return (ImageDTO) intent.getSerializableExtra(EXTRA_IMAGE_DATA);
	}

	private static PhotoListType extractImageType(Intent intent) {
		return (PhotoListType) intent.getSerializableExtra(EXTRA_IMAGE_TYPE);
	}


	private GalleryManager galleryManager = new GalleryManager(this);
	ImageDTO imageData;
	PhotoListType imageType;

	@BindView(R.id.selectedPhoto)
	ImageView selectedPhoto;
	@BindView(R.id.photoTitle)
	TextView photoTitle;
	@BindView(R.id.photoPrice)
	TextView photoPrice;
	@BindView(R.id.photoDescription)
	TextView photoDescription;
	@BindView(R.id.deleteImageButton)
	Button deleteImageButton;

	@SuppressLint({"SetTextI18n", "CheckResult"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_detail);
		ButterKnife.bind(this);

		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

		imageData = extractImageData(getIntent());
		imageType = extractImageType(getIntent());

		if (imageType == PhotoListType.SOLD) {
			deleteImageButton.setVisibility(View.INVISIBLE);
		}

		galleryManager.getImage(imageData.filename)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(bitmap -> selectedPhoto.setImageBitmap(bitmap), error -> Log.e("Error", ""));

		photoTitle.setText(imageData.title);
		photoPrice.setText(String.valueOf(imageData.price) + " €");
		photoDescription.setText(imageData.description);
	}

	@OnClick(R.id.deleteImageButton)
	public void deleteImageClick(View view) {
		new android.app.AlertDialog.Builder(this)
				.setMessage("Do you want to delete this picture ?")
				.setPositiveButton("yes", (dialog, id) -> {
					galleryManager.deleteImage(imageData.id)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribeOn(Schedulers.newThread())
							.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
							.subscribe(x -> {
								setResult(Activity.RESULT_OK);
								finish();
							}, error -> Log.e("Error", ""));
				})
				.setNegativeButton("no", null)
				.create().show();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
