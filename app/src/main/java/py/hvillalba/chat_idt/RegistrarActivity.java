package py.hvillalba.chat_idt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import py.hvillalba.chat_idt.utils.Constant;

public class RegistrarActivity extends AppCompatActivity {

    EditText edNombre, edUserName, edPassword;
    Button btnRegistrar;
    private int READ_STATE_PHONE = 555;
    TelephonyManager tMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        edNombre = findViewById(R.id.edNombre);
        edUserName = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        leerNumero();
        initQB();
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edUserName.getText().toString();
                String password = edPassword.getText().toString();
                String nombre = edNombre.getText().toString();

                QBUser qbUser = new QBUser(user, password);
                qbUser.setFullName(nombre);

                QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        RegistrarActivity.this.finish();
                        Log.e("Registrar", "Se registro correctamente");
                        Toast.makeText(RegistrarActivity.this, "Se registro correctamente",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(RegistrarActivity.this, "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void initQB() {
        QBSettings.getInstance().init(RegistrarActivity.this, Constant.APPLICATION_ID,
                Constant.AUTH_KEY,
                Constant.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.ACCOUNT_KEY);
    }

    private void leerNumero() {
        tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) RegistrarActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, READ_STATE_PHONE);
            return;
        }
        String mPhoneNumber = tMgr.getDeviceId();
        edUserName.setText(mPhoneNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STATE_PHONE){
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                @SuppressLint("MissingPermission")
                String mPhoneNumber = tMgr.getDeviceId();
                edUserName.setText(mPhoneNumber);
            }
        }
    }
}
