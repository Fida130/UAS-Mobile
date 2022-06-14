package com.app.tabunganuts.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.app.tabunganuts.MainActivity
import com.app.tabunganuts.MainActivity.Companion.listNasabah
import com.app.tabunganuts.R
import com.app.tabunganuts.activity.NasabahAddActivity
import com.app.tabunganuts.activity.NasabahAddActivity.Companion.UPDATE_AKUN
import com.app.tabunganuts.adapter.NasabahAdapter
import kotlinx.android.synthetic.main.fragment_money.*

class NasabahFragment : Fragment() {
    lateinit var adapterNasabah: NasabahAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_money, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        btnAdd.setOnClickListener {
            val intent = Intent(activity, NasabahAddActivity::class.java)
//            intent.putExtra("send_add", SEND_ADD_SHOES)
            startActivity(intent)
        }

        lvData.setOnItemClickListener { adapterView, view, i, l ->
            var menuPopup = PopupMenu(activity,view)
            menuPopup.menuInflater.inflate(R.menu.menu_popup,menuPopup.menu)
            menuPopup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.menuUpdate -> {
                        var intent = Intent(activity, NasabahAddActivity::class.java)
                        intent.putExtra(UPDATE_AKUN,listNasabah[i])
                        activity?.startActivity(intent)
                        true
                    }
                    R.id.menuDelete -> {
                        Toast.makeText(activity,"Hapus "+listNasabah[i].nama,Toast.LENGTH_LONG)
                        listNasabah.removeAt(i)
                        adapterNasabah.notifyDataSetChanged()
                        true
                    }
                    R.id.menuLink ->{
                        var url = listNasabah[i].imgnasabah
                        var intentWebsite = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        activity?.startActivity(intentWebsite)
                    }
                }
                false
            }
            menuPopup.show()
        }
    }

    override fun onResume() {
        super.onResume()
        showData()
    }

    fun showData(){
        var listAkun = MainActivity.listNasabah
        adapterNasabah = NasabahAdapter(listAkun, activity as MainActivity)
        lvData.adapter = adapterNasabah

    }
}