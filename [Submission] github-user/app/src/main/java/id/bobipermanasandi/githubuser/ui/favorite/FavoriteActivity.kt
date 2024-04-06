package id.bobipermanasandi.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.bobipermanasandi.githubuser.adapter.FavoriteAdapter
import id.bobipermanasandi.githubuser.data.local.entity.FavoriteEntity
import id.bobipermanasandi.githubuser.databinding.ActivityFavoriteBinding
import id.bobipermanasandi.githubuser.factory.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFavoriteData()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)
    }

    private fun setupFavoriteData() {
        favoriteViewModel.getAllFavorite().observe(this) { fav ->
            if(fav.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    "Data Favorite Not Found",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            setupAdapter(fav)

        }
    }

    private fun setupAdapter(users: List<FavoriteEntity>) {
        val adapter = FavoriteAdapter()
        adapter.submitList(users)
        binding.rvFavorite.adapter = adapter
    }

}