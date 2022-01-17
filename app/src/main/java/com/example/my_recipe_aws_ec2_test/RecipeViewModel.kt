package com.example.my_recipe_aws_ec2_test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeViewModel : ViewModel() {

    private val uri = "http://10.0.2.2:8080/"

    private val recipeApiService = Retrofit.Builder()
        .baseUrl(uri)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RecipeApi::class.java)

    var title = MutableLiveData("No Title")
    var description = MutableLiveData("No Description")
    var image = MutableLiveData("")
    var uploadImage = ""
    var imageFileName = ""

    suspend fun getRecipe() {

        val recipe = recipeApiService.fetchRecipe()

        if (recipe != null) {
            title.value = recipe.title
        }
        if (recipe != null) {
            description.value = recipe.description
        }
        if (recipe != null) {
            image.value = recipe.image
        }

    }

    suspend fun addRecipe() {
        val recipe = title.value?.let {
            description.value?.let { it1 ->
                AddRecipe(title = it, description = it1, image= uploadImage,
                    imageFileName= imageFileName)
            }
        }
        if (recipe != null) {
            recipeApiService.addRecipe(recipe)
        }
    }


}