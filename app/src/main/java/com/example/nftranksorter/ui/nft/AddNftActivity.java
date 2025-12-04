package com.example.nftranksorter.ui.nft;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nftranksorter.R;
import com.example.nftranksorter.data.CollectionDatabase;
import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.NFT;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNftActivity extends AppCompatActivity {
    private EditText edtName, edtTraitKey, edtTraitValue;
    private TextView txtTraitList;
    private Button btnAddTrait, btnSave;
    private Map<String, String> traits = new HashMap<>();
    private String collectionName;
    Spinner spinnerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nft);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtName = findViewById(R.id.edtNftName);
        edtTraitKey = findViewById(R.id.edtTraitKey);
        edtTraitValue = findViewById(R.id.edtTraitValue);
        txtTraitList = findViewById(R.id.txtTraitList);
        btnAddTrait = findViewById(R.id.btnAddTrait);
        btnSave = findViewById(R.id.btnSaveNft);
        spinnerImage  = findViewById(R.id.spinnerImageSelect);

        collectionName = getIntent().getStringExtra("collection_name");
        if (collectionName == null) collectionName = "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add NFT");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Drawable'lardan otomatik NFT resimlerini çek
        List<String> imageNames = new ArrayList<>();
        Field[] drawables = R.drawable.class.getFields();
        for (Field f : drawables) {
            String name = f.getName();
            // Sadece "nft" ile başlayan drawable'ları ekle (örn: nft1, nft_dog, nft_rare3)
            if (name.startsWith("nft")) {
                imageNames.add(name);
            }
        }

        // --- Mevcut NFT'lerde kullanılan görselleri ele ---
        CollectionDatabase collectionDbForFilter = new CollectionDatabase(this);
        NFTDatabase nftDbForFilter = new NFTDatabase(this, collectionDbForFilter);

        // Bu koleksiyondaki NFT'lerin kullandığı image isimlerini toplayalım
        // Eğer TÜM projede kullanılanları elemek istersen collectionName'e göre filtreleme yapmadan da gidebiliriz.
        List<String> usedImages = new ArrayList<>();
        for (com.example.nftranksorter.model.NFT nft : nftDbForFilter.getAllNFTs()) {

            // Eğer sadece BU koleksiyondaki kullanımları elemek istersen:
            // ilgili koleksiyona ait mi diye bakabilirsin
            // ama şu an basit haliyle tüm NFT'lerde kullanılan her image'i eliyoruz.

            String path = nft.getImagePatch();   // örn: "nft1.png"
            if (path != null && !path.isEmpty()) {
                // .png'yi at -> "nft1"
                int dotIndex = path.lastIndexOf('.');
                String baseName = (dotIndex != -1) ? path.substring(0, dotIndex) : path;
                if (!usedImages.contains(baseName)) {
                    usedImages.add(baseName);
                }
            }
        }
        // imageNames listesinden kullanılanları çıkar
        imageNames.removeIf(usedImages::contains);

        if (imageNames.isEmpty()) {
            // Hiç nft* resmi yoksa kullanıcıya söyle
            imageNames.add("nft1"); // fallback, projede en az bir tane nft1.png olduğunu varsayalım
        }

        // Spinner'a bağla
        ArrayAdapter<String> imageAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, imageNames);
        spinnerImage.setAdapter(imageAdapter);

        btnAddTrait.setOnClickListener(v -> {
            String key = edtTraitKey.getText().toString().trim();
            String value = edtTraitValue.getText().toString().trim();

            if (key.isEmpty() || value.isEmpty()) {
                Toast.makeText(this, "Trait key ve value boş olamaz", Toast.LENGTH_SHORT).show();
                return;
            }

            traits.put(key, value);
            updateTraitListText();

            edtTraitKey.setText("");
            edtTraitValue.setText("");
        });

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String selectedImageName = (String) spinnerImage.getSelectedItem();

            if (name.isEmpty()) {
                Toast.makeText(this, "NFT adı boş olamaz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageName == null || selectedImageName.isEmpty()) {
                Toast.makeText(this, "Bir resim seçmelisin", Toast.LENGTH_SHORT).show();
                return;
            }

            String imagePath = selectedImageName + ".png";

            CollectionDatabase collectionDb = new CollectionDatabase(this);
            NFTDatabase nftDb = new NFTDatabase(this, collectionDb);

            nftDb.addNFT(name, imagePath, traits, collectionName);

            Toast.makeText(this, "NFT eklendi", Toast.LENGTH_SHORT).show();
            finish(); // liste ekranına geri dön
        });
    }

    private void updateTraitListText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Traits:\n");
        for (Map.Entry<String, String> e : traits.entrySet()) {
            sb.append("• ")
                    .append(e.getKey())
                    .append(" : ")
                    .append(e.getValue())
                    .append("\n");
        }
        txtTraitList.setText(sb.toString());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

