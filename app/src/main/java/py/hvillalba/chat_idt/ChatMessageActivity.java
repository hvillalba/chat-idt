package py.hvillalba.chat_idt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

import py.hvillalba.chat_idt.recycler.RecyclerViewMessageAdapter;
import py.hvillalba.chat_idt.utils.cache.QBChatMessageHolder;

public class ChatMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewMessageAdapter adapter;
    private ImageButton submitButton;
    private EditText editText;
    QBChatDialog qbChatDialog;
    private Toolbar toolbar;
    private TextView tvNombreUsuario, tvEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        toolbar = findViewById(R.id.toolbar_top);
        tvNombreUsuario = toolbar.findViewById(R.id.toolbar_title);
        tvEstado = toolbar.findViewById(R.id.estado);
        initViews();
        initChatDialog();
        retriveMessages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.list_of_message);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        editText = findViewById(R.id.edt_content);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    qbChatDialog.sendIsTypingNotification();
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    qbChatDialog.sendStopTypingNotification();
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        });
        submitButton = findViewById(R.id.send_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")){
                    return;
                }
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(editText.getText().toString());
                qbChatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                qbChatMessage.setSaveToHistory(true);
                try {
                    qbChatDialog.sendMessage(qbChatMessage);
                } catch (SmackException.NotConnectedException e) {
                    Log.e("sendMessage", e.getMessage());
                }
                //Agrega el mensaje en el cache (Singleton)
                QBChatMessageHolder.getInstance().putMessage(qbChatDialog.getDialogId(), qbChatMessage);
                //Obtiene la lista por DialogId
                List<QBChatMessage> lista = QBChatMessageHolder.getInstance()
                        .getMessagesByDialogId(qbChatDialog.getDialogId());
                //Seteamos el adapter con la lista obtenida
                //adapter = new RecyclerViewMessageAdapter(ChatMessageActivity.this, lista);
                //recyclerView.setAdapter(adapter);
                adapter.agregarItem(lista);
                recyclerView.scrollToPosition(adapter.getItemCount() -1);
                //Se va hasta la ultima posicion
                //Limpiamos el campo de texto
                editText.setText("");
                editText.setFocusable(true);
            }
        });
    }

    private void retriveMessages() {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);
        if (qbChatDialog != null){
            QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    QBChatMessageHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatMessages);
                    adapter = new RecyclerViewMessageAdapter(getBaseContext(), qbChatMessages);
                    recyclerView.setAdapter(adapter);
                    //Se va hasta la ultima posicion
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter.getItemCount() > 0){
                                recyclerView.smoothScrollToPosition(adapter.getItemCount() -1 );
                            }
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

    private void initChatDialog() {
        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra("dialogs");
        qbChatDialog.initForChat(QBChatService.getInstance());
        //setTitle(qbChatDialog.getName());
        tvNombreUsuario.setText(qbChatDialog.getName());
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("processMessage", s);
                Log.e("qbChatMessage", qbChatMessage.getSmackMessage().getBody());
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("Error", e.getMessage());
            }
        });
        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                QBChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessageHolder.getInstance().getMessagesByDialogId(qbChatMessage.getDialogId());
                //adapter = new RecyclerViewMessageAdapter(getBaseContext(), messages);
                //recyclerView.setAdapter(adapter);
                adapter.agregarItem(messages);
                recyclerView.scrollToPosition(adapter.getItemCount() -1);
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });

        registerTyping(qbChatDialog);
    }

    private void registerTyping(QBChatDialog qbChatDialog) {
        QBChatDialogTypingListener listener = new QBChatDialogTypingListener() {
            @Override
            public void processUserIsTyping(String s, Integer integer) {
                tvEstado.setVisibility(View.VISIBLE);
            }

            @Override
            public void processUserStopTyping(String s, Integer integer) {
                tvEstado.setVisibility(View.GONE);
            }
        };
        qbChatDialog.addIsTypingListener(listener);
    }


}
