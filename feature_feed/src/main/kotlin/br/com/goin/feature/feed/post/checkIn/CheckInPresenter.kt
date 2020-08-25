package br.com.goin.feature.feed.post.checkIn

interface CheckInPresenter {
    fun onCreate()
    fun loadEventsToCheckIn(myLocation: FloatArray)
    fun onReceiveLocation(location: FloatArray?)
    fun logOnAnalytics(action: String, label: String?)

}
