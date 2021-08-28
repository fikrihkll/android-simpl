package com.teamdagger.simpl.ui.add_wallet

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.FragmentAddWalletBinding
import com.teamdagger.simpl.databinding.FragmentWalletBinding
import com.teamdagger.simpl.ui.getting_started.fragments.BankAdapter
import com.teamdagger.simpl.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWalletFragment : BottomSheetDialogFragment() {

    companion object{
        const val EXTRA_USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    val viewModel : AddWalletViewModel by viewModels()

    private var _binding : FragmentAddWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BankAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddWalletBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var userId=0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArgs()
        prepareRc()

        binding.btnAddWalletStart.setOnClickListener {
            if(!isThereEmpty()){
                try{
                    adapter.addDataWallet(
                        WalletModel(
                            0,
                            if(binding.etWalletName.text.toString().isEmpty()) "My Wallet" else binding.etWalletName.text.toString(),
                            userId,
                            binding.etBalanceWalletStart.text.toString().toLong()
                        )
                    )
                }catch (e:Exception){

                }
            }
        }

        binding.btnContinue3Start.setOnClickListener {
            if(adapter.getListWallet().isNotEmpty()){

                viewModel.addWallet(adapter.getListWallet())
            }
        }
        subscribe()
    }

    private fun subscribe(){
        viewModel.walletStateEvent.observe(this,{
            when(it){
                is DataState.Success -> {
                    Log.w("SYKL","${it.data}")
                    if(it.data>0){
                        val snackbar = Snackbar.make(binding.root,"Success",Snackbar.LENGTH_LONG)
                        snackbar.show()
                        object : CountDownTimer(2000,1000){
                            override fun onTick(millisUntilFinished: Long) {

                            }

                            override fun onFinish() {
                                Log.w("SYKL","IN")
                                if(binding.root!=null){
                                    dismiss()
                                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                        Intent("UPDATE_HOME")
                                    )
                                }
                            }

                        }.start()

                    }
                }
                is DataState.Loading -> {

                }
            }
        })
    }

    private fun fetchArgs(){
        if(requireArguments().getInt(EXTRA_USER_ID)!=null){
            userId = requireArguments().getInt(EXTRA_USER_ID)
        }
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


}