package cs.chua.com.walmartproductlist.model.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christopherchua on 10/4/17.
 */

public class PaginationPageCount implements Parcelable {
    public final static int TOTAL_ITEMS_PER_PAGE = 30;
    private final int totalItems;
    private final int totalPages;
    private final int lastPageItemsCount;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PaginationPageCount createFromParcel(Parcel in) {
            return new PaginationPageCount(in);
        }

        public PaginationPageCount[] newArray(int size) {
            return new PaginationPageCount[size];
        }
    };

    public PaginationPageCount(final Parcel in) {
        this(in.readInt());
    }

    public PaginationPageCount(final int totalItems) {
        this.totalItems = totalItems;
        totalPages = totalItems / TOTAL_ITEMS_PER_PAGE;
        lastPageItemsCount = totalItems % TOTAL_ITEMS_PER_PAGE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalItems);
    }

    public int getTotalPages() {
        // lastPageItemsCount means we need to add another page for the remaining items
        return lastPageItemsCount > 0 ? totalPages + 1 : totalPages;
    }

    public int getItemsCountForPage(final int pageNumber) {
        if (pageNumber == getTotalPages()) {
            return getLastPageItemsCount();
        }
        return TOTAL_ITEMS_PER_PAGE;
    }

    private int getLastPageItemsCount() {
        // totalPages might not be a perfect divisible of TOTAL_ITEMS_PER_PAGE, therefore
        // lastPageItemsCount may contain the remainder count
        return lastPageItemsCount > 0 ? lastPageItemsCount : TOTAL_ITEMS_PER_PAGE;
    }
}
