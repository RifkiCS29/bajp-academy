package com.rifki.jetpackpro.academy.ui.bookmark

import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity

interface BookmarkFragmentCallback {
    fun onShareClick(course: CourseEntity)
}
