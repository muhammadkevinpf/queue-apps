package com.example.queue_apps

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_card_detail.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CardDetailFragment : Fragment() {

    lateinit var cardsDbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cardsDbHelper = DbHelper(requireContext())

        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val param = arguments?.getString("idCustomer")
        getCustomerDetailByID(view, param!!)
        onClickLogout(view, view.tv_id.text.toString())
    }

    private fun getCustomerDetailByID(view: View, id: String) {
        val card = cardsDbHelper.findCardByCardNumber(id)
        if (card.size > 0) {
            view.tv_id.text = card[0].id
            view.tv_no_kartu.text = card[0].no_kartu_pengunjung
            view.tv_nama.text = card[0].nama
            view.tv_jenis_kelamin.text = card[0].jenis_kelamin
            view.tv_waktu_masuk.text = card[0].waktu_masuk
            view.tv_waktu_keluar.text = card[0].waktu_keluar

            if (!card[0].waktu_keluar.isNullOrEmpty()) {
                view.btn_keluar.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickLogout(view: View, id: String) {
        view.btn_keluar.setOnClickListener{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current.format(formatter)
            cardsDbHelper.updateCard(formatted, id)
            findNavController().navigate(R.id.action_CardDetailFragment_to_FirstFragment)
            Snackbar.make(view, "Data berhasil diubah", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}