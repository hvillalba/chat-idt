package py.hvillalba.chat_idt.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import py.hvillalba.chat_idt.R;

public class CustomViewHolderListChat extends RecyclerView.ViewHolder {
    TextView textView, textView2, textView3;

    public CustomViewHolderListChat(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
        textView2 = itemView.findViewById(R.id.textView2);
        textView3 = itemView.findViewById(R.id.textView3);
    }
}
