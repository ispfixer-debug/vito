package com.vito.core.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WalletCard(
    cardNumber: String = "",
    onTopUp: () -> Unit = {},
    modifier: Modifier = Modifier,
    balance: Double = 0.0,
    onAddMoney: () -> Unit = {}
) {}

@Composable
fun SosButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {}

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    message: String = ""
) {}

@Composable
fun DriverCard(
    modifier: Modifier = Modifier,
    name: String = "",
    photoUrl: String? = null,
    rating: Double = 4.5,
    vehiclePlate: String = "",
    vehicleType: String = "Car",
    onCall: () -> Unit = {},
    onMessage: () -> Unit = {}
) {}

@Composable
fun StatusTimeline(
    modifier: Modifier = Modifier,
    events: List<TimelineEvent> = emptyList(),
    status: RideStatus = RideStatus.REQUESTED,
    currentStatus: RideStatus = RideStatus.REQUESTED
) {}
