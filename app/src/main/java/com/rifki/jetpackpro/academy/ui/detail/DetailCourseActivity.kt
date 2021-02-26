package com.rifki.jetpackpro.academy.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rifki.jetpackpro.academy.R
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity
import com.rifki.jetpackpro.academy.databinding.ActivityDetailCourseBinding
import com.rifki.jetpackpro.academy.databinding.ContentDetailCourseBinding
import com.rifki.jetpackpro.academy.ui.reader.CourseReaderActivity
import com.rifki.jetpackpro.academy.viewmodel.ViewModelFactory
import com.rifki.jetpackpro.academy.vo.Status

class DetailCourseActivity : AppCompatActivity() {

    private var _activityDetailCourseBinding: ActivityDetailCourseBinding? = null
    private val detailBinding get() = _activityDetailCourseBinding
    private val contentBinding get() = _activityDetailCourseBinding?.detailContent

    private lateinit var viewModel: DetailCourseViewModel
    private var menu: Menu? = null

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityDetailCourseBinding = ActivityDetailCourseBinding.inflate(layoutInflater)
        setContentView(detailBinding?.root)

        setSupportActionBar(detailBinding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailAdapter = DetailCourseAdapter()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailCourseViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val courseId = extras.getString(EXTRA_COURSE)
            if (courseId != null) {
                viewModel.setSelectedCourse(courseId)

                viewModel.courseModule.observe(this, { courseWithModuleResource ->
                    if (courseWithModuleResource != null) {
                        when (courseWithModuleResource.status) {
                            Status.LOADING -> detailBinding?.progressBar?.visibility = View.VISIBLE
                            Status.SUCCESS -> if (courseWithModuleResource.data != null) {
                                detailBinding?.progressBar?.visibility = View.GONE
                                detailBinding?.content?.visibility = View.VISIBLE

                                detailAdapter.setModules(courseWithModuleResource.data.mModules)
                                detailAdapter.notifyDataSetChanged()
                                populateCourse(courseWithModuleResource.data.mCourse)
                            }
                            Status.ERROR -> {
                                detailBinding?.progressBar?.visibility = View.GONE
                                Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }

        with(contentBinding?.rvModule) {
            this?.isNestedScrollingEnabled = false
            this?.layoutManager = LinearLayoutManager(this@DetailCourseActivity)
            this?.setHasFixedSize(true)
            this?.adapter = detailAdapter
            val dividerItemDecoration = DividerItemDecoration(this?.context, DividerItemDecoration.VERTICAL)
            this?.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun populateCourse(courseEntity: CourseEntity) {
        contentBinding?.textTitle?.text = courseEntity.title
        contentBinding?.textDescription?.text = courseEntity.description
        contentBinding?.textDate?.text = resources.getString(R.string.deadline_date, courseEntity.deadline)

        contentBinding?.imagePoster?.let {
            Glide.with(this)
                .load(courseEntity.imagePath)
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error))
                .into(it)
        }

        contentBinding?.btnStart?.setOnClickListener {
            val intent = Intent(this@DetailCourseActivity, CourseReaderActivity::class.java)
            intent.putExtra(CourseReaderActivity.EXTRA_COURSE_ID, courseEntity.courseId)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        viewModel.courseModule.observe(this, { courseWithModule ->
            if (courseWithModule != null)  {
                when (courseWithModule.status) {
                    Status.LOADING -> detailBinding?.progressBar?.visibility = View.VISIBLE
                    Status.SUCCESS -> if (courseWithModule.data != null) {
                        detailBinding?.progressBar?.visibility = View.GONE
                        val state = courseWithModule.data.mCourse.bookmarked
                        setBookmarkState(state)
                    }
                    Status.ERROR -> {
                        detailBinding?.progressBar?.visibility = View.GONE
                        Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==  R.id.action_bookmark) {
            viewModel.setBookmark()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBookmarkState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmarked_white)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_white)
        }
    }
}