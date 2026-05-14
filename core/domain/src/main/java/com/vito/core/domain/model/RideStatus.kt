package com.vito.core.domain.model

enum class RideStatus {
    REQUESTED, ACCEPTED, ARRIVING, IN_PROGRESS, COMPLETED, CANCELLED
}

data class StatusTimeline(
    val status: RideStatus,
    val events: List<TimelineEvent>
)

data class TimelineEvent(
    val status: RideStatus,
    val timestamp: Long,
    val message: String
)

fun getDefaultTimelineEvents(): List<TimelineEvent> = emptyList()
