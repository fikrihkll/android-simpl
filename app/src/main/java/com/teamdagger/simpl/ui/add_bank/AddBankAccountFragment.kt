package com.teamdagger.simpl.ui.add_bank

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.databinding.FragmentAddBankAccountBinding
import com.teamdagger.simpl.databinding.FragmentBankBinding
import com.teamdagger.simpl.ui.add_wallet.AddWalletFragment
import com.teamdagger.simpl.ui.getting_started.fragments.BankAdapter
import com.teamdagger.simpl.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBankAccountFragment : BottomSheetDialogFragment() {

    companion object{
        const val EXTRA_USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    val viewModel : AddBankViewModel by viewModels()

    private var _binding: FragmentAddBankAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BankAdapter

    private var userId=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentAddBankAccountBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArgs()
        prepareRc()

        binding.btnAddAccountStart.setOnClickListener {
            if(!isThereEmpty()){
                try {
                    adapter.addDataBank(
                        BankModel(
                            0,
                            binding.etAccountName.text.toString(),
                            userId,
                            binding.etBalanceStart.text.toString().toLong(),
                            binding.etRekStart.text.toString()
                        )
                    )
                }catch (e:Exception){

                }
            }
        }

        binding.btnContinue2Start.setOnClickListener {
            if(adapter.getListBank().isNotEmpty()){
               viewModel.addBank(adapter.getListBank())
            }
        }
        subscribe()
    }

    private fun subscribe(){
        viewModel.bankStateEvent.observe(this,{
            when(it){
                is DataState.Success -> {
                    if(it.data>0){
                        val snackbar = Snackbar.make(binding.root,"Success",
                            Snackbar.LENGTH_LONG)
                        snackbar.show()
                        object : CountDownTimer(2000,1000){
                            override fun onTick(millisUntilFinished: Long) {

                            }

                            override fun onFinish() {
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
        if(requireArguments().getInt(AddWalletFragment.EXTRA_USER_ID)!=null){
            userId = requireArguments().getInt(AddWalletFragment.EXTRA_USER_ID)
        }
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


}