package com.teamdagger.simpl.ui.edit_balance

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.DescModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.FragmentEditBalanceBinding
import com.teamdagger.simpl.databinding.FragmentSafeDetailBinding
import com.teamdagger.simpl.ui.edit_safe.EditSafeFragment
import com.teamdagger.simpl.ui.update_safe.safe_detail.SafeDetailFragment
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class EditBalanceFragment : BottomSheetDialogFragment() {

    companion object{
        const val EXTRA_USER_ID = "USER_ID"
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog!!.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = resources.displayMetrics.heightPixels //replace to whatever you want
        view?.requestLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    private lateinit var binding : FragmentEditBalanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBalanceBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var isSaving = true

    val viewModel : EditBalanceViewModel by viewModels()

    private lateinit var adapterSpinner : ArrayAdapter<String>
    private lateinit var cardAdapter : SliderCardAdapter

    private var listBank = mutableListOf<BankModel>()
    private var listWallet = mutableListOf<WalletModel>()

    private lateinit var adapterSuggestion: SuggestionAdapter

    private var userId=0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArgs()
        radioListener()
        prepareSpinner()
        prepareViewPager()
        prepareRcSuggestion()

        subscribe()
        viewModel.getBankStateEvent(EditBalanceStateEvent.GetBank,1)
        viewModel.getWalletStateEvent(EditBalanceStateEvent.GetWallet,1)
        viewModel.getDescStateEvent(EditBalanceStateEvent.GetDesc)

        onClick()
    }

    private fun fetchArgs(){
        if(requireArguments().getInt(EXTRA_USER_ID)!=null)
            userId = requireArguments().getInt(EXTRA_USER_ID)
    }

    private fun prepareViewPager(){
        cardAdapter = SliderCardAdapter(requireContext())
        binding.vpAccount.adapter = cardAdapter

        binding.vpAccount.clipToPadding = false
        binding.vpAccount.clipChildren = false
        binding.vpAccount.offscreenPageLimit=3
        binding.vpAccount.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER

        var transformer = CompositePageTransformer()
        transformer.addTransformer { page, position ->
            var a = 1 - Math.abs(position)
            page.scaleY = 0.85f + a*0.15f
        }
        binding.vpAccount.setPageTransformer(transformer)

        val vpChange = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.w("SYKL",position.toString())
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        }
        binding.vpAccount.registerOnPageChangeCallback(vpChange)
    }

    private fun prepareRcSuggestion(){
        adapterSuggestion = SuggestionAdapter(requireContext())
        binding.rcSuggestion.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.rcSuggestion.adapter = adapterSuggestion
    }

    private fun setListCard(){
        when(binding.spWhereEdit.selectedItemPosition){
            0 ->{
                cardAdapter.setNewDataBank(listBank)
                cardAdapter.changeType(true)
            }
            1->{
                cardAdapter.setNewDataWallet(listWallet)
                cardAdapter.changeType(false)
            }
        }
    }

    private fun isBalanceValidated():Boolean{
        if(binding.etBalanceEditPage.text.toString().isEmpty() || binding.etBalanceEditPage.text.toString().toLong() <=0){
            binding.etBalanceEditPage.requestFocus()
            return false
        }

        if(binding.spWhereEdit.selectedItemPosition == 0 && cardAdapter.getBankSize()==0){
            return false
        }

        if(binding.spWhereEdit.selectedItemPosition == 1 && cardAdapter.getWalletSize()==0){
            return false
        }

        return true
    }

    private fun subscribe(){
        viewModel.bankStateEvent.observe(this,{
            when(it){
                is DataState.Success ->{
                    listBank.clear()
                    listBank.addAll(it.data)
                    setListCard()
                }
                is DataState.Loading ->{

                }
                is DataState.Error ->{

                }
            }
        })

        viewModel.walletStateEvent.observe(this,{
            when(it){
                is DataState.Success ->{
                    listWallet.clear()
                    listWallet.addAll(it.data)
                    setListCard()
                }
                is DataState.Loading ->{

                }
                is DataState.Error ->{

                }
            }
        })

        viewModel.bankUpdateStateEvent.observe(this, {
            when(it){
                is DataState.Success ->{
                    viewModel.getBankStateEvent(EditBalanceStateEvent.GetBank,1)
                    Toast.makeText(requireContext(),"Successfully Saved",Toast.LENGTH_LONG).show()
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("UPDATE_HOME"))
                }
            }
        })

        viewModel.walletUpdateStateEvent.observe(this, {
            when(it){
                is DataState.Success ->{
                    viewModel.getWalletStateEvent(EditBalanceStateEvent.GetWallet,1)
                    Toast.makeText(requireContext(),"Successfully Saved",Toast.LENGTH_LONG).show()
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("UPDATE_HOME"))
                }
            }
        })

        viewModel.descStateEvent.observe(this, {
            when(it){
                is DataState.Success ->{
                    var listStr = mutableListOf<DescModel>()
                    for(i in it.data)
                        listStr.add(DescModel(i.descName))
                    adapterSuggestion.setData(listStr)
                }
                is DataState.Loading ->{

                }
                is DataState.Error ->{

                }
            }
        })

    }

    private fun prepareDescBeforeSend(desc:String):String{
        var raw = desc.lowercase()
        var firstChar = raw[0].uppercase()

        var finalValue = firstChar
        for(i in 1 until raw.length)
            finalValue+=raw[i]


        return finalValue
    }

    private fun onClick(){
        binding.btnSaveEmergencyEditPage.setOnClickListener{
            val frag = SafeDetailFragment()
            val bd = Bundle()
            bd.putBoolean(SafeDetailFragment.EMERGENCY_KEY,true)
            frag.arguments = bd
            frag.show(parentFragmentManager, "EMERGENCY_SAFE")
        }

        adapterSuggestion.onClick {
            binding.etDescEditPage.setText(it)
        }

        binding.btnDoneEditPage.setOnClickListener {
            if(isBalanceValidated()){
                //This will check, does the user want to save or use the money
                if(isSaving){
                    //This will check, where the user want to save, bank or his wallet
                    when(binding.spWhereEdit.selectedItemPosition){
                        0 ->{
                            var rawBank = cardAdapter.getBank(binding.vpAccount.currentItem)
                            var balance = binding.etBalanceEditPage.text.toString().toLong()
                            rawBank.balance += balance
                            var desc = if(binding.etDescEditPage.text.toString().isEmpty()) null else prepareDescBeforeSend(binding.etDescEditPage.text.toString())

                            viewModel.setUpdateBankStateEvent(EditBalanceStateEvent.UpdateBank,rawBank,desc,UtilFun.TYPE_IN,balance,userId)
                        }
                        1 ->{
                            var rawWallet = cardAdapter.getWallet(binding.vpAccount.currentItem)
                            var balance = binding.etBalanceEditPage.text.toString().toLong()
                            rawWallet.balance += balance
                            var desc = if(binding.etDescEditPage.text.toString().isEmpty()) null else prepareDescBeforeSend(binding.etDescEditPage.text.toString())

                            viewModel.setUpdateWalletStateEvent(EditBalanceStateEvent.UpdateWallet,rawWallet,desc,UtilFun.TYPE_IN,balance,userId)
                        }
                    }
                }else{
                    //This will check, where the user want to save, bank or his wallet
                    when(binding.spWhereEdit.selectedItemPosition){
                        0 ->{
                            var rawBank = cardAdapter.getBank(binding.vpAccount.currentItem)
                            var balance = binding.etBalanceEditPage.text.toString().toLong()
                            rawBank.balance -= balance
                            var desc = if(binding.etDescEditPage.text.toString().isEmpty()) null else prepareDescBeforeSend(binding.etDescEditPage.text.toString())

                            viewModel.setUpdateBankStateEvent(EditBalanceStateEvent.UpdateBank,rawBank,desc,UtilFun.TYPE_OUT,-balance,userId)
                        }
                        1 ->{
                            var rawWallet = cardAdapter.getWallet(binding.vpAccount.currentItem)
                            var balance = binding.etBalanceEditPage.text.toString().toLong()
                            rawWallet.balance -= balance
                            var desc = if(binding.etDescEditPage.text.toString().isEmpty()) null else prepareDescBeforeSend(binding.etDescEditPage.text.toString())

                            viewModel.setUpdateWalletStateEvent(EditBalanceStateEvent.UpdateWallet,rawWallet,desc,UtilFun.TYPE_OUT,-balance,userId)
                        }
                    }
                }
            }
        }
    }

    private fun prepareSpinner(){
        adapterSpinner = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("Bank/E-Wallet","Wallet"))
        binding.spWhereEdit.adapter = adapterSpinner

        binding.spWhereEdit.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setListCard()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun radioListener(){
        binding.rdgEditType.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.rdSave.id ->{
                    isSaving = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.rdSave.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.teal))
                        binding.rdOut.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray))
                    }
                }
                binding.rdOut.id ->{
                    isSaving = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.rdSave.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray))
                        binding.rdOut.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))
                    }
                }
            }
        }
    }
}