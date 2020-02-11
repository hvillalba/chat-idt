package py.hvillalba.chat_idt.utils.cache;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QBUserHolder implements Serializable {
    private static QBUserHolder instance;

    private SparseArray<QBUser> qbUserList;

    //Constructor privado (Singleton)
    private QBUserHolder(){
        qbUserList = new SparseArray<>();
    }

    public static synchronized QBUserHolder getInstance(){
        if (instance == null)
            instance = new QBUserHolder();
        return instance;
    }

    //Agrega una lista de usuarios
    public void putUsers(List<QBUser> users){
        for (QBUser user: users)
            putUser(user);
    }

    //Agrega un usuario a la lista
    private void putUser(QBUser user) {
        qbUserList.put(user.getId(), user);
    }

    //Obtiene un usuario por su id
    public QBUser getUserById(int id){
        return qbUserList.get(id);
    }

    //Obtiene una lista de usuarios de acuerdo a la lista de Ids que se la pasa...
    public List<QBUser> getUsersByIds(List<Integer> ids){
        List<QBUser> qbUser = new ArrayList<>();
        for (Integer id: ids){
            QBUser user = getUserById(id);
            if (user != null)
                qbUser.add(user);
        }
        return qbUser;
    }



}
