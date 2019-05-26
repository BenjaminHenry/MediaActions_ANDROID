package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.RxUtils;

public class PhotoDetail extends AppCompatActivity {

	public static final String EXTRA_IMAGE_DATA = "PhotoDetail.EXTRA_IMAGE_DATA";

	public static Intent prepare(Context context, ImageDTO image) {
		return new Intent(context, PhotoDetail.class)
				.putExtra(EXTRA_IMAGE_DATA, image);
	}

	private static ImageDTO extractImageData(Intent intent) {
		return (ImageDTO) intent.getSerializableExtra(EXTRA_IMAGE_DATA);
	}

	private GalleryManager galleryManager = new GalleryManager(this);
	ImageDTO imageData;

	@BindView(R.id.selectedPhoto)
	ImageView selectedPhoto;
	@BindView(R.id.photoTitle)
	TextView photoTitle;
	@BindView(R.id.photoPrice)
	TextView photoPrice;
	@BindView(R.id.photoDescription)
	TextView photoDescription;

	@SuppressLint({"SetTextI18n", "CheckResult"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_detail);
		ButterKnife.bind(this);

		imageData = extractImageData(getIntent());

		galleryManager.getImage(imageData.filename)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(bitmap -> selectedPhoto.setImageBitmap(bitmap), error -> Log.e("Error", ""));

		photoTitle.setText(imageData.title);
		photoPrice.setText(String.valueOf(imageData.price) + " €");
		photoDescription.setText(imageData.description);
	}
}
