// Generated by data binding compiler. Do not edit!
package com.wayapaychat.ng.walletTopup.databinding;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.wayapaychat.ng.walletTopup.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ReuseableBillerOptionItemBinding extends ViewDataBinding {
  @NonNull
  public final ConstraintLayout container;

  @Bindable
  protected String mDisplayText;

  @Bindable
  protected Drawable mStartImage;

  @Bindable
  protected Boolean mShowBottomline;

  protected ReuseableBillerOptionItemBinding(Object _bindingComponent, View _root,
      int _localFieldCount, ConstraintLayout container) {
    super(_bindingComponent, _root, _localFieldCount);
    this.container = container;
  }

  public abstract void setDisplayText(@Nullable String displayText);

  @Nullable
  public String getDisplayText() {
    return mDisplayText;
  }

  public abstract void setStartImage(@Nullable Drawable startImage);

  @Nullable
  public Drawable getStartImage() {
    return mStartImage;
  }

  public abstract void setShowBottomline(@Nullable Boolean showBottomline);

  @Nullable
  public Boolean getShowBottomline() {
    return mShowBottomline;
  }

  @NonNull
  public static ReuseableBillerOptionItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.reuseable_biller_option_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ReuseableBillerOptionItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ReuseableBillerOptionItemBinding>inflateInternal(inflater, R.layout.reuseable_biller_option_item, root, attachToRoot, component);
  }

  @NonNull
  public static ReuseableBillerOptionItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.reuseable_biller_option_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ReuseableBillerOptionItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ReuseableBillerOptionItemBinding>inflateInternal(inflater, R.layout.reuseable_biller_option_item, null, false, component);
  }

  public static ReuseableBillerOptionItemBinding bind(@NonNull View view) {
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
  public static ReuseableBillerOptionItemBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ReuseableBillerOptionItemBinding)bind(component, view, R.layout.reuseable_biller_option_item);
  }
}