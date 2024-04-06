package id.bobipermanasandi.githubuser.ui.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.bobipermanasandi.githubuser.adapter.FollowAdapter
import id.bobipermanasandi.githubuser.data.remote.response.FollowResponseItem
import id.bobipermanasandi.githubuser.databinding.FragmentFollowBinding
import id.bobipermanasandi.githubuser.factory.ViewModelFactory


class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FollowViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        viewModel.getDataFollow(username!!, position!!)

        setupRecyclerView()
        setupFollowData()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)
    }

    private fun setupFollowData() {
        viewModel.follow.observe(viewLifecycleOwner) { setupAdapter(it) }
        viewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
    }

    private fun setupAdapter(data: List<FollowResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(data)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}