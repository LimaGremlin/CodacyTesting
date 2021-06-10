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
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wayapaychat.core.R
import com.wayapaychat.core.listeners.NoArgClickListener
import com.wayapaychat.core.utils.navigation.WayaNavigationCommand

abstract class BaseFragment<in D : ViewDataBinding, out V : BaseViewModel> : Fragment() {

    /**
     * There should be an injection done here
     * example -
     * *@Inject
     * lateinit var getLoggedInUser: GetLoggedInUser
     * For more information, check;
     * https://github.com/google/dagger/issues/955 and
     * https://github.com/google/dagger/issues/1104
     */

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getBindingVariable(): Int

    abstract fun getLayoutBinding(binding: D)

    open fun setViewModelObservers() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: D = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.apply {
            setVariable(getBindingVariable(), getViewModel())
            executePendingBindings()
            lifecycleOwner = this@BaseFragment
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
        val dialog = AlertDialog.Builder(requireContext()).apply {
            val binding: BINDING = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
            with(binding.root) {
                findViewById<MaterialButton>(R.id.positive_button).setOnClickListener {
                    positiveButtonClickListener?.invoke()
                }
                findViewById<AppCompatImageView>(R.id.negative_button).setOnClickListener {
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
        val dialog = AlertDialog.Builder(requireContext()).create()
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

    protected fun <BINDING : ViewDataBinding> createSingleButtonDialogWithClickListener(
        @LayoutRes layoutId: Int,
        buttonClickListener: NoArgClickListener? = null,
        parent: ViewGroup,
        bindingVariable: (B: BINDING) -> Unit,
        dialogVariable: (D: AlertDialog) -> Unit
    ): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val binding: BINDING = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
        with(binding.root) {
            findViewById<MaterialButton>(R.id.okay_button).setOnClickListener {
                buttonClickListener?.invoke()
            }
        }
        bindingVariable(binding)
        dialogVariable(dialog)
        dialog.setView(binding.root)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.WayaAppDialogAnimation
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        dialog.setCancelable(false)
        return dialog
    }

    protected fun navigate(navCommand: WayaNavigationCommand) {
        when (navCommand) {
            is WayaNavigationCommand.Back -> findNavController().navigateUp()
            is WayaNavigationCommand.To -> findNavController().navigate(navCommand.direction)
            //is GokadaNavigationCommand.BackToRoot -> findNavController().popBackStack()
        }
    }

    open fun setBackPressedListener(actionToPerform: () -> Unit) {
        var backPressedCallback: OnBackPressedCallback? = null
        val performActionAndRemoveCallback = {
            actionToPerform()
            backPressedCallback!!.remove()
        }
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                performActionAndRemoveCallback()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
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

    fun showSnackBar(
        view: View,
        message: String,
        isError: Boolean = false,
        duration: Int = Snackbar.LENGTH_SHORT,
        isWarning: Boolean = false
    ) =
        (activity as BaseActivity<*, *>).showSnackBar(view, message, isError, duration, isWarning)

    fun showLoadingDialog() = (activity as BaseActivity<*, *>).showLoadingDialog()

    fun dismissLoadingDialog() = (activity as BaseActivity<*, *>).dismissLoadingDialog()

    fun isNetworkConnected(): Boolean = (activity as BaseActivity<*, *>).isNetworkConnected()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getViewModel().navigationCommand.observe(viewLifecycleOwner, { navCommand ->
            when (navCommand) {
                is WayaNavigationCommand.Back -> findNavController().navigateUp()
                is WayaNavigationCommand.To -> findNavController().navigate(navCommand.direction)
            }
        })
    }
}
