package mediaaction.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mediaaction.android.R;
import mediaaction.android.core.SessionManager;
import mediaaction.android.logic.FragmentBuilder;

public class ParametersFragment extends RxFragment {

	public static Fragment prepare() {
		return FragmentBuilder.prepare(new ParametersFragment())
				.build();
	}

	private SessionManager sessionManager;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_parameters, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		sessionManager = new SessionManager(getContext());
	}

	@OnClick(R.id.LogoutSection)
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
}
