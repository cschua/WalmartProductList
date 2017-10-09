package cs.chua.com.walmartproductlist.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by christopherchua on 10/6/17.
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    private ItemClickListener itemClickListener;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
        }
        return true;
    }

    public interface ItemClickListener {
        void onClick(final View view, int position, boolean isLongClick);
    }
}
