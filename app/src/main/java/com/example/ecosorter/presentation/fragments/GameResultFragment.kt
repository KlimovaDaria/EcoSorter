package com.example.ecosorter.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecosorter.R
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecosorter.databinding.FragmentGameResultBinding
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.presentation.adapters.WrongAnswersAdapter
import com.example.ecosorter.presentation.viewmodels.GameResultFragmentViewModel
import com.example.ecosorter.presentation.viewmodels.GameResultFragmentViewModelFactory
import kotlin.jvm.java


class GameResultFragment : Fragment() {

    private val args by navArgs<GameResultFragmentArgs>()

    private val viewModelFactory by lazy {
        GameResultFragmentViewModelFactory(requireActivity().application, args.gameResult)
    }

    private val gameResultFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameResultFragmentViewModel::class.java]
    }

    private var _binding: FragmentGameResultBinding? = null
    private val binding: FragmentGameResultBinding
        get() = _binding ?: throw RuntimeException("FragmentGameResultBinding==null")

    private lateinit var gameResult: GameResult

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameResult = args.gameResult
        gameResultFragmentViewModel.highScore.observe(viewLifecycleOwner){
            binding.tvHighScore.text = String.format(
                getString(R.string.high_score),
                it
            )
        }
        setViews()
        setOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViews() {
        with(binding) {
            tvGameResult.text =
                if (gameResult.winner) getString(R.string.you_are_win)
                else getString(R.string.you_are_lose)
            tvAnswersProgress.text = String.format(
                getString(R.string.progress_answers),
                gameResult.countOfRightAnswers,
                gameResult.level.minCountOfRightAnswers
            )
            tvRightAnswersPercent.text = String.format(
                getString(R.string.percent_right_answers),
                gameResult.percentOfRightAnswers,
                gameResult.level.minPercentOfRightAnswers
            )
            emojiResult.setImageResource(getSmileResId())
            if (gameResult.wrongAnswersList.isEmpty()) {
                tvWrongTitle.visibility = View.GONE
                rvWrongAnswers.visibility = View.GONE
            } else {
                tvWrongTitle.visibility = View.VISIBLE
                rvWrongAnswers.visibility = View.VISIBLE
                rvWrongAnswers.layoutManager = LinearLayoutManager(requireContext())
                rvWrongAnswers.adapter = WrongAnswersAdapter(gameResult.wrongAnswersList, requireContext())
            }
        }
    }

    private fun setOnClickListeners() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun getSmileResId(): Int {
        with(gameResult) {
            return if (winner) {
                R.drawable.clean_planet
            } else {
                R.drawable.pollution_planet
            }
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}