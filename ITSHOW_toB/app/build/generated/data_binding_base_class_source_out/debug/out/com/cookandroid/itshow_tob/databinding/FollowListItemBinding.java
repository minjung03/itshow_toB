// Generated by view binder compiler. Do not edit!
package com.cookandroid.itshow_tob.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.cookandroid.itshow_tob.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FollowListItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CircleImageView imgUserProfile;

  @NonNull
  public final LinearLayout linearlayoutFollow;

  @NonNull
  public final TextView textUserName;

  private FollowListItemBinding(@NonNull LinearLayout rootView,
      @NonNull CircleImageView imgUserProfile, @NonNull LinearLayout linearlayoutFollow,
      @NonNull TextView textUserName) {
    this.rootView = rootView;
    this.imgUserProfile = imgUserProfile;
    this.linearlayoutFollow = linearlayoutFollow;
    this.textUserName = textUserName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FollowListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FollowListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.follow_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FollowListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.img_userProfile;
      CircleImageView imgUserProfile = rootView.findViewById(id);
      if (imgUserProfile == null) {
        break missingId;
      }

      LinearLayout linearlayoutFollow = (LinearLayout) rootView;

      id = R.id.text_userName;
      TextView textUserName = rootView.findViewById(id);
      if (textUserName == null) {
        break missingId;
      }

      return new FollowListItemBinding((LinearLayout) rootView, imgUserProfile, linearlayoutFollow,
          textUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
