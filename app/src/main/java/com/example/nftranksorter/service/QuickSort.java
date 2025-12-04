package com.example.nftranksorter.service;

import com.example.nftranksorter.model.NFT;

import java.util.List;

public class QuickSort {
    public static void sort(List<NFT> nftList){
        if(nftList == null || nftList.size() < 2)
            return;
        quickSort(nftList, 0, nftList.size() - 1);
    }

    private static void quickSort(List<NFT> nftList, int left, int right){
        if(left < right){
            int pivotIndex = partition(nftList, left, right);
            //Sol taraf
            quickSort(nftList, left, pivotIndex - 1);
            //Sağ taraf
            quickSort(nftList, pivotIndex + 1, right);
        }
    }

    private static int partition(List<NFT> nftList, int left, int right){
        int mid = (left + right) / 2; //Pivot ortadaki eleman
        NFT pivot = nftList.get(mid);
        double pivotScore = pivot.getRarityScore();

        swap(nftList, mid, right); //Pivotu sağa aldık

        int i = left -1;

        for(int j = left; j < right; j++){
            if(nftList.get(j).getRarityScore() >= pivotScore){
                i++;
                swap(nftList,i,j);
            }
        }
        swap(nftList, i + 1, right);
        return i + 1;
    }

    private static void swap(List<NFT> nftList, int i, int j){
        NFT temp = nftList.get(i);
        nftList.set(i, nftList.get(j));
        nftList.set(j, temp);
    }
}

