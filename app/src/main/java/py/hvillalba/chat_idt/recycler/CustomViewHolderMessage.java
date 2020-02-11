package py.hvillalba.chat_idt.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;

import py.hvillalba.chat_idt.R;

public class CustomViewHolderMessage extends RecyclerView.ViewHolder {
    TextView textView;
    BubbleTextView bubbleTextView;


    public CustomViewHolderMessage(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.message_user);
        bubbleTextView = itemView.findViewById(R.id.message_content);

    }
}
