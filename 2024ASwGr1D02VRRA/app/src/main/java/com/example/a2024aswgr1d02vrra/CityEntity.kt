package com.example.a2024aswgr1d02vrra

import android.os.Parcel
import android.os.Parcelable

class CityEntity(
    var id: Int,
    var cityName: String,
    var description: String,
    var countryId: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(cityName)
        parcel.writeString(description)
        parcel.writeInt(countryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityEntity> {
        override fun createFromParcel(parcel: Parcel): CityEntity {
            return CityEntity(parcel)
        }

        override fun newArray(size: Int): Array<CityEntity?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString():String {
        return "$id - $cityName"
    }

}