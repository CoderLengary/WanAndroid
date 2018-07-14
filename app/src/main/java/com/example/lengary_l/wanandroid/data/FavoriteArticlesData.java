package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class FavoriteArticlesData {
    @Expose
    @SerializedName("errorCode")
    private int errorCode;
    @Expose
    @SerializedName("errorMsg")
    private String errorMsg;
    @Expose
    @SerializedName("data")
    private Data data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @Expose
        @SerializedName("curPage")
        private int curPage;
        @Expose
        @SerializedName("datas")
        private List<FavoriteArticleDetailData> datas;
        @Expose
        @SerializedName("offset")
        private int offset;
        @Expose
        @SerializedName("over")
        private boolean over;
        @Expose
        @SerializedName("pageCount")
        private int pageCount;
        @Expose
        @SerializedName("size")
        private int size;
        @Expose
        @SerializedName("total")
        private int total;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public List<FavoriteArticleDetailData> getDatas() {
            return datas;
        }

        public void setDatas(List<FavoriteArticleDetailData> datas) {
            this.datas = datas;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
