Macalester COMP 225 Spring 2014. This repository holds the code for our group's android mobile application KitchenSync.
Requires: Maven, Gson, Apache Commons, and Apache httmlComponents
Our app connects to a server. The server code can be found at https://github.com/paulyeo21/KitchenSync_Server

Controller: the package containing all the UI classes
    MenuActivity is the first screen a user sees, it displays the menu for a day and allows users to switch days and apply filters
        Uses private subclass and apache libs to pull menu information from our server
    ReviewActivities are launched by clicking on foods, they display details about the foods and allow users to rate the food
        Pushes reviews to the server using apache libs
    HelpActivity is a screen which shows help to the users
    MealListAdapter allows food to be displayed in an expandableListView
    Filters represent a set of dietary restrictions and can be applied to any level of the model to return a new version with only the foods that match those dietary restrictions

Model: the package containing our model for cafe mac's server
Please do not modify the fields of the model classes without making corresponding changes to the server
    The menu comes in a chunk called week that contains days
    Days contain meals which have a meal type
    Meals contain Stations
    Stations contain Food
    Foods contain reviews and have dietary restrictions
    Reviews may have a text, a rating or both
