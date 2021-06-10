package com.wayapaychat.ng.walletTopup;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.wayapaychat.ng.walletTopup.databinding.ReuseableBillerOptionItemBindingImpl;
import com.wayapaychat.ng.walletTopup.databinding.WhyBvnDialogBindingImpl;
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
  private static final int LAYOUT_REUSEABLEBILLEROPTIONITEM = 1;

  private static final int LAYOUT_WHYBVNDIALOG = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.wayapaychat.ng.walletTopup.R.layout.reuseable_biller_option_item, LAYOUT_REUSEABLEBILLEROPTIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.wayapaychat.ng.walletTopup.R.layout.why_bvn_dialog, LAYOUT_WHYBVNDIALOG);
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
        case  LAYOUT_REUSEABLEBILLEROPTIONITEM: {
          if ("layout/reuseable_biller_option_item_0".equals(tag)) {
            return new ReuseableBillerOptionItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for reuseable_biller_option_item is invalid. Received: " + tag);
        }
        case  LAYOUT_WHYBVNDIALOG: {
          if ("layout/why_bvn_dialog_0".equals(tag)) {
            return new WhyBvnDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for why_bvn_dialog is invalid. Received: " + tag);
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
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(3);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.wayapaychat.core.DataBinderMapperImpl());
    result.add(new com.wayapaychat.navigation.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(14);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "buttonImage");
      sKeys.put(2, "buttonLabel");
      sKeys.put(3, "buttonText");
      sKeys.put(4, "descriptionText");
      sKeys.put(5, "displayText");
      sKeys.put(6, "emptyImage");
      sKeys.put(7, "hintText");
      sKeys.put(8, "leftImage");
      sKeys.put(9, "rightImage");
      sKeys.put(10, "showBottomline");
      sKeys.put(11, "startImage");
      sKeys.put(12, "stateImage");
      sKeys.put(13, "toolBarTitle");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/reuseable_biller_option_item_0", com.wayapaychat.ng.walletTopup.R.layout.reuseable_biller_option_item);
      sKeys.put("layout/why_bvn_dialog_0", com.wayapaychat.ng.walletTopup.R.layout.why_bvn_dialog);
    }
  }
}
