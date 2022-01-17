package com.example.my_recipe_aws_ec2_test

import retrofit2.http.*

interface RecipeApi {

    @GET("/api/get-recipe")
    suspend fun fetchRecipe(): Recipe?

    @POST("/api/add-recipe")
    suspend fun addRecipe(
        @Body recipe: AddRecipe
    )

}