package com.example.nftranksorter.ui.nft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nftranksorter.R;
import com.example.nftranksorter.model.NFT;

import java.util.List;
import java.util.Locale;

public class NFTAdapter extends RecyclerView.Adapter<NFTAdapter.NFTViewHolder> {
    public interface OnNftClickListener {
        void onNftClick(NFT nft);
    }

    private List<NFT> nftList;
    private OnNftClickListener listener;

    public NFTAdapter(List<NFT> nftList, OnNftClickListener listener) {
        this.nftList = nftList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NFTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nft, parent, false);
        return new NFTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NFTViewHolder holder, int position) {
        NFT nft = nftList.get(position);
        holder.txtName.setText(nft.getName());

        double score = nft.getRarityScore();
        String rarityText = String.format(Locale.getDefault(), "Rarity: %.3f", score);
        holder.txtRarity.setText(rarityText);

        String imagePath = nft.getImagePatch();
        if (imagePath != null && !imagePath.isEmpty()) {
            String imageName = imagePath;
            int dotIndex = imageName.lastIndexOf('.');
            if (dotIndex != -1) {
                imageName = imageName.substring(0, dotIndex);
            }

            int resId = holder.itemView.getContext().getResources()
                    .getIdentifier(imageName, "drawable",
                            holder.itemView.getContext().getPackageName());

            if (resId != 0) {
                holder.imgNftThumb.setImageResource(resId);
            } else {
                holder.imgNftThumb.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            holder.imgNftThumb.setImageResource(R.drawable.ic_launcher_foreground);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNftClick(nft);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nftList.size();
    }

    static class NFTViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtRarity;
        ImageView imgNftThumb;

        public NFTViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNftThumb = itemView.findViewById(R.id.imgNftThumb);
            txtName = itemView.findViewById(R.id.txtNftName);
            txtRarity = itemView.findViewById(R.id.txtNftRarity);
        }
    }
}
