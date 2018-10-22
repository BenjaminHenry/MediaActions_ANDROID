package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.UserDTO;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserManager;

public class ProfileActivity extends AppCompatActivity {

	public static final String EXTRA_USER_DATA = "ProfileActivity.EXTRA_USER_DATA";

	private static Intent prepare(Context context, UserDTO userData) {
		return new Intent(context, ProfileActivity.class)
				.putExtra(EXTRA_USER_DATA, userData);
	}

	private static UserDTO extractUserData(Intent intent) {
		return (UserDTO) intent.getSerializableExtra(EXTRA_USER_DATA);
	}

	@BindView(R.id.profileName)
	TextView profileName;
	@BindView(R.id.salesCount)
	TextView salesCount;
	@BindView(R.id.averagePrice)
	TextView averagePrice;

	//private SessionManager sessionManager = new SessionManager(this);
	private UserManager userManager = new UserManager();

	@SuppressLint({"SetTextI18n", "CheckResult"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);


		averagePrice.setText("average prices : 0");
		salesCount.setText("sold photo : 0");

		UserDTO userData = extractUserData(getIntent());

		profileName.setText(userData.username);
		userManager.getAvgSellsPrice(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x ->
								averagePrice.setText("average prices : " + x.avg)
						, error ->
								Log.e("Error", "")
				);

		userManager.getSoldPhotos(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x ->
								salesCount.setText("sold photo : " + x.size())
						, error ->
								Log.e("Error", "")
				);
	}

	@OnClick(R.id.myUploadButton)
	public void myUploadClick(View view) {
		Intent intent = new Intent(this, PhotoListActivity.class);
		intent.putExtra(PhotoListActivity.EXTRA_PHOTO_LIST_TYPE, PhotoListActivity.EXTRA_TYPE_MY_UPLOAD);
		intent.putExtra(PhotoListActivity.EXTRA_USER_ID, extractUserData(getIntent()).id);
		startActivity(intent);
	}

	@OnClick(R.id.uploadPhotoButton)
	public void uploadPhotoClick(View view) {
		Intent intent = new Intent(this, UploadActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.mySoldPhotos)
	public void mySoldPhotosClick(View view) {
		Intent intent = new Intent(this, PhotoListActivity.class);
		intent.putExtra(PhotoListActivity.EXTRA_PHOTO_LIST_TYPE, PhotoListActivity.EXTRA_TYPE_SOLD_PHOTO);
		intent.putExtra(PhotoListActivity.EXTRA_USER_ID, extractUserData(getIntent()).id);
		startActivity(intent);
	}
}
