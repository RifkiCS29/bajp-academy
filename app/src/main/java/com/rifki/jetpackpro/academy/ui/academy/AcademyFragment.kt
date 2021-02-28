package com.rifki.jetpackpro.academy.ui.academy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifki.jetpackpro.academy.databinding.FragmentAcademyBinding
import com.rifki.jetpackpro.academy.viewmodel.ViewModelFactory
import com.rifki.jetpackpro.academy.vo.Status

class AcademyFragment : Fragment() {

    private var _fragmentAcademyBinding: FragmentAcademyBinding? = null
    private val binding get() = _fragmentAcademyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentAcademyBinding = FragmentAcademyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[AcademyViewModel::class.java]

            val academyAdapter = AcademyAdapter()

            viewModel.getCourses().observe(viewLifecycleOwner, { courses ->
                if (courses != null) {
                    when(courses.status) {
                        Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding?.progressBar?.visibility = View.GONE
                            academyAdapter.submitList(courses.data)
                        }
                        Status.ERROR -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(binding?.rvAcademy) {
                this?.layoutManager = LinearLayoutManager(context)
                this?.setHasFixedSize(true)
                this?.adapter = academyAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentAcademyBinding = null
    }
}