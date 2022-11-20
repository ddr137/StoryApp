package com.ddr1.storyapp

import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.data.remote.response.LoginResponse
import com.ddr1.storyapp.data.remote.response.NewStoryResponse
import com.ddr1.storyapp.data.remote.response.RegisterResponse
import com.ddr1.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyStoriesEntity(): List<StoriesEntity> {

        val storiesList = ArrayList<StoriesEntity>()

        for (i in 0..10) {
            val stories = StoriesEntity(
                "id $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                -6.917464,
                107.619125,
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z"
            )
            storiesList.add(stories)
        }

        return storiesList

    }

    fun generateDummyStoriesResponse(): StoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoriesResponse.Story>()

        for (i in 0 until 10) {
            val story = StoriesResponse.Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-10-01T18:38:11.598Z",
                name = "Dadan",
                description = "Lorem Ipsum ndolor siamaet",
                lon = 17.012,
                lat = -10.567,
            )

            listStory.add(story)
        }

        return StoriesResponse(error, listStory, message)
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResponse.LoginResult(
            userId = "user-yj5pc_LARC_AgK61",
            name = "Dadan Ramdhani",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )

        return LoginResponse(
            error = false,
            message = "success",
            loginResult = loginResult,
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateDummyImageFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyDescriptionRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateDummyNewStoryResponse(): NewStoryResponse {
        return NewStoryResponse(
            error = false,
            message = "success",
        )
    }

    fun generateDummyStoriesRepoEntity(): List<StoriesEntity> {

        val storiesList = ArrayList<StoriesEntity>()

        for (i in 0..9) {
            val stories = StoriesEntity(
                "id $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                -6.917464,
                107.619125,
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z"
            )
            storiesList.add(stories)
        }

        return storiesList

    }
}