package py.hvillalba.chat_idt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import io.realm.Realm;
import py.hvillalba.chat_idt.db.User;
import py.hvillalba.chat_idt.utils.Constant;

public class LoginActivity extends AppCompatActivity {

    EditText edUserName, edPassword;
    Button btnLogin;
    Button btnRegistrar;
    SharedPreferences sharedPreferences;
    Realm realm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("chat", Context.MODE_PRIVATE);
        edUserName = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(i);
            }
        });
        initQB();
        //verificarLogin();
        

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = edUserName.getText().toString();
                final String password = edPassword.getText().toString();

                QBUser qbUser = new QBUser(user, password);
                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("logueado", true);
                        editor.apply();

//                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
//                        editor1.putString("user", user);
//                        editor1.apply();
//
//                        SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                        editor2.putString("password", password);
//                        editor2.apply();
                        realm.beginTransaction();
                        User usuario = new User();
                        usuario.setId(1);
                        usuario.setEmail(user);
                        usuario.setPassword(password);
                        usuario.setFullName(qbUser.getFullName());
                        realm.copyToRealmOrUpdate(usuario);
                        realm.commitTransaction();

                        usuario = realm.where(User.class).equalTo("id",1).findFirst();

                        LoginActivity.this.finish();
                        Intent intent = new Intent(LoginActivity.this, ChatListActivity.class);
                        intent.putExtra("user", usuario.getEmail());
                        intent.putExtra("pass", usuario.getPassword());
                        startActivity(intent);

                        //Borra la tabla User
//                        realm.where(User.class)
//                                .equalTo("id", 1)
//                                .findAll()
//                                .deleteAllFromRealm();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void verificarLogin() {
        final String user = sharedPreferences.getString("user","");
        final String password = sharedPreferences.getString("password", "");
        QBUser qbUser = new QBUser(user, password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logueado", true);
                editor.apply();

                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString("user", user);
                editor1.apply();

                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                editor2.putString("password", password);
                editor2.apply();

                LoginActivity.this.finish();
                Intent intent = new Intent(LoginActivity.this, ChatListActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("pass", password);
                startActivity(intent);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initQB() {
        QBSettings.getInstance().init(LoginActivity.this, Constant.APPLICATION_ID,
                Constant.AUTH_KEY,
                Constant.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.ACCOUNT_KEY);
    }
}
