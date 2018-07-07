# Popcine-db | Popular Movies project stage 1

![Popcine app screenshots](https://github.com/pramodbharti/Popcine-db/blob/master/Popcine.jpeg)

# About the code

* Code is written in java
* Retrofit is used for networking task
* Picasso for image loading 
* ButterKnife for binding Android views which uses annotation processing to generate boilerplate code

# How to run the app

* Enter your TMDB API KEY in `gradle.properties` file like this `myApiKey="YOUR_API_KEY" `. If `gradle.properties` file doesn't exist, then you will have to create it.
* You must have Internet connectivity to load the movies data

# Features

* The api loads 20 movies detail at a time, when last 2 rows are visible, api loads next batch automatically.
* You can change from Popular Movies to Top Rated Movies usin menu
* After clicking on poster image, you can see more details about that movie
