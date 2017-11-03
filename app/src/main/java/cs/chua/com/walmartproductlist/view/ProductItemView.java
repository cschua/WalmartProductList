package cs.chua.com.walmartproductlist.view;

/**
 * Created by christopherchua on 11/1/17.
 */

public interface ProductItemView {
    void setImageView(final String url);
    void setPriceTextView(final String price);
    void setNameTextView(final String name);
    void setRatingStarBar(final float rating);
    void setRatingCountTextView(final String ratingCount);
    void setOutOfStockVisibility(final boolean isVisible);
    void setShortDescriptionTextView(final String shortDescription);
    void setShortDescriptionVisibility(final boolean isVisible);
    void setLongDescriptionTextView(final String longDescription);
    void setLongDescriptionVisibility(final boolean isVisible);
}
