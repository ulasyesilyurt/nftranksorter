package com.example.nftranksorter.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.nftranksorter.model.Collection;
import com.example.nftranksorter.model.NFT;
import com.example.nftranksorter.service.RarityService;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NFTDatabase {
    private List<NFT> nftList = new ArrayList<>();
    private int nextId; // Yeni NFT eklendikçe artacak ID
    private final File nftsFile;

    private final RarityService rarityService;
    private final CollectionDatabase collectionDb;

    public NFTDatabase(Context context, CollectionDatabase collectionDb) {
        this.collectionDb = collectionDb;
        this.nftsFile = new File(context.getFilesDir(), "nfts.json");

        loadFromJson();

        if (nftList == null) {          // eğer json dosyası yoksa
            this.nftList = new ArrayList<>();  // boş liste oluştur
        }

        //maxId'yi buluyoruz
        int maxId = 0;
        for (NFT nft : nftList) {
            if (nft.getId() > maxId) {
                maxId = nft.getId();
            }
        }
        this.nextId = maxId + 1;

        this.rarityService = new RarityService(this, collectionDb);
    }

    public void addNFT(String name, String imagePath, Map<String, String> traits,String collectionName) {
        if (name == null) {
            System.out.println("NFT adı boş olamaz.");
            return;
        }
        for (NFT nft : nftList) {
            if (nft.getName().equals(name)) {
                System.out.println("Bu NFT zaten mevcut, eklenmedi: " + name);
                return;
            }
        }

        NFT nft = new NFT(nextId, name, imagePath);
        if (traits != null) {
            for (Map.Entry<String, String> entry : traits.entrySet()) {
                nft.addTrait(entry.getKey(), entry.getValue());
            }
        }
        nftList.add(nft);

        // Koleksiyon bul ve NFT'yi koleksiyona ekle
        Collection collection = collectionDb.getCollectionByName(collectionName);
        if (collection != null) {
            collection.addNFT(nft.getId());
            rarityService.updateCollectionRarity(collection);
        } else {
            System.out.println("Koleksiyon bulunamadı: " + collectionName);
        }

        nextId++;
        saveToJson();
    }

    public boolean removeNFT(int id) {
        //NFT'yi bul
        NFT found = null;
        for (NFT nft : nftList) {
            if (nft.getId() == id) {
                found = nft;
                break;
            }
        }
        // Eğer bulunduysa sil ve koleksiyonlardan da çıkar
        if (found != null) {
            Collection ownerCollection = null;

            // Koleksiyonlardaki ID listesinden sil
            for (Collection c : collectionDb.getAllCollections()) {
                if (c.getNftIds().contains(id)) {
                    ownerCollection = c;
                    c.getNftIds().remove(Integer.valueOf(id));
                    break;
                }
            }

            nftList.remove(found);

            if (ownerCollection != null) {
                ownerCollection.getNftIds().remove(Integer.valueOf(id));
                rarityService.updateCollectionRarity(ownerCollection);
            }

            saveToJson();          // NFT'leri kaydet
            collectionDb.saveToJson(); // Koleksiyonu kaydet
            System.out.println("NFT silindi: " + found.getName());
            return true;
        }
        // NFT bulunamadıysa
        System.out.println("NFT bulunamadı! id=" + id);
        return false;
    }

    public List<NFT> getAllNFTs(){
        return nftList;
    }

    public void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(nftsFile)) {
            gson.toJson(nftList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFromJson() {
        Gson gson = new Gson();
        if (!nftsFile.exists()) {
            nftList = new ArrayList<>();
            return;
        }
        try (FileReader reader = new FileReader(nftsFile)) {
            NFT[] nfts = gson.fromJson(reader, NFT[].class);
            if (nfts != null) {
                nftList = new ArrayList<>(Arrays.asList(nfts));
            } else {
                nftList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            nftList = new ArrayList<>();
        }
    }

    public NFT getNFTById(int id) {
        for (NFT nft : nftList) {
            if (nft.getId() == id) {
                return nft;
            }
        }
        return null;
    }

}
