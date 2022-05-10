package com.massimoregoli.movieapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.massimoregoli.movieapp.api.OmdbApi
import com.massimoregoli.movieapp.model.MovieResult
import com.massimoregoli.movieapp.model.SearchResult
import com.massimoregoli.movieapp.mycomposable.NetworkImageComponentPicasso
import com.massimoregoli.movieapp.ui.theme.MovieAppTheme
import org.json.JSONException
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                val list = remember {
                    mutableStateListOf<SearchResult>()
                }
                val movie = rememberSaveable {
                    mutableStateOf(MovieResult())
                }
                val textState = rememberSaveable { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if(movie.value.imdbID == "") {
                        Greeting(textState, list, movie)
                    } else {
                        Detail(movie)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    textState: MutableState<String>,
    list: SnapshotStateList<SearchResult>,
    movie: MutableState<MovieResult>
) {


    val search = rememberSaveable { mutableStateOf(textState.value) }
    val total = rememberSaveable { mutableStateOf(0) }
    val current_page = rememberSaveable { mutableStateOf(1) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val s = OmdbApi(context)

    if (textState.value != "") {
        s.searchTitle(textState.value, current_page.value,
            {
                val jo = JSONObject(it)
                try {
                    total.value = jo.getInt("totalResults")
                    val ja = jo.getJSONArray("Search")
                    val sType = object : TypeToken<List<SearchResult>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<SearchResult>>(ja.toString(), sType)
                    list.clear()
                    list.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Log.v("IMDB", "KAOS")
            }
        )
    }
    Column {
        OutlinedTextField(
            leadingIcon = {
              Icon(imageVector = Icons.Outlined.Search,
                  null)
            },
            label = {
              Text("Search Title")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            value = search.value,
            onValueChange = { search.value = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                textState.value = search.value
            }),
        )
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            itemsIndexed(list) { _, item ->
                Card(
                    elevation = 4.dp,
                    backgroundColor = Color(0xFFFFFFE0),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier
                        .clickable {
                            s.getMovie(item.imdbID,
                                {
                                    if (movie.value.imdbID == item.imdbID) {
                                        movie.value = MovieResult()
                                    } else {
                                        val sType = object : TypeToken<MovieResult>() {}.type

                                        val gson = Gson()
                                        movie.value = gson.fromJson(it, sType)
                                    }
                                },
                                {
                                    Log.v("IMDB", "KAOS")
                                }
                            )
                        }
                        .padding(4.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFFFFFE0))) {
                        NetworkImageComponentPicasso(url = item.Poster)
                        Column {
                            Text(
                                item.Title,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Blue
                            )
                            Text(
                                item.Year,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black
                            )
                            Text(
                                item.imdbID,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black
                            )
                            if (movie.value.imdbID == item.imdbID) {
                                Text(text = movie.value.Plot)
                                Text(text = movie.value.Rated)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(movie: MutableState<MovieResult>) {
    Column {
        Text(movie.value.Title, modifier = Modifier.clickable {
            movie.value = MovieResult()
        })
    }
}


