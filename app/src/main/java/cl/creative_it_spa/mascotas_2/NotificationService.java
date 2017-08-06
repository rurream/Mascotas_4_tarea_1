package cl.creative_it_spa.mascotas_2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Rodrigo on 01-08-2017.
 */

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom() + "*************************************************************************");
        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());





        Intent intent= new Intent(this, MainActivity.class);
        //intent.putExtra("mostrar_detalle", "si");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificacion = new NotificationCompat
                .Builder(this)
                .setSmallIcon(R.drawable.icons8_megaphone_64)
                .setContentTitle("Se ha recibido un Like en Mascotas")
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(sonido)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacion.build());
    }
}
