package com.rifki.jetpackpro.academy.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity
import com.rifki.jetpackpro.academy.data.AcademyRepository

class BookmarkViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    fun getBookmarks(): LiveData<List<CourseEntity>> {
        return academyRepository.getBookmarkedCourses()
    }
}