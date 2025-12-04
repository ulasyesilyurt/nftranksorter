package com.example.nftranksorter.service;

import com.example.nftranksorter.data.NFTDatabase;
import com.example.nftranksorter.model.Collection;
import com.example.nftranksorter.model.NFT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class RarityCalculator {

    // Koleksiyon bazında rarity hesaplar
    public static void calculate(Collection collection, NFTDatabase nftDb) {
        List<NFT> nfts = new ArrayList<>();
        for (int id : collection.getNftIds()) {
            NFT nft = nftDb.getNFTById(id);
            if (nft != null) {
                nfts.add(nft);
            }
        }

        int totalNFTs = nfts.size();
        if (totalNFTs == 0) return;

        for (NFT nft : nfts) {
            double score = 0;

            for (Map.Entry<String, String> trait : nft.getTraits().entrySet()) {
                long count = nfts.stream()
                        .filter(n -> {
                            String v = n.getTraits().get(trait.getKey());
                            return v != null && v.equals(trait.getValue());
                        })
                        .count();

                double rarity = 1.0 / ((double) count / totalNFTs);
                score += rarity;
            }

            nft.setRarityScore(score);
        }
    }
}

