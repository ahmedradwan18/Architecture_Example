package com.example.architectureexample.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.architectureexample.Model.Note;
import com.example.architectureexample.Model.NoteAdapter;
import com.example.architectureexample.R;
import com.example.architectureexample.ViewModel.NoteViewModel;
import com.example.architectureexample.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel viewModel;
    private ActivityMainBinding binding;
    public static final int REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.floatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEditNote.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        final NoteAdapter adapter;
        adapter = new NoteAdapter();
        binding.recyclerView.setAdapter(adapter);


        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.Delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted...", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.recyclerView);

        adapter.setOnItemClickListner(new NoteAdapter.onItemClickListner() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNote.class);
                intent.putExtra(AddEditNote.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNote.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNote.EXTRA_priority, note.getPriority());
                intent.putExtra(AddEditNote.EXTRA_ID, note.getId());
                startActivityForResult(intent, EDIT_REQUEST_CODE);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                viewModel.DeleteAllNotes();
                Toast.makeText(this, "All Notes Are Deleted..", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNote.EXTRA_priority, 1);

            Note note = new Note(title, description, priority);
            viewModel.Insert(note);
            Toast.makeText(this, "Note Saved..", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNote.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note Can't be updated..", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNote.EXTRA_priority, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            viewModel.Update(note);
            Toast.makeText(this, "Note Updated..", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Note not saved!..", Toast.LENGTH_SHORT).show();
        }
    }
}
