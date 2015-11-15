package manuelvicnt.com.nessiec1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.reimaginebanking.api.java.Constants.AccountType;
import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.models.Account;

public class MainActivity extends AppCompatActivity {

    private NessieClient nessieClient;

    private Button loginButton;
    private Button submitButton;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submitButton = (Button) findViewById(R.id.submit_button);
        handleOnClickOnSubmitButton();

        loginButton = (Button) findViewById(R.id.login_button);
        handleOnClickOnLoginButton();

        nameEditText = (EditText) findViewById(R.id.customer_name);

        nessieClient = NessieClient.getInstance();
        nessieClient.setAPIKey("YOUR-API-KEY");
    }

    private void handleOnClickOnLoginButton() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String customerName = nameEditText.getText().toString();

                if (TextUtils.isEmpty(customerName)) {

                    displayValidationErrorDialog();
                } else {

                    final ProgressDialog progressDialog = showLoadingMessage();
                    getCustomerAccount(customerName, progressDialog);
                }
            }
        });
    }

    private void handleOnClickOnSubmitButton() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String customerName = nameEditText.getText().toString();

                if (TextUtils.isEmpty(customerName)) {

                    displayValidationErrorDialog();
                } else {

                    final ProgressDialog progressDialog = showLoadingMessage();
                    createCustomerAccount(customerName, progressDialog);
                }
            }
        });
    }

    private void createCustomerAccount(final String customerName, final ProgressDialog progressDialog) {

        nessieClient.createAccount(customerName, createAccount(customerName), new NessieResultsListener() {
            @Override
            public void onSuccess(Object o, NessieException e) {

                if (e == null) {

                    getCustomerAccount(customerName, progressDialog);
                } else {

                    progressDialog.dismiss();
                    displayCreateAccountError();
                }
            }
        });
    }

    private void getCustomerAccount(String customerName, final ProgressDialog progressDialog) {

        nessieClient.getAccount(customerName, new NessieResultsListener() {
            @Override
            public void onSuccess(Object o, NessieException e) {

                progressDialog.dismiss();

                if (e == null) {

                    goToMyAccount((Account) o);
                } else {

                    displayLoginAccountError();
                }
            }
        });
    }

    private void goToMyAccount(Account myAccount) {

        Intent goToMyAccountIntent = new Intent(MainActivity.this, MyAccountActivity.class);
        goToMyAccountIntent.putExtra(MyAccountActivity.PASS_NAME_TO_ACCOUNT_ACTIVITY,
                myAccount.getCustomer());

        goToMyAccountIntent.putExtra(MyAccountActivity.PASS_BALANCE_TO_ACCOUNT_ACTIVITY,
                Double.valueOf(myAccount.getBalance()));

        goToMyAccountIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(goToMyAccountIntent);

    }

    private Account createAccount(String customerName) {

        return new Account( customerName, AccountType.CREDITCARD,
                            customerName, 0, 0, null,
                            customerName);
    }

    private ProgressDialog showLoadingMessage() {

        return ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
    }

    private void displayValidationErrorDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Your name field cannot be empty")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void displayLoginAccountError() {

        new AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage("We couldn't log you in")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void displayCreateAccountError() {

        new AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage("We couldn't create your account")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
