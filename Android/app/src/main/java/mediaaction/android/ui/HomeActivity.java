package mediaaction.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import mediaaction.android.R;
import mediaaction.android.logic.User.UserDTO;
import mediaaction.android.utils.IntentUtils;

public class HomeActivity extends AppCompatActivity implements IPickResult {

	public static final String EXTRA_USER_DATA = "HomeFragment.EXTRA_USER_DATA";

	public static Intent prepare(Context context, UserDTO userData) {
		return new Intent(context, HomeActivity.class)
				.putExtra(EXTRA_USER_DATA, userData)
				.addFlags(IntentUtils.FLAGS_CLEAN);
	}

	private static UserDTO extractUserData(Intent intent) {
		return (UserDTO) intent.getSerializableExtra(EXTRA_USER_DATA);
	}

	UserDTO userData;

	@BindView(R.id.bottomNavigation)
	BottomNavigationView bottomNavigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		setTitle("Media Actions");

		userData = extractUserData(getIntent());
		setTitle("Home");

		bottomNavigation.setSelectedItemId(R.id.action_home);
		bottomNavigation.setOnNavigationItemSelectedListener(item -> {
			switch (item.getItemId()) {
				case R.id.action_home: {
					changeFragment(HomeFragment.prepare());
					setTitle("Home");
					break;
				}
				case R.id.action_take_picture: {
					changeFragment(UploadFragment.prepare());
					setTitle("Take picture");
					break;
				}
				case R.id.action_request_wall:
					changeFragment(RequestWallFragment.prepare(userData.id));
					setTitle("Request wall");
					break;
				case R.id.action_parameters:
					changeFragment(ParametersFragment.prepare());
					setTitle("Parameters");
					break;
			}
			return true;
		});

		final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout, new HomeFragment());
		transaction.commit();
	}

	public void changeFragment(Fragment selectedFragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_layout, selectedFragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	void pushFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_layout, fragment, fragment.getClass().getName())
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(fragment.getClass().getName())
				.commit();
	}

	@Override
	public void onPickResult(PickResult pickResult) {
		UploadFragment uploadFragment = (UploadFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
		uploadFragment.PickResult(pickResult);
	}
}
