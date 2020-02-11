package py.hvillalba.chat_idt.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import py.hvillalba.chat_idt.ChatMessageActivity;
import py.hvillalba.chat_idt.R;

public class RecyclerViewListUser extends RecyclerView.Adapter<CustomViewHolderUserList> {
    private Context context;
    private List<QBUser> qbUserList = new ArrayList<>();

    //Constructor
    public RecyclerViewListUser(Context context, List<QBUser> list){
        this.context = context;
        this.qbUserList = list;
    }

    @NonNull
    @Override
    public CustomViewHolderUserList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_user, null);
        CustomViewHolderUserList customViewHolderUserList = new CustomViewHolderUserList(view);
        return customViewHolderUserList;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderUserList holder, int i) {
        final QBUser qbUser = qbUserList.get(holder.getAdapterPosition());
        holder.textView.setText(qbUser.getFullName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamar Chat Activity
                //Llamar Chat Activity
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(qbUser.getId());
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        Intent intent = new Intent(context, ChatMessageActivity.class);
                        intent.putExtra("dialogs", qbChatDialog);
                        context.startActivity(intent);
                        Log.e("Success", "satisfactorio");
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error", e.getMessage());
                    }
                });
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamar Chat Activity
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(qbUser.getId());
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        Intent intent = new Intent(context, ChatMessageActivity.class);
                        intent.putExtra("dialogs", qbChatDialog);
                        context.startActivity(intent);
                        Log.e("Success", "satisfactorio");
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error", e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return qbUserList.size();
    }
}
