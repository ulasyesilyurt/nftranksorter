package com.example.nftranksorter.ui.collections;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nftranksorter.R;
import com.example.nftranksorter.data.CollectionDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddCollectionActivity extends AppCompatActivity {

    private EditText edtName, edtDescription;
    private Spinner spinnerCover;
    private Button btnSave;

    private List<String> coverNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtName = findViewById(R.id.edtCollectionName);
        edtDescription = findViewById(R.id.edtCollectionDescription);
        spinnerCover = findViewById(R.id.spinnerCoverSelect);
        btnSave = findViewById(R.id.btnSaveCollection);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Collection");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        loadCoverImages();

        btnSave.setOnClickListener(v -> saveCollection());
    }

    private void loadCoverImages() {
        Field[] drawables = R.drawable.class.getFields();

        for (Field f : drawables) {
            String name = f.getName();
            if (name.startsWith("col_")) { // örn: col_art, col_dog
                coverNames.add(name + ".png");
            }
        }

        if (coverNames.isEmpty()) {
            coverNames.add("col_default.png");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                coverNames
        );
        spinnerCover.setAdapter(adapter);
    }

    private void saveCollection() {
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String cover = spinnerCover.getSelectedItem().toString(); // col_dog.png

        if (name.isEmpty()) {
            Toast.makeText(this, "Koleksiyon adı boş olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            description = "";
        }

        CollectionDatabase db = new CollectionDatabase(this);
        db.addCollection(name, description, cover);

        Toast.makeText(this, "Koleksiyon eklendi", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}