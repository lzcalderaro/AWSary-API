package com.awsary.dto

import kotlinx.serialization.Serializable

@Serializable
data class AwsItem(
    val shortDescription: String?,
    val imageURL: String?,
    val longName: String?,
    val youtubeId: String?,
    val id: String?,
    val name: String?
)