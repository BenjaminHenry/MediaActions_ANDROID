package mediaaction.android.ui;

import android.annotation.SuppressLint;
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
import mediaaction.android.logic.Account.AccountManager;
import mediaaction.android.logic.RxUtils;

public class ConnectionActivity extends AppCompatActivity {

	@BindView(R.id.editTextEmail)
	EditText username;
	@BindView(R.id.editTextPassword)
	EditText userPassword;

	private AccountManager accountManager = new AccountManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.loginButton)
	@SuppressLint("CheckResult")
	public void loginButtonClick(View view) {
		if (username.getText() != null && userPassword.getText() != null) {
			accountManager.login(username.getText().toString(), userPassword.getText().toString())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.newThread())
					.compose(RxUtils.displayCommonRestErrorDialogSingle(this))
					.subscribe(userData ->
									startActivity(new Intent(this, ProfileActivity.class)
									.putExtra(ProfileActivity.EXTRA_USER_DATA, userData))
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
