package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Request.RequestManager;
import mediaaction.android.logic.RxUtils;

public class RequestWallActivity extends AppCompatActivity {

	public static final String EXTRA_USER_ID = "RequestWallActivity.EXTRA_USER_ID";

	public static Intent prepare(Context context, String userId) {
		return new Intent(context, RequestWallActivity.class)
				.putExtra(EXTRA_USER_ID, userId);
	}

	private static String extractUserData(Intent intent) {
		return (String) intent.getSerializableExtra(EXTRA_USER_ID);
	}

	private RequestManager requestManager = new RequestManager(this);
	private String userId;
	private RequestWallAdapter requestWallAdapter;
	private RecyclerView recyclerView;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_wall);
		ButterKnife.bind(this);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Request Wall");

		userId = extractUserData(getIntent());
		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		RequestWallAdapter.OnRequestClickListener listener = request -> startActivity(RequestActivity.prepare(this, request, userId));

		requestManager.getRequests()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
				.subscribe(requestList -> {
							recyclerView.setAdapter(new RequestWallAdapter(requestList, listener));
						}
						, error ->
								Log.e("Error", "")
				);
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
