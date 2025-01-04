package com.example.contentprovidehw

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _contacts = MutableLiveData<MutableList<ContactModel>>()

    val contacts: LiveData<MutableList<ContactModel>> = _contacts

    @SuppressLint("Range")
    fun loadContacts() {
        val lists = mutableListOf<ContactModel>()

        Log.d("@@@", "Запустилась гет контакт")

        val phones = getApplication<Application>().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactModel = ContactModel(name, phoneNumber)
            lists.add(contactModel)
        }
        phones.close()



        _contacts.value = lists
    }

}