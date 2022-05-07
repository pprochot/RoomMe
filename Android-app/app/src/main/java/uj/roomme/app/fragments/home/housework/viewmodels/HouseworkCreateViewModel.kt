package uj.roomme.app.fragments.home.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.fragments.home.housework.models.HouseworkCreateModel
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.flat.FlatUsersGetReturnModel
import uj.roomme.domain.housework.HouseworkPostModel
import uj.roomme.domain.housework.HouseworkPostReturnModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.HouseworkService

class HouseworkCreateViewModel(
    session: SessionViewModel,
    private val houseworkService: HouseworkService,
    private val flatService: FlatService,
    private val flatId: Int
) : ServiceViewModel(session) {

    private companion object {
        val TAG = "HouseworkCreateViewModel"
    }

    val createdHouseworkEvent = MutableLiveData<Event<HouseworkPostReturnModel>>()
    val apartmentLocators = MutableLiveData<FlatUsersGetReturnModel>()

    fun fetchApartmentLocatorsFromService() {
        val logTag = "$TAG.fetchApartmentLocatorsFromService()"
        flatService.getFlatUsers(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> apartmentLocators.value = body!!
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun createHouseworkViaService(model: HouseworkCreateModel) {
        val logTag = "$TAG.createHouseworkViaService()"
        houseworkService.postHousework(accessToken, model.toServiceModel())
            .processAsync { code, body, error ->
                when (code) {
                    200 -> createdHouseworkEvent.value = Event(body!!)
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    private fun HouseworkCreateModel.toServiceModel() = HouseworkPostModel(
        this.name, flatId, this.description,
        this.selectedUsersIds, this.frequencyId, this.days
    )

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val houseworkService: HouseworkService,
        private val flatService: FlatService,
        private val flatId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkCreateViewModel::class.java))
                return HouseworkCreateViewModel(session, houseworkService, flatService, flatId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}