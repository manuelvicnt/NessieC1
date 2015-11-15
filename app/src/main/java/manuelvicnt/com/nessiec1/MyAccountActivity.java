package manuelvicnt.com.nessiec1;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {

    public static final String PASS_NAME_TO_ACCOUNT_ACTIVITY = "passNameToAccountActivity";
    public static final String PASS_BALANCE_TO_ACCOUNT_ACTIVITY = "passBalanceToAccountActivity";

    private TextView helloTextView;
    private TextView balanceTextView;

    private String name;
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helloTextView = (TextView) findViewById(R.id.hello);
        balanceTextView = (TextView) findViewById(R.id.balance);

        Bundle bundleFromPreviousActivity = getIntent().getExtras();
        getInfoFromBundle(bundleFromPreviousActivity);

        Resources resources = getResources();
        if (TextUtils.isEmpty(name) || balance < 0) {

            helloTextView.setText(resources.getString(R.string.account_error_message));
        } else {

            helloTextView.setText(resources.getString(R.string.account_hello_message) + name);
            balanceTextView.setText(resources.getString(R.string.account_balance_message) + balance);
        }
    }

    private void getInfoFromBundle(Bundle bundleFromPreviousActivity) {

        if (bundleFromPreviousActivity != null) {

            if (bundleFromPreviousActivity.containsKey(PASS_NAME_TO_ACCOUNT_ACTIVITY)) {
                name = bundleFromPreviousActivity.getString(PASS_NAME_TO_ACCOUNT_ACTIVITY);
            }

            if (bundleFromPreviousActivity.containsKey(PASS_BALANCE_TO_ACCOUNT_ACTIVITY)) {
                balance = bundleFromPreviousActivity.getDouble(PASS_BALANCE_TO_ACCOUNT_ACTIVITY);
            } else {
                balance = -1;
            }
        } else {
            balance = -1;
        }
    }

}
