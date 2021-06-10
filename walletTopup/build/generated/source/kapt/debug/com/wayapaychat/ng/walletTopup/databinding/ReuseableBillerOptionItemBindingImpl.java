package com.wayapaychat.ng.walletTopup.databinding;
import com.wayapaychat.ng.walletTopup.R;
import com.wayapaychat.ng.walletTopup.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ReuseableBillerOptionItemBindingImpl extends ReuseableBillerOptionItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final com.google.android.material.textview.MaterialTextView mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ReuseableBillerOptionItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private ReuseableBillerOptionItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            );
        this.container.setTag(null);
        this.mboundView1 = (com.google.android.material.textview.MaterialTextView) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.showBottomline == variableId) {
            setShowBottomline((java.lang.Boolean) variable);
        }
        else if (BR.displayText == variableId) {
            setDisplayText((java.lang.String) variable);
        }
        else if (BR.startImage == variableId) {
            setStartImage((android.graphics.drawable.Drawable) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setShowBottomline(@Nullable java.lang.Boolean ShowBottomline) {
        this.mShowBottomline = ShowBottomline;
    }
    public void setDisplayText(@Nullable java.lang.String DisplayText) {
        this.mDisplayText = DisplayText;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.displayText);
        super.requestRebind();
    }
    public void setStartImage(@Nullable android.graphics.drawable.Drawable StartImage) {
        this.mStartImage = StartImage;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.startImage);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String displayText = mDisplayText;
        android.graphics.drawable.Drawable startImage = mStartImage;

        if ((dirtyFlags & 0xaL) != 0) {
        }
        if ((dirtyFlags & 0xcL) != 0) {
        }
        // batch finished
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, displayText);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setDrawableStart(this.mboundView1, startImage);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): showBottomline
        flag 1 (0x2L): displayText
        flag 2 (0x3L): startImage
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}