package com.teamdagger.simpl.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdagger.simpl.data.model.*
import com.teamdagger.simpl.data.repo.BalanceRepository
import com.teamdagger.simpl.data.repo.UserRepository
import com.teamdagger.simpl.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
public class MainViewModel
@Inject
constructor(
    private val repoBalance:BalanceRepository,
    private val repoUser: UserRepository
):ViewModel()
{

    private val _bankStateEvent : MutableLiveData<DataState<SumModel>> = MutableLiveData()
    private val _walletStateEvent : MutableLiveData<DataState<SumModel>> = MutableLiveData()
    private val _emergencyStateEvent : MutableLiveData<DataState<WantToBuyModel>> = MutableLiveData()
    private val _userStateEvent : MutableLiveData<DataState<UserModel>> = MutableLiveData()
    private val _safeStateEvent : MutableLiveData<DataState<List<WantToBuyModel>>> = MutableLiveData()

    val bankStateEvent : LiveData<DataState<SumModel>>
    get() = _bankStateEvent

    val walletStateEvent : LiveData<DataState<SumModel>>
    get() = _walletStateEvent

    val emergencyStateEvent : LiveData<DataState<WantToBuyModel>>
    get() = _emergencyStateEvent

    val userStateEvent : LiveData<DataState<UserModel>>
    get() = _userStateEvent

    val safeStateEvent : LiveData<DataState<List<WantToBuyModel>>>
    get() = _safeStateEvent

    fun setBankStateEvent(stateEvent: MainStateEvent,userId:Int){
        when(stateEvent){
            is MainStateEvent.GetBankStateEvent ->{
                viewModelScope.launch {
                    repoBalance.getTotalBankBalance(userId).onEach {
                        _bankStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setWalletStateEvent(stateEvent: MainStateEvent,userId: Int){
        when(stateEvent){
            is MainStateEvent.GetWalletStateEvent ->{
                Log.w("SYKL","IN")
                viewModelScope.launch {
                    repoBalance.getWalletBalance(userId).onEach {
                        _walletStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setEmergencyStateEvent(stateEvent: MainStateEvent,userId: Int){
        when(stateEvent){
            is MainStateEvent.GetEmergencyStateEvent ->{
                viewModelScope.launch {
                    repoBalance.getEmergencyBalance(userId).onEach {
                        _emergencyStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setUserStateEvent(stateEvent: MainStateEvent,userId: Int){
        when(stateEvent){
            is MainStateEvent.GetUser ->{

                viewModelScope.launch {
                    repoUser.getUserData(userId).onEach {
                        _userStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun getListSafe(stateEvent: MainStateEvent){
        viewModelScope.launch {
            when(stateEvent){
                MainStateEvent.GetListSafe ->{
                    viewModelScope.launch {
                        repoBalance.getListSafe().onEach {
                            _safeStateEvent.value = it
                        }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }

}

sealed class MainStateEvent{
    object GetUser:MainStateEvent()
    object GetBankStateEvent:MainStateEvent()
    object GetWalletStateEvent:MainStateEvent()
    object GetEmergencyStateEvent:MainStateEvent()
    object GetListSafe:MainStateEvent()
}