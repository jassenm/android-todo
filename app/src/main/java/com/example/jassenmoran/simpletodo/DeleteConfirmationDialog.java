package com.example.jassenmoran.simpletodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by jassenmoran on 2/23/17.
 */

public class DeleteConfirmationDialog extends DialogFragment
{
    private long mId = -1;
    private static final String ID = "id";

    public DeleteConfirmationDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    public interface DeleteConfirmDialogListener {
        void onDeleteConfirmed(Long id);
        void onDeleteCancelled();
    }


    public static DeleteConfirmationDialog newInstance(Long id) {
        DeleteConfirmationDialog frag = new DeleteConfirmationDialog();
        Bundle bundle = getBundle(id);
        frag.setArguments(bundle);
        return frag;
    }

    private static Bundle getBundle(Long id) {
        Bundle returnedBundle = new Bundle();
        returnedBundle.putLong(ID, id);
        return returnedBundle;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();
        mId = bundle.getLong(ID);

        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DeleteConfirmationDialog.DeleteConfirmDialogListener listener = (DeleteConfirmationDialog.DeleteConfirmDialogListener) getActivity();
                        listener.onDeleteConfirmed(mId);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteConfirmationDialog.DeleteConfirmDialogListener listener = (DeleteConfirmationDialog.DeleteConfirmDialogListener) getActivity();

                        listener.onDeleteCancelled();

                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }
}