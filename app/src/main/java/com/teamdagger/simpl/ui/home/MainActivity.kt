package com.teamdagger.simpl.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.*
import com.teamdagger.simpl.databinding.ActivityMainBinding
import com.teamdagger.simpl.databinding.DialogMenuBalanceBinding
import com.teamdagger.simpl.ui.add_bank.AddBankAccountFragment
import com.teamdagger.simpl.ui.add_wallet.AddWalletFragment
import com.teamdagger.simpl.ui.edit_balance.EditBalanceFragment
import com.teamdagger.simpl.ui.edit_safe.EditSafeFragment
import com.teamdagger.simpl.ui.update_safe.safe_detail.SafeDetailFragment
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    val viewModel : MainViewModel by viewModels()

    private lateinit var user : UserModel
    private lateinit var bank : SumModel
    private lateinit var wallet : SumModel
    private lateinit var emergency : WantToBuyModel

    private lateinit var adapterSafe : SafeHomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        subscribe()
        prepareSafeRecycler()
        viewModel.setUserStateEvent(MainStateEvent.GetUser,1)
        viewModel.getListSafe(MainStateEvent.GetListSafe)
        listener()

        LocalBroadcastManager.getInstance(this).registerReceiver(bcReceiver, IntentFilter("UPDATE_HOME"))
    }


    private fun showMenuAddBalance(){
        val dl = BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
        val bindingDl = DialogMenuBalanceBinding.inflate(LayoutInflater.from(this))
        dl.setContentView(bindingDl.root)

        bindingDl.tvAddBankDl.setOnClickListener {
            var frag = AddBankAccountFragment()
            var bundle = Bundle()
            bundle.putInt(AddBankAccountFragment.EXTRA_USER_ID,user.id)
            frag.arguments = bundle

            frag.show(supportFragmentManager,"ADD_BANK")
        }
        bindingDl.ivAddBankDl.setOnClickListener {
            var frag = AddBankAccountFragment()
            var bundle = Bundle()
            bundle.putInt(AddBankAccountFragment.EXTRA_USER_ID,user.id)
            frag.arguments = bundle

            frag.show(supportFragmentManager,"ADD_BANK")
        }
        bindingDl.tvAddWalletDl.setOnClickListener {
            var frag = AddWalletFragment()
            var bundle = Bundle()
            bundle.putInt(AddBankAccountFragment.EXTRA_USER_ID,user.id)
            frag.arguments = bundle

            frag.show(supportFragmentManager,"ADD_WALLET")
        }
        bindingDl.ivAddWalletDl.setOnClickListener {
            var frag = AddWalletFragment()
            var bundle = Bundle()
            bundle.putInt(AddBankAccountFragment.EXTRA_USER_ID,user.id)
            frag.arguments = bundle

            frag.show(supportFragmentManager,"ADD_WALLET")
        }

        dl.show()
    }

    private fun listener() {
        binding.btnAddSafeHome.setOnClickListener {
            var frag = EditSafeFragment()
            frag.show(supportFragmentManager,"ADD_SAFE")
        }

        binding.btnAddNewHome.setOnClickListener {
            showMenuAddBalance()
        }

        binding.btnSaveHome.setOnClickListener {
            val editBalance =  EditBalanceFragment()
            var bundle = Bundle()
            bundle.putInt(EditBalanceFragment.EXTRA_USER_ID,user.id)
            editBalance.arguments = bundle

            editBalance.show(supportFragmentManager,"EDIT_BALANCE")
        }
        binding.btnOutHome.setOnClickListener {
            val editBalance =  EditBalanceFragment()
            var bundle = Bundle()
            bundle.putInt(EditBalanceFragment.EXTRA_USER_ID,user.id)
            editBalance.arguments = bundle

            editBalance.show(supportFragmentManager,"EDIT_BALANCE")
        }
        adapterSafe.onAddSaveClicked {
            val frag = SafeDetailFragment()
            val bd = Bundle()
            bd.putInt(SafeDetailFragment.SAFE_ID_KEY,it.id)
            frag.arguments = bd
            frag.show(supportFragmentManager,"SAVE_MONEY")
        }

        adapterSafe.onRootClicked {
            val frag = EditSafeFragment()
            var bd = Bundle()
            bd.putInt(EditSafeFragment.ID_KEY,it.id)
            frag.arguments = bd
            frag.show(supportFragmentManager,"EDIT_SAFE")
        }
    }

    private fun displayUser(){
        binding.tvNameHome.text = user.name
        binding.tvUsableBalanceHome.text = "Rp.-"
        Log.w("SYKL","IN-HOME")
        viewModel.setBankStateEvent(MainStateEvent.GetBankStateEvent,user.id)
        viewModel.setWalletStateEvent(MainStateEvent.GetWalletStateEvent,user.id)
        viewModel.setEmergencyStateEvent(MainStateEvent.GetEmergencyStateEvent,user.id)
    }

    private fun prepareSafeRecycler(){
        adapterSafe = SafeHomeAdapter(this)
        binding.rcWantToBuyHome.layoutManager = LinearLayoutManager(this)
        binding.rcWantToBuyHome.adapter = adapterSafe
    }

    private fun subscribe(){
        viewModel.userStateEvent.observe(this,  {
            when(it){
                is DataState.CacheSuccess ->{
                    user = it.data
                    UtilFun.userId = user.id
                    displayUser()
                    Log.w("SYKL","IN-HOME")
                }
            }
        })

        viewModel.bankStateEvent.observe(this,{
            when(it){
                is DataState.CacheSuccess ->{
                    bank=it.data
                    binding.tvBankBalanceHome.text = "Rp."+UtilFun.parseBalance(bank.balance)
                }
            }
        })

        viewModel.walletStateEvent.observe(this,{
            when(it){
                is DataState.CacheSuccess ->{
                    wallet = it.data
                    binding.tvWalletBalanceHome.text = "Rp."+UtilFun.parseBalance(wallet.balance)
                }
            }
        })

        viewModel.emergencyStateEvent.observe(this,{
            when(it){
                is DataState.CacheSuccess ->{
                    emergency=it.data
                    Log.w("SYKL","EMERG-${it.data}")
                    binding.tvBalanceEmergency.text = "Rp."+UtilFun.parseBalance(emergency.progress)
                }
            }
        })

        viewModel.safeStateEvent.observe(this,{
            when(it){
                is DataState.Success ->{
                    adapterSafe.setNewData(it.data.toMutableList())
                }
            }
        })
    }

    private val bcReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            displayUser()
        }

    }
}