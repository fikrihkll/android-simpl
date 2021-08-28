package com.teamdagger.simpl.ui.getting_started.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.teamdagger.simpl.R
import com.teamdagger.simpl.data.model.UserModel
import com.teamdagger.simpl.databinding.FragmentProfileBinding
import java.io.IOException

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var listener : (()->Unit)? = null
    var listenerData : ((UserModel)->Unit)? = null

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var actLauncher : ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinueStart.setOnClickListener {
            if(!isThereEmpty()){
                listener?.invoke()
                listenerData?.invoke(packData())
            }

        }

        listenerPicker()
        binding.tilPict.setStartIconOnClickListener {
            openGallery()
        }
    }


    //Packing All Data From Edit Text
    private fun packData():UserModel{
        return UserModel(
            0,binding.etName.text.toString(),binding.etPict.text.toString(),binding.etEmail.text.toString()
        )
    }

    //Listener for Image Picker
    private fun listenerPicker(){
        actLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if(it.resultCode == Activity.RESULT_OK){
                val selectedImage: Uri? = it.data?.data

                binding.etPict.setText(selectedImage.toString())
            }
        }
    }

    //OPEN Gallery using Intent
    private fun openGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        actLauncher.launch(photoPickerIntent)
    }

    //Validate edit text
    private fun isThereEmpty():Boolean{
        if(binding.etName.text.toString().isEmpty()){
            binding.etName.requestFocus()
            return true
        }
        if(binding.etEmail.text.toString().isEmpty()){
            binding.etEmail.requestFocus()
            return true
        }
        if(binding.etPict.text.toString().isEmpty()){
            binding.etPict.requestFocus()
            return true
        }

        return false
    }

    //Interface for page information, will be called in getting started activity
    fun changePage(listener :()->Unit){
        this.listener = listener
    }

    //Interface to send Data to the Activity, will be called in getting started activity
    fun sendData(listenerData: (UserModel)->Unit){
        this.listenerData= listenerData
    }

}