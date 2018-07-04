package com.example.gyubeompark.test2;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

/**
 * Created by gyubeom.park on 2018-03-15.
 */

public class CustomDialogue extends Dialog {
    public CustomDialogue(@NonNull Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_dialogue);
    }

}
