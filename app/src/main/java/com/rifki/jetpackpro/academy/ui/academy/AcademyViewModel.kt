package com.rifki.jetpackpro.academy.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity
import com.rifki.jetpackpro.academy.data.AcademyRepository
import com.rifki.jetpackpro.academy.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    fun getCourses(): LiveData<Resource<PagedList<CourseEntity>>> = academyRepository.getAllCourses()
}