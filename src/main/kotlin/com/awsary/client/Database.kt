package com.awsary.client

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.GetItemRequest
import aws.sdk.kotlin.services.dynamodb.model.ScanRequest
import com.awsary.dto.AwsItem
import com.awsary.dto.AwsItems

class Database {

    private val client: DynamoDbClient = DynamoDbClient{ region = "us-east-1" }

    suspend fun getItems(): AwsItems {

        val request = ScanRequest {
            tableName = "awsary"
        }

        client.use { ddb ->
            val dbScan = ddb.scan(request)

            val items = dbScan.items?.map { item ->
                mapItem(item)
            }

            return AwsItems(
                data = items
            )
        }
    }

    suspend fun getItem(item: String): AwsItem? {

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet["id"] = AttributeValue.N(item)

        val request = GetItemRequest {
            key = keyToGet
            tableName = "awsary"
        }

        DynamoDbClient { region = "us-east-1" }.use { ddb ->
            val returnedItem = ddb.getItem(request)
            val numbersMap = returnedItem.item
            return numbersMap?.let { mapItem(it) }
        }
    }

    private fun mapItem(item: Map<String, AttributeValue>): AwsItem {
        return AwsItem(
            shortDescription = item.get("shortDescription")?.asS(),
            imageURL = item.get("imageURL")?.asS(),
            longName = item.get("longName")?.asS(),
            youtubeId = item.get("youtubeId")?.asS(),
            id = item.get("id")?.asN(),
            name = item.get("name")?.asS()
        )
    }

}