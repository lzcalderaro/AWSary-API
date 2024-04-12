package com.awsary.dto

import kotlinx.serialization.Serializable

@Serializable
data class AwsItems(
    val data: List<AwsItem>?
)
