package mediaaction.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mediaaction.android.R;
import mediaaction.android.core.UserDTO;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);

		UserDTO userData = extractUserData(getIntent());
		if (userData != null) {
			profileName.setText(userData.username);
			salesCount.setText("0");
			averagePrice.setText("0");
		}
	}

	@OnClick(R.id.myUploadButton)
	public void myUploadClick(View view) {
		Intent intent = new Intent(this, PhotoListActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.uploadButton)
	public void uploadPhotoClick(View view) {
		Intent intent = new Intent(this, UploadActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.mySoldPhotos)
	public void mySoldPhotosClick(View view) {
		Intent intent = new Intent(this, PhotoListActivity.class);
		startActivity(intent);
	}
}
