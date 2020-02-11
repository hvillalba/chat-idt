package py.hvillalba.chat_idt.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.List;

import py.hvillalba.chat_idt.R;
import py.hvillalba.chat_idt.utils.cache.QBUserHolder;

public class RecyclerViewMessageAdapter extends RecyclerView.Adapter<CustomViewHolderMessage> {
    Context context;
    List<QBChatMessage> qbChatMessageList= new ArrayList<>();
    private static final int VIEW_TYPE_ME = 0;
    private static final int VIEW_TYPE_USER= 1;

    //Constructor
    public RecyclerViewMessageAdapter(Context context, List<QBChatMessage> lista){
        this.context = context;
        this.qbChatMessageList = lista;
    }

    @Override
    public int getItemViewType(int position) {
        if (qbChatMessageList.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId()))
            return VIEW_TYPE_ME;
        else
            return VIEW_TYPE_USER;
    }

    @NonNull
    @Override
    public CustomViewHolderMessage onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId;
        if (VIEW_TYPE_ME == viewType){
            layoutId = R.layout.item_send_message;
        }else {
            layoutId = R.layout.item_receive_message;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        CustomViewHolderMessage customViewHolderMessageChat = new CustomViewHolderMessage(view);
        return customViewHolderMessageChat;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderMessage holder, int i) {
        QBChatMessage qbChatMessage = qbChatMessageList.get(holder.getAdapterPosition());
        if (qbChatMessage.getSenderId().equals(QBChatService.getInstance().getUser().getId())){
            holder.bubbleTextView.setText(qbChatMessage.getBody());
        }else {
            holder.bubbleTextView.setText(qbChatMessage.getBody());
            holder.textView.setText(QBUserHolder.getInstance().getUserById(qbChatMessage.getSenderId()).getFullName());
        }

    }

    @Override
    public int getItemCount() {
        return qbChatMessageList.size();
    }


    public void agregarItem(List<QBChatMessage> lista){
        this.qbChatMessageList = lista;
        this.notifyDataSetChanged();
    }
}
