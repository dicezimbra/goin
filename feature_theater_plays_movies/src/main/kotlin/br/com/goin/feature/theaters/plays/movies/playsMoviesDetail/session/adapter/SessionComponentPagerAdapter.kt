package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessions
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.SessionFragment

class SessionComponentPagerAdapter(fragmentManager: FragmentManager,private val sessionsByEventByDate: List<EventSessions>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return SessionFragment.newInstance(sessionsByEventByDate[position])
    }

    override fun getCount(): Int {
        return sessionsByEventByDate.size
    }
}