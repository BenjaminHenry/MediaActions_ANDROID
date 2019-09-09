package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.core.SessionManager;
import mediaaction.android.logic.Account.AccountManager;
import mediaaction.android.logic.RxUtils;
import mediaaction.android.utils.IntentUtils;

public class ConnectionActivity extends AppCompatActivity {

	public static Intent prepare(Context context) {
		return new Intent(context, ConnectionActivity.class)
				.addFlags(IntentUtils.FLAGS_CLEAN);
	}

	@BindView(R.id.editTextEmail)
	EditText username;
	@BindView(R.id.editTextPassword)
	EditText userPassword;

	private AccountManager accountManager = new AccountManager(this);
	private SessionManager sessionManager;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		ButterKnife.bind(this);
		setTitle("Login");

		sessionManager = new SessionManager(this);

		String userName = sessionManager.getUserName();
		String userPWD = sessionManager.getUserPwd();
		if (userName != null && userPWD != null) {
			Log.d("USERNAME & USERPWD", userName + " " + userPWD);
			accountManager.login(userName, userPWD)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(userData ->
									startActivity(HomeActivity.prepare(this, userData))
							, error ->
									Log.e("Error", "")
					);
		}
	}

	@OnClick(R.id.loginButton)
	@SuppressLint("CheckResult")
	public void loginButtonClick(View view) {
		if (username.getText() != null && userPassword.getText() != null) {
			accountManager.login(username.getText().toString(), userPassword.getText().toString())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(userData -> {
								if (userData.role.equals("SELLER"))
									startActivity(HomeActivity.prepare(this, userData));
								else {
									new android.app.AlertDialog.Builder(this)
											.setMessage("Application is for seller only, you're trying to connect with a buyer account.")
											.setNeutralButton("Ok", (dialog, id) -> {
											})
											.create().show();
								}
							}
							, error ->
									Log.e("Error", "")
					);
		}
	}

	@OnClick(R.id.registerButton)
	public void registerBtnFn(View view) {
		startActivity(new Intent(this, RegisterActivity.class));
	}

	@OnClick(R.id.forgetPasswordClickText)
	public void forgetPasswordClick(View view) {
		startActivity(new Intent(this, ForgetPasswordActivity.class));
	}
}
