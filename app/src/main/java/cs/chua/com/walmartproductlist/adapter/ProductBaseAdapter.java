package cs.chua.com.walmartproductlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.presenter.ProductItemPresenter;
import cs.chua.com.walmartproductlist.util.PaginationScrollListener;
import cs.chua.com.walmartproductlist.view.holder.BaseRecyclerViewHolder;
import cs.chua.com.walmartproductlist.view.holder.ProductItemViewHolder;

/**
 * Created by christopherchua on 10/13/17.
 */

public abstract class ProductBaseAdapter extends RecyclerView.Adapter<ProductItemViewHolder> {
    public abstract View getInflatedView(final LayoutInflater inflater, final ViewGroup parent);

    public abstract BaseRecyclerViewHolder.ItemClickListener getViewClickListener();

    private final static int TYPE_PRODUCT = 0;
    private final static int TYPE_PROGRESS_LOADING = 1;

    private final ProductItemPresenter presenter;
    private final LayoutInflater inflater;
    private PaginationScrollListener paginationScrollListener;
    private boolean isLoadingAdded;

    public ProductBaseAdapter(final Context context, final ProductItemPresenter presenter, final boolean isLoadingAdded) {
        inflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.isLoadingAdded = isLoadingAdded;
    }

    @Override
    public ProductItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        // we're supporting pagination so check for viewType
        if (TYPE_PRODUCT == viewType) { // default behavior, it's a product
            itemView = getInflatedView(inflater, parent);
        } else { // otherwise it's the progress item (should be the last item if available)
            itemView = inflater.inflate(R.layout.item_progress, parent, false);
        }
        return new ProductItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductItemViewHolder holder, int position) {
        // we're supporting pagination so we need to make sure to bind on product
        if (TYPE_PRODUCT == getItemViewType(position)) {
            presenter.onPopulateViewHolder(holder, position);
            holder.setItemClickListener(getViewClickListener());
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getProductCount();
    }

    @Override
    public int getItemViewType(int position) {
        // last one is progress item if there are more items to load
        boolean isLoading = position == getItemCount() - 1 && isLoadingAdded();
        return isLoading ? TYPE_PROGRESS_LOADING : TYPE_PRODUCT;
    }

    public void onDestroy() {
        paginationScrollListener = null;
    }

    public void setPaginationScrollListener(final PaginationScrollListener onScrollListener) {
        this.paginationScrollListener = onScrollListener;
    }

    // the following methods below are used for pagination
    public void updateList(final List<Product> items) {
        if (paginationScrollListener != null) {
            // reset the loading state of pagination
            paginationScrollListener.updateIsLoading(false);
        }

        if (getItemCount() > 0) {
            // if we have items in the list, we need to make sure to remove the progress first
            removeLoadingView();
        }
        presenter.addProducts(items);

        // we only support pagination if a listener is provided and there are more items to load
        if (paginationScrollListener != null && paginationScrollListener.moreItemsToLoad()) {
            addLoadingView();
        } else {
            removeLoadingView();
        }
    }

    public void addLoadingView() {
        // add a progress item at the end of the list
        if (!isLoadingAdded) {
            isLoadingAdded = true;
            presenter.addProduct(null);
            notifyDataSetChanged();
        }
    }

    public void removeLoadingView() {
        // remove the progress item (it's the last one in the list)
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = getItemCount() - 1;
            if (position >= 0) {
                presenter.removeProduct(position);
                notifyItemRemoved(position);
            }
        }
    }

    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }
}
