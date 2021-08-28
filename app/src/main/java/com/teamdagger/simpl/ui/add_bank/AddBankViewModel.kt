package com.teamdagger.simpl.ui.add_bank

import androidx.lifecycle.*
import com.teamdagger.simpl.data.model.BankModel
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
public class AddBankViewModel
@Inject
constructor(
    private val repo: BalanceRepository,
    private val savedStateHandle: SavedStateHandle
) :ViewModel(){

    private val _bankStateEvent : MutableLiveData<DataState<Long>> = MutableLiveData()

    val bankStateEvent : LiveData<DataState<Long>>
        get() = _bankStateEvent

    fun addBank(bankModel: List<BankModel>){
        viewModelScope.launch {
            repo.addListBank(bankModel).onEach {
                _bankStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }

}