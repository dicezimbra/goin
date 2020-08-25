package br.com.legacy.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import br.com.legacy.models.ItemAvatarTextModel;
import br.com.R;
import br.com.databinding.ItemAvatarTextBinding;

import java.util.List;

/**
 * Created by appsimples on 20/09/17.
 */

public class ItemAvatarTextAdapter extends RecyclerView.Adapter<ItemAvatarTextAdapter.ItemViewHolder> {

    private List<ItemAvatarTextModel> items;

    public ItemAvatarTextAdapter(List<ItemAvatarTextModel> list) {
        this.items = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAvatarTextBinding binding = ItemAvatarTextBinding.inflate(inflater, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ItemAvatarTextAdapter.ItemViewHolder holder, final int position) {
        final ItemAvatarTextModel model = items.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemAvatarTextBinding binding;

        public ItemViewHolder (ItemAvatarTextBinding binding) {
            super (binding.getRoot());
            this.binding = binding;
        }

        public void bind(final ItemAvatarTextModel item) {
            binding.setItem(item);
            Glide.with(item.activity)
                    .load(item.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_icon)) // TODO colocar um placeholder
                    .into(binding.avatar);
        }
    }

    public void setItems(List<ItemAvatarTextModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}