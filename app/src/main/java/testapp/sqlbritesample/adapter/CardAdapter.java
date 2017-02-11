package testapp.sqlbritesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import testapp.sqlbritesample.R;
import testapp.sqlbritesample.model.ContentModel;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<ContentModel> contentList;
    final String TAG = "CardAdapter";
    private Context mContext;
    private OnLoadMore onLoadMore ;

    public void setOnLoadMore(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public interface OnLoadMore {
        void onLoadMoreCall(int id);
    }

    public CardAdapter(List<ContentModel> contentList, Context mContext) {
        this.contentList = contentList;
        this.mContext = mContext ;
        Log.e(TAG, "contentList->" + contentList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        final ContentModel cm = contentList.get(i);

//        contactViewHolder.position = contactViewHolder.getAdapterPosition();
        myViewHolder.position = i;

        myViewHolder.tvId.setText(cm.getId()+"");
        myViewHolder.tvName.setText(cm.getName());
        myViewHolder.tvLanguage.setText(cm.getLanguage());

        if ( ( i >= ( contentList.size() - 1 ) ) && onLoadMore != null ){
            onLoadMore.onLoadMoreCall( contentList.get(i).getId() );
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId;
        TextView tvName;
        TextView tvLanguage;
        int position = 0 ;

        MyViewHolder(View v) {
            super(v);
            tvId = (TextView)  v.findViewById(R.id.tvId);
            tvName = (TextView)  v.findViewById(R.id.tvName);
            tvLanguage = (TextView)  v.findViewById(R.id.tvLanguage);
        }
    }
}