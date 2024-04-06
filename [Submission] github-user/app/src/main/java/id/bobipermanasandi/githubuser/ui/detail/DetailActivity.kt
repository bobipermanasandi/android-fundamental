package id.bobipermanasandi.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.bobipermanasandi.githubuser.R
import id.bobipermanasandi.githubuser.adapter.SectionsPagerAdapter
import id.bobipermanasandi.githubuser.data.local.entity.FavoriteEntity
import id.bobipermanasandi.githubuser.data.remote.response.UserDetailResponse
import id.bobipermanasandi.githubuser.databinding.ActivityDetailBinding
import id.bobipermanasandi.githubuser.factory.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private var username: String? = null
    private var favoriteStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent != null && intent.hasExtra(EXTRA_USERNAME)) {
            username = intent.getStringExtra(EXTRA_USERNAME)
            detailViewModel.getDetailUser(username!!)
        }

        setupUserData()
        setupViewPager()

        detailViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnFavorite.setOnClickListener {
            if(username != null) {
                val user = detailViewModel.user.value
                if(user != null) {
                    val favorite = FavoriteEntity(username!!, user.avatarUrl)
                    if (favoriteStatus) {
                        detailViewModel.deleteFavorite(favorite)
                        Toast.makeText(this, getString(R.string.remove_favorite_user), Toast.LENGTH_SHORT).show()
                    } else {
                        detailViewModel.addFavorite(favorite)
                        Toast.makeText(this, getString(R.string.add_favorite_user), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (username != null) {
            detailViewModel.getFavoriteUserByUsername(username!!).observe(this){ favUser ->
                favoriteStatus = if (favUser != null){
                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite_white)
                    true
                } else {
                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border_white)
                    false
                }
            }
        }
    }

    private fun setupUserData() {
        detailViewModel.user.observe(this) { setUserDetail(it) }
        detailViewModel.isLoading.observe(this) { showLoading(it) }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username!!)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setUserDetail(userDetail: UserDetailResponse) {
        with(this) {
            Glide.with(this@DetailActivity)
                .load(userDetail.avatarUrl)
                .into(binding.ivUserImage)

            binding.tvUsername.text = userDetail.login

            if (userDetail.name.isNullOrEmpty() || userDetail.name.isBlank()) {
                binding.tvFullName.visibility = View.INVISIBLE

                val usernameLayoutParams =
                    binding.tvUsername.layoutParams as ConstraintLayout.LayoutParams
                usernameLayoutParams.topToTop = binding.ivUserImage.id
                usernameLayoutParams.bottomToBottom = binding.ivUserImage.id
            } else {
                binding.tvFullName.text = userDetail.name
            }

            if(userDetail.bio.isNullOrEmpty()|| userDetail.bio.isBlank()) {
                binding.tvBio.visibility = View.INVISIBLE
                val followersLayoutParams =
                    binding.tvFollowers.layoutParams as ConstraintLayout.LayoutParams
                followersLayoutParams.topToBottom = binding.ivUserImage.id
            }else {
                binding.tvBio.text = userDetail.bio
            }

            binding.tvFollowers.text = getString(R.string.followers, userDetail.followers)
            binding.tvFollowing.text = getString(R.string.following, userDetail.following)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "DetailActivity"
        const val EXTRA_USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }
}