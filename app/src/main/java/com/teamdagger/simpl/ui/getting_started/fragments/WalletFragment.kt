package com.teamdagger.simpl.ui.getting_started.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.FragmentWalletBinding


class WalletFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    var listener : (()->Unit)? = null
    var listenerData :((List<WalletModel>)->Unit)? = null

    private var _binding : FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BankAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRc()

        binding.btnAddWalletStart.setOnClickListener {
            if(!isThereEmpty()){
                adapter.addDataWallet(
                    WalletModel(
                        0,
                        if(binding.etWalletName.text.toString().isEmpty()) "My Wallet" else binding.etWalletName.text.toString(),
                        -1,
                        binding.etBalanceWalletStart.text.toString().toLong()
                    )
                )
            }
        }

        binding.btnContinue3Start.setOnClickListener {
            if(adapter.getListWallet().isNotEmpty()){
                listenerData?.invoke(packData())
                listener?.invoke()
            }
        }
    }

    private fun packData():List<WalletModel>{
        return adapter.getListWallet()
    }

    //Validate edit text
    private fun isThereEmpty():Boolean{
        if(binding.etBalanceWalletStart.text.toString().isEmpty()){
            binding.etBalanceWalletStart.requestFocus()
            return true
        }

        return false
    }

    //Prepare list for Wallet Adapter
    private fun prepareRc(){
        //Adapter same with Bank, because there is no difference
        adapter = BankAdapter(requireContext(),false)
        binding.rcWalletStart.layoutManager = LinearLayoutManager(requireContext())
        binding.rcWalletStart.adapter = adapter
    }

    //Interface for page information, will be called in getting started activity
    fun changePage(listener :()->Unit){
        this.listener = listener
    }

    //Interface to send data, will be called in getting started activity
    fun sendData(listenerData :(List<WalletModel>)->Unit){
        this.listenerData = listenerData
    }

}