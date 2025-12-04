package com.example.nftranksorter.ui.collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nftranksorter.R;
import com.example.nftranksorter.model.Collection;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    public interface OnCollectionClickListener {
        void onCollectionClick(Collection collection);      // karta tıklama
        void onCollectionDelete(Collection collection);     // çöp kutusuna tıklama
    }

    private List<Collection> collectionList;
    private OnCollectionClickListener listener;

    public CollectionAdapter(List<Collection> list, OnCollectionClickListener listener) {
        this.collectionList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_collection, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection c = collectionList.get(position);
        holder.txtName.setText(c.getName());

        //Koleksiyon açıklaması
        if(c.getDescription() != null && !c.getDescription().isEmpty()) {
            holder.txtDesc.setText(c.getDescription());
        }else{
            holder.txtDesc.setText("Koleksiyon Açıklaması Yok");
        }

        // NFT sayısı
        if (c.getNftIds() != null) {
            holder.txtInfo.setText(c.getNftIds().size() + " NFT");
        } else {
            holder.txtInfo.setText("0 NFT");
        }

        // Kapak resmi
        String coverPath = c.getCoverImagePath(); // "col_dogs.png" gibi
        if (coverPath != null && !coverPath.isEmpty()) {
            String base = coverPath.replace(".png", "");
            int resId = holder.itemView.getContext()
                    .getResources()
                    .getIdentifier(
                            base,
                            "drawable",
                            holder.itemView.getContext().getPackageName()
                    );
            if (resId != 0) {
                holder.imgCover.setImageResource(resId);
            }
        }

        // Kartın kendisine tıklayınca koleksiyona gir
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCollectionClick(c);
            }
        });

        // Çöp kutusuna tıklayınca silme
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCollectionDelete(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, btnDelete;
        TextView txtName, txtInfo,txtDesc;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            imgCover  = itemView.findViewById(R.id.imgCollectionCover);
            btnDelete = itemView.findViewById(R.id.btnDeleteCollection);
            txtName   = itemView.findViewById(R.id.txtCollectionName);
            txtInfo   = itemView.findViewById(R.id.txtCollectionInfo);
            txtDesc   = itemView.findViewById(R.id.txtCollectionDesc);
        }
    }
}