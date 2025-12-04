package com.example.nftranksorter.ui.collections;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.nftranksorter.R;
import com.example.nftranksorter.data.CollectionDatabase;
import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.Collection;
import com.example.nftranksorter.ui.nft.NFTListActivity;

import java.util.ArrayList;
import java.util.List;

public class CollectionListActivity extends AppCompatActivity {

    private RecyclerView recyclerCollections;
    private Button btnAddCollection;

    private CollectionAdapter adapter;
    private List<Collection> collectionList = new ArrayList<>();

    private CollectionDatabase collectionDb;
    private NFTDatabase nftDb;  // silerken kullanacağız

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Collections");
        }

        btnAddCollection    = findViewById(R.id.btnAddCollection);
        recyclerCollections = findViewById(R.id.recyclerViewCollections);

        recyclerCollections.setLayoutManager(new LinearLayoutManager(this));

        collectionDb = new CollectionDatabase(this);
        nftDb        = new NFTDatabase(this, collectionDb);

        adapter = new CollectionAdapter(collectionList, new CollectionAdapter.OnCollectionClickListener() {
            @Override
            public void onCollectionClick(Collection c) {
                // Koleksiyona tıklanınca NFT listesine git
                Intent intent = new Intent(CollectionListActivity.this, NFTListActivity.class);
                intent.putExtra("collection_name", c.getName());
                startActivity(intent);
            }

            @Override
            public void onCollectionDelete(Collection c) {
                // Çöp kutusuna tıklanınca silme diyaloğu
                showDeleteDialog(c);
            }
        });

        recyclerCollections.setAdapter(adapter);

        btnAddCollection.setOnClickListener(v -> {
            Intent intent = new Intent(CollectionListActivity.this, AddCollectionActivity.class);
            startActivity(intent);
        });

        // İlk yükleme
        reloadCollections();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadCollections();
    }

    private void reloadCollections() {
        collectionDb = new CollectionDatabase(this);

        collectionList.clear();
        collectionList.addAll(collectionDb.getAllCollections());
        adapter.notifyDataSetChanged();
    }

    private void showDeleteDialog(Collection c) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Collection")
                .setMessage("Do you want to delete the "+ c.getName() +" collection?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Koleksiyonun NFT id'lerini al
                    List<Integer> ids = new ArrayList<>(c.getNftIds());

                    // Koleksiyonu sil
                    collectionDb.removeCollection(c.getName());

                    // NFT'leri de tamamen silmek istersen
                    for (int id : ids) {
                        nftDb.removeNFT(id);
                    }

                    // Listeyi yenile
                    reloadCollections();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}