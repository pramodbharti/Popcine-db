# Popcine

![Popcine app screenshots](https://github.com/pramodbharti/Popcine-db/screenshots/blob/master/popcine.jpeg)

![Popcine app screenshots](https://github.com/pramodbharti/Popcine-db/screenshots/blob/master/popcine-landscape.jpeg)

# About the code

* Code is written in java
* Retrofit is used for networking task
* Picasso for image loading 
* ButterKnife for binding Android views which uses annotation processing to generate boilerplate code

# How to run the app

* Enter your TMDB API KEY in `gradle.properties` file like this `myApiKey="YOUR_API_KEY" `. If `gradle.properties` file doesn't exist, then you will have to create it.
* Enter your Fabric API KEY in `fabric.properties` file like this `apiSecret=YOUR_API_KEY `. If `fabric.properties` file doesn't exist, then you will have to create it.
* You must have Internet connectivity to load the movies data

# Features

* The api loads 20 movies detail at a time, when last 2 rows are visible, api loads next batch automatically.
* You can change menu Popular Movies, Top Rated Movies and Favorite Movies
* After clicking on poster image, you can see more details about that movie
