package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.RxUtils;

public class UploadActivity extends AppCompatActivity {

	public static final String EXTRA_USER_ID = "ProfileActivity.EXTRA_USER_ID";

	static String extractUserId(Intent intent) {
		return intent.getStringExtra(EXTRA_USER_ID);
	}

	@BindView(R.id.addTitleText)
	EditText imageTitle;
	@BindView(R.id.editPrice)
	EditText editprice;
	@BindView(R.id.selectedImage)
	ImageView image;
	@BindView(R.id.alertText)
	TextView alertext;

	public Uri selectedImage;
	public static final int GET_FROM_GALLERY = 1001;
	public List<String> tagslist = new ArrayList<>();
	private GalleryManager galleryManager = new GalleryManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		ButterKnife.bind(this);
	}

	public void uploadAction(View view) {
		startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
	}

	@SuppressLint("CheckResult")
	public void Post(View view) throws FileNotFoundException {
		if (selectedImage == null) {
			alertext.setText("Image missing");
			return;
		}
		if (editprice.getText().toString().isEmpty()) {
			alertext.setText("Price missing");
			return;
		}

		Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String encImage = Base64.encodeToString(b, Base64.DEFAULT);

		galleryManager.uploadImage(encImage, "image/jpeg", imageTitle.getText().toString(), "description", Integer.parseInt(editprice.getText().toString()), extractUserId(getIntent()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x -> {
						}
						//	finish()
						, error ->
								Log.e("Error", "")
				);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
			selectedImage = data.getData();
			Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
				image.setImageBitmap(bitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
