package com.example.a2024aswgr1d02vrra

import android.os.Parcel
import android.os.Parcelable

class CountryEntity(
    var id: Int,
    var name: String,
    var population: Int,
    var description: String,
    var latitude: String,
    var longitude: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(population)
        parcel.writeString(description)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryEntity> {
        override fun createFromParcel(parcel: Parcel): CountryEntity {
            return CountryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CountryEntity?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString():String {
        return "$id - $name"
    }
}