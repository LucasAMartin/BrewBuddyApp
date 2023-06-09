package com.example.brewbuddycs380;

import android.widget.ImageView;
import java.util.*;

/**
 * Define an enum to represent the different properties that a coffee can have.
 */
enum Properties {
    HOT(0), ICED(1), STRONG(2), WEAK(3), SWEET(4),
    CREAMY(5), BLACK(6), FOAM(7), NOFOAM(8), DECAF(9), BITTER(10),
    FLAVOR_FRUIT(11), FLAVOR_CHOCOLATE(12), FLAVOR_CARAMEL(13), FLAVOR_VANILLA(14);

    // Each property has an index value so it can be converted to a series of numbers to go in the database
    private final int index;

    /**
     * Constructs a Properties enum with the given index.
     *
     * @param index the index value of the property
     */
    Properties(int index) {
        this.index = index;
    }

    /**
     * Returns the index value of the property.
     *
     * @return the index value
     */
    public int getIndex() {
        return index;
    }
}

/**
 * Define a class to represent a coffee object.
 */
class Coffee implements Comparable<Coffee> {
    private String name; // The name of the coffee
    private String description;
    private int similarityScore; // The similarity score of the coffee with respect to the user's preference
    private Set<Properties> coffeeProperties; // The properties of the coffee
    private int drawableId;

    /**
     * Returns the name of the coffee.
     *
     * @return the name of the coffee
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the drawable ID of the coffee image.
     *
     * @return the drawable ID of the coffee image
     */
    public int getDrawableId(){
        return drawableId;
    }

    /**
     * Returns the description of the coffee.
     *
     * @return the description of the coffee
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the similarity score of the coffee.
     *
     * @param s the similarity score
     */
    public void setSimilarityScore(int s){
        this.similarityScore =  s;
    }

    // Define an array of Coffee objects representing the different coffees available in the menu
    static Coffee[] MENU = new Coffee[]{
            new Coffee("Drip Coffee", EnumSet.of(Properties.STRONG, Properties.BLACK, Properties.CREAMY, Properties.NOFOAM, Properties.HOT, Properties.ICED), R.drawable.drip, "A classic coffee made by brewing ground coffee beans with hot water. It has a strong and bold flavor. Drip coffee is a popular choice for those who enjoy a simple and straightforward cup of coffee. It is often served black but can also be enjoyed with sugar or cream."),
            new Coffee("Espresso", EnumSet.of(Properties.STRONG, Properties.BLACK, Properties.NOFOAM, Properties.BITTER, Properties.HOT), R.drawable.latte, "A concentrated coffee made by forcing hot water through finely ground coffee beans. It has a rich and intense flavor. Espresso is often enjoyed on its own or used as a base for other coffee drinks such as lattes and cappuccinos."),
            new Coffee("Americano", EnumSet.of(Properties.WEAK, Properties.BLACK, Properties.NOFOAM, Properties.HOT, Properties.ICED), R.drawable.espresso, "A coffee made by adding hot water to an espresso shot. It has a milder flavor than espresso. Americano is a popular choice for those who enjoy the taste of espresso but prefer a less intense flavor. It can be served hot or iced."),
            new Coffee("Latte", EnumSet.of(Properties.WEAK, Properties.SWEET, Properties.CREAMY, Properties.FOAM, Properties.HOT, Properties.ICED), R.drawable.latte, "A coffee made with espresso and steamed milk. It has a creamy and smooth texture. Latte is a popular choice for those who enjoy the taste of coffee but prefer a milder flavor and a creamy texture. It can be served hot or iced and is often topped with milk foam."),
            new Coffee("Cappuccino", EnumSet.of(Properties.WEAK, Properties.SWEET, Properties.CREAMY, Properties.FOAM, Properties.HOT), R.drawable.cappuccino, "A coffee made with espresso, steamed milk, and milk foam. It has a creamy texture and a frothy top. Cappuccino is a popular choice for those who enjoy the taste of coffee and the texture of milk foam. It is typically served hot and can be topped with cinnamon or cocoa powder."),
            new Coffee("Mocha", EnumSet.of(Properties.WEAK, Properties.SWEET, Properties.CREAMY, Properties.NOFOAM, Properties.FLAVOR_CHOCOLATE, Properties.HOT), R.drawable.mocha, "A coffee made with espresso, steamed milk, and chocolate syrup. It has a sweet and chocolatey flavor. Mocha is a popular choice for those who enjoy the taste of coffee and chocolate. It is typically served hot and can be topped with whipped cream."),
            new Coffee("Macchiato", EnumSet.of(Properties.STRONG, Properties.CREAMY, Properties.FOAM, Properties.BITTER, Properties.FOAM, Properties.HOT), R.drawable.macchiato,"A coffee made with espresso and a small amount of milk foam. It has a strong flavor with a creamy texture. Macchiato is often enjoyed on its own or used as a base for other coffee drinks such as lattes and cappuccinos."),
            new Coffee("Flat White", EnumSet.of(Properties.WEAK, Properties.SWEET, Properties.NOFOAM, Properties.CREAMY, Properties.HOT), R.drawable.latte,"A coffee made with espresso and steamed milk. It has a smooth and velvety texture. Flat White is a popular choice for those who enjoy the taste of coffee but prefer a smooth texture. It is typically served hot.")
    };

    /**
     * Constructs a new Coffee object with the specified properties.
     *
     * @param name            the name of the coffee
     * @param coffeeProperties the properties of the coffee
     * @param drawableId      the drawable ID of the coffee image
     * @param description     the description of the coffee
     */
    public Coffee(String name, Set<Properties> coffeeProperties, int drawableId, String description) {
        this.name = name;
        this.coffeeProperties = coffeeProperties;
        this.description = description;
        this.drawableId = drawableId;
    }

    /**
     * Calculates the similarity score of the coffee with respect to the user's preference.
     *
     * @param userPreference the user's preference
     * @return the similarity score
     */
    public int calculateSimilarityScore(Set<Properties> userPreference) {
        int similarityScore = 0;
        for (Properties property : userPreference) {
            if (coffeeProperties.contains(property)) {
                similarityScore++;
            }
        }
        return similarityScore;
    }

    /**
     * Returns the properties of this coffee.
     *
     * @return the properties of this coffee
     */
    public final Set<Properties> getCoffeeProperties(){
        return this.coffeeProperties;
    }

    /**
     * Compares this coffee with another coffee based on their similarity scores.
     *
     * @param coffee the coffee to compare to
     * @return the difference between the similarity scores of the two coffees
     *         A positive value means this coffee has a lower similarity score than the coffee being compared to.
     *         A negative value means this coffee has a higher similarity score than the coffee being compared to.
     */
    @Override
    public int compareTo(Coffee coffee) {
        return coffee.similarityScore - this.similarityScore;
    }
}

/**
 * Define a class with static methods to recommend coffees based on the user's preference.
 */
public class CoffeeRecommender {
    /**
     * Recommends the top 5 coffees based on the user's preference.
     *
     * @param userPreference the user's preference
     * @return an array of the top 5 recommended coffee objects
     */
    public static Coffee[] recommend5Coffee(Set<Properties> userPreference) {
        // Create a priority queue to store the recommended coffee objects
        // I chose this because it automatically sorts the coffee objects
        // based on similarityScore to the userPreferences
        PriorityQueue<Coffee> recommendedCoffees = new PriorityQueue<>();

        // Calculate the similarity score for each coffee object in the menu
        for (Coffee coffee : Coffee.MENU) {
            int s = coffee.calculateSimilarityScore(userPreference);
            coffee.setSimilarityScore(coffee.calculateSimilarityScore(userPreference));
            recommendedCoffees.add(coffee);
        }

        // Create an array to store the top 5 recommended coffee objects
        Coffee[] topRecommendedCoffees = new Coffee[5];
        for (int i = 0; i < topRecommendedCoffees.length; i++) {
            topRecommendedCoffees[i] = recommendedCoffees.poll();
        }
        return topRecommendedCoffees;
    }

    /**
     * Recommends the top coffee based on the user's preference.
     *
     * @param userPreference the user's preference
     * @return the top recommended coffee object
     */
    public static Coffee recommendTopCoffee(Set<Properties> userPreference) {
        // Create a priority queue to store the recommended coffee objects
        PriorityQueue<Coffee> recommendedCoffees = new PriorityQueue<>();

        // Calculate the similarity score for each coffee object in the menu
        for (Coffee coffee : Coffee.MENU) {
            coffee.setSimilarityScore(coffee.calculateSimilarityScore(userPreference));
            recommendedCoffees.add(coffee);
        }

        // Return the top recommended coffee object
        return recommendedCoffees.poll();
    }

    /**
     * Returns a String representation of the user's preferences.
     *
     * @param userPreferences the user's preferences
     * @return a String representation of the user's preferences
     */
    public static String getPreferencesString(Set<Properties> userPreferences) {
        StringBuilder sb = new StringBuilder();
        for (Properties property : userPreferences) {
            switch (property) {
                case ICED:
                    sb.append("ice, ");
                    break;
                case CREAMY:
                    sb.append("cream, ");
                    break;
                case SWEET:
                    sb.append("sugar, ");
                    break;
                case FLAVOR_FRUIT:
                    sb.append("strawberry syrup, ");
                    break;
                case FLAVOR_CHOCOLATE:
                    sb.append("chocolate syrup, ");
                    break;
                case FLAVOR_VANILLA:
                    sb.append("vanilla syrup, ");
                    break;
                case FLAVOR_CARAMEL:
                    sb.append("caramel syrup, ");
                    break;
                case DECAF:
                    sb.append("decaf, ");
                    break;
            }
        }
        // This part cuts off the last comma and space
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    /**
     * Converts the user's preference to a string representation.
     *
     * @param userPreference the user's preference
     * @return a string representation of the user's preference
     */
    public static String userPreferenceToString(Set<Properties> userPreference) {
        StringBuilder sb = new StringBuilder();
        for (Properties property : userPreference) {
            sb.append(property.getIndex());
            sb.append(",");
        }
        // This part cuts off the last comma
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Converts a string representation of user's preference to a set of properties.
     *
     * @param string the string representation of user's preference
     * @return a set of properties representing the user's preference
     */
    public static Set<Properties> stringToUserPreference(String string) {
        // Create an empty set of properties enums
        Set<Properties> userPreference = EnumSet.noneOf(Properties.class);
        String[] indices = string.split(",");
        for (String index : indices) {
            int i = Integer.parseInt(index);
            for (Properties property : Properties.values()) {
                if (property.getIndex() == i) {
                    userPreference.add(property);
                }
            }
        }
        return userPreference;
    }
}