package com.example.nftranksorter.service;

import com.example.nftranksorter.data.CollectionDatabase;
import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.Collection;

public class RarityService {
    private final NFTDatabase nftDb;
    private final CollectionDatabase collectionDb;

    public RarityService(NFTDatabase nftDb, CollectionDatabase collectionDb) {
        this.nftDb = nftDb;
        this.collectionDb = collectionDb;
    }

    // Sadece bir koleksiyonun raritysini güncelle ve kaydet
    public void updateCollectionRarity(Collection collection) {
        if (collection == null) return;

        RarityCalculator.calculate(collection, nftDb);
        nftDb.saveToJson();
        collectionDb.saveToJson();
    }

    // Tüm koleksiyonların raritysini yeniden hesapla ve kaydet
    public void updateAllCollections() {
        for (Collection c : collectionDb.getAllCollections()) {
            RarityCalculator.calculate(c, nftDb);
        }
        nftDb.saveToJson();
        collectionDb.saveToJson();
    }
}

