package com.infruit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.infruit.R
import com.infruit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Glide.with(binding.profilePhoto).load(R.drawable.profile_blank).circleCrop().into(binding.profilePhoto)

        val sliderImageList = ArrayList<SlideModel>()

        sliderImageList.add(SlideModel(R.drawable.special_slider_1))
        sliderImageList.add(SlideModel(R.drawable.special_slider_2))

        binding.imageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE)
        binding.imageSlider.setImageList(sliderImageList, ScaleTypes.CENTER_CROP)

        return root
    }
}