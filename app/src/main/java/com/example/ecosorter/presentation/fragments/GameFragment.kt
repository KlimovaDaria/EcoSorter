package com.example.ecosorter.presentation.fragments

import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecosorter.databinding.FragmentGameBinding
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
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
        observeViewModel()
        setupDragAndDrop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        gameFragmentViewModel.question.observe(viewLifecycleOwner) {
            setQuestion(it)
        }
        gameFragmentViewModel.timerStr.observe(viewLifecycleOwner) {
            setTimer(it)
        }
        gameFragmentViewModel.gameResult.observe(viewLifecycleOwner){
            launchGameFinishedFragment(it)
        }
        gameFragmentViewModel.progressAnswers.observe(viewLifecycleOwner){
            setAnswersProgressText(it)
        }
        gameFragmentViewModel.countOfQuestions.observe(viewLifecycleOwner){
            setCountQuestionsText(it)
        }
        gameFragmentViewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            binding.tvRightAnswersPercent.text = it
        }
    }

    private fun setCountQuestionsText(string: String?) {
        binding.tvCountQuestions.text = string
    }

    private fun setAnswersProgressText(string: String?) {
        binding.tvAnswersProgress.text = string
    }


    private fun setupDragAndDrop() {
        binding.tvTrashItem.setOnLongClickListener { view ->
            val shadowBuilder = FullyOpaqueShadowBuilder(view)
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
        binding.tvTrashItem.text = question.trashItem.name
    }

    private fun setTimer(timerStr: String) {
        binding.tvTimer.text = timerStr
    }

    private fun launchGameFinishedFragment(gameResult: GameResult){
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameResultFragment(gameResult))
    }

    companion object {

    }
}