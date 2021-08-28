package com.teamdagger.simpl.ui.edit_safe

import androidx.lifecycle.*
import com.teamdagger.simpl.data.model.WantToBuyModel
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
public class EditSafeViewModel
@Inject
constructor(
    private val repo:BalanceRepository,
    private val savedStateHandle: SavedStateHandle
):ViewModel(){

    private val _addSafeStateEvent : MutableLiveData<DataState<Long>> = MutableLiveData()
    private val _editSafeStateEvent : MutableLiveData<DataState<Int>> = MutableLiveData()
    private val _getSafeStateEvent : MutableLiveData<DataState<WantToBuyModel>> = MutableLiveData()

    val addSafeStateEvent : LiveData<DataState<Long>>
    get() = _addSafeStateEvent

    val editSafeStateEvent : LiveData<DataState<Int>>
    get() = _editSafeStateEvent

    val getSafeStateEvent : LiveData<DataState<WantToBuyModel>>
    get() = _getSafeStateEvent

    fun getSafe(id:Int){
        viewModelScope.launch {
            repo.getSafe(id).onEach {
                _getSafeStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun addSafe(safeEntity : WantToBuyModel){
        viewModelScope.launch {
            repo.addSafe(safeEntity).onEach {
                _addSafeStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun editSafe(safeEntity : WantToBuyModel){
        viewModelScope.launch {
            repo.updateSafe(safeEntity).onEach {
                _editSafeStateEvent.value = it
            }.launchIn(viewModelScope)
        }
    }
}