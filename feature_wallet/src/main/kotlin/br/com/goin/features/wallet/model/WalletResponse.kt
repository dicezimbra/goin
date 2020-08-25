package br.com.goin.features.wallet.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WalletResponse(@SerializedName("wallet") val wallet: List<Ticket>): Serializable