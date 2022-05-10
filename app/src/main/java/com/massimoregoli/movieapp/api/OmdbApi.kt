package com.massimoregoli.movieapp.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class OmdbApi(private var context: Context) {
    fun getMovie(id: String,
                 onSuccess: (String) -> Unit,
                 onError: (VolleyError) -> Unit) {
        val url = "https://www.omdbapi.com/?i=$id&apikey=$APIKEY_1"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                onSuccess(it)
            },
            {
                onError(it)
            })
        queue.add(stringRequest)
    }

    fun searchTitle(search: String, page: Int,
                    onSuccess: (String) -> Unit,
                    onError: (VolleyError) -> Unit) {
        val url = "https://www.omdbapi.com/?s=$search&apikey=2a17bb24&page=$page"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                onSuccess(it)
            },
            {
                onError(it)
            })
        queue.add(stringRequest)
    }
}