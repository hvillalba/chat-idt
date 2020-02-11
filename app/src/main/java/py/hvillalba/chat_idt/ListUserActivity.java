package py.hvillalba.chat_idt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import py.hvillalba.chat_idt.recycler.RecyclerViewListUser;
import py.hvillalba.chat_idt.utils.cache.QBUserHolder;

public class ListUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewListUser adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        recyclerView = findViewById(R.id.recyclerUserList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        getAllUsers();
    }

    private void getAllUsers() {
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUserHolder.getInstance().putUsers(qbUsers);
                List<QBUser> lista = new ArrayList<>();
                for (QBUser user : qbUsers){
                    if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())){
                        lista.add(user);

                    }
                }
                adapter = new RecyclerViewListUser(ListUserActivity.this, lista);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", e.getMessage());
            }
        });
    }
}
