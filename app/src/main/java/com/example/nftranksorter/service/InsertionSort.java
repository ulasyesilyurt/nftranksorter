package com.example.nftranksorter.service;

import com.example.nftranksorter.model.NFT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsertionSort {
    public static void sort(List<NFT> nftList){

        if(nftList == null || nftList.size() < 2)
            return;

        for(int i = 1; i < nftList.size(); i++){
            NFT key = nftList.get(i);
            double keyScore = key.getRarityScore();

            int j = i - 1;

            while( j >= 0 && keyScore > nftList.get(j).getRarityScore()){
                nftList.set(j + 1, nftList.get(j));
                j--;
            }

            nftList.set(j + 1, key);
        }
    }
}

