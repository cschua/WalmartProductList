package cs.chua.com.walmartproductlist.controller;

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
import cs.chua.com.walmartproductlist.view.ProductViewHolder;

/**
 * Created by christopherchua on 10/3/17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private final static int MAX_RATING_COUNT = 9999;
    private List<Product> productList;
    private final LayoutInflater inflater;

    public ProductListAdapter(final Context context) {
        productList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.product_summary_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = productList.get(position);

        GlideApp.with(holder.itemView.getContext())
                .load(product.getProductImage())
                .centerCrop()
                .into(holder.imageView);

        holder.priceTextView.setText(product.getPrice());
        holder.nameTextView.setText(product.getProductName());

        holder.ratingStars.setRating(product.getReviewRating().floatValue());

        final int reviewCount = product.getReviewCount();
        if (reviewCount > MAX_RATING_COUNT) {
            holder.ratingCountTextView.setText(Integer.toString(MAX_RATING_COUNT)+"+");
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

    public void setList(List<Product> productList) {
        this.productList = productList;
    }
}
