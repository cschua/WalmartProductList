package cs.chua.com.walmartproductlist.presenter;

import java.util.ArrayList;
import java.util.List;

import cs.chua.com.walmartproductlist.model.remote.Product;
import cs.chua.com.walmartproductlist.view.holder.ProductItemViewHolder;

/**
 * Created by christopherchua on 11/1/17.
 */

public class ProductItemPresenter {
    public final static int MODE_LIST = 1;
    public final static int MODE_PRESENTATION = 2;
    private final static int MAX_RATING_COUNT = 9999;

    private final List<Product> productList;
    private int mode;

    /**
     *
     * @param mode ProductItemPresenter.MODE_LIST, ProductItemPresenter.MODE_PRESENTATION
     */
    public ProductItemPresenter(final int mode) {
        productList = new ArrayList<>();
        this.mode = mode;
    }

    public void onPopulateViewHolder(final ProductItemViewHolder viewHolder, final int position) {

        if (MODE_LIST == mode) {
            populateProduct(productList.get(position), viewHolder);
        } else {
            populateDetailedProduct(productList.get(position), viewHolder);
        }
    }

    private void populateProduct(final Product product, final ProductItemViewHolder holder) {
        holder.setImageView(product.getImage());
        holder.setPriceTextView(product.getPrice());
        holder.setNameTextView(product.getName());
        holder.setRatingStarBar(product.getReviewRating().floatValue());

        final int reviewCount = product.getReviewCount();
        if (reviewCount > MAX_RATING_COUNT) {
            holder.setRatingCountTextView(Integer.toString(MAX_RATING_COUNT) + "+");
        } else {
            holder.setRatingCountTextView(Integer.toString(reviewCount));
        }

        if (product.getInStock()) {
            holder.setOutOfStockVisibility(false);
        } else {
            holder.setOutOfStockVisibility(true);
        }
    }

    private void populateDetailedProduct(final Product product, final ProductItemViewHolder holder) {
        holder.itemView.scrollTo(0, 0); // reset scroll position
        populateProduct(product, holder);

        final String shortDescription = product.getShortDescription();
        if (shortDescription == null) {
            holder.setShortDescriptionVisibility(false);
        } else {
            holder.setShortDescriptionTextView(shortDescription);
            holder.setShortDescriptionVisibility(true);
        }

        final String longDescription = product.getLongDescription();
        if (longDescription == null) {
            holder.setLongDescriptionVisibility(false);
        } else {
            holder.setLongDescriptionTextView(longDescription);
            holder.setLongDescriptionVisibility(true);
        }
    }

    public int getProductCount() {
        return productList.size();
    }

    public void addProducts(final List<Product> items) {
        if (items == null) {
            return;
        }
        productList.addAll(items);
    }

    public void addProduct(final Product product) {
        productList.add(product);
    }

    public void removeProduct(final int position) {
        productList.remove(position);
    }
}
