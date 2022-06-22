// Generated by view binder compiler. Do not edit!
package com.cookandroid.itshow_tob.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.cookandroid.itshow_tob.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SearchItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button itemButton;

  private SearchItemBinding(@NonNull LinearLayout rootView, @NonNull Button itemButton) {
    this.rootView = rootView;
    this.itemButton = itemButton;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SearchItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SearchItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.search_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SearchItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.item_button;
      Button itemButton = rootView.findViewById(id);
      if (itemButton == null) {
        break missingId;
      }

      return new SearchItemBinding((LinearLayout) rootView, itemButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
