package com.example.contentprovidehw

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovidehw.databinding.ItemListBinding

class CustomAdapter(
    var contacts: MutableList<ContactModel>,
    val functionCall: (contact: ContactModel) -> Unit,
    val functionSendSms: (contact: ContactModel) -> Unit
) :
    RecyclerView.Adapter<CustomAdapter.ContactItemViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ContactItemViewHolder,
        position: Int
    ) {
        val contact = contacts[position]
        holder.binding.contactName.text = contact.name
        holder.binding.contactPhone.text = contact.phone
        holder.binding.iconCallContact.setOnClickListener {
            functionCall(contact)
        }
        holder.binding.iconSendSms.setOnClickListener {
            functionSendSms(contact)
        }
    }

    override fun getItemCount() = contacts.size


    class ContactItemViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }



}