package com.example.smarttext.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FireBaseDatabaseManager {
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private String myPhoneNumber;

    public FireBaseDatabaseManager() {
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            myPhoneNumber= firebaseAuth.getCurrentUser().getPhoneNumber();
        }
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void sandNotActive() {
        Calendar calendar=Calendar.getInstance();
        ActiveNow activeNow=new ActiveNow();
        activeNow.setActive(0);
        activeNow.setLastActiveTime(calendar.get(Calendar.DATE));
        databaseRef.child(Config.Node_ACTIVE_NOW)
                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .setValue(activeNow);
    }

    public void sandActiveNow()
    {
        ActiveNow activeNow=new ActiveNow();
        activeNow.setActive(1);
        databaseRef.child(Config.Node_ACTIVE_NOW)
                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .setValue(activeNow);
    }
}
