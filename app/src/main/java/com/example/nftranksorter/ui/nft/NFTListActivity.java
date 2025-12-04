package com.example.nftranksorter.ui.nft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.os.SystemClock;

import com.example.nftranksorter.service.InsertionSort;
import com.example.nftranksorter.service.MergeSort;
import com.example.nftranksorter.service.QuickSort;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.nftranksorter.R;
import com.example.nftranksorter.data.CollectionDatabase;
import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.Collection;
import com.example.nftranksorter.model.NFT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFTListActivity extends AppCompatActivity {

    private RecyclerView recyclerNfts;
    private TextView txtSortInfo;
    private Button btnInsertion, btnMerge, btnQuick, btnReset, btnAddNft;
    private List<NFT> nftList = new ArrayList<>();
    private List<NFT> originalList = new ArrayList<>();
    private NFTAdapter adapter;

    // Yenileme için field'lar
    private CollectionDatabase collectionDb;
    private NFTDatabase nftDb;
    private String collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nft_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSortInfo = findViewById(R.id.txtSortInfo);
        recyclerNfts = findViewById(R.id.recyclerNfts);
        btnInsertion = findViewById(R.id.btnInsertionSort);
        btnMerge = findViewById(R.id.btnMergeSort);
        btnQuick = findViewById(R.id.btnQuickSort);
        btnReset = findViewById(R.id.btnResetSort);
        btnAddNft = findViewById(R.id.btnAddNft);

        recyclerNfts.setLayoutManager(new GridLayoutManager(this, 2));

        collectionName = getIntent().getStringExtra("collection_name");
        if (collectionName == null) collectionName = "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(collectionName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // DB nesnelerini field olarak oluştur
        collectionDb = new CollectionDatabase(this);
        nftDb = new NFTDatabase(this, collectionDb);

        // Adapter'ı bir kere oluştur, boş liste ile başla
        adapter = new NFTAdapter(nftList, nft -> {
            Intent intent = new Intent(NFTListActivity.this, NFTDetailActivity.class);
            intent.putExtra("nft_id", nft.getId());
            startActivity(intent);
        });
        recyclerNfts.setAdapter(adapter);

        // İlk açılışta veriyi yükle
        reloadNfts();

        setupSortButtons();

        btnAddNft.setOnClickListener(v -> {
            Intent intent = new Intent(NFTListActivity.this, AddNftActivity.class);
            intent.putExtra("collection_name", collectionName);
            startActivity(intent);
        });
    }

    // Toolbar geri tuşu için
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void reloadNfts() {
        collectionDb = new CollectionDatabase(this);
        nftDb = new NFTDatabase(this, collectionDb);

        nftList.clear();

        Collection collection = collectionDb.getCollectionByName(collectionName);
        if (collection != null) {
            for (Integer id : collection.getNftIds()) {
                for (NFT nft : nftDb.getAllNFTs()) {
                    if (nft.getId() == id) {
                        nftList.add(nft);
                        break;
                    }
                }
            }
        }

        // Orijinal liste de güncellensin ki sort/reset doğru çalışsın
        originalList.clear();
        originalList.addAll(nftList);

        adapter.notifyDataSetChanged();
        txtSortInfo.setText("Not sorted yet");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Detaydan / ekleme ekranından dönünce listeyi tazele
        reloadNfts();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setupSortButtons() {
        btnInsertion.setOnClickListener(v -> {
            nftList.clear();
            nftList.addAll(originalList);

            long start = System.nanoTime();
            InsertionSort.sort(nftList);
            long end = System.nanoTime();
            double elapsedMs = (end - start) / 1_000_000.0;
            adapter.notifyDataSetChanged();
            txtSortInfo.setText("InsertionSort: " + elapsedMs + " ms");
        });

        btnMerge.setOnClickListener(v -> {
            nftList.clear();
            nftList.addAll(originalList);

            long start = System.nanoTime();
            MergeSort.sort(nftList);
            long end = System.nanoTime();
            double elapsedMs = (end - start) / 1_000_000.0;
            adapter.notifyDataSetChanged();
            txtSortInfo.setText("MergeSort: " + elapsedMs + " ms");
        });

        btnQuick.setOnClickListener(v -> {
            nftList.clear();
            nftList.addAll(originalList);

            long start = System.nanoTime();
            QuickSort.sort(nftList);
            long end = System.nanoTime();
            double elapsedMs = (end - start) / 1_000_000.0;
            adapter.notifyDataSetChanged();
            txtSortInfo.setText("QuickSort: " + elapsedMs + " ms");
        });

        btnReset.setOnClickListener(v -> {
            nftList.clear();
            nftList.addAll(originalList);

            adapter.notifyDataSetChanged();
            txtSortInfo.setText("Not sorted yet");
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nft_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_trait_stats) {
            showTraitStatsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showInfoDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Trait statistics")
                .setMessage(message)
                .setPositiveButton("Done", null)
                .show();
    }
    private void showTraitStatsDialog() {
        // Koleksiyonda NFT yoksa
        if (nftList == null || nftList.isEmpty()) {
            showInfoDialog("There are no NFTs in this collection yet.");
            return;
        }

        // traitKey -> (traitValue -> count)
        Map<String, Map<String, Integer>> stats = new HashMap<>();

        for (NFT nft : nftList) {
            if (nft.getTraits() == null) continue;

            for (Map.Entry<String, String> entry : nft.getTraits().entrySet()) {
                String key = entry.getKey();      // örn: "Color"
                String value = entry.getValue();  // örn: "Red"

                // İlgili trait için iç map'i al (yoksa oluştur)
                Map<String, Integer> valueMap = stats.get(key);
                if (valueMap == null) {
                    valueMap = new HashMap<>();
                    stats.put(key, valueMap);
                }

                // Bu value kaç kere geçmiş +1
                int oldCount = valueMap.getOrDefault(value, 0);
                valueMap.put(value, oldCount + 1);
            }
        }

        // Hiç trait yoksa
        if (stats.isEmpty()) {
            showInfoDialog("No traits were found on NFTs in this collection.");
            return;
        }

        // String'e çevirip göster
        StringBuilder sb = new StringBuilder();
        sb.append("Trait uses in this collection:\n\n");

        for (Map.Entry<String, Map<String, Integer>> traitEntry : stats.entrySet()) {
            String traitKey = traitEntry.getKey(); // örn: "Color"
            sb.append(traitKey).append(":\n");

            Map<String, Integer> values = traitEntry.getValue();
            for (Map.Entry<String, Integer> valEntry : values.entrySet()) {
                sb.append("  • ")
                        .append(valEntry.getKey())      // örn: "Red"
                        .append(" : ")
                        .append(valEntry.getValue())    // örn: 3
                        .append(" NFT\n");
            }
            sb.append("\n");
        }

        showInfoDialog(sb.toString());
    }

}
