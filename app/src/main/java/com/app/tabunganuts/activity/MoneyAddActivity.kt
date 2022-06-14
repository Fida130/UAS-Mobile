package com.app.tabunganuts.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.app.tabunganuts.MainActivity.Companion.listNasabah
import com.app.tabunganuts.MainActivity.Companion.listMoney
import com.app.tabunganuts.R
import com.app.tabunganuts.model.MoneyModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_money_add.*
import java.sql.Date
import java.text.SimpleDateFormat

class MoneyAddActivity : AppCompatActivity() {
    companion object{
        val UPDATE_TABUNGAN:String = "update_tabungan"
    }
    lateinit var dateData:String
    lateinit var timeData:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_add)
        layout_tabungan.visibility = View.VISIBLE
        var items:ArrayList<String> = ArrayList<String>()
        listNasabah.map {
            items.add(it.nama)
        }
        val adapterNasabah = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items)
        (inpAkunTabungan.editText as? AutoCompleteTextView)?.setAdapter(adapterNasabah)
        val adapterJenis = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("Setor","Tarik"))
        (inpJenisTransaksi.editText as? AutoCompleteTextView)?.setAdapter(adapterJenis)

        btnDateTabungan.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                dateData = dateFormatter.format(Date(it))
                txtTanggalTabungan.setText("Tanggal : "+dateData)
                txtTanggalTabungan.visibility = View.VISIBLE
            }

        }
        btnTimeTabungan.setOnClickListener {
            // instance of MDC time picker
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECT YOUR TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "MainActivity")
            materialTimePicker.addOnPositiveButtonClickListener {

                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute
                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                        }
                    }
                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                        }
                    }
                }
                timeData =  formattedTime
                txtTimeTabungan.setText("Waktu  : "+timeData)
                txtTimeTabungan.visibility = View.VISIBLE
            }
        }
        var intentGet:MoneyModel? = intent?.getParcelableExtra<MoneyModel>(UPDATE_TABUNGAN)
        if (intentGet==null){
            getSupportActionBar()!!.setTitle("Add Tabungan");

            btnAddAkunTabungan.setOnClickListener {
                inputTabungan()
                finish()
            }
        }
        else{

        }
    }
    fun inputTabungan(){

        listMoney.add(
            MoneyModel(
                inpAkunTabungan.editText?.text.toString(),
                inpJenisTransaksi.editText?.text.toString(),
                dateData,
                timeData,
                inpJumlahTabungan.editText?.text.toString().toInt()
            )
        )
    }

}