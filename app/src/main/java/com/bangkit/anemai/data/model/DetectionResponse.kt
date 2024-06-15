package com.bangkit.anemai.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetectionResponse (
        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("hasil")
        val result: String? = null,

        @field:SerializedName("imageUrl")
        val imageUrl: String? = null,

        @field:SerializedName("waktu_prediksi")
        val createdAt: String? = null,

        @field:SerializedName("akurasi")
        val akurasi: String? = null,

        @field:SerializedName("deskripsi")
        val deskripsi: String? = null,

        @field:SerializedName("informasi_tambahan")
        val informasiTambahan: InformasiTambahan? = null,
    ) : Parcelable

@Parcelize
data class InformasiTambahan(

        @field:SerializedName("gayahidup_sehat")
        val gayahidupSehat: String? = null,

        @field:SerializedName("tindakan_saran")
        val tindakanSaran: String? = null,

        @field:SerializedName("perawatan_medis")
        val perawatanMedis: String? = null,

        @field:SerializedName("risiko_komplikasi")
        val risikoKomplikasi: String? = null,

        @field:SerializedName("pencegahan")
        val pencegahan: String? = null
) : Parcelable