package cs.chua.com.walmartproductlist.util;

/**
 * Created by christopherchua on 10/9/17.
 */

public interface Pagination {
    void addLoadingView();
    void removeLoadingView();
    boolean isLoadingAdded();
}
