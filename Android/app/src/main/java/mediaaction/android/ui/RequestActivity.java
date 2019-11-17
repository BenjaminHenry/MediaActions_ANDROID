package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import mediaaction.android.logic.Request.RequestDTO;
import mediaaction.android.logic.Request.RequestManager;
import mediaaction.android.logic.RxUtils;

public class RequestActivity extends AppCompatActivity {

	public static final String EXTRA_REQUEST_DATA = "RequestActivity.EXTRA_REQUEST_DATA";
	public static final String EXTRA_USER_ID = "RequestActivity.EXTRA_USER_ID";
	public static final String RESULT_IMAGE_ID = "RequestActivity.RESULT_IMAGE_ID";

	private static final int REQUEST_UPLOAD_CODE = 1;

	public static Intent prepare(Context context, RequestDTO requestData, String userid) {
		return new Intent(context, RequestActivity.class)
				.putExtra(EXTRA_REQUEST_DATA, requestData)
				.putExtra(EXTRA_USER_ID, userid);
	}

	private static RequestDTO extractRequestData(Intent intent) {
		return (RequestDTO) intent.getSerializableExtra(EXTRA_REQUEST_DATA);
	}

	private static String extractUserId(Intent intent) {
		return intent.getStringExtra(EXTRA_USER_ID);
	}

	@BindView(R.id.RequestTitle)
	TextView requestTitle;
	@BindView(R.id.RequestDescription)
	TextView requestDescription;
	@BindView(R.id.RequestReward)
	TextView requestReward;

	private RequestManager requestManager = new RequestManager(this);

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		ButterKnife.bind(this);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle("Request");

		RequestDTO requestData = extractRequestData(getIntent());

		requestTitle.setText(requestData.title);
		requestDescription.setText(requestData.description);
		requestReward.setText("Reward : " + requestData.reward.toString() + " €");
	}

	@OnClick(R.id.AnswerRequestButton)
	void AnswerRequestClick(View view) {
		startActivityForResult(UploadActivity.prepare(this, extractUserId(getIntent())), REQUEST_UPLOAD_CODE);
	}

	@SuppressLint("CheckResult")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_UPLOAD_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				String imageId = data.getStringExtra(RESULT_IMAGE_ID);
				requestManager.answerRequest(extractRequestData(getIntent()).id, extractUserId(getIntent()), imageId)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeOn(Schedulers.newThread())
						.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
						.subscribe(x -> finish()
								, error ->
										Log.e("Error", "")
						);
			}
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
