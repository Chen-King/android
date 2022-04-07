package com.example.psycounselplatform.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psycounselplatform.R;
import com.example.psycounselplatform.data.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> mList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private View.OnClickListener clickListener;

    public enum ITEM_TYPE {
        ME,
        OTHER
    }

    public MessageAdapter(Context context, List<Message> messages){
        this.context = context;
        this.mList = messages;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setmList(List<Message> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.ME.ordinal()){
            return new MeViewHolder(mLayoutInflater.inflate(R.layout.item_message_right, parent, false));
        }
        return new OtherViewHolder(mLayoutInflater.inflate(R.layout.item_message_left, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return (mList.get(position).isSelf() ? ITEM_TYPE.ME: ITEM_TYPE.OTHER).ordinal();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MeViewHolder){
            ((MeViewHolder)holder).content.setText(mList.get(position).getContent());
        }else if(holder instanceof OtherViewHolder){
            ((OtherViewHolder)holder).content.setText(mList.get(position).getContent());
        }
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MeViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        public MeViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    public static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        public OtherViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
