package com.wayapaychat.ng.walletTopup.databinding;
import com.wayapaychat.ng.walletTopup.R;
import com.wayapaychat.ng.walletTopup.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class WhyBvnDialogBindingImpl extends WhyBvnDialogBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.leftGuide, 1);
        sViewsWithIds.put(R.id.rightGuide, 2);
        sViewsWithIds.put(R.id.negative_button, 3);
        sViewsWithIds.put(R.id.why_bvn_text, 4);
        sViewsWithIds.put(R.id.red_text, 5);
        sViewsWithIds.put(R.id.we_only_need_text, 6);
        sViewsWithIds.put(R.id.full_name_text, 7);
        sViewsWithIds.put(R.id.phone_number_text, 8);
        sViewsWithIds.put(R.id.date_of_birth_text, 9);
        sViewsWithIds.put(R.id.positive_button, 10);
    }
    // views
    @NonNull
    private final androidx.cardview.widget.CardView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WhyBvnDialogBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private WhyBvnDialogBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.appcompat.widget.AppCompatTextView) bindings[9]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[7]
            , (androidx.constraintlayout.widget.Guideline) bindings[1]
            , (androidx.appcompat.widget.AppCompatImageView) bindings[3]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[8]
            , (com.google.android.material.button.MaterialButton) bindings[10]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[5]
            , (androidx.constraintlayout.widget.Guideline) bindings[2]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[6]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[4]
            );
        this.mboundView0 = (androidx.cardview.widget.CardView) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
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
            return variableSet;
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}