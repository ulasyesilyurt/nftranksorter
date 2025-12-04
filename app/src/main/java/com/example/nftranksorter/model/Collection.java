package com.example.nftranksorter.model;

import java.util.List;
import java.util.ArrayList;


public class Collection {
    private String name;
    private String description;
    private String coverImagePath;

    private List<Integer> nftIds;

    public Collection(String name, String description, String coverImagePath) {
        this.name = name;
        this.description = description;
        this.coverImagePath = coverImagePath;
        this.nftIds = new ArrayList<>();
    }
    //Getter
    public List<Integer> getNftIds() {
        return nftIds;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getCoverImagePath() {
        return coverImagePath;
    }
    //Setter
    public void setName(String name) {
        this.name = name;
    }

    public void addNFT(int nftId) {
        if (!nftIds.contains(nftId)) {
            nftIds.add(nftId);
        }
    }

    public void removeNFT(int nftId) {
        nftIds.remove(Integer.valueOf(nftId));
    }

}