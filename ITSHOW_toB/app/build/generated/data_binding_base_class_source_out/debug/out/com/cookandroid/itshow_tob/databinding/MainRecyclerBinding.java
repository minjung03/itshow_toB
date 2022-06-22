// Generated by view binder compiler. Do not edit!
package com.cookandroid.itshow_tob.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.cookandroid.itshow_tob.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MainRecyclerBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView recycelerViewMain;

  private MainRecyclerBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView recycelerViewMain) {
    this.rootView = rootView;
    this.recycelerViewMain = recycelerViewMain;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MainRecyclerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MainRecyclerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.main_recycler, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MainRecyclerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recyceler_view_main;
      RecyclerView recycelerViewMain = rootView.findViewById(id);
      if (recycelerViewMain == null) {
        break missingId;
      }

      return new MainRecyclerBinding((LinearLayout) rootView, recycelerViewMain);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
