package com.example.notesapppratyush;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class NotesFragment extends Fragment implements NoteInputDialogFragment.NoteInputListener ,NoteEditDialogFragment.NoteEditListener
    {
    private RecyclerView recyclerView;
    private List<String> notesList;
    private NoteAdapter adapter;
    private MyDBHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_notes);
        Button addButton = view.findViewById(R.id.button_add_note);
        notesList = new ArrayList<>();
            dbHelper = new MyDBHelper(getActivity());
            List<String> datanotesList = dbHelper.getAllNotes();
            if (datanotesList != null && !datanotesList.isEmpty()) {
                notesList.addAll(datanotesList); // Add dataNotesList to notesList
            }
        adapter = new NoteAdapter(notesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
           for(int i=0;i<datanotesList.size();i++)
               Log.d("note",datanotesList.get(i));
            addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new note
                DialogFragment dialogFragment = new NoteInputDialogFragment();
                dialogFragment.show(getChildFragmentManager(), "NoteInputDialogFragment");
                String notes = fetchNotesFromUser();
                notesList.add(notes);
                adapter.notifyItemInserted(notes.length()-1);
                recyclerView.scrollToPosition(0);
            }
        });
        return view;
    }
        // Method to add a note in the database
        private void addNoteToDatabase(String note) {
            dbHelper.addNote(note);
        }
        // Method to update a note in the database
        private void updateNoteInDatabase(int position, String newNote) {
            dbHelper.updateNote(position, newNote);
        }
        // Method to delete a note from the database
        private void deleteNoteFromDatabase(String delete) {
            dbHelper.deleteNote(delete);
        }
        @Override
        public void onNoteInput(String note) {
             // Update the TextView with the received note
             TextView textViewNote = getView().findViewById(R.id.text_view_note);
             textViewNote.setText(note);
             addNoteToDatabase(note);
        }
        @Override
        public void onNoteEdit(int position, String editedNote) {
            // Update the TextView with the updated note
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder instanceof NoteAdapter.NoteViewHolder) {
                NoteAdapter.NoteViewHolder noteViewHolder = (NoteAdapter.NoteViewHolder) viewHolder;
                noteViewHolder.noteTextView.setText(editedNote);
            }
        }
        private String fetchNotesFromUser() {
            String s="";
            return s;
        }
        private class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
        private List<String> notes;
        public NoteAdapter(List<String> notes) {
            this.notes = notes;
        }
        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            return new NoteViewHolder(view);
        }
            @Override
            public void onBindViewHolder(@NonNull NoteViewHolder holder,final int position) {
                holder.noteTextView.setText(notes.get(position));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPosition = holder.getAdapterPosition();
                        String selectedNote = notes.get(clickedPosition);
                        showEditNoteDialogFragment(clickedPosition,selectedNote);
                        notifyItemChanged(clickedPosition);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int clickedPosition = holder.getAdapterPosition();
                        String delete= notes.get(clickedPosition);
                        notes.remove(clickedPosition);
                        deleteNoteFromDatabase(delete);
                        notifyItemRemoved(clickedPosition);
                        Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            public void showEditNoteDialogFragment(final int position, String currentNote) {
                DialogFragment dialogFragment = new NoteEditDialogFragment();
                Bundle args = new Bundle();
                args.putInt("position", position);
                args.putString("note", currentNote);
                dialogFragment.setArguments(args);
                dialogFragment.show(getChildFragmentManager(), "NoteInputDialogFragment");
                updateNoteInDatabase(position,currentNote);
            }
        @Override
        public int getItemCount() {
            return notes.size();
        }
            private List<String> datanotesList = new ArrayList<>();
            public void setNotesList(List<String> datanotesList) {
                this.datanotesList = datanotesList;
                notifyDataSetChanged();
            }
            public class NoteViewHolder extends RecyclerView.ViewHolder {
            TextView noteTextView;
            public NoteViewHolder(@NonNull View itemView) {
                super(itemView);
                noteTextView = itemView.findViewById(R.id.text_view_note);
            }
        }
    }
}

