package uj.roomme.app.fragments.home.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.flat.FlatShortModel
import uj.roomme.domain.housework.HouseworkFrequencyModel
import uj.roomme.domain.housework.HouseworkModel
import uj.roomme.domain.housework.HouseworkSettingsModel
import uj.roomme.domain.schedule.ScheduleShortModel
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.services.service.HouseworkService
import java.time.OffsetDateTime

class HouseworkDetailsViewModel(
    session: SessionViewModel,
    private val houseworkService: HouseworkService,
    private val houseworkId: Int
) : ServiceViewModel(session) {

    private companion object {
        private const val TAG = "HouseworkDetailsViewModel"
    }

    val houseworkDetails = MutableLiveData<HouseworkModel>()
    val deletedHouseworkEvent = MutableLiveData<Event<String>>()

    fun fetchHouseworkDetailsFromService() {
        houseworkService.getHouseworkFull(accessToken, houseworkId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> houseworkDetails.value = body
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
//        houseworkDetails.value = HouseworkModel(
//            1,
//            "FakeHousework",
//            FlatShortModel(1, "MyApartment", "myApartment2"),
//            UserNicknameModel(1, "pprochot"),
//            "FakeDescr",
//            listOf(
//                UserNicknameModel(1, "pprochot"),
//                UserNicknameModel(2, "fakeUser2")
//            ),
//            ScheduleShortModel(1, UserNicknameModel(1, "pprochot"), OffsetDateTime.now(), 1),
//            HouseworkSettingsModel(1, HouseworkFrequencyModel(1, "Once", 1), listOf(1, 2, 3))
//        )
    }

    fun deleteHouseworkViaService() {
        houseworkService.removeHousework(accessToken, houseworkId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> deletedHouseworkEvent.value = Event("Deleted housework")
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val houseworkService: HouseworkService,
        private val houseworkId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkDetailsViewModel::class.java))
                return HouseworkDetailsViewModel(session, houseworkService, houseworkId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}