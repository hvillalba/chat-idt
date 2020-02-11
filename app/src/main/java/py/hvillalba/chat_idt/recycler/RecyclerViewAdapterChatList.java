package py.hvillalba.chat_idt.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.chat.model.QBChatDialog;

import java.text.SimpleDateFormat;
import java.util.List;

import py.hvillalba.chat_idt.ChatMessageActivity;
import py.hvillalba.chat_idt.R;

public class RecyclerViewAdapterChatList extends RecyclerView.Adapter<CustomViewHolderListChat> {
    private Context context;
    private List<QBChatDialog> qbChatDialogList;

    public RecyclerViewAdapterChatList(Context context, List<QBChatDialog> list){
        this.context = context;
        this.qbChatDialogList = list;
    }

    @NonNull
    @Override
    public CustomViewHolderListChat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_chat, null);
        CustomViewHolderListChat customViewHolderListChat = new CustomViewHolderListChat(view);
        return customViewHolderListChat;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderListChat holder, int i) {
        final QBChatDialog qbChatDialog = qbChatDialogList.get(holder.getAdapterPosition());
        holder.textView.setText(qbChatDialog.getName());
        holder.textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra("dialogs", qbChatDialog);
                context.startActivity(intent);
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra("dialogs", qbChatDialog);
                context.startActivity(intent);
            }
        });
        holder.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra("dialogs", qbChatDialog);
                context.startActivity(intent);
            }
        });
        holder.textView2.setText(qbChatDialog.getLastMessage());
        SimpleDateFormat timeStampFormat =
                new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateStr = timeStampFormat.format(qbChatDialog.getUpdatedAt());
        holder.textView3.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return qbChatDialogList.size();
    }
}
