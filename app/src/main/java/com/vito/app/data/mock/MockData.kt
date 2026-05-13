package com.vito.app.data.mock

import com.vito.app.domain.model.TripHistoryItem

object MockData {
    val mockPickupLocations = listOf(
        MockLocation("123 Main St", 37.7749, -122.4194),
        MockLocation("456 Market St", 37.7897, -122.4000),
        MockLocation("789 Mission St", 37.7857, -122.4062),
        MockLocation("101 California St", 37.7932, -122.3968),
        MockLocation("202 Market St", 37.7911, -122.4005)
    )

    val mockDropoffLocations = listOf(
        MockLocation("500 Stanford Ave", 37.4275, -122.1697),
        MockLocation("600 Berkeley Way", 37.8344, -122.2700),
        MockLocation("700 Oakland Blvd", 37.8044, -122.2712),
        MockLocation("800 Mountain View", 37.3861, -122.0839),
        MockLocation("900 San Jose St", 37.3382, -121.8863)
    )

    val mockDrivers = listOf(
        MockDriver("driver1", "John Smith", "Toyota Camry (ABC-1234)", 4.8, "https://randomuser.me/api/portraits/men/1.jpg"),
        MockDriver("driver2", "Sarah Johnson", "Honda Civic (DEF-5678)", 4.9, "https://randomuser.me/api/portraits/women/2.jpg"),
        MockDriver("driver3", "Mike Davis", "Hyundai Elantra (GHI-9012)", 4.7, "https://randomuser.me/api/portraits/men/3.jpg"),
        MockDriver("driver4", "Emily Wilson", "Nissan Altima (JKL-3456)", 4.9, "https://randomuser.me/api/portraits/women/4.jpg"),
        MockDriver("driver5", "David Brown", "Kia Forte (MNO-7890)", 4.6, "https://randomuser.me/api/portraits/men/5.jpg")
    )

    val mockUsers = listOf(
        MockUser("user1", "oussama", "Oussama", "oussama@example.com", "https://randomuser.me/api/portraits/men/10.jpg", 25.0),
        MockUser("user2", "alice", "Alice", "alice@example.com", "https://randomuser.me/api/portraits/women/11.jpg", 50.0),
        MockUser("user3", "bob", "Bob", "bob@example.com", "https://randomuser.me/api/portraits/men/12.jpg", 100.0)
    )

    val mockProducts = listOf(
        MockProduct("prod1", "Coca-Cola", "Refreshing cold drink", 2.50, "drinks", listOf("https://picsum.photos/200?random=1")),
        MockProduct("prod2", "Pepsi", "Another refreshing drink", 2.50, "drinks", listOf("https://picsum.photos/200?random=2")),
        MockProduct("prod3", "Orange Juice", "Freshly squeezed", 4.00, "drinks", listOf("https://picsum.photos/200?random=3")),
        MockProduct("prod4", "Chips", "Crunchy potato chips", 3.00, "snacks", listOf("https://picsum.photos/200?random=4")),
        MockProduct("prod5", "Chocolate Bar", "Sweet treat", 2.00, "snacks", listOf("https://picsum.photos/200?random=5")),
        MockProduct("prod6", "Cookies", "Crunchy cookies", 3.50, "snacks", listOf("https://picsum.photos/200?random=6")),
        MockProduct("prod7", "Water", "Bottled water", 1.50, "drinks", listOf("https://picsum.photos/200?random=7")),
        MockProduct("prod8", "Energy Drink", "Boosts energy", 4.50, "drinks", listOf("https://picsum.photos/200?random=8"))
    )

    fun generateMockTrips(): List<TripHistoryItem> = listOf(
        TripHistoryItem("trip_history_1", "123 Main St", "500 Stanford Ave", 12.50, "completed", System.currentTimeMillis() - 86400000, "John Smith", "completed", 5),
        TripHistoryItem("trip_history_2", "456 Market St", "600 Berkeley Way", 15.00, "completed", System.currentTimeMillis() - 172800000, "Sarah Johnson", "completed", 8),
        TripHistoryItem("trip_history_3", "789 Mission St", "700 Oakland Blvd", 18.00, "cancelled", System.currentTimeMillis() - 259200000, null, "cancelled", 0)
    )
}

data class MockLocation(val address: String, val lat: Double, val lng: Double)
data class MockDriver(val id: String, val name: String, val vehicle: String, val rating: Double, val photoUrl: String)
data class MockUser(val id: String, val username: String, val alias: String, val email: String, val photoUrl: String, val balance: Double)
data class MockProduct(val id: String, val name: String, val description: String?, val price: Double, val category: String, val imageUrls: List<String>)
