package mediaaction.android.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.R;
import mediaaction.android.logic.Account.AccountManager;
import mediaaction.android.logic.RxUtils;

public class RegisterActivity extends AppCompatActivity {

	@BindView(R.id.userInput)
	EditText userInput;
	@BindView(R.id.pwdInput)
	EditText pwdInput;
	@BindView(R.id.mailInput)
	EditText mailInput;
	@BindView(R.id.pwdConfInput)
	EditText pwdConfInput;

	private AccountManager accountManager = new AccountManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ButterKnife.bind(this);
		setTitle("Register");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@OnClick(R.id.registrationButtton)
	@SuppressLint("CheckResult")
	public void registerBtnClick(View view) {

		final String userName = String.valueOf(userInput.getText());
		final String mail = String.valueOf(mailInput.getText());
		final String pwd = String.valueOf(pwdInput.getText());
		final String pwdConf = String.valueOf(pwdConfInput.getText());

		if (userName.isEmpty() || mail.isEmpty() || pwd.isEmpty() || pwdConf.isEmpty()) {
			Toast.makeText(this, R.string.emptyFieldError, Toast.LENGTH_SHORT).show();
			return;
		}

		Pattern p1 = Pattern.compile(".*\\d+.*");
		Matcher m1 = p1.matcher(pwd);
		boolean b1 = m1.matches();

		Pattern p2 = Pattern.compile(".*[a-zA-Z]+.*");
		Matcher m2 = p2.matcher(pwd);
		boolean b2 = m2.matches();

		if (pwd.length() < 8 || !b1 || !b2) {
			Toast.makeText(this, "Password is not correct", Toast.LENGTH_LONG).show();
			return;
		}

		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
			Toast.makeText(this, "Email is not correct", Toast.LENGTH_LONG).show();
			return;
		}

		if (!pwd.equals(pwdConf)) {
			Toast.makeText(this, "Password don't correspond", Toast.LENGTH_LONG).show();
			return;
		}

		accountManager.register(mail, userName, pwd)
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

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
