package com.beepbeep.vito.app.data.mock

import com.beepbeep.vito.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class MockUser(val uid: String = "user-001", val alias: String = "Oussama", val role: UserRole = UserRole.CLIENT, val walletBalance: Int = 2500)

object MockAuthRepository {
    private val _user = MutableStateFlow<MockUser?>(null)
    val currentUser: Flow<MockUser?> = _user
    fun login(u: String, p: String) = if (u == "oussama" && p == "oussama") { _user.value = MockUser(); Result.success(_user.value!!) } else Result.failure(Exception("Invalid"))
    fun logout() { _user.value = null }
    fun switchRole(r: UserRole) { _user.value = _user.value?.copy(role = r) }
}

object MockRideRepository {
    private val _ride = MutableStateFlow<Ride?>(null)
    val current: Flow<Ride?> = _ride
    fun estimate(p: List<Double>, d: List<Double>, c: String) = Result.success(FareEstimate(500, 600, 5.0))
    fun create(r: Ride): Result<String> { _ride.value = r.copy(id = "r1"); Result.success(_ride.value!!.id) }
    val history = listOf(Ride("r1", "u1", "d1", Location(30.0, 31.0, "Cairo"), Location(30.0, 31.5, "Giza"), 500, RideStatus.COMPLETED))
}

object MockSendRepository {
    fun create(p: Package) = Result.success("p1")
    val history = listOf(Package("p1", "u1", "d1", Location(), Location(), status = PackageStatus.DELIVERED))
}

object MockMartRepository {
    val products = listOf(MartProduct("p1", "Cola", "Drink", 150, "Drinks", 100))
    val categories = listOf("All", "Drinks")
    private val _cart = MutableStateFlow<List<MartOrderItem>>(emptyList())
    val cart: Flow<List<MartOrderItem>> = _cart
    fun add(p: MartProduct) { val c = _cart.value.toMutableList(); c.add(MartOrderItem(p.id, p.name, p.price, 1)); _cart.value = c }
    fun place(a: Location, t: Int) = Result.success("o1")
}

object MockChatRepository {
    val messages = MutableStateFlow(listOf(ChatMessage("m1", "c1", "d1", "Hello"), ChatMessage("m2", "c1", "u1", "Hi")))
    fun send(cid: String, sid: String, t: String) { val m = ChatMessage("m\$System.currentTimeMillis()", cid, sid, t); messages.value = messages.value + m }
}
