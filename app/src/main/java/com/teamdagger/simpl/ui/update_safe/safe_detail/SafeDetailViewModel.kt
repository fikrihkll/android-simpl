package com.teamdagger.simpl.ui.update_safe.safe_detail

import android.util.Log
import androidx.lifecycle.*
import com.teamdagger.simpl.data.model.ActivityModel
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.data.repo.BalanceRepository
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class SafeDetailViewModel
@Inject
constructor(
    private val repo:BalanceRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _bankStateEvent : MutableLiveData<DataState<List<BankModel>>> = MutableLiveData()
    private val _walletStateEvent : MutableLiveData<DataState<List<WalletModel>>> = MutableLiveData()

    val bankStateEvent : LiveData<DataState<List<BankModel>>>
        get() = _bankStateEvent
    val walletStateEvent : LiveData<DataState<List<WalletModel>>>
        get() = _walletStateEvent

    fun getListBankAndWallet(){
        viewModelScope.launch {
            repo.getWallet(UtilFun.userId).onEach {
                _walletStateEvent.value = it
            }.launchIn(viewModelScope)
        }

        viewModelScope.launch {
            repo.getBank(UtilFun.userId).onEach {
                _bankStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun updateSafeWithBank(balance:Long,safeId:Int,bankId:Int,activityModel: ActivityModel,updateAccountBalance:Boolean,isEmergency:Boolean){
        viewModelScope.launch {
            repo.updateSafeWithBank(balance, safeId, bankId, activityModel, updateAccountBalance,isEmergency)
                .launchIn(viewModelScope)
        }
    }

    fun updateSafeWithWallet(balance:Long,safeId:Int,walletId:Int,activityModel: ActivityModel,updateAccountBalance:Boolean,isEmergency:Boolean){
        viewModelScope.launch {
            repo.updateSafeWithWallet(balance, safeId, walletId, activityModel, updateAccountBalance,isEmergency)
                .launchIn(viewModelScope)
        }
    }
}