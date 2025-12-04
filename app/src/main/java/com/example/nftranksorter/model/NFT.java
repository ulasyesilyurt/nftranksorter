package com.example.nftranksorter.model;
import java.util.HashMap;
import java.util.Map;

public class NFT {
    private int id;
    private String name;
    private String imagePatch;
    private Map<String, String> traits;
    private double rarityScore;

    public NFT(int id, String name, String imagePatch) {
        this.id = id;
        this.name = name;
        this.imagePatch = imagePatch;
        this.traits = new HashMap<>();
        this.rarityScore = 0;
    }

    public void addTrait(String key, String value){
        traits.put(key,value);
    }

    // Getter
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getImagePatch(){
        return imagePatch;
    }
    public Map<String, String> getTraits(){
        return traits;
    }
    public double getRarityScore(){
        return rarityScore;
    }
    // Setter
    public void setRarityScore(double rarityScore){
        this.rarityScore = rarityScore;
    }
    public void setTraits(Map<String, String> traits){
        this.traits = traits;
    }
}