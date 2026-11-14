package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counsel_sessions")
data class CounselSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val situationSummary: String,
    val goalModeName: String,
    val impactScaleName: String,
    val conflictPreferenceName: String,
    val resolutionStatusName: String,
    val counselSummary: String,
    val rationale: String,
    val blindSpotPassed: Boolean,
    val guaranteesUpheldCount: Int,
    val outcomeReflectionLabel: String,
    val outcomeReflectionNote: String,
    val trustReviewerNote: String,
    val isReviewedByTrust: Boolean,
    val timestampMillis: Long = System.currentTimeMillis()
)
