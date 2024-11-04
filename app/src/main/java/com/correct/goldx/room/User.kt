package com.correct.goldx.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey
    var id: String,
    val name: String,
    val mail: String,
    val phone: String,
    val password: String,
    val country: String,
    val city: String,
    val street: String,
    val buildNo: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        if (id != other.id) return false
        if (name != other.name) return false
        if (mail != other.mail) return false
        if (phone != other.phone) return false
        if (password != other.password) return false
        if (country != other.country) return false
        if (city != other.city) return false
        if (street != other.street) return false
        if (buildNo != other.buildNo) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + mail.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + buildNo.hashCode()
        return result
    }
}