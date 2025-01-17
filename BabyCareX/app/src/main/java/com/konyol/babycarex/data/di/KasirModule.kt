// AppModule.kt
package com.konyol.babycarex.data.di

import com.konyol.babycarex.R
import com.konyol.babycarex.adapter.BarangKasirAdapter
import com.konyol.babycarex.activity.kasir.ListBarangModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object KasirModule {
//    @Provides
//    fun provideItemList(): List<ListBarangModel> {
//        return listOf(
//            ListBarangModel("Bebek2an", "Hasbro", "Mainan", R.drawable.toy_duck, "Rp20.000"),
//            ListBarangModel("Mobil2an", "Audi", "Mainan", R.drawable.audi, "Rp500.000"),
//            ListBarangModel("Susu2an", "Ultramilk", "Makanan", R.drawable.blue_milk, "Rp100.000"),
//            ListBarangModel("Drip Baby", "China", "Pajangan", R.drawable.drip_baby, "Rp1.000.000")
//        )

//    }

//    @Provides
//    fun provideBarangKasirAdapter(itemList: List<ListBarangModel>): BarangKasirAdapter {
//        return BarangKasirAdapter(itemList)
//    }
}
