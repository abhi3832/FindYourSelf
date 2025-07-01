package com.example.findyourself.dataClasses

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class CommonInterests(val id : ImageVector, val interest : String)
{
    Music(Icons.Default.MusicNote, "Music"),
    MoviesAndTv(Icons.Default.MovieCreation, "Music & Tv"),
    Gaming(Icons.Default.SportsEsports, "Gaming"),
    FoodsAndDrink(Icons.Default.Fastfood, "Foods & Drink"),
    FashionAndBeauty(Icons.Default.FaceRetouchingNatural, "Fashion & Beauty"),
    ArtAndCulture(Icons.Default.Diversity2, "Art & Culture"),
    BooksAndLiterature(Icons.Default.AutoStories, "Books & Literature"),
    BusinessAndEnt(Icons.Default.Business, "Business & Entrepreneur"),
    Finance(Icons.Default.CurrencyRupee, "Finance"),
    Engineering(Icons.Default.Engineering, "Engineering"),
    Teaching(Icons.Default.School, "Teaching"),
    Medicine(Icons.Default.HealthAndSafety, "Medicine"),
    Animals(Icons.Default.Pets, "Animals"),
    Science(Icons.Default.Science, "Science"),
    Sports(Icons.Default.SportsBasketball, "Sports"),
    Coding(Icons.Default.Code, "Code"),
}