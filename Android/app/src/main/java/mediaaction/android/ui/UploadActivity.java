package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.RxUtils;

public class UploadActivity extends AppCompatActivity implements IPickResult {

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
	public static final int CHOOSE_IMAGE = 1001;
	private GalleryManager galleryManager = new GalleryManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		ButterKnife.bind(this);
	}

	public void uploadAction(View view) {
		PickImageDialog.build(new PickSetup()).show(this);
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

		galleryManager.uploadImage(b, "image/jpeg", imageTitle.getText().toString(), "description", Integer.parseInt(editprice.getText().toString()), extractUserId(getIntent()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x -> {
							finish();
						}
						, error ->
								Log.e("Error", "")
				);
	}

	@Override
	public void onPickResult(PickResult r) {
		if (r.getError() == null) {
			//If you want the Uri.
			//Mandatory to refresh image from Uri.
			//getImageView().setImageURI(null);
			selectedImage = r.getUri();

			//Setting the real returned image.
			//getImageView().setImageURI(r.getUri());

			//If you want the Bitmap.
			image.setImageBitmap(r.getBitmap());

			//Image path
			//r.getPath();
		} else {
			//Handle possible errors
			//TODO: do what you have to do with r.getError();
			Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
