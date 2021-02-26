package com.rifki.jetpackpro.academy.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifki.jetpackpro.academy.R
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity
import com.rifki.jetpackpro.academy.databinding.FragmentBookmarkBinding
import com.rifki.jetpackpro.academy.viewmodel.ViewModelFactory

class BookmarkFragment : Fragment(), BookmarkFragmentCallback {

    private var _fragmentBookmarkBinding: FragmentBookmarkBinding? = null
    private val binding get() = _fragmentBookmarkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentBookmarkBinding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]

            val bookmarkAdapter = BookmarkAdapter(this)

            binding?.progressBar?.visibility = View.VISIBLE
            viewModel.getBookmarks().observe(viewLifecycleOwner, { courses ->
                binding?.progressBar?.visibility = View.GONE
                bookmarkAdapter.setCourses(courses)
                bookmarkAdapter.notifyDataSetChanged()
            })


            binding?.rvBookmark?.layoutManager = LinearLayoutManager(context)
            binding?.rvBookmark?.setHasFixedSize(true)
            binding?.rvBookmark?.adapter = bookmarkAdapter
        }
    }

    override fun onShareClick(course: CourseEntity) {
        if (activity != null) {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                    .from(requireActivity())
                    .setType(mimeType)
                    .setChooserTitle("Share this App Now.")
                    .setText(resources.getString(R.string.share_text, course.title))
                    .startChooser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBookmarkBinding = null
    }

}