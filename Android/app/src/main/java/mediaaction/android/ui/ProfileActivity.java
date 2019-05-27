package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.SessionManager;
import mediaaction.android.logic.FragmentBuilder;
import mediaaction.android.logic.PhotoListType;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.UploadType;
import mediaaction.android.logic.User.UserDTO;
import mediaaction.android.logic.User.UserManager;
import mediaaction.android.utils.IntentUtils;

public class ProfileActivity extends AppCompatActivity {

	private static final Integer REQUEST_UPLOAD_IMAGE = 1;
	public static final Integer REQUEST_IMAGE_DELETE = 2;

	public static final String EXTRA_USER_DATA = "ProfileActivity.EXTRA_USER_DATA";

	public static Intent prepare(Context context, UserDTO userData) {
		return new Intent(context, ProfileActivity.class)
				.putExtra(EXTRA_USER_DATA, userData)
				.addFlags(IntentUtils.FLAGS_CLEAN);
	}

	private static UserDTO extractUserData(Intent intent) {
		return (UserDTO) intent.getSerializableExtra(EXTRA_USER_DATA);
	}

	@BindView(R.id.profileName)
	TextView profileName;
	@BindView(R.id.salesCount)
	TextView salesCount;
	@BindView(R.id.uploadCount)
	TextView uploadCount;
	@BindView(R.id.settingsButton)
	ImageView settingsButton;

	@BindView(R.id.viewPager)
	ViewPager viewPager;

	private SessionManager sessionManager;
	private UserManager userManager = new UserManager();
	private UserDTO userData;
	private Integer soldPhotos = 0;
	private Integer uploadedPhotos = 0;

	@SuppressLint({"SetTextI18n", "CheckResult"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);
		setTitle("Your profile");

		sessionManager = new SessionManager(this);

		salesCount.setText("0");
		uploadCount.setText("0");

		userData = extractUserData(getIntent());
		profileName.setText(userData.username);

		updateList(userData.id);
	}


	public void logoutAction() {
		new android.app.AlertDialog.Builder(this)
				.setMessage("Do you want to logout ?")
				.setPositiveButton("yes", (dialog, id) -> {
					sessionManager.clearSession();
					startActivity(ConnectionActivity.prepare(ProfileActivity.this));
				})
				.setNegativeButton("cancel", null)
				.create().show();
	}

	@OnClick(R.id.fabUploadPhoto)
	public void myUploadClick(View view) {
		startActivityForResult(UploadActivity.prepare(this, userData.id, UploadType.GALLERY), REQUEST_UPLOAD_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_UPLOAD_IMAGE || requestCode == REQUEST_IMAGE_DELETE) {
			if (resultCode == Activity.RESULT_OK) {
				updateList(userData.id);
			}
		}
	}

	@OnClick(R.id.settingsButton)
	public void showMenu() {
		PopupMenu popup = new PopupMenu(this, settingsButton);
		//Inflating the Popup using xml file
		popup.getMenuInflater()
				.inflate(R.menu.profile_menu, popup.getMenu());

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getItemId() == R.id.logout) {
					logoutAction();
				}
				if (item.getItemId() == R.id.parameters) {
					/*TODO*/
				}
				return true;
			}
		});

		popup.show(); //showing popup menu
	}

	@OnClick(R.id.requestsWallButton)
	public void requestWallClick() {
		startActivity(RequestWallActivity.prepare(getApplicationContext(), userData.id));
	}


	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		PhotoListFragment[] fragments = new PhotoListFragment[2];
		String[] tabTitles;

		CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
			if (uploadedPhotos == 0)
				fragments[0] = preparePageFragment(PhotoListType.EMPTY, userData.id);
			else
				fragments[0] = preparePageFragment(PhotoListType.UPLOADED, userData.id);
			if (soldPhotos == 0)
				fragments[1] = preparePageFragment(PhotoListType.EMPTY, userData.id);
			else
				fragments[1] = preparePageFragment(PhotoListType.SOLD, userData.id);
			tabTitles = new String[]{"Uploaded", "Sold"};
		}

		@Override
		public Fragment getItem(int i) {
			return fragments[i];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return tabTitles[position];
		}

		private PhotoListFragment preparePageFragment(PhotoListType type, String userId) {
			if (type == PhotoListType.EMPTY)
				Log.i("PREPAREFRAGMENT", "EMPTY");
			if (type == PhotoListType.SOLD)
				Log.i("PREPAREFRAGMENT", "SOLD");
			if (type == PhotoListType.UPLOADED)
				Log.i("PREPAREFRAGMENT", "UPLOADED");
			return FragmentBuilder.prepare(new PhotoListFragment())
					.put(PhotoListFragment.ARG_PHOTO_LIST_TYPE, type)
					.put(PhotoListFragment.ARG_USER_ID, userId)
					.build();
		}
	}

	@SuppressLint("CheckResult")
	public void updateList(String userId) {
		userManager.getSoldPhotos(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(y -> {
							soldPhotos = y.size();
							salesCount.setText(String.valueOf(soldPhotos));
							userManager.getUserUploads(userData.id)
									.observeOn(AndroidSchedulers.mainThread())
									.subscribeOn(Schedulers.newThread())
									.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
									.subscribe(z -> {
												uploadedPhotos = z.size();
												uploadCount.setText(String.valueOf(uploadedPhotos));
												CollectionPagerAdapter collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
												viewPager.setAdapter(collectionPagerAdapter);
											}
											, error ->
													Log.e("Error", "")
									);
						}
						, error ->
								Log.e("Error", "")
				);
	}
}
