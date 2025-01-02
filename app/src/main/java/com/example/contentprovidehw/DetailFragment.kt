package com.example.contentprovidehw

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.contentprovidehw.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var phone: String
    lateinit var name: String
    lateinit var smsManager: SmsManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        phone = DetailFragmentArgs.Companion.fromBundle(requireArguments()).phone
        name = DetailFragmentArgs.Companion.fromBundle(requireArguments()).name

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.detailFragmentName.text = name
        binding.detailFragmentPhone.text = phone

        val message = binding.detailFragmentSmsField.text.toString()

        if (message.isEmpty()){
            myToast("Пустое сообщение нельзя отправить")
        } else {
            try {
                if (Build.VERSION.SDK_INT>=23){
                    smsManager = requireActivity().getSystemService(SmsManager::class.java)
                } else {
                    smsManager = SmsManager.getDefault()
                }
                smsManager.sendTextMessage(phone,null,message,null,null)
                myToast("Сообщение отправлено")
            } catch (e: Exception){
                myToast("Напишите что нибудь ${e.message.toString()}")
            }


        }

    }
    private fun myToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}