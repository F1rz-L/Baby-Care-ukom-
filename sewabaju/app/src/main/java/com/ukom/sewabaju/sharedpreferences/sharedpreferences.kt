package com.ukom.sewabaju.sharedpreferences

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class sharedpreferences {
    companion object {
        fun Context.setCustomData(key: String, value: String) {
            getSharedPreferences("HAHO", Context.MODE_PRIVATE).edit().putString(key, value).apply()
        }

        fun Context.getCustomData(key: String): String? {
            return getSharedPreferences("HAHO", Context.MODE_PRIVATE).getString(key, null)
        }

        fun Context.clearCustomData(key: String) {
            getSharedPreferences("HAHO", Context.MODE_PRIVATE).edit().remove(key).apply()
        }

        fun Context.addCart(json: JSONObject) {
            val items = getCart()

            var index = -1
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                if (item.getInt("id") == json.getInt("id") && item.getString("size") == json.getString("size")) index = i
            }

            if (index == -1) {
                items.put(json)
            } else {
                val item = items.getJSONObject(index)
                item.put("amount", item.getInt("amount") + json.getInt("amount"))
                item.put("subTotal", item.getInt("amount") * item.getInt("price"))
                items.put(index, item)
            }

            getSharedPreferences("HAHO", Context.MODE_PRIVATE).edit().putString("cart", items.toString()).apply()
        }

        fun Context.removeCart(json: JSONObject) {
            val items = getCart()

            var index = -1
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                if (item.getInt("id") == json.getInt("id") && item.getString("size") == json.getString("size")) index = i
            }

            if (index != -1) {
                items.remove(index)
            }

            getSharedPreferences("HAHO", Context.MODE_PRIVATE).edit().putString("cart", items.toString()).apply()
        }

        fun Context.getCart(): JSONArray {
            return JSONArray(getSharedPreferences("HAHO", Context.MODE_PRIVATE).getString("cart", "[]"))
        }

        fun Context.clearCart() {
            getSharedPreferences("HAHO", Context.MODE_PRIVATE).edit().remove("cart").apply()
        }

        fun Context.getCartItemAmount(id: Int, size: String): Int? {
            val items = getCart()

            var index = -1
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                if (item.getInt("id") == id && item.getString("size") == size) index = i
            }

            return if (index == -1) null else items.getJSONObject(index).getInt("amount")
        }
    }
}