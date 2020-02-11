package py.hvillalba.chat_idt.utils.cache;

import com.quickblox.chat.model.QBChatMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QBChatMessageHolder implements Serializable {
    private static QBChatMessageHolder instance;
    private HashMap<String, ArrayList<QBChatMessage>> qbChatMessageArray;

    public static synchronized QBChatMessageHolder getInstance(){
        QBChatMessageHolder qbChatMessageHolder;
        synchronized (QBChatMessageHolder.class){
            if (instance == null){
                instance = new QBChatMessageHolder();
            }
            qbChatMessageHolder = instance;
        }
        return qbChatMessageHolder;
    }

    private QBChatMessageHolder(){
        this.qbChatMessageArray = new HashMap<>();
    }

    public void putMessages(String dialogId, ArrayList<QBChatMessage> qbChatMessages){
        this.qbChatMessageArray.put(dialogId, qbChatMessages);
    }

    public void putMessage(String dialogId, QBChatMessage qbChatMessage){
        List<QBChatMessage> lsResult = (List) this.qbChatMessageArray.get(dialogId);
        lsResult.add(qbChatMessage);
        ArrayList<QBChatMessage> listAdded = new ArrayList<>(lsResult.size());
        listAdded.addAll(lsResult);
        putMessages(dialogId, listAdded);
    }

    public ArrayList<QBChatMessage> getMessagesByDialogId(String dialogId){
        return  (ArrayList<QBChatMessage>)this.qbChatMessageArray.get(dialogId);
    }
}
