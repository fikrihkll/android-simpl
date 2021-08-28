package com.teamdagger.simpl.ui.edit_safe

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.WantToBuyModel
import com.teamdagger.simpl.databinding.FragmentEditSafeBinding
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditSafeFragment: BottomSheetDialogFragment() {

    companion object{
        const val ID_KEY = "ID"
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

    private lateinit var binding:FragmentEditSafeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditSafeBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var idSafe = -1
    val viewModel : EditSafeViewModel by viewModels()
    private lateinit var savedModel : WantToBuyModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchArgs()
        checkEditOrAdd()
        listener()
        subscribe()
    }

    private fun displayData(model:WantToBuyModel){
        binding.etSavingNameSafe.setText(model.name)
        binding.etTargetSafe.setText(model.price.toString())
        binding.etDescSafe.setText(model.desc)
        binding.etDateSafe.setText(model.deadline)
        binding.etPictSafe.setText(model.pict)
    }

    private fun subscribe(){
        viewModel.getSafeStateEvent.observe(viewLifecycleOwner,{
            when(it){
                is DataState.Success ->{
                    savedModel = it.data
                    displayData(savedModel)
                }
                is DataState.Error ->{
                    Toast.makeText(requireContext(),"Something went wrong, please contact the developer",Toast.LENGTH_LONG).show()
                    this.dismiss()
                }
            }
        })

        viewModel.addSafeStateEvent.observe(viewLifecycleOwner,{
            when(it){
                is DataState.Success ->{
                    Toast.makeText(requireContext(),"Savings Successfully Saved",Toast.LENGTH_LONG).show()
                    this.dismiss()
                }
                is DataState.Error ->{
                    Toast.makeText(requireContext(),"Something went wrong, please contact the developer",Toast.LENGTH_LONG).show()
                    this.dismiss()
                }
            }
        })

        viewModel.editSafeStateEvent.observe(viewLifecycleOwner,{
            when(it){
                is DataState.Success ->{
                    Toast.makeText(requireContext(),"Savings Successfully Saved",Toast.LENGTH_LONG).show()
                    this.dismiss()
                }
                is DataState.Error ->{
                    Toast.makeText(requireContext(),"Something went wrong, please contact the developer",Toast.LENGTH_LONG).show()
                    this.dismiss()
                }
            }
        })
    }

    private fun listener(){
        binding.tilDateSafe.setStartIconOnClickListener {
            openDatePicker()
        }

        binding.btnSaveSafe.setOnClickListener {
            if(validate()){
                var model = WantToBuyModel(
                    0,
                    binding.etSavingNameSafe.text.toString(),
                    binding.etDescSafe.text.toString(),
                    0,
                    binding.etTargetSafe.text.toString().toLong(),
                    binding.etPictSafe.text.toString(),
                    UtilFun.timestamp(),
                    binding.etDateSafe.text.toString(),
                    UtilFun.userId,
                    false
                )

                if(idSafe != -1 && idSafe!= 0){
                    model.id = savedModel.id
                    model.progress = savedModel.progress
                    model.timestamp = savedModel.timestamp
                    viewModel.editSafe(model)
                }
                else
                    viewModel.addSafe(model)

            }
        }
    }

    private fun openDatePicker(){
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                var cal = Calendar.getInstance()
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                binding.etDateSafe.setText(UtilFun.sdfDate.format(cal.time))
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun validate():Boolean{
        if(binding.etSavingNameSafe.text.toString().isEmpty()){
            binding.etSavingNameSafe.requestFocus()
            return false
        }
        if(binding.etTargetSafe.text.toString().isEmpty()){
            binding.etTargetSafe.requestFocus()
            return false
        }
        return true
    }

    private fun fetchArgs(){
        var safeId = arguments?.getInt(ID_KEY)
        if(safeId!=null){
            idSafe =safeId
        }else{
            idSafe = 0
        }
    }

    private fun checkEditOrAdd(){
        if(idSafe == -1 || idSafe ==0)
            binding.tvTitleSafeEs.text = "Add New Savings"
        else{
            viewModel.getSafe(idSafe)
            binding.tvTitleSafeEs.text = "Edit Savings"
        }
    }
}