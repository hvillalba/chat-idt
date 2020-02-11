package py.hvillalba.chat_idt.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import py.hvillalba.chat_idt.R;

public class CustomViewHolderUserList extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;

    public CustomViewHolderUserList(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView1);
        imageView = itemView.findViewById(R.id.imageView1);
    }
}
