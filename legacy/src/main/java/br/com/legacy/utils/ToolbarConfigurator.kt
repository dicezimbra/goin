package br.com.legacy.utils

import androidx.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import br.com.R2
import br.com.databinding.ItemCustomToolbarBinding

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.item_new_custom_toolbar.view.*

import java.lang.Character.isLowerCase

/**
 * Created by teruya on 02/10/2017.
 */

object ToolbarConfigurator {
    /**
     * Set the parameters on a custom toolbar with a title, left and right icons.
     * @param toolbarLayout Custom toolbar layout
     * @param title Screen title
     * @param leftIconResId    Set the left icon if id != 0
     * @param rightIconResId   Set the right icon if id != 0
     */
    fun configToolbar(toolbarLayout: View, title: String, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int) {

        val includedToolbar = IncludedToolbar()
        ButterKnife.bind(includedToolbar, toolbarLayout)
        var titleCap = title
        if (!title.isEmpty() || isStringFormattable(title)) {
            titleCap = title.substring(0, 1).toUpperCase() + title.substring(1)
            toolbarLayout.toolbar_title.visibility = View.VISIBLE
            toolbarLayout.toolbar_title.text = titleCap

        } else {
            toolbarLayout.toolbar_title.visibility = View.GONE
        }

        if (leftIconResId != 0) {
            toolbarLayout.toolbar_left_icon.visibility = View.VISIBLE
            toolbarLayout.toolbar_left_icon.setImageResource(leftIconResId)
        } else {
            toolbarLayout.toolbar_left_icon.visibility = View.INVISIBLE
        }
        try {
            if (rightIconResId != 0) {
                toolbarLayout.toolbar_right_icon!!.visibility = View.VISIBLE
                toolbarLayout.toolbar_right_icon!!.setImageResource(rightIconResId)
            } else {
                toolbarLayout.toolbar_right_icon!!.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {

        }

        try {
            toolbarLayout.toolbar_right_second_icon!!.visibility = View.INVISIBLE
        } catch (e: Exception) {

        }

        TypefaceUtil.initilize(toolbarLayout.context)
        TypefaceUtil.boldFont(toolbarLayout.toolbar_title)
    }

    fun configToolbar(toolbarLayout: View, title: String, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int, @DrawableRes secondRightIconResId: Int) {

        val includedToolbar = IncludedToolbar()
        ButterKnife.bind(includedToolbar, toolbarLayout)
        var titleCap = title
        if (!title.isEmpty() || isStringFormattable(title)) {
            titleCap = title.substring(0, 1).toUpperCase() + title.substring(1)
            toolbarLayout.toolbar_title.visibility = View.VISIBLE
            toolbarLayout.toolbar_title.text = titleCap

        } else {
            toolbarLayout.toolbar_title.visibility = View.GONE
        }

        if (leftIconResId != 0) {
            toolbarLayout.toolbar_left_icon.visibility = View.VISIBLE
            toolbarLayout.toolbar_left_icon.setImageResource(leftIconResId)
        } else {
            toolbarLayout.toolbar_left_icon.visibility = View.INVISIBLE
        }
        try {
            if (rightIconResId != 0) {
                toolbarLayout.toolbar_right_icon!!.visibility = View.VISIBLE
                toolbarLayout.toolbar_right_icon!!.setImageResource(rightIconResId)
            } else {
                toolbarLayout.toolbar_right_icon!!.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {

        }

        //includedToolbar.middleIcon.setVisibility(View.GONE);

        try {
            if (secondRightIconResId != 0) {
                toolbarLayout.toolbar_right_second_icon!!.visibility = View.VISIBLE
                toolbarLayout.toolbar_right_second_icon!!.setImageResource(secondRightIconResId)
            } else {
                toolbarLayout.toolbar_right_second_icon!!.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {

        }


        TypefaceUtil.initilize(toolbarLayout.context)
        TypefaceUtil.boldFont(toolbarLayout.toolbar_title)
    }

    fun configToolbar(toolbarLayout: View, @DrawableRes middleIconResId: Int, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int) {
        val includedToolbar = IncludedToolbar()
        ButterKnife.bind(includedToolbar, toolbarLayout)
        if (middleIconResId != 0) {
            //includedToolbar.middleIcon.setVisibility(View.VISIBLE);
            //includedToolbar.middleIcon.setImageResource(middleIconResId);
        } else {
            //includedToolbar.middleIcon.setVisibility(View.GONE);
        }
        if (leftIconResId != 0) {
            toolbarLayout.toolbar_left_icon.visibility = View.VISIBLE
            toolbarLayout.toolbar_left_icon.setImageResource(leftIconResId)
        } else {
            toolbarLayout.toolbar_left_icon.visibility = View.INVISIBLE
        }
        try {
            if (rightIconResId != 0) {
                toolbarLayout.toolbar_right_icon!!.visibility = View.VISIBLE
                toolbarLayout.toolbar_right_icon!!.setImageResource(rightIconResId)
            } else {
                toolbarLayout.toolbar_right_icon!!.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {

        }

        try {
            toolbarLayout.toolbar_right_second_icon!!.visibility = View.INVISIBLE
        } catch (e: Exception) {

        }

        toolbarLayout.toolbar_title.visibility = View.GONE
    }

    fun configToolbar(binding: ItemCustomToolbarBinding, title: String, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int) {

        var titleCap = title
        if (!title.isEmpty() || isStringFormattable(title)) {
            titleCap = title.substring(0, 1).toUpperCase() + title.substring(1)
            binding.toolbarTitle.visibility = View.VISIBLE
            binding.toolbarTitle.text = titleCap
        } else {
            binding.toolbarTitle.visibility = View.GONE
        }

        if (leftIconResId != 0) {
            binding.toolbarLeftIcon.visibility = View.VISIBLE
            binding.toolbarLeftIcon.setImageResource(leftIconResId)
        } else {
            binding.toolbarLeftIcon.visibility = View.INVISIBLE
        }
        if (rightIconResId != 0) {
            binding.toolbarRightIcon.visibility = View.VISIBLE
            binding.toolbarRightIcon.setImageResource(rightIconResId)
        } else {
            binding.toolbarRightIcon.visibility = View.INVISIBLE
        }


        binding.toolbarMiddleIcon.visibility = View.GONE
    }

    fun configToolbar2(binding: ItemCustomToolbarBinding, @DrawableRes middleIconResId: Int, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int) {

        if (middleIconResId != 0) {
            binding.toolbarMiddleIcon.visibility = View.VISIBLE
            binding.toolbarMiddleIcon.setImageResource(leftIconResId)
        } else {
            binding.toolbarMiddleIcon.visibility = View.GONE
        }
        if (leftIconResId != 0) {
            binding.toolbarLeftIcon.visibility = View.VISIBLE
            binding.toolbarLeftIcon.setImageResource(leftIconResId)
        } else {
            binding.toolbarLeftIcon.visibility = View.INVISIBLE
        }
        if (rightIconResId != 0) {
            binding.toolbarRightIcon.visibility = View.VISIBLE
            binding.toolbarRightIcon.setImageResource(rightIconResId)
        } else {
            binding.toolbarRightIcon.visibility = View.INVISIBLE
        }
        binding.toolbarTitle.visibility = View.GONE
    }

    fun configToolbar(binding: ItemCustomToolbarBinding, @DrawableRes middleIconResId: Int, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int) {

        if (middleIconResId != 0) {
            binding.toolbarMiddleIcon.visibility = View.VISIBLE
            binding.toolbarMiddleIcon.setImageResource(leftIconResId)
        } else {
            binding.toolbarMiddleIcon.visibility = View.GONE
        }
        if (leftIconResId != 0) {
            binding.toolbarLeftIcon.visibility = View.VISIBLE
            binding.toolbarLeftIcon.setImageResource(leftIconResId)
        } else {
            binding.toolbarLeftIcon.visibility = View.INVISIBLE
        }
        if (rightIconResId != 0) {
            binding.toolbarRightIcon.visibility = View.VISIBLE
            binding.toolbarRightIcon.setImageResource(rightIconResId)
        } else {
            binding.toolbarRightIcon.visibility = View.INVISIBLE
        }

        binding.toolbarTitle.visibility = View.GONE
    }

    fun configToolbar(toolbarLayout: View, title: String, @DrawableRes leftIconResId: Int, @DrawableRes rightIconResId: Int, textRight: String?) {

        val includedToolbar = IncludedToolbar()
        ButterKnife.bind(includedToolbar, toolbarLayout)
        var titleCap = title
        if (!title.isEmpty() || isStringFormattable(title)) {
            titleCap = title.substring(0, 1).toUpperCase() + title.substring(1)
            toolbarLayout.toolbar_title.visibility = View.VISIBLE
            toolbarLayout.toolbar_title.text = titleCap

        } else {
            toolbarLayout.toolbar_title.visibility = View.GONE
        }

        if (leftIconResId != 0) {
            toolbarLayout.toolbar_left_icon.visibility = View.VISIBLE
            toolbarLayout.toolbar_left_icon.setImageResource(leftIconResId)
        } else {
            toolbarLayout.toolbar_left_icon.visibility = View.INVISIBLE
        }
        if (rightIconResId != 0) {
            toolbarLayout.toolbar_right_icon!!.visibility = View.VISIBLE
            toolbarLayout.toolbar_right_icon!!.setImageResource(rightIconResId)
        } else {
            toolbarLayout.toolbar_right_icon!!.visibility = View.INVISIBLE
        }

        if (textRight != null) {

            //includedToolbar.toolbarRightText.setText(textRight);
            //includedToolbar.toolbarRightText.setVisibility(View.VISIBLE);
        }
        //includedToolbar.middleIcon.setVisibility(View.GONE);
        try {
            toolbarLayout.toolbar_right_second_icon!!.visibility = View.INVISIBLE
        } catch (e: Exception) {

        }

        TypefaceUtil.initilize(toolbarLayout.context)
        TypefaceUtil.boldFont(toolbarLayout.toolbar_title)
    }


    fun isStringFormattable(s: String): Boolean {
        // s = MFDSMFPDSM -> return true
        // s = mkdfmsklfmsdklfsm -> return true
        // s = Amdnsdaiojdsaiodas -> return false
        if (!s.isEmpty()) {
            val isLowerCase = Character.isLowerCase(s[0])
            for (i in 1 until s.length) {
                if (isLowerCase != Character.isLowerCase(s[i]) && s[i] != ' ') {
                    return false
                }
            }
            return true
        }
        return false
    }

}
