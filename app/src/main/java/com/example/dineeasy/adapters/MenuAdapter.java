package com.example.dineeasy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dineeasy.R;
import com.example.dineeasy.database.entities.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private List<MenuItem> menuItemsFull;
    private boolean isStaff;
    private OnMenuItemClickListener listener;

    public interface OnMenuItemClickListener {
        void onEditClick(MenuItem menuItem);
        void onDeleteClick(MenuItem menuItem);
    }

    public MenuAdapter(boolean isStaff, OnMenuItemClickListener listener) {
        this.menuItems = new ArrayList<>();
        this.menuItemsFull = new ArrayList<>();
        this.isStaff = isStaff;
        this.listener = listener;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.menuItemsFull = new ArrayList<>(menuItems);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        menuItems.clear();
        if (query.isEmpty()) {
            menuItems.addAll(menuItemsFull);
        } else {
            String lowerCaseQuery = query.toLowerCase(Locale.getDefault());
            for (MenuItem item : menuItemsFull) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    item.getDescription().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    item.getCategory().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    menuItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.tvItemName.setText(menuItem.getName());
        holder.tvCategory.setText(menuItem.getCategory());
        holder.tvDescription.setText(menuItem.getDescription());
        holder.tvPrice.setText(String.format(Locale.UK, "£%.2f", menuItem.getPrice()));

        if (isStaff) {
            holder.layoutStaffActions.setVisibility(View.VISIBLE);
            holder.btnEditItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(menuItem);
                }
            });
            holder.btnDeleteItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(menuItem);
                }
            });
        } else {
            holder.layoutStaffActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvCategory, tvDescription, tvPrice;
        LinearLayout layoutStaffActions;
        Button btnEditItem, btnDeleteItem;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            layoutStaffActions = itemView.findViewById(R.id.layoutStaffActions);
            btnEditItem = itemView.findViewById(R.id.btnEditItem);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
