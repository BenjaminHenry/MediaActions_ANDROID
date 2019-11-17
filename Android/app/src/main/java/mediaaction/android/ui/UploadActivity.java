package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Request.RequestManager;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.utils.LocationUtils;

public class UploadActivity extends AppCompatActivity implements IPickResult {

	public static final String EXTRA_USER_ID = "UploadActivity.EXTRA_USER_ID";

	public static Intent prepare(Context context, String userId) {
		return new Intent(context, UploadActivity.class)
				.putExtra(EXTRA_USER_ID, userId);
	}

	private static String extractUserId(Intent intent) {
		return (String) intent.getSerializableExtra(EXTRA_USER_ID);
	}

	@BindView(R.id.addTitleEditText)
	EditText imageTitle;
	@BindView(R.id.editPrice)
	EditText editPrice;
	@BindView(R.id.editDescription)
	EditText editDescription;
	@BindView(R.id.editLocalisation)
	EditText editLocation;
	@BindView(R.id.selectedImage)
	ImageView image;
	@BindView(R.id.postButton)
	Button postButton;
	@BindView(R.id.alertText)
	TextView alertext;

	public String selectedImage;
	private String userId;
	private RequestManager requestManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_upload);
		ButterKnife.bind(this);

		requestManager = new RequestManager(this);
		userId = extractUserId(getIntent());
	}

	@OnClick(R.id.selectedImage)
	public void uploadAction(View view) {
		PickImageDialog.build(new PickSetup()).show(this);
	}

	@SuppressLint("CheckResult")
	@OnClick(R.id.postButton)
	public void Post(View view) {
		if (selectedImage == null) {
			alertext.setText("Image missing");
			return;
		}
		if (editPrice.getText().toString().isEmpty()) {
			alertext.setText("Price missing");
			return;
		}

		postButton.setEnabled(false);

		Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String encImage = Base64.encodeToString(b, Base64.DEFAULT);

		requestManager.uploadImageRequest(encImage, "image/jpeg", imageTitle.getText().toString(), editDescription.getText().toString(), Integer.parseInt(editPrice.getText().toString()), editLocation.getText().toString(), userId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x -> {
							Intent result = new Intent()
									.putExtra(RequestActivity.RESULT_IMAGE_ID, x.id);
							setResult(Activity.RESULT_OK, result);
							finish();
						}
						, error -> {
							postButton.setEnabled(true);
							Log.e("Error", "");
						}
				);
	}

	public void onPickResult(PickResult r) {
		if (r.getError() == null) {
			//If you want the Uri.
			//Mandatory to refresh image from Uri.
			//getImageView().setImageURI(null);
			//selectedImage = r.getUri();

			//Setting the real returned image.
			//getImageView().setImageURI(r.getUri());

			//If you want the Bitmap.
			image.setImageBitmap(r.getBitmap());
			image.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.whiteDefaut, null));
			//Image path
			selectedImage = r.getPath();

			String address = null;
			try {
				address = LocationUtils.getLocation(this, new ExifInterface(selectedImage));
				editLocation.setText(address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//Handle possible errors
			//TODO: do what you have to do with r.getError();
			Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
