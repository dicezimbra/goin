package br.com.goin.feature.place

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.feature.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_voucher.view.*

class VouchersAdapter(var vouchers: List<ApiVoucher>) : androidx.recyclerview.widget.RecyclerView.Adapter<VouchersAdapter.ViewHolder>() {

    var onClickVoucherListener: ((voucher: ApiVoucher) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.item_voucher, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = vouchers[position]
        val context = holder.itemView.context

        Glide.with(holder.itemView.context)
                .load(voucher.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageViewVoucher)

        val title = "${voucher.title} ${voucher.subtitle}"
        val subtitle = voucher.subtitle ?: ""
        val titleSpannable = SpannableString(title)

        val redColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context, R.color.gray_rating)))
        val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, redColor, null)
        titleSpannable.setSpan(highlightSpan, title.indexOf(subtitle), title.indexOf(subtitle) + subtitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.itemView.textViewLabelVoucher.text = voucher.detailTitle
        holder.textViewTitle.text = titleSpannable
        holder.itemView.textViewLocation.text = voucher.placeName
        holder.itemVoucherContainer.setOnClickListener {onClickVoucherListener?.invoke(voucher)}
    }

    override fun getItemCount(): Int {
        return vouchers.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val textViewTitle = itemView.textViewTitle
        val imageViewVoucher = itemView.imageViewVoucher
        val itemVoucherContainer = itemView.itemVoucherContainer
    }
}