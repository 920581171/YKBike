package com.yk.bike.response;

import java.util.List;

public class SiteLocationListResponse extends BaseResponse{

    /**
     * data : {"id":4,"siteId":"site514394","latitude":123,"longitude":456,"radius":123}
     */

    private List<SiteLocationResponse.SiteLocation> data;

    public List<SiteLocationResponse.SiteLocation> getData() {
        return data;
    }

    public void setData(List<SiteLocationResponse.SiteLocation> data) {
        this.data = data;
    }

}
