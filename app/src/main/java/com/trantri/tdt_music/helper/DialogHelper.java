package com.trantri.tdt_music.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranTien
 * Date 06/28/2020.
 */
public class DialogHelper {
    public static void showDialogInput(@NonNull Context context, @StringRes int title, @StringRes int message, @StringRes int btnOk, @StringRes int btnCancel, @NonNull OnSelectedDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        EditText edtInput = new EditText(context);
        edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder
                .setTitle(title)
                .setMessage(message)
                .setView(edtInput)
                .setPositiveButton(btnOk, (dialog, which) -> listener.onPositiveButton(dialog, edtInput.getText().toString()))
                .setNegativeButton(btnCancel, (dialog, which) -> listener.onNegativeButton(dialog))
                .create();

        builder.show();
    }

    private static final String TAG = "111111";

    public static void showListDialog(@NonNull Context context, @NonNull ArrayList<String> listOptions, int positionSelectedDefault, @StringRes int title, @StringRes int btnOk, @StringRes int btnCancel, @NonNull OnSelectedDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice, listOptions);

        builder
                .setTitle(title)
                .setSingleChoiceItems(arrayAdapter, positionSelectedDefault, null)
                .setPositiveButton(btnOk, (dialog, which) -> {
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    listener.onPositiveButton(dialog, selectedPosition);
                })
                .setNegativeButton(btnCancel, (dialog, which) -> listener.onNegativeButton(dialog))
                .create();

        builder.show();
    }

    public interface OnSelectedDialogListener {
        default void onPositiveButton(DialogInterface dialog, String message) {
        }

        default void onPositiveButton(DialogInterface dialog, int which) {
        }

        default void onNegativeButton(DialogInterface dialog) {
        }

    }
}
