package com.app.tabunganuts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
data class MoneyModel(
    var akun:String,
    var jenisTransaksi:String,
    var tanggal:String,
    var time:String,
    var jumlah:Int,
): Parcelable
