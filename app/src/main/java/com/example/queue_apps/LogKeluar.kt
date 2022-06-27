package com.example.queue_apps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_log_keluar.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class LogKeluar : Fragment() {

    lateinit var cardsDbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cardsDbHelper = DbHelper(requireContext())
        return inflater.inflate(R.layout.fragment_log_keluar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAllLogoutUsers(view)
    }

    private fun showAllLogoutUsers(view: View){
        var cards = cardsDbHelper.readAllCards("keluar")
        this.ll_entries.removeAllViews()
        cards.forEach{card->
            var tvCustomer = TextView(requireContext())
            tvCustomer.textSize = 25F
            tvCustomer.text = "No. Kartu " + card.no_kartu_pengunjung
            tvCustomer.setBackgroundResource(R.drawable.border_bottom)
            tvCustomer.setPadding(25)
            tvCustomer.setOnClickListener {
                val bundle = bundleOf("idCustomer" to card.id)
                findNavController().navigate(R.id.action_FirstFragment_to_CardDetailFragment, bundle)
            }
            this.ll_entries.addView(tvCustomer)
        }
    }
}