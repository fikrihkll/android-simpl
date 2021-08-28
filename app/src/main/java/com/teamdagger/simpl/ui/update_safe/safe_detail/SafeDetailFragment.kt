package com.teamdagger.simpl.ui.update_safe.safe_detail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.ActivityModel
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.FragmentSafeDetailBinding
import com.teamdagger.simpl.ui.edit_balance.SliderCardAdapter
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SafeDetailFragment : BottomSheetDialogFragment() {

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

    companion object{
        var SAFE_ID_KEY = "SAFE_ID_KEY"
        var EMERGENCY_KEY = "EMERGENCY"
    }

    private lateinit var binding : FragmentSafeDetailBinding

    private var safeId = -1
    private var isSaving = true
    private var isBank = true

    private var isEmergency = false

    private lateinit var cardAdapter: SliderCardAdapter

    private var listBank = mutableListOf<BankModel>()
    private var listWallet = mutableListOf<WalletModel>()

    val viewModel : SafeDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSafeDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArgs()
        prepareViewPager()
        radioTypeListener()
        radioAccountListener()


        subscribe()

        if(isEmergency){
            prepareViewForEmergency()
        }
        viewModel.getListBankAndWallet()
        onClick()
    }

    private fun prepareViewForEmergency(){
        binding.textView18.text = "Use your emergency balance correctly"
        binding.textView9.text = "Emergency Safe"
    }

    private fun onClick(){
        binding.btnSaveNowUs.setOnClickListener {

            if(validate()){
                if(binding.rdBankUs.isChecked){

                    var bank = cardAdapter.getBank(binding.vpAccountUs.currentItem)
                    var balance = binding.etBalanceUs.text.toString().toLong()
                    var type = UtilFun.TYPE_IN
                    if(!isSaving){
                        type = UtilFun.TYPE_OUT
                        balance *= -1
                    }

                    var model = ActivityModel(0,type,null,balance,UtilFun.SOURCE_BANK,bank.id,UtilFun.timestamp(),UtilFun.userId)
                    viewModel.updateSafeWithBank(balance,safeId,bank.id,model,!binding.cbMoneyInsideUs.isChecked,isEmergency)
                    this.dismiss()
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("UPDATE_HOME"))
                }else{
                    var wallet = cardAdapter.getWallet(binding.vpAccountUs.currentItem)
                    var balance = binding.etBalanceUs.text.toString().toLong()
                    var type = UtilFun.TYPE_IN
                    if(!isSaving){
                        type = UtilFun.TYPE_OUT
                        balance *= -1
                    }

                    var model = ActivityModel(0,type,null,balance,UtilFun.SOURCE_WALLET,wallet.id,UtilFun.timestamp(),UtilFun.userId)
                    viewModel.updateSafeWithWallet(balance,safeId,wallet.id,model,!binding.cbMoneyInsideUs.isChecked,isEmergency)
                    this.dismiss()
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("UPDATE_HOME"))
                }
            }




        }
    }

    private fun validate():Boolean{
        if(binding.etBalanceUs.text.isEmpty()){
            binding.etBalanceUs.requestFocus()
            return false
        }

        return true
    }

    private fun radioAccountListener(){
        binding.rdgSaveUs.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rd_bank_us ->{
                    isBank = true
                    cardAdapter.changeType(true)
                }
                R.id.rd_wallet_us ->{
                    isBank = false
                    cardAdapter.changeType(false)
                }
            }
        }
    }

    private fun subscribe(){
        viewModel.bankStateEvent.observe(viewLifecycleOwner,{
            when(it){
                is DataState.Success ->{
                    cardAdapter.setNewDataBank(it.data.toMutableList())
                }
            }
        })

        viewModel.walletStateEvent.observe(viewLifecycleOwner,{
            when(it){
                is DataState.Success ->{
                    cardAdapter.setNewDataWallet(it.data.toMutableList())
                }
            }
        })
    }

    private fun prepareViewPager(){
        cardAdapter = SliderCardAdapter(requireContext())
        binding.vpAccountUs.adapter = cardAdapter

        binding.vpAccountUs.clipToPadding = false
        binding.vpAccountUs.clipChildren = false
        binding.vpAccountUs.offscreenPageLimit=3
        binding.vpAccountUs.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER

        var transformer = CompositePageTransformer()
        transformer.addTransformer { page, position ->
            var a = 1 - Math.abs(position)
            page.scaleY = 0.85f + a*0.15f
        }
        binding.vpAccountUs.setPageTransformer(transformer)

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

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        }
        binding.vpAccountUs.registerOnPageChangeCallback(vpChange)
    }

    private fun fetchArgs(){
        if(arguments?.getInt(SAFE_ID_KEY)!=null){
            safeId = requireArguments().getInt(SAFE_ID_KEY)
        }
        if(arguments?.getBoolean(EMERGENCY_KEY)!=null){
            isEmergency = requireArguments().getBoolean(EMERGENCY_KEY,false)
        }
    }

    private fun radioTypeListener(){
        binding.rdgTypeSd.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.rdSaveSd.id ->{
                    isSaving = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.rdSaveSd.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.teal))
                        binding.rdOutSd.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray))
                    }
                }
                binding.rdOutSd.id ->{
                    isSaving = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.rdSaveSd.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.gray))
                        binding.rdOutSd.buttonTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))
                    }
                }
            }
        }
    }


}