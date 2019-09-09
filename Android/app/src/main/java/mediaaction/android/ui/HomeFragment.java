package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.SessionManager;
import mediaaction.android.logic.FragmentBuilder;
import mediaaction.android.logic.PhotoListType;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.logic.User.UserDTO;
import mediaaction.android.logic.User.UserManager;

public class HomeFragment extends RxFragment {

	private static final Integer REQUEST_UPLOAD_IMAGE = 1;
	public static final Integer REQUEST_IMAGE_DELETE = 2;

	public static Fragment prepare() {
		return FragmentBuilder.prepare(new HomeFragment())
				.build();
	}

	@BindView(R.id.profileName)
	TextView profileName;
	@BindView(R.id.salesCount)
	TextView salesCount;
	@BindView(R.id.uploadCount)
	TextView uploadCount;

	@BindView(R.id.viewPager)
	ViewPager viewPager;

	private SessionManager sessionManager;
	private UserManager userManager = new UserManager();
	private UserDTO userData;
	private Integer soldPhotos = 0;
	private Integer uploadedPhotos = 0;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		sessionManager = new SessionManager(getContext());

		salesCount.setText("0");
		uploadCount.setText("0");

		userData = ((HomeActivity) Objects.requireNonNull(getActivity())).userData;
		profileName.setText(userData.username);

		updateList(userData.id);
	}


	public void logoutAction() {
		Context context = getContext();
		if (context != null) {
			new android.app.AlertDialog.Builder(context)
					.setMessage("Do you want to logout ?")
					.setPositiveButton("yes", (dialog, id) -> {
						sessionManager.clearSession();
						startActivity(ConnectionActivity.prepare(context));
					})
					.setNegativeButton("cancel", null)
					.create().show();
		}
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

		Context context = getContext();
		if (context != null) {
			userManager.getSoldPhotos(userData.id)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
					.subscribe(y -> {
								soldPhotos = y.size();
								salesCount.setText(String.valueOf(soldPhotos));
								userManager.getUserUploads(userData.id)
										.observeOn(AndroidSchedulers.mainThread())
										.subscribeOn(Schedulers.newThread())
										.compose(RxUtils.displayCommonRestErrorDialogSingle(context))
										.subscribe(z -> {
													uploadedPhotos = z.size();
													uploadCount.setText(String.valueOf(uploadedPhotos));
													CollectionPagerAdapter collectionPagerAdapter = new CollectionPagerAdapter(getFragmentManager());
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
}
