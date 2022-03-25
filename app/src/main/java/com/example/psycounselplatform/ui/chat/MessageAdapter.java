package com.example.psycounselplatform.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.psycounselplatform.R;
import com.example.psycounselplatform.data.model.Message;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<Message> mList;
    private Context context;
    private LayoutInflater mInflater;
    private View.OnClickListener clickListener;

    public MessageAdapter(Context context, List<Message> mList){
        this.context = context;
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = mList.get(position);

        ViewHolder viewHolder = null;
        if(convertView == null){
            if(message.isMe()){
                convertView = mInflater.inflate(R.layout.item_message_right, null);
            }else {
                convertView = mInflater.inflate(R.layout.item_message_left, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.profile = convertView.findViewById(R.id.profile);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(message.getContent());
        viewHolder.profile.setImageDrawable(context.getDrawable(R.drawable.profile));

        convertView.setOnClickListener(clickListener);
        return convertView;
    }

    static class ViewHolder{
        public TextView content;
        public ImageView profile;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setmList(List<Message> mList) {
        this.mList = mList;
    }
}
