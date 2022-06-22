// Generated by data binding compiler. Do not edit!
package com.cookandroid.itshow_tob.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.cookandroid.itshow_tob.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class HeartListBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frame;

  @NonNull
  public final Button heartListPopular;

  @NonNull
  public final Button heartListRecent;

  @NonNull
  public final LinearLayout layoutSearchRecycler;

  @NonNull
  public final RecyclerView recycelerHeartList;

  protected HeartListBinding(Object _bindingComponent, View _root, int _localFieldCount,
      FrameLayout frame, Button heartListPopular, Button heartListRecent,
      LinearLayout layoutSearchRecycler, RecyclerView recycelerHeartList) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frame = frame;
    this.heartListPopular = heartListPopular;
    this.heartListRecent = heartListRecent;
    this.layoutSearchRecycler = layoutSearchRecycler;
    this.recycelerHeartList = recycelerHeartList;
  }

  @NonNull
  public static HeartListBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.heart_list, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static HeartListBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<HeartListBinding>inflateInternal(inflater, R.layout.heart_list, root, attachToRoot, component);
  }

  @NonNull
  public static HeartListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.heart_list, null, false, component)
   */
  @NonNull
  @Deprecated
  public static HeartListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<HeartListBinding>inflateInternal(inflater, R.layout.heart_list, null, false, component);
  }

  public static HeartListBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static HeartListBinding bind(@NonNull View view, @Nullable Object component) {
    return (HeartListBinding)bind(component, view, R.layout.heart_list);
  }
}
