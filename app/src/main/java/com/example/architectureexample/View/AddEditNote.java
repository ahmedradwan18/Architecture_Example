package com.example.architectureexample.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.architectureexample.R;
import com.example.architectureexample.databinding.ActivityAddNoteBinding;

public class AddEditNote extends AppCompatActivity {
    private ActivityAddNoteBinding binding;
    private EditText titleEdt;
    private EditText descriptionEdt;
    public static final String EXTRA_TITLE = "com.example.architectureexample.View_Title";
    public static final String EXTRA_ID = "com.example.architectureexample.View_ID";
    public static final String EXTRA_DESCRIPTION = "com.example.architectureexample.View_description";
    public static final String EXTRA_priority = "com.example.architectureexample.View_priority";
    private NumberPicker numberPicker;
    int priority = 0;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleEdt = findViewById(R.id.TitleEdt);
        descriptionEdt = findViewById(R.id.DescriptionEdt);
        numberPicker = findViewById(R.id.numberPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(15);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            titleEdt.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_priority, 1));
        } else {
            setTitle("Add Note");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }


    private void saveNote() {

        String title = titleEdt.getText().toString();
        String description = descriptionEdt.getText().toString();
        priority = numberPicker.getValue();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "please enter title and description...", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_priority, priority);

        id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
