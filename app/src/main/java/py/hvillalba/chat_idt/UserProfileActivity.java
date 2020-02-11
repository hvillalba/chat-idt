package py.hvillalba.chat_idt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class UserProfileActivity extends AppCompatActivity {

    EditText edNombre, edEmail, edPassword, edTelefono,edUserName, edOldPassword;
    Button btnActualizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        edEmail = findViewById(R.id.edEmail);
        edNombre = findViewById(R.id.edNombre);
        edPassword = findViewById(R.id.edPassword);
        edTelefono = findViewById(R.id.edTelefono);
        edUserName = findViewById(R.id.edUsername);
        edOldPassword = findViewById(R.id.edOldPassword);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBUser qbUser = new QBUser();
                qbUser.setId(QBChatService.getInstance().getUser().getId());
                qbUser.setPassword(edPassword.getText().toString());
                qbUser.setFullName(edNombre.getText().toString());
                qbUser.setEmail(edEmail.getText().toString());
                qbUser.setLogin(edUserName.getText().toString());
                qbUser.setPhone(edTelefono.getText().toString());
                qbUser.setOldPassword(edOldPassword.getText().toString());

                QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(UserProfileActivity.this,
                                "Se actualizo correctamente", Toast.LENGTH_LONG).show();
                        UserProfileActivity.this.finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error", e.getMessage());
                    }
                });
            }
        });
    }
}
