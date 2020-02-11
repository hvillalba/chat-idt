package py.hvillalba.chat_idt;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import py.hvillalba.chat_idt.recycler.RecyclerViewAdapterChatList;
import py.hvillalba.chat_idt.utils.cache.QBUserHolder;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterChatList adapterChatList;
    private FloatingActionButton floatingActionButton;
    String user;
    String pass;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        //Set Toolbar
        toolbar = findViewById(R.id.chat_dialog_toolbar);
        toolbar.setTitle("Chat IDT");
        setSupportActionBar(toolbar);

        user = getIntent().getStringExtra("user");
        pass = getIntent().getStringExtra("pass");
        recyclerView = findViewById(R.id.recyclerChatList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        floatingActionButton = findViewById(R.id.btnChat);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChatListActivity.this, ListUserActivity.class);
                startActivity(i);
            }
        });
        createSessionForChat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_dialog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat_dialog_menu_user:
                showUserProfile();
                break;
            default:
                break;
        }
        return true;
    }

    private void showUserProfile() {
        Intent i = new Intent(ChatListActivity.this, UserProfileActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        leerChats();
    }

    private void leerChats() {
        QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();
        QBRestChatService.getChatDialogs(null, qbRequestGetBuilder)
                .performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                adapterChatList = new RecyclerViewAdapterChatList(ChatListActivity.this, qbChatDialogs);
                recyclerView.setAdapter(adapterChatList);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });
    }

    private void createSessionForChat() {
        final QBUser qbUser = new QBUser(user, pass);
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUserHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("QBResponseException", e.getMessage());
            }
        });

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e("onSuccess", "Proceso satisfactorio");
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("onError", e.getMessage());
                    }
                });


            }
            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });
    }
}
