package com.rifki.jetpackpro.academy.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.rifki.jetpackpro.academy.data.AcademyRepository
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseWithModule
import com.rifki.jetpackpro.academy.vo.Resource

class DetailCourseViewModel(private val academyRepository: AcademyRepository): ViewModel() {
    val courseId = MutableLiveData<String>()

    fun setSelectedCourse(courseId: String) {
        this.courseId.value = courseId
    }

    //Transformations.switchMap digunakan untuk mengambil data setiap kali courseId-nya berubah
    var courseModule: LiveData<Resource<CourseWithModule>> = Transformations.switchMap(courseId) { mCourseId ->
        academyRepository.getCourseWithModules(mCourseId)
    }

    fun setBookmark() {
        val moduleResource = courseModule.value
        if (moduleResource != null) {
            val courseWithModule = moduleResource.data

            if (courseWithModule != null) {
                val courseEntity = courseWithModule.mCourse
                val newState = !courseEntity.bookmarked
                academyRepository.setCourseBookmark(courseEntity, newState)
            }
        }
    }
}