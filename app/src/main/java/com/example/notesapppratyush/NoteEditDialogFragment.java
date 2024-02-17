package com.example.notesapppratyush;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
public class NoteEditDialogFragment extends DialogFragment {
    private EditText editTextNote;
    private int position;
    public interface NoteEditListener {
        void onNoteEdit(int position, String editedNote);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_note_edit_dialog, null);
        editTextNote = view.findViewById(R.id.edit_text_note);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("position", -1);
            String currentNote = args.getString("note", "");
            editTextNote.setText(currentNote);
        }
        builder.setView(view)
                .setTitle("Edit Note")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedNote = editTextNote.getText().toString().trim();
                        Log.d("hi edit ","checking error");
                        NoteEditListener listener = (NoteEditListener) getParentFragment();
                        listener.onNoteEdit(position, editedNote);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
