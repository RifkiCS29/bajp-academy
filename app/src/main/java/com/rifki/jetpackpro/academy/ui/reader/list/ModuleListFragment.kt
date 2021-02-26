package com.rifki.jetpackpro.academy.ui.reader.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifki.jetpackpro.academy.data.source.local.entity.ModuleEntity
import com.rifki.jetpackpro.academy.databinding.FragmentModuleListBinding
import com.rifki.jetpackpro.academy.ui.reader.CourseReaderActivity
import com.rifki.jetpackpro.academy.ui.reader.CourseReaderCallback
import com.rifki.jetpackpro.academy.ui.reader.CourseReaderViewModel
import com.rifki.jetpackpro.academy.viewmodel.ViewModelFactory
import com.rifki.jetpackpro.academy.vo.Status

class ModuleListFragment : Fragment(), MyAdapterClickListener {

    private var _fragmentModuleListBinding: FragmentModuleListBinding? = null
    private val binding get() = _fragmentModuleListBinding

    private lateinit var moduleListAdapter: ModuleListAdapter
    private lateinit var courseReaderCallback: CourseReaderCallback

    private lateinit var viewModel: CourseReaderViewModel

    companion object {
        val TAG: String = ModuleListFragment::class.java.simpleName
        fun newInstance(): ModuleListFragment = ModuleListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentModuleListBinding = FragmentModuleListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), factory)[CourseReaderViewModel::class.java]
        moduleListAdapter = ModuleListAdapter(this)

        viewModel.modules.observe(viewLifecycleOwner, { moduleEntities ->
            if (moduleEntities != null) {
                when (moduleEntities.status)  {
                    Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                    Status.SUCCESS -> {
                        binding?.progressBar?.visibility = View.GONE
                        populateRecyclerView(moduleEntities.data as List<ModuleEntity>)
                    }
                    Status.ERROR -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        courseReaderCallback = context as CourseReaderActivity
    }

    override fun onItemClicked(position: Int, moduleId: String) {
        courseReaderCallback.moveTo(position, moduleId)
        viewModel.setSelectedModule(moduleId)
    }

    private fun populateRecyclerView(modules: List<ModuleEntity>) {
        binding?.progressBar?.visibility = View.GONE
        moduleListAdapter.setModules(modules)
        with(binding?.rvModule) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = moduleListAdapter
            val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            this?.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentModuleListBinding = null
    }

}