package com.tuuzed.androidx.dialog;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import com.tuuzed.androidx.exdialog.ExDialog;
import com.tuuzed.androidx.exdialog.ext.ExDialogWrapper;
import com.tuuzed.androidx.exdialog.ext.SingleChoiceItemsController;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@SuppressLint("Registered")
public class JavaUsing extends AppCompatActivity {
    public void using() {
        new ExDialog(this).show(new Function1<ExDialog, Unit>() {
            @Override
            public Unit invoke(ExDialog exDialog) {
                ExDialogWrapper.singleChoiceItems(exDialog, new Function1<SingleChoiceItemsController<String>, Unit>() {
                    @Override
                    public Unit invoke(SingleChoiceItemsController<String> singleChoiceItemsController) {
                        return null;
                    }
                });
                return null;
            }
        });
    }
}
