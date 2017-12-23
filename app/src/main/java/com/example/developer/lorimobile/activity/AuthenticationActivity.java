
package com.example.developer.lorimobile.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.model.Token;
import com.example.developer.lorimobile.rest.APIFactory;
import com.example.developer.lorimobile.rest.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private EditText etLogin;
    private EditText etPassword;

    private boolean isVisiblePassword;

    private ImageView appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        isVisiblePassword=false;

        etLogin = (EditText)findViewById(R.id.etLogin);
        etPassword = (EditText)findViewById(R.id.etPassword);

        appIcon = (ImageView) findViewById(R.id.appIcon);
        appIcon.setImageResource(R.drawable.lori_black);

        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(!isVisiblePassword) {
                            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isVisiblePassword=true;
                        }
                        else {
                            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isVisiblePassword=false;
                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public void onBtnSignInClick(View view) {
        if (validateLogPass()) {
            APIService service = new APIFactory().getAPIService();

            Call<Token> call = service.signIn("password", etLogin.getText().toString(), etPassword.getText().toString());
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).edit().putString(TOKEN_KEY, response.body().getToken()).apply();
                        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.errorBody() != null) {
                        int code=response.code();
                        if(code>=400&&code<=451)
                        {
                            Toast.makeText(AuthenticationActivity.this,getString(R.string.error_invalid_login_or_password),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Toast.makeText(AuthenticationActivity.this,getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean validateLogPass() {
        boolean valid = true;
        if (TextUtils.isEmpty(etLogin.getText().toString())) {
            etLogin.setError(getString(R.string.error_empty_login));
            valid = false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.error_empty_password));
            valid = false;
        }
        return valid;
    }
}
