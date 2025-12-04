package com.example.nftranksorter.service;

import com.example.nftranksorter.model.NFT;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    public static void sort(List<NFT> nftList) {
        if (nftList == null || nftList.size() < 2) return;
        mergeSort(nftList, 0, nftList.size() - 1);
    }

    public static void mergeSort(List<NFT> nftList, int left, int right) {
        if(left >= right){
            return;
        }
        int mid = left + (right - left) / 2;

        // sol yarıyı sırala
        mergeSort(nftList, left, mid);
        // sağ yarıyı sırala
        mergeSort(nftList, mid+1, right);
        //birlestirme
        merge(nftList, left, mid, right);
    }

    public static void merge(List<NFT> nftList, int left, int mid, int right) {
        List<NFT> temp = new ArrayList<>(right - left +1);

        int i = left;
        int j = mid + 1;

        while(i <= mid && j <= right){
            double leftScore = nftList.get(i).getRarityScore();
            double rightScore = nftList.get(j).getRarityScore();

            if(leftScore >= rightScore){
                temp.add(nftList.get(i));
                i++;
            }else{
                temp.add(nftList.get(j));
                j++;
            }
        }
        while(i <= mid){
            temp.add(nftList.get(i));
            i++;
        }

        while(j <= right){
            temp.add(nftList.get(j));
            j++;
        }

        for(int k = 0; k < temp.size(); k++){
            nftList.set(left + k, temp.get(k));
        }
    }
}

