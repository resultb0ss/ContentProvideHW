package com.example.contentprovidehw


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentprovidehw.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binging: FragmentMainBinding? = null
    private val binding get() = _binging!!

    private val viewModel = MainViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binging = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncherSingle.launch(Manifest.permission.READ_CONTACTS)
        initContacts()

    }

    private fun initContacts() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.contacts.observe(requireActivity(), Observer {
                binding.mainRecyclerView.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                binding.mainRecyclerView.adapter = CustomAdapter(it, { contact ->
                    callToContact(contact.phone)
                }) { contact ->
                    sendSmsToContact(contact.phone)
                }
            })
            viewModel.loadContacts()

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            myToast("Оно необходимо для звонков")
        } else {
            permissionLauncherSingle.launch(Manifest.permission.READ_CONTACTS)
        }

    }


    private fun callToContact(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(phoneIntent)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            myToast("Оно необходимо для звонков")
        } else {
            permissionLauncherSingle.launch(Manifest.permission.CALL_PHONE)
        }

    }

    private fun sendSmsToContact(phoneNumber: String) {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val intentSms = Intent(Intent.ACTION_SENDTO)
            intentSms.data = Uri.parse("smsto:$phoneNumber")
            startActivity(intentSms)

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
            myToast("Оно необходимо для отправки сообщений")
        } else {
            permissionLauncherSingle.launch(Manifest.permission.SEND_SMS)
        }

    }

    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            myToast("Разрешение получено")
        } else {
            myToast("Разрешение не получено")
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binging = null
    }

    private fun myToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}