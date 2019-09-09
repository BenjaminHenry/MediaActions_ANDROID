package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.FragmentBuilder;
import mediaaction.android.logic.Gallery.GalleryManager;
import mediaaction.android.logic.Request.RequestManager;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.UploadType;
import mediaaction.android.logic.User.UserDTO;

public class UploadFragment extends RxFragment implements IPickResult {

	public static final String EXTRA_UPLOAD_TYPE = "HomeFragment.EXTRA_UPLOAD_TYPE";

	public static Fragment prepare(UploadType uploadType) {
		return FragmentBuilder.prepare(new UploadFragment())
				.put(EXTRA_UPLOAD_TYPE, uploadType)
				.build();
	}

	@BindView(R.id.addTitleEditText)
	EditText imageTitle;
	@BindView(R.id.editPrice)
	EditText editPrice;
	@BindView(R.id.editDescription)
	EditText editDescription;
	@BindView(R.id.selectedImage)
	ImageView image;
	@BindView(R.id.postButton)
	Button postButton;
	@BindView(R.id.alertText)
	TextView alertext;

	public String selectedImage;
	private UserDTO userData;
	private UploadType uploadType;
	private GalleryManager galleryManager;
	private RequestManager requestManager;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_upload, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			uploadType = (UploadType) getArguments().get(EXTRA_UPLOAD_TYPE);
		}

		userData = ((HomeActivity) Objects.requireNonNull(getActivity())).userData;
		galleryManager = new GalleryManager(getContext());
		requestManager = new RequestManager(getContext());
	}

	@OnClick(R.id.selectedImage)
	public void uploadAction(View view) {
		PickImageDialog.build(new PickSetup()).show(getFragmentManager());
	}

	@SuppressLint("CheckResult")
	@OnClick(R.id.postButton)
	public void Post(View view) {
		Context context = getContext();
		if (context == null)
			return;

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

		if (uploadType == UploadType.GALLERY) {
			galleryManager.uploadImage(encImage, "image/jpeg", imageTitle.getText().toString(), editDescription.getText().toString(), Integer.parseInt(editPrice.getText().toString()), userData.id)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
					.subscribe(x -> ((HomeActivity) getActivity()).changeFragment(HomeFragment.prepare())
							, error -> {
								postButton.setEnabled(true);
								Log.e("Error", "");
							}
					);
		} else {
			requestManager.uploadImageRequest(encImage, "image/jpeg", imageTitle.getText().toString(), editDescription.getText().toString(), Integer.parseInt(editPrice.getText().toString()), userData.id)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
					.subscribe(x -> ((HomeActivity) getActivity()).changeFragment(HomeFragment.prepare())
							, error -> {
								postButton.setEnabled(true);
								Log.e("Error", "");
							}
					);
		}
	}

	@Override
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
		} else {
			//Handle possible errors
			//TODO: do what you have to do with r.getError();
			Toast.makeText(getContext(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
