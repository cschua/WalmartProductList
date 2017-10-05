package cs.chua.com.walmartproductlist.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private ItemClickListener itemClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
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
