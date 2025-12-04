package com.example.nftranksorter.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.nftranksorter.model.Collection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionDatabase {
    private List<Collection> collections;
    private final File collectionsFile;

    public CollectionDatabase(Context context) {
        this.collectionsFile = new File(context.getFilesDir(), "collections.json");
        loadFromJson();
        if (collections == null) collections = new ArrayList<>();
    }
    public void loadFromJson() {
        Gson gson = new Gson();
        if (!collectionsFile.exists()) {
            collections = new ArrayList<>();
            return;
        }
        try (FileReader reader = new FileReader(collectionsFile)) {
            Collection[] arr = gson.fromJson(reader, Collection[].class);
            collections = new ArrayList<>();
            if (arr != null) {
                for (Collection c : arr) collections.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            collections = new ArrayList<>();
        }
    }

    // Yeni bir koleksiyon ekle
    public Collection addCollection(String name, String description, String coverImagePath) {
        Collection existing = getCollectionByName(name);
        if (existing != null) return existing;
        Collection c = new Collection(name, description, coverImagePath);
        collections.add(c);
        saveToJson();
        return c;
    }


    // Tüm Koleksiyonları çek
    public List<Collection> getAllCollections() {
        return collections;
    }

    // İsme göre koleksiyon bul ve geri döndür
    public Collection getCollectionByName(String name) {
        for (Collection c : collections) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    // Koleksiyonu sil
    public boolean removeCollection(String name) {
        boolean removed = collections.removeIf(c -> c.getName().equalsIgnoreCase(name));
        if (removed) saveToJson();
        return removed;
    }

    // JSON'a kaydet
    public void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(collectionsFile)) {
            gson.toJson(collections, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}