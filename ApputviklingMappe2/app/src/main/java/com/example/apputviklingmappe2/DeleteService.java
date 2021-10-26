package com.example.apputviklingmappe2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.List;

public class DeleteService extends Service {
    private DBHandler db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHandler(this);
        String currentTime = Preferanser.getCurrentTime();
        System.out.println("Tiden er "+currentTime);
        if (db.findNumberofuniqueBestillinger() > 0) {
            List<Bestilling> bestillinger = db.findAllBestillinger();
            for (Bestilling bestilling : bestillinger) {
                if (bestilling.getTime().equals(currentTime)) {
                    System.out.println("Den fant et bra tidspunkt for sletting");
                    db.deleteBestilling(bestilling.get_ID(),bestilling.getVenn().get_ID());
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
