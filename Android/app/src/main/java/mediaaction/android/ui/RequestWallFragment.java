package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.FragmentBuilder;
import mediaaction.android.logic.Request.RequestManager;
import mediaaction.android.logic.RxUtils;

public class RequestWallFragment extends RxFragment {

	public static final String EXTRA_USER_ID = "RequestWallFragment.EXTRA_USER_ID";

	public static Fragment prepare(String userId) {
		return FragmentBuilder.prepare(new RequestWallFragment())
				.put(EXTRA_USER_ID, userId)
				.build();
	}

	private RequestManager requestManager;
	private String userId;
	private RequestWallAdapter requestWallAdapter;

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_request_wall, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@SuppressLint("CheckResult")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			userId = (String) getArguments().get(EXTRA_USER_ID);
		}

		requestManager = new RequestManager(getContext());
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		RequestWallAdapter.OnRequestClickListener listener = request -> startActivity(RequestActivity.prepare(getContext(), request, userId));

		requestManager.getRequests()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(getContext()))
				.subscribe(requestList -> {
							recyclerView.setAdapter(new RequestWallAdapter(requestList, listener));
						}
						, error ->
								Log.e("Error", "")
				);
	}
}
