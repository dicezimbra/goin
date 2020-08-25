package br.com.goin.feature.search.category.filter.component

import android.content.Context
import android.view.LayoutInflater
import br.com.goin.feature.search.category.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipComponent(context: Context) : ChipGroup(context) {

        fun get(): Chip{

            return LayoutInflater.from(context).inflate(R.layout.chip_tag, this, false) as Chip
    }
}