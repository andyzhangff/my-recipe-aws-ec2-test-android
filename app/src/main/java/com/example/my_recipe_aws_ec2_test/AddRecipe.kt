package com.example.my_recipe_aws_ec2_test

data class AddRecipe(
    val title: String,
    val description: String,
    val image: String,
    val imageFileName: String,
)
