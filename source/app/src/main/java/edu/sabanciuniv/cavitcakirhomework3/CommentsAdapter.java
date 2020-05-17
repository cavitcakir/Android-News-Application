package edu.sabanciuniv.cavitcakirhomework3;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    List<CommentItem> commentItems;
    Context context;

    public CommentsAdapter(List<CommentItem> commentItems, Context context) {
        this.commentItems = commentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comments_row_layout,parent,false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {

        holder.txtName.setText(commentItems.get(position).getName());
        holder.txtMessage.setText(commentItems.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }



    class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtMessage;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtlistname);
            txtMessage = itemView.findViewById(R.id.txtlistmessage);
            root = itemView.findViewById(R.id.container);

        }
    }
}
