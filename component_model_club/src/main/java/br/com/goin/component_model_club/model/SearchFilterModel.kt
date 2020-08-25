package br.com.goin.component_model_club.model

import java.io.Serializable

class SearchFilterModel(var currentPage: Int, var totalPages: String, var totalItems: String,
                        var events: List<br.com.goin.component.model.event.Event>,
                        var clubModels: List<ClubModel>) : Serializable {

    fun hasMorePages(): Boolean {
        try {
            val totalPages = Integer.parseInt(this.totalPages)
            val currentPage = this.currentPage

            return currentPage < totalPages
        } catch (e: Exception) {
        }

        return false
    }
}
