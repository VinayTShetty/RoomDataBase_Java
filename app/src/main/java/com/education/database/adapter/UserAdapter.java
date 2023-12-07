package com.education.database.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.education.database.R;
import com.education.database.room.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemViewHolder> {

    private List<Users> usersList;
    private UserItemClickListener itemClickListener;
    public UserAdapter(List<Users> usersList, UserItemClickListener itemClickListener) {
        this.usersList=usersList;
        this.itemClickListener = itemClickListener;

    }

    public void updateData(List<Users> newData) {
        this.usersList=newData;
        notifyDataSetChanged();
    }
    public void addUsers(Users users){
        usersList.add(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_item,parent,false);
        ItemViewHolder myItemViewHolder=new ItemViewHolder(view);
        return myItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Users users=usersList.get(position);
        holder.userName.setText(users.getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(users);
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onEditClick(users);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList != null ? usersList.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public ImageView delete,edit;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
             userName=(TextView)itemView.findViewById(R.id.editTextName);
             edit=(ImageView)itemView.findViewById(R.id.imageview_edit);
             delete=(ImageView)itemView.findViewById(R.id.imageview_delete);
        }
    }

    public interface UserItemClickListener {
        void onEditClick(Users users);

        void onDeleteClick(Users users);
    }
}
