package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.ImageDTO;
import mediaaction.android.core.PhotoListType;
import mediaaction.android.core.SessionManager;
import mediaaction.android.core.UserDTO;
import mediaaction.android.logic.FragmentBuilder;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserManager;
import mediaaction.android.utils.IntentUtils;

public class ProfileActivity extends AppCompatActivity {

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
	@BindView(R.id.averagePrice)
	TextView averagePrice;
	@BindView(R.id.profileMenu)
	ImageView profileMenu;

	@BindView(R.id.viewPager)
	ViewPager viewPager;

	private CollectionPagerAdapter collectionPagerAdapter;

	private SessionManager sessionManager;
	private UserManager userManager = new UserManager();
	private UserDTO userData;
	private Float avgPrice;
	private List<ImageDTO> uploadedPhotos = new ArrayList<ImageDTO>();

	@SuppressLint({"SetTextI18n", "CheckResult"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ButterKnife.bind(this);
		setTitle("Your profile");

		sessionManager = new SessionManager(this);

		salesCount.setText("sold photos : 0");
		averagePrice.setText("average price : 0 €");

		userData = extractUserData(getIntent());
		profileName.setText(userData.username);

		userManager.getAvgSellsPrice(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x ->
								avgPrice = Float.parseFloat(x.avg)
						, error ->
								Log.e("Error", "")
				);

		userManager.getSoldPhotos(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x ->
								salesCount.setText(String.valueOf(x.size()))
						, error ->
								Log.e("Error", "")
				);

		userManager.getUserUploads(userData.id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(x ->
								uploadedPhotos = new ArrayList<ImageDTO>(x)
						, error ->
								Log.e("Error", "")
				);

		if (avgPrice == null)
			averagePrice.setText("0 €");
		else
			averagePrice.setText(avgPrice + " €");

		collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(collectionPagerAdapter);
	}

	public void logoutAction() {
		new android.app.AlertDialog.Builder(this)
				.setMessage("Do you want to logout ?")
				.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						sessionManager.clearSession();
						startActivity(ConnectionActivity.prepare(ProfileActivity.this));
					}
				})
				.setNegativeButton("cancel", null)
				.create().show();
	}

	@OnClick(R.id.fabUploadPhoto)
	public void myUploadClick(View view) {
		Intent intent = new Intent(this, UploadActivity.class);
		intent.putExtra(UploadActivity.EXTRA_USER_ID, userData.id);
		startActivity(intent);
	}

	@OnClick(R.id.profileMenu)
	public void showMenu() {
		PopupMenu popup = new PopupMenu(this, profileMenu);
		//Inflating the Popup using xml file
		popup.getMenuInflater()
				.inflate(R.menu.profile_menu, popup.getMenu());

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				if (item.getItemId() == R.id.logout) {
					logoutAction();
				}
				return true;
			}
		});

		popup.show(); //showing popup menu
	}

	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		PhotoListFragment[] fragments = new PhotoListFragment[2];
		String[] tabTitles;

		CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
			if (uploadedPhotos.size() == 0)
				fragments[0] = preparePageFragment(PhotoListType.EMPTY, userData.id);
			else
				fragments[0] = preparePageFragment(PhotoListType.UPLOADED, userData.id);
			if (avgPrice == null)
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
			return FragmentBuilder.prepare(new PhotoListFragment())
					.put(PhotoListFragment.ARG_PHOTO_LIST_TYPE, type)
					.put(PhotoListFragment.ARG_USER_ID, userId)
					.build();
		}
	}
}
