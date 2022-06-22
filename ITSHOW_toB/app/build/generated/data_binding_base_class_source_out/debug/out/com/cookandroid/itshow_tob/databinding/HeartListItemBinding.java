// Generated by view binder compiler. Do not edit!
package com.cookandroid.itshow_tob.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.cookandroid.itshow_tob.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HeartListItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RelativeLayout Title;

  @NonNull
  public final TextView itemDate;

  @NonNull
  public final TextView itemHeartUser;

  @NonNull
  public final TextView itemLocationUser;

  @NonNull
  public final TextView itemMinPrice;

  @NonNull
  public final TextView itemTextDetail;

  @NonNull
  public final TextView itemTitle;

  @NonNull
  public final TextView itemTitlePrice;

  @NonNull
  public final LinearLayout layoutMinPrice;

  private HeartListItemBinding(@NonNull LinearLayout rootView, @NonNull RelativeLayout Title,
      @NonNull TextView itemDate, @NonNull TextView itemHeartUser,
      @NonNull TextView itemLocationUser, @NonNull TextView itemMinPrice,
      @NonNull TextView itemTextDetail, @NonNull TextView itemTitle,
      @NonNull TextView itemTitlePrice, @NonNull LinearLayout layoutMinPrice) {
    this.rootView = rootView;
    this.Title = Title;
    this.itemDate = itemDate;
    this.itemHeartUser = itemHeartUser;
    this.itemLocationUser = itemLocationUser;
    this.itemMinPrice = itemMinPrice;
    this.itemTextDetail = itemTextDetail;
    this.itemTitle = itemTitle;
    this.itemTitlePrice = itemTitlePrice;
    this.layoutMinPrice = layoutMinPrice;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static HeartListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HeartListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.heart_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HeartListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Title;
      RelativeLayout Title = rootView.findViewById(id);
      if (Title == null) {
        break missingId;
      }

      id = R.id.item_date;
      TextView itemDate = rootView.findViewById(id);
      if (itemDate == null) {
        break missingId;
      }

      id = R.id.item_heartUser;
      TextView itemHeartUser = rootView.findViewById(id);
      if (itemHeartUser == null) {
        break missingId;
      }

      id = R.id.item_locationUser;
      TextView itemLocationUser = rootView.findViewById(id);
      if (itemLocationUser == null) {
        break missingId;
      }

      id = R.id.item_minPrice;
      TextView itemMinPrice = rootView.findViewById(id);
      if (itemMinPrice == null) {
        break missingId;
      }

      id = R.id.item_textDetail;
      TextView itemTextDetail = rootView.findViewById(id);
      if (itemTextDetail == null) {
        break missingId;
      }

      id = R.id.item_Title;
      TextView itemTitle = rootView.findViewById(id);
      if (itemTitle == null) {
        break missingId;
      }

      id = R.id.item_titlePrice;
      TextView itemTitlePrice = rootView.findViewById(id);
      if (itemTitlePrice == null) {
        break missingId;
      }

      id = R.id.layout_minPrice;
      LinearLayout layoutMinPrice = rootView.findViewById(id);
      if (layoutMinPrice == null) {
        break missingId;
      }

      return new HeartListItemBinding((LinearLayout) rootView, Title, itemDate, itemHeartUser,
          itemLocationUser, itemMinPrice, itemTextDetail, itemTitle, itemTitlePrice,
          layoutMinPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
