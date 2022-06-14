package com.app.tabunganuts.activity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.app.tabunganuts.MainActivity
import com.app.tabunganuts.R
import com.app.tabunganuts.model.NasabahModel

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_money_add.*
import kotlinx.android.synthetic.main.akun_list.*
import mumayank.com.airlocationlibrary.AirLocation

class NasabahAddActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object{
        var UPDATE_AKUN:String = "Update Nasabah"
    }
    var lat : Double = 0.0; var lng : Double = 0.0;
    var airLoc : AirLocation? = null
    var gMap : GoogleMap? = null
    lateinit var mapFragment : SupportMapFragment
    private val MAPBOX_TOKEN = "pk.eyJ1IjoiaGlsbWl5dXN1Zjc4IiwiYSI6ImNsNDhlMm8yNDA2ZHIzZG8yc2xmbXMxdzYifQ.RLnKKlkytbMLIDkLRa4RTg"
    //    pk.eyJ1IjoiaGlsbWl5dXN1Zjc4IiwiYSI6ImNsNDg4Mm95ZjBodWUzZG5jYjl2dXc3eHEifQ.OqUO5VVve0HXmgGu4ThZHA
    var URL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_add)
        layout_nasabah.visibility = View.VISIBLE
        var intentGet: NasabahModel? = intent?.getParcelableExtra<NasabahModel>(UPDATE_AKUN)
        if (intentGet==null){
            getSupportActionBar()!!.setTitle("Add Akun");
            radioAlamatNasabah.setOnCheckedChangeListener { radioGroup, i ->
                var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
                if (radioButton.text == "Alamat"){
                    inpAlamatAkun.visibility = View.VISIBLE
                    layoutMaps.visibility = View.GONE
                }else{
                    inpAlamatAkun.visibility = View.VISIBLE
                    layoutMaps.visibility = View.VISIBLE
                    URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/address.json?proximity=$lng,$lat&access_token=" +
                            "$MAPBOX_TOKEN&limit=1"
                    airLoc = AirLocation(this,true,true,
                        object : AirLocation.Callbacks{
                            override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                                Toast.makeText(this@NasabahAddActivity, "Gagal mendapatkan posisi saat ini",
                                    Toast.LENGTH_SHORT).show()
                            }

                            override fun onSuccess(location: Location) {
                                val ll = LatLng(location.latitude,location.longitude)
                                gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
//                                getAddressLocation(URL)
                            }
                        })
                }
            }
            radioImageNasabah.setOnCheckedChangeListener { radioGroup, i ->
                var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
                if (radioButton.text.toString() == "Image URL" ){
                    inpImageAkun.visibility = View.VISIBLE
                    inpImageAkun.editText?.setOnClickListener {
                        if (it.isFocusable) {
                            imageViewAkun.visibility = View.VISIBLE
                            Picasso.get().load(inpImageAkun.editText!!.text.toString())
                                .into(imageViewAkun)
                        }
                    }
                }else{
                    imageViewAkun.visibility = View.VISIBLE
                    inpImageAkun.visibility = View.GONE
                    imageViewAkun.setOnClickListener{
                        var intentGaleri = Intent()
                        intentGaleri.setType("image/*")
                        intentGaleri.setAction(Intent.ACTION_GET_CONTENT)
                        startActivity(Intent.createChooser(intentGaleri,"Pilih Gambar ... "))
                    }
                }
            }
            btnAddAkunTabungan.setOnClickListener {
                inputAKun()
                finish()
            }
        }else{
            txtNamaAkunList.text = "Update Akun"
        }
    }
    fun getAddressLocation(url : String){
        val request = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener {
                val features = it.getJSONArray("features").getJSONObject(0)
                val place_name = features.getString("place_name")
                val center = features.getJSONArray("center")
                val lat = center.get(0).toString()
                val lng = center.get(1).toString()
                inpAlamatAkun.editText?.setText(place_name.toString())

            }, Response.ErrorListener {
                Toast.makeText(this,"can't get destination location", Toast.LENGTH_SHORT).show()
            })
        val q = Volley.newRequestQueue(this)
        q.add(request)
    }


    fun inputAKun() {
        MainActivity.listNasabah.add(
            NasabahModel(
                inpNamaAkun.editText?.text.toString(),
                inpAlamatAkun.editText?.text.toString(),
                inpHPAkun.editText?.text.toString(),
                inpImageAkun.editText?.text.toString(),
                0,
            )
        )
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        if(gMap!=null){
            airLoc = AirLocation(this,true,true,
                object : AirLocation.Callbacks{
                    override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                        Toast.makeText(this@NasabahAddActivity, "Gagal mendapatkan posisi saat ini",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(location: Location) {
                        val ll = LatLng(location.latitude,location.longitude)
                        gMap!!.addMarker(MarkerOptions().position(ll).title("Posisi Saya"))
                        gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,16.0f))
                        lat= location.latitude
                        lng=location.longitude
                    }
                })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLoc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}