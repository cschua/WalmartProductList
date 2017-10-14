package cs.chua.com.walmartproductlist.controller.product.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.R;
import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.util.GlideApp;
import cs.chua.com.walmartproductlist.util.Pagination;
import cs.chua.com.walmartproductlist.util.PaginationScrollListener;
import cs.chua.com.walmartproductlist.view.ProductViewHolder;

/**
 * Created by christopherchua on 10/13/17.
 */

public abstract class ProductBaseAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Pagination {
    public abstract View getInflatedView(final LayoutInflater inflater, final ViewGroup parent);

    public abstract void setViewClickListener(final ProductViewHolder holder);

    private final static int MAX_RATING_COUNT = 9999;
    private final static int TYPE_PRODUCT = 0;
    private final static int TYPE_PROGRESS_LOADING = 1;

    private PaginationScrollListener paginationScrollListener;
    private boolean isLoadingAdded = false;

    private final LayoutInflater inflater;
    private List<Product> productList;

    public interface OnProductItemListener {
        void onProductListAdapterClick(final int position);
    }

    public ProductBaseAdapter(final Context context, final boolean isLoadingAdded) {
        this.isLoadingAdded = isLoadingAdded;
        productList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        // we're supporting pagination so check for viewType
        if (TYPE_PRODUCT == viewType) { // default behavior, it's a product
            itemView = getInflatedView(inflater, parent);
        } else { // otherwise it's the progress item (should be the last item if available)
            itemView = inflater.inflate(R.layout.item_progress, parent, false);
        }
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // we're supporting pagination so we need to make sure to bind on product
        if (TYPE_PRODUCT == getItemViewType(position)) {
            final Product product = productList.get(position);
            populateProductItem(product, holder);
            setViewClickListener(holder);
        }
    }

    public void populateProductItem(final Product product, final ProductViewHolder holder) {
        GlideApp.with(holder.imageView.getContext())
                .load(product.getImage())
                .centerCrop()
                .into(holder.imageView);

        holder.priceTextView.setText(product.getPrice());
        holder.nameTextView.setText(product.getName());

        holder.ratingStars.setRating(product.getReviewRating().floatValue());

        final int reviewCount = product.getReviewCount();
        if (reviewCount > MAX_RATING_COUNT) {
            holder.ratingCountTextView.setText(Integer.toString(MAX_RATING_COUNT) + "+");
        } else {
            holder.ratingCountTextView.setText(Integer.toString(reviewCount));
        }

        if (product.getInStock()) {
            holder.outOfStockTextView.setVisibility(View.GONE);
        } else {
            holder.outOfStockTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // last one is progress item if there are more items to load
        boolean isLoading = position == productList.size() - 1 && isLoadingAdded;
        return isLoading ? TYPE_PROGRESS_LOADING : TYPE_PRODUCT;
    }

    public void onDestroy() {
        paginationScrollListener = null;
    }

    public List<Product> getProductList() {
        final List<Product> list = new ArrayList<>(this.productList);
        // clean up last item, which is an item for the progress used in pagination
        final int index = list.size() - 1;
        if (isLoadingAdded && list.get(index) == null) {
            list.remove(index);
        }
        return list;
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
            // we already have items in the list so we need to make sure to remove the progress
            removeLoadingView();
            productList.addAll(items);
        } else {
            productList = items == null ? new ArrayList<Product>() : items;
        }
        // we only support pagination if a listener is provided and there are more items to load
        if (paginationScrollListener != null && paginationScrollListener.moreItemsToLoad()) {
            addLoadingView();
        } else {
            removeLoadingView();
        }
    }

    @Override
    public void addLoadingView() {
        // add a progress item at the end of the list
        if (!isLoadingAdded && productList != null) {
            isLoadingAdded = true;
            productList.add(null);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeLoadingView() {
        // remove the progress item (it's the last one in the list)
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = getItemCount() - 1;
            if (position >= 0) {
                productList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }
}
