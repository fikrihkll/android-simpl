package com.teamdagger.simpl.ui.add_wallet

import androidx.lifecycle.*
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.SumModel
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
public class AddWalletViewModel
@Inject
constructor(
    private val repo: BalanceRepository,
    private val savedStateHandle: SavedStateHandle
) :ViewModel(){

    private val _walletStateEvent : MutableLiveData<DataState<Long>> = MutableLiveData()

    val walletStateEvent : LiveData<DataState<Long>>
        get() = _walletStateEvent

    fun addWallet(walletModel: List<WalletModel>){
        viewModelScope.launch {
            repo.addListWallet(walletModel).onEach {
                _walletStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }

}