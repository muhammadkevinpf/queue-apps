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
import com.example.queue_apps.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null

    lateinit var cardsDbHelper: DbHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MainFragmentBinding.inflate(inflater, container, false)
        cardsDbHelper = DbHelper(requireContext())

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAllLoginUsers(view)

    }

    private fun showAllLoginUsers(view: View){
        var cards = cardsDbHelper.readAllCards("masuk")
        this.ll_entries.removeAllViews()
        cards.forEach{ card ->
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