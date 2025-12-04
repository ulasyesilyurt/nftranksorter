package com.example.nftranksorter.ui.nft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.nftranksorter.R;
import com.example.nftranksorter.data.CollectionDatabase;
import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.NFT;


import java.util.Locale;
import java.util.Map;

public class NFTDetailActivity extends AppCompatActivity {

    private ImageView imgBig;
    private TextView txtRarity, txtTraits;
    private Button btnDelete;
    private int nftId;
    private CollectionDatabase collectionDb;
    private NFTDatabase nftDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nft_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgBig = findViewById(R.id.imgNftBig);
        txtRarity = findViewById(R.id.txtDetailRarity);
        txtTraits = findViewById(R.id.txtDetailTraits);
        btnDelete = findViewById(R.id.btnDeleteNft);

        nftId = getIntent().getIntExtra("nft_id", -1);

        collectionDb = new CollectionDatabase(this);
        nftDb = new NFTDatabase(this, collectionDb);

        NFT nft = nftDb.getNFTById(nftId);

        if (nft == null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("NFT not found");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(nft.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        double score = nft.getRarityScore();
        String rarityText = String.format(Locale.getDefault(), "Rarity: %.3f", score);
        txtRarity.setText(rarityText);

        // RESİM YÜKLEME
        String imagePath = nft.getImagePatch();
        if (imagePath != null && !imagePath.isEmpty()) {
            String imageName = imagePath;
            int dotIndex = imageName.lastIndexOf('.');
            if (dotIndex != -1) imageName = imageName.substring(0, dotIndex);

            int resId = getResources().getIdentifier(
                    imageName, "drawable", getPackageName());

            if (resId != 0) imgBig.setImageResource(resId);
            else imgBig.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // TRAITS YÜKLEME
        StringBuilder traitsText = new StringBuilder();

        for (Map.Entry<String, String> t : nft.getTraits().entrySet()) {
            traitsText.append("• ")
                    .append(t.getKey())
                    .append(" : ")
                    .append(t.getValue())
                    .append("\n");
        }

        txtTraits.setText(traitsText.toString());
        btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(NFTDetailActivity.this)
                    .setTitle("Remove NFT")
                    .setMessage("Are you sure you want to delete this NFT?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean result = nftDb.removeNFT(nftId);
                        if (result) {
                            finish(); // liste ekranına dön
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
