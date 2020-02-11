package py.hvillalba.chat_idt;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChatIdtApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Inicializa la instancia de Realm...
        Realm.init(getApplicationContext());

        RealmConfiguration configuration =  new  RealmConfiguration.Builder()
                .name("chat-idt.realm")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(configuration);
        Realm.compactRealm(configuration);



    }
}
