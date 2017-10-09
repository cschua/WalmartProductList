package cs.chua.com.walmartproductlist.model.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by christopherchua on 10/5/17.
 */

public class Product implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(final Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(final int size) {
            return new Product[size];
        }
    };

    public Product() {
        // fake data, can be used in pagination or testing
        productId = "";
        productName = "";
        shortDescription = "";
        longDescription = "";
        price = "";
        productImage = "";
        reviewRating = 0d;
        reviewCount = 0;
        inStock = true;
    }

    public Product(final Parcel in) {
        productId = in.readString();
        productName = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        price = in.readString();
        productImage = in.readString();
        reviewRating = in.readDouble();
        reviewCount = in.readInt();
        inStock = in.readByte() != 0;
    }

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("longDescription")
    @Expose
    private String longDescription;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("reviewRating")
    @Expose
    private Double reviewRating;
    @SerializedName("reviewCount")
    @Expose
    private Integer reviewCount;
    @SerializedName("inStock")
    @Expose
    private Boolean inStock;

    public String getId() {
        return productId;
    }

    public void setId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return productName;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return productImage;
    }

    public void setImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeString(price);
        dest.writeString(productImage);
        dest.writeDouble(reviewRating);
        dest.writeInt(reviewCount);
        dest.writeByte((byte) (inStock ? 1 : 0));
    }
}
