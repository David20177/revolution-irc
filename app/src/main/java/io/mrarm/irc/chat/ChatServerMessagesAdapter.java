package io.mrarm.irc.chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import io.mrarm.irc.R;
import io.mrarm.irc.util.ColoredTextBuilder;

public class ChatServerMessagesAdapter extends RecyclerView.Adapter<ChatServerMessagesAdapter.ViewHolder> {

    private ArrayList<Item> mItems = new ArrayList<>();
    private int mSecondaryColor;

    public ChatServerMessagesAdapter(Context context) {
        TypedArray ta = context.obtainStyledAttributes(R.style.AppTheme, new int[] {
                android.R.attr.textColorSecondary });
        mSecondaryColor = ta.getColor(0, Color.BLACK);
        ta.recycle();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.server_command_error_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(Item item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(int index) {
        mItems.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItem(Item item) {
        removeItem(mItems.indexOf(item));
    }

    public void clear() {
        int i = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, i);
    }

    public interface Item {

        CharSequence getText();

        void onClick();

    }

    public static class CommandErrorItem implements Item {

        private ChatServerMessagesAdapter mAdapter;
        private String mCommand;
        private CharSequence mError;
        private OnClickListener mListener;

        public CommandErrorItem(ChatServerMessagesAdapter adapter, String command, CharSequence error, OnClickListener listener) {
            mAdapter = adapter;
            mCommand = command;
            mError = error;
            mListener = listener;
        }

        @Override
        public CharSequence getText() {
            ColoredTextBuilder b = new ColoredTextBuilder();
            b.append(mCommand, new ForegroundColorSpan(mAdapter.mSecondaryColor));
            b.append(" ");
            b.append(mError);
            return b.getSpannable();
        }

        @Override
        public void onClick() {
            mListener.onClick(this);
        }

        public String getCommand() {
            return mCommand;
        }

        public interface OnClickListener {

            void onClick(CommandErrorItem item);

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view;
            view.setOnClickListener((View v) -> ((Item) mTextView.getTag()).onClick());
        }

        public void bind(Item item) {
            mTextView.setText(item.getText());
            mTextView.setTag(item);
        }

    }

}
