package com.teamdagger.simpl.ui.getting_started.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.databinding.FragmentBankBinding

class BankFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var listener: (() -> Unit)? = null
    var listenerData: ((List<BankModel>)->Unit)? = null

    private var _binding: FragmentBankBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BankAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRc()

        binding.btnContinue2Start.setOnClickListener {
            if (adapter.getListBank().isNotEmpty()){
                listenerData?.invoke(packData())
            }
            listener?.invoke()
        }

        binding.btnAddAccountStart.setOnClickListener {
            if(!isThereEmpty()){
                adapter.addDataBank(
                    BankModel(
                        0,
                        binding.etAccountName.text.toString(),
                        -1,
                        binding.etBalanceStart.text.toString().toLong(),
                        binding.etRekStart.text.toString()
                    )
                )
            }
        }


    }

    //Pack Data from All Edit Text
    private fun packData():List<BankModel>{
        return adapter.getListBank()
    }

    //Validate edit text
    private fun isThereEmpty():Boolean{
        if(binding.etAccountName.text.toString().isEmpty()){
            binding.etAccountName.requestFocus()
            return true
        }
        if(binding.etBalanceStart.text.toString().isEmpty()){
            binding.etBalanceStart.requestFocus()
            return true
        }
        if(binding.etRekStart.text.toString().isEmpty()){
            binding.etRekStart.requestFocus()
            return true
        }

        return false
    }

    //Prepare Bank Account List
    private fun prepareRc() {
        adapter = BankAdapter(requireContext(), true)
        binding.rcBankStart.layoutManager = LinearLayoutManager(requireContext())
        binding.rcBankStart.adapter = adapter
    }

    //Interface for page information, will be called in getting started activity
    fun changePage(listener: () -> Unit) {
        this.listener = listener
    }

    //Interface to send data, will be called in getting started activity
    fun sendData(listenerData:(List<BankModel>)->Unit){
        this.listenerData = listenerData
    }
}