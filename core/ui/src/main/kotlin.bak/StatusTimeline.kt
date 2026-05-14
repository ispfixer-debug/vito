package com.vito.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class RideStatus {
    REQUESTED, DRIVER_ASSIGNED, DRIVER_EN_ROUTE, DRIVER_ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED
}

data class TimelineEvent(
    val status: RideStatus,
    val label: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun StatusTimeline(
    events: List<TimelineEvent>,
    currentStatus: RideStatus,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        events.forEachIndexed { index, event ->
            val isCompleted = event.status.ordinal <= currentStatus.ordinal
            val isCurrent = event.status == currentStatus
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Timeline indicator
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 0.dp)
                                .size(2.dp, 12.dp)
                                .background(
                                    if (events[index - 1].status.ordinal < currentStatus.ordinal) 
                                        MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.outlineVariant
                                )
                        )
                    }
                    
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isCurrent -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(12.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Event content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = event.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCompleted || isCurrent) 
                            MaterialTheme.colorScheme.onSurface 
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isCurrent) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "In progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

fun getDefaultTimelineEvents(): List<TimelineEvent> = listOf(
    TimelineEvent(RideStatus.REQUESTED, "Ride requested"),
    TimelineEvent(RideStatus.DRIVER_ASSIGNED, "Driver assigned"),
    TimelineEvent(RideStatus.DRIVER_EN_ROUTE, "Driver en route"),
    TimelineEvent(RideStatus.DRIVER_ARRIVED, "Driver arrived"),
    TimelineEvent(RideStatus.IN_PROGRESS, "In progress"),
    TimelineEvent(RideStatus.COMPLETED, "Completed")
)