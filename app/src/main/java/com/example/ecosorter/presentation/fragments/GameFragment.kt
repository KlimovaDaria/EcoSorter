package com.example.ecosorter.presentation.fragments

import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecosorter.R
import com.example.ecosorter.databinding.FragmentGameBinding
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
import com.example.ecosorter.presentation.helpers.SoundHelper
import com.example.ecosorter.presentation.viewmodels.GameFragmentViewModel
import com.example.ecosorter.presentation.viewmodels.GameFragmentViewModelFactory

class GameFragment : Fragment() {

    class FullyOpaqueShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint)
            shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            view.draw(canvas)
        }
    }

    private val args by navArgs<GameFragmentArgs>()

    private lateinit var soundHelper: SoundHelper

    private val viewModelFactory by lazy {
        GameFragmentViewModelFactory(requireActivity().application, args.level)
    }

    private val gameFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameFragmentViewModel::class.java]
    }

    private val options by lazy {
        mutableListOf<LinearLayout>().apply {
            with(binding) {
                add(containerOption1)
                add(containerOption2)
                add(containerOption3)
                add(containerOption4)
            }
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding==null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        soundHelper = SoundHelper(requireContext())
        observeViewModel()
        setupDragAndDrop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundHelper.release()
        _binding = null
    }

    private fun observeViewModel() {
        gameFragmentViewModel.question.observe(viewLifecycleOwner) {
            setQuestion(it)
        }
        gameFragmentViewModel.timerStr.observe(viewLifecycleOwner) {
            setTimer(it)
        }
        gameFragmentViewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
        gameFragmentViewModel.countOfQuestions.observe(viewLifecycleOwner) {
            setCountQuestionsText(it)
        }
        gameFragmentViewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            setProgressBarForRightAnswers(it)
        }
        gameFragmentViewModel.globalStreak.observe(viewLifecycleOwner) {
            setCountCurrentStreak(it)
        }
        gameFragmentViewModel.progressAnswers.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.text = it
        }
    }

    private fun setCountCurrentStreak(i: Int?) {
        binding.tvCountCurrentStreak.text = getString(
            R.string.current_global_streak,
            i
        )
    }

    private fun setProgressBarForRightAnswers(i: Int) {
        binding.progressBar.setProgress(i, true)
        binding.tvRightAnswersPercent.text = getString(
            R.string.success_progress_bar,
            i
        )
        binding.tvTargetPercentLabel.text = getString(
            R.string.target_progress_bar,
            gameFragmentViewModel.level.minPercentOfRightAnswers
        )
    }

    private fun setCountQuestionsText(string: String?) {
        binding.tvCountQuestions.text = string
    }

    private fun setupDragAndDrop() {
        binding.containerTrashItem.setOnLongClickListener { view ->
            val currentQuestion = gameFragmentViewModel.question.value
            val shadowImage = ImageView(requireContext()).apply {
                val imageResId = getDrawableIdByName(currentQuestion?.trashItem?.imageName ?: "")
                setImageResource(imageResId)
                layoutParams = ViewGroup.LayoutParams(300, 300)
            }
            shadowImage.measure(
                View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY)
            )
            shadowImage.layout(0, 0, shadowImage.measuredWidth, shadowImage.measuredHeight)
            val shadowBuilder = FullyOpaqueShadowBuilder(shadowImage)
            view.startDragAndDrop(null, shadowBuilder, view, 0)
            true
        }

        options.forEach { binContainer ->
            binContainer.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start()
                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                        val categoryStr = view.tag.toString()
                        val categoryEnum = TrashCategory.valueOf(categoryStr)
                        soundHelper.playDrop()
                        gameFragmentViewModel.chooseAnswer(categoryEnum)
                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setQuestion(question: Question) {
        binding.tvTrashItem.alpha = 0f
        binding.tvTrashItem.translationY = 40f

        binding.tvTrashItem.text = question.trashItem.name
        val imageResId = getDrawableIdByName(question.trashItem.imageName)
        binding.ivTrashItem.setImageResource(imageResId)

        binding.tvTrashItem.animate()
            .alpha(1.0f)
            .translationY(0f)
            .setDuration(350)
            .start()

        binding.ivTrashItem.animate()
            .alpha(1.0f)
            .translationY(0f)
            .setDuration(350)
            .start()
    }

    private fun setTimer(timerStr: String) {
        binding.tvTimer.text = timerStr
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameResultFragment(
                gameResult
            )
        )
    }

    private fun getDrawableIdByName(imageName: String): Int {
        return requireContext().resources.getIdentifier(
            imageName,
            "drawable",
            requireContext().packageName
        )
    }

}