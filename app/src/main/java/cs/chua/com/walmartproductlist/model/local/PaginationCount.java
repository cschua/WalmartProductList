package cs.chua.com.walmartproductlist.model.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christopherchua on 10/4/17.
 */

public class PaginationCount implements Parcelable {
    public final static int TOTAL_ITEMS_PER_PAGE = 30; // better if this is from the server
    private final int totalItems;
    private final int totalPagination;
    private final int lastPaginationCount;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PaginationCount createFromParcel(Parcel in) {
            return new PaginationCount(in);
        }

        public PaginationCount[] newArray(int size) {
            return new PaginationCount[size];
        }
    };

    public PaginationCount(final Parcel in) {
        this(in.readInt());
    }

    public PaginationCount(final int totalItems) {
        this.totalItems = totalItems; // total products from the server
        totalPagination = totalItems / TOTAL_ITEMS_PER_PAGE;
        lastPaginationCount = totalItems % TOTAL_ITEMS_PER_PAGE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalItems);
    }

    public int getTotalPagination() {
        // lastPaginationCount means we need to add another page for the remaining items
        return lastPaginationCount > 0 ? totalPagination + 1 : totalPagination;
    }

    public int getItemsCountForPage(final int pageNumber) {
        if (pageNumber == getTotalPagination()) {
            return getLastPaginationCount();
        }
        return TOTAL_ITEMS_PER_PAGE;
    }

    private int getLastPaginationCount() {
        // totalPagination might not be a perfect divisible of TOTAL_ITEMS_PER_PAGE, therefore
        // lastPaginationCount may contain the remainder count
        return lastPaginationCount > 0 ? lastPaginationCount : TOTAL_ITEMS_PER_PAGE;
    }
}
