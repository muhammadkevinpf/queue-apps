package com.example.queue_apps

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.queue_apps.databinding.NewCardFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewCardFragment : Fragment() {

    private var _binding: NewCardFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var cardsDbHelper: DbHelper

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = NewCardFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardsDbHelper = DbHelper(requireContext())

        binding.buttonSecond.setOnClickListener {
            var card = ArrayList<CardModel>()
            var cards = ArrayList<CardModel>()
            try {
                var namaPengunjung: String = binding.txtNamaPengunjung.text.toString()
                var jenisKelamin: String = binding.spinnerJenisKelamin.selectedItem.toString()
//                var waktuMasuk: String = binding.txtWaktuMasuk.text.toString()
                var waktuMasuk = dateFormatter()
                val id: UUID = UUID.randomUUID()
                var noKartu: String = binding.txtNokartuPengunjung.text.toString()
                card = cardsDbHelper.findCardByCardNumber(noKartu)
                cards = cardsDbHelper.readAllCards("masuk")
                if (card.size > 0) {
                    Snackbar.make(view, String.format("No kartu %s telah terpakai!", noKartu), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else if (cards.size > 1000) {
                    Snackbar.make(view, "Ruangan penuh!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }
                else {
                    if (textValidation()) {
                        cardsDbHelper.insertCard(
                            CardModel(
                                id.toString(),
                                noKartu,
                                namaPengunjung,
                                jenisKelamin,
                                waktuMasuk,
                                ""
                            )
                        )
                        Snackbar.make(view, "Data berhasil disimpan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    }

                }
            } catch(e: Exception) {
                Snackbar.make(view, "Data gagal disimpan", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        }
//        binding.btnWaktuMasuk.setOnClickListener {
//            val calendar: Calendar = Calendar.getInstance()
//            day = calendar.get(Calendar.DAY_OF_MONTH)
//            month = calendar.get(Calendar.MONTH)
//            year = calendar.get(Calendar.YEAR)
//            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//
//                onDateSet(view, year, monthOfYear, dayOfMonth)
//
//            }, year, month, day)
//            dpd.show()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        myHour = hourOfDay
//        myMinute = minute
//        val fullDate = String.format("%d/%d/%d %d:%d:00", myYear, myMonth + 1, myDay, myHour, myMinute)
//        binding.txtWaktuMasuk?.setText(fullDate)
//    }
//
//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        myDay = day
//        myYear = year
//        myMonth = month
//        val calendar: Calendar = Calendar.getInstance()
//        hour = calendar.get(Calendar.HOUR)
//        minute = calendar.get(Calendar.MINUTE)
//        val timePickerDialog = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//            onTimeSet(timePicker, hour, minute)
//        }
//        TimePickerDialog(requireContext(), timePickerDialog, hour, minute, true).show()
//    }

    private fun textValidation(): Boolean {
        var namaPengunjung: String = binding.txtNamaPengunjung.text.toString()
//        var waktuMasuk: String = binding.txtWaktuMasuk.text.toString()
        var noKartu: String = binding.txtNokartuPengunjung.text.toString()
        if (namaPengunjung.isEmpty()) {
            binding.txtNamaPengunjung.requestFocus()
            binding.txtNamaPengunjung.error = "Mohon isi nama"
            return false
        } else if (noKartu.isEmpty()) {
            binding.txtNokartuPengunjung.requestFocus()
            binding.txtNokartuPengunjung.error = "Mohon isi nomor kartu"
            return false
        }
//        else if (waktuMasuk.isEmpty()) {
//            binding.txtWaktuMasuk.requestFocus()
//            binding.txtWaktuMasuk.error = "Mohon isi jam masuk"
//            return false
//        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormatter(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        return formatted;
    }

}