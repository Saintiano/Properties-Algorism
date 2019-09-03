package com.codeimagination.properties.Interface;

import com.codeimagination.properties.models.Dashboard_Slider_Items;

import java.util.List;

public interface IFirestoreLoadDone {

    void onFireStoreLoadSuccess(List<Dashboard_Slider_Items> dashboard_slider_items);
    void onFireStoreLoadFailed(String message);

}
