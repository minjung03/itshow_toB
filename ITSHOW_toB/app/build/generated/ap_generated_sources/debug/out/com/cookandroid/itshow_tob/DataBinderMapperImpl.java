package com.cookandroid.itshow_tob;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.cookandroid.itshow_tob.databinding.HeartListBindingImpl;
import com.cookandroid.itshow_tob.databinding.MainBindingImpl;
import com.cookandroid.itshow_tob.databinding.ReviewBindingImpl;
import com.cookandroid.itshow_tob.databinding.SearchBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_HEARTLIST = 1;

  private static final int LAYOUT_MAIN = 2;

  private static final int LAYOUT_REVIEW = 3;

  private static final int LAYOUT_SEARCH = 4;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(4);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cookandroid.itshow_tob.R.layout.heart_list, LAYOUT_HEARTLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cookandroid.itshow_tob.R.layout.main, LAYOUT_MAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cookandroid.itshow_tob.R.layout.review, LAYOUT_REVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cookandroid.itshow_tob.R.layout.search, LAYOUT_SEARCH);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_HEARTLIST: {
          if ("layout/heart_list_0".equals(tag)) {
            return new HeartListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for heart_list is invalid. Received: " + tag);
        }
        case  LAYOUT_MAIN: {
          if ("layout/main_0".equals(tag)) {
            return new MainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for main is invalid. Received: " + tag);
        }
        case  LAYOUT_REVIEW: {
          if ("layout/review_0".equals(tag)) {
            return new ReviewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for review is invalid. Received: " + tag);
        }
        case  LAYOUT_SEARCH: {
          if ("layout/search_0".equals(tag)) {
            return new SearchBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for search is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(4);

    static {
      sKeys.put("layout/heart_list_0", com.cookandroid.itshow_tob.R.layout.heart_list);
      sKeys.put("layout/main_0", com.cookandroid.itshow_tob.R.layout.main);
      sKeys.put("layout/review_0", com.cookandroid.itshow_tob.R.layout.review);
      sKeys.put("layout/search_0", com.cookandroid.itshow_tob.R.layout.search);
    }
  }
}
