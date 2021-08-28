package com.teamdagger.simpl.ui.edit_balance

import android.util.Log
import androidx.lifecycle.*
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.DescModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.data.repo.BalanceRepository
import com.teamdagger.simpl.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
public class EditBalanceViewModel
@Inject
constructor(
    private val balanceRepo:BalanceRepository,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {

    private val _bankStateEvent : MutableLiveData<DataState<List<BankModel>>> = MutableLiveData()
    private val _walletStateEvent : MutableLiveData<DataState<List<WalletModel>>> = MutableLiveData()
    private val _bankUpdateStateEvent : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    private val _walletUpdateStateEvent : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    private val _descStateEvent : MutableLiveData<DataState<List<DescModel>>> = MutableLiveData()

    val bankStateEvent : LiveData<DataState<List<BankModel>>>
    get() = _bankStateEvent
    val walletStateEvent : LiveData<DataState<List<WalletModel>>>
        get() = _walletStateEvent
    val bankUpdateStateEvent : LiveData<DataState<Boolean>>
        get() = _bankUpdateStateEvent
    val walletUpdateStateEvent : LiveData<DataState<Boolean>>
        get() = _walletUpdateStateEvent
    val descStateEvent : LiveData<DataState<List<DescModel>>>
        get() = _descStateEvent

    fun getBankStateEvent(stateEvent: EditBalanceStateEvent,userId:Int){
        when(stateEvent){
            is EditBalanceStateEvent.GetBank ->{
                viewModelScope.launch {
                    balanceRepo.getBank(userId).onEach {
                        _bankStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun getWalletStateEvent(stateEvent: EditBalanceStateEvent,userId:Int){
        when(stateEvent){
            is EditBalanceStateEvent.GetWallet ->{
                viewModelScope.launch {
                    balanceRepo.getWallet(userId).onEach {
                        _walletStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setUpdateBankStateEvent(stateEvent: EditBalanceStateEvent,
                                bank: BankModel,
                                desc: String?,
                                type: String,
                                balanceAdded: Long,
                                userId: Int){
        when(stateEvent){
            is EditBalanceStateEvent.UpdateBank ->{
                viewModelScope.launch {
                    Log.w("SYKL","IN-VM")
                    balanceRepo.updateBank(bank,desc,type,balanceAdded,userId).onEach {
                        _bankUpdateStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setUpdateWalletStateEvent(stateEvent: EditBalanceStateEvent,
                                  wallet: WalletModel,
                                  desc: String?,
                                  type: String,
                                  balanceAdded: Long,
                                  userId: Int){
        when(stateEvent){
            is EditBalanceStateEvent.UpdateWallet ->{
                viewModelScope.launch {
                    balanceRepo.updateWallet(wallet,desc,type,balanceAdded,userId).onEach {
                        _walletUpdateStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun getDescStateEvent(stateEvent: EditBalanceStateEvent){
        when(stateEvent){
            is EditBalanceStateEvent.GetDesc ->{
                viewModelScope.launch {
                    balanceRepo.getDesc().onEach {
                        _descStateEvent.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }



}
sealed class EditBalanceStateEvent{
    object GetBank:EditBalanceStateEvent()
    object UpdateBank:EditBalanceStateEvent()
    object GetWallet:EditBalanceStateEvent()
    object UpdateWallet:EditBalanceStateEvent()

    object GetDesc:EditBalanceStateEvent()
}