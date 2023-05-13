package com.nurfauzi.storyapp

import com.nurfauzi.storyapp.data.network.responses.ListStoryItem
import com.nurfauzi.storyapp.domain.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story>{
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..10){
            val story = Story(
                id = "$i",
                name = "fauzi31",
                description = "lorem ipsum $i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2023-01-12T11:01:05.963Z",
                lat = 11.12,
                lon = 11.31
            )
            items.add(story)
        }
        return items
    }

    fun getTokenDummy() : String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVEaFU3RmdOMC04RHlVaVgiLCJpYXQiOjE2ODI1OTc3NDR9.GhZ8OKFzOMnrhvwrlHRVAaBKdZMBnz7S_Xnet-60Jd8"
    }
}