package com.wayapaychat.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wayapaychat.core.R
import com.wayapaychat.core.listeners.NoArgClickListener
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand

abstract class BaseBottomSheetDialogFragment<in D : ViewDataBinding, out V : BaseViewModel> : BottomSheetDialogFragment() {

    /**
     * *@Inject
     * *lateinit var getLoggedInUser: GetLoggedInUser
     * For more information, check;
     * https://github.com/google/dagger/issues/955 and
     * https://github.com/google/dagger/issues/1104
     */

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getBindingVariable(): Int

    abstract fun getLayoutBinding(binding: D)

    open fun setViewModelObservers(){}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: D = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.apply {
            setVariable(getBindingVariable(), getViewModel())
            executePendingBindings()
            lifecycleOwner = this@BaseBottomSheetDialogFragment
        }
        setViewModelObservers()
        getLayoutBinding(binding)
        return binding.root
    }

    protected fun <BINDING : ViewDataBinding> createDoubleButtonDialog(
        @LayoutRes layoutId: Int,
        parent: ViewGroup,
        positiveButtonClickListener: NoArgClickListener? = null,
        negativeButtonClickListener: NoArgClickListener? = null
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context!!).apply {
            val binding: BINDING = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
            with(binding.root) {
                findViewById<MaterialButton>(R.id.positive_button).setOnClickListener {
                    positiveButtonClickListener?.invoke()
                }
                findViewById<MaterialButton>(R.id.negative_button).setOnClickListener {
                    negativeButtonClickListener?.invoke()
                }
            }
            setView(binding.root)
        }.create()
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.WayaAppDialogAnimation
        }
        return dialog
    }

    protected fun <BINDING : ViewDataBinding> createSingleButtonDialog(
        @LayoutRes layoutId: Int,
        parent: ViewGroup
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context!!).create()
        val binding: BINDING = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
        with(binding.root) {
            findViewById<MaterialButton>(R.id.okay_button).setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.setView(binding.root)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.WayaAppDialogAnimation
        }
        return dialog
    }

    protected fun navigate(navCommand: WayaNavigationCommand) {
        when (navCommand) {
            is WayaNavigationCommand.Back -> findNavController().navigateUp()
            is WayaNavigationCommand.To -> findNavController().navigate(navCommand.direction)
        }
    }

    open fun setBackPressedListener(actionToPerform: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                actionToPerform()
            }
        })
    }

    fun hideKeyBoard(token: IBinder) {
        val inputMethodManager = activity?.getSystemService<InputMethodManager>()
        inputMethodManager?.hideSoftInputFromWindow(token, 0)
    }

    fun showKeyBoard() {
        val inputMethodManager = activity?.getSystemService<InputMethodManager>()
        inputMethodManager?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun showSnackBar(view: View, message: String, isError: Boolean = false, duration: Int = Snackbar.LENGTH_SHORT, isWarning: Boolean = false) =
        (activity as BaseActivity<*, *>).showSnackBar(view, message, isError, duration, isWarning)

    fun showLoadingDialog() = (activity as BaseActivity<*, *>).showLoadingDialog()

    fun dismissLoadingDialog() = (activity as BaseActivity<*, *>).dismissLoadingDialog()

    fun isNetworkConnected() : Boolean = (activity as BaseActivity<*, *>).isNetworkConnected()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getViewModel().navigationCommand.observe(this, Observer { navCommand ->
            when (navCommand) {
                is WayaNavigationCommand.Back -> findNavController().navigateUp()
                is WayaNavigationCommand.To -> findNavController().navigate(navCommand.direction)
            }
        })
    }
}
