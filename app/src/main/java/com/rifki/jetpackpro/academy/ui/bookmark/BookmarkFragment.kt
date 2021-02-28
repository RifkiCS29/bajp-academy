package com.rifki.jetpackpro.academy.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rifki.jetpackpro.academy.R
import com.rifki.jetpackpro.academy.data.source.local.entity.CourseEntity
import com.rifki.jetpackpro.academy.databinding.FragmentBookmarkBinding
import com.rifki.jetpackpro.academy.viewmodel.ViewModelFactory

class BookmarkFragment : Fragment(), BookmarkFragmentCallback {

    private var _fragmentBookmarkBinding: FragmentBookmarkBinding? = null
    private val binding get() = _fragmentBookmarkBinding

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentBookmarkBinding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouchHelper.attachToRecyclerView(binding?.rvBookmark)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]

            bookmarkAdapter = BookmarkAdapter(this)

            binding?.progressBar?.visibility = View.VISIBLE
            viewModel.getBookmarks().observe(viewLifecycleOwner, { courses ->
                binding?.progressBar?.visibility = View.GONE
                bookmarkAdapter.submitList(courses)
            })

            binding?.rvBookmark?.layoutManager = LinearLayoutManager(context)
            binding?.rvBookmark?.setHasFixedSize(true)
            binding?.rvBookmark?.adapter = bookmarkAdapter
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val courseEntity = bookmarkAdapter.getSwipedData(swipedPosition)
                courseEntity?.let { viewModel.setBookmark(it) }

                val snackbar = Snackbar.make(view as View, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.message_ok) { v ->
                    courseEntity?.let { viewModel.setBookmark(it) }
                }
                snackbar.show()
            }
        }
    })

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