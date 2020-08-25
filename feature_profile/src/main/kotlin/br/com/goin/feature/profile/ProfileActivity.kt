package br.com.goin.feature.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity(){

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"

        fun starter(context: Context, profileId: String?){
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(PROFILE_ID, profileId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        configureProfileFragment()
    }

    private fun configureProfileFragment() {
        val fragment = ProfileFragment.newInstance(intent.getStringExtra(PROFILE_ID))

        val supportFragmentManager = supportFragmentManager
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                        fragment,
                        ProfileFragment::class.java.simpleName)
                .commitAllowingStateLoss()

        supportFragmentManager.executePendingTransactions()
    }
}