package com.example.multiplechoicesrs.logic

import com.example.multiplechoicesrs.ext.isNumber
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionData

object NumberAwareStringComparator: Comparator<String> {
    override fun compare(string1: String, string2: String): Int {
        val splitString1 = splitStringByNumbers(string1)
        val splitString2 = splitStringByNumbers(string2)

        var index = 0
        while (index < splitString1.size && index < splitString2.size) {
            val toCompare1 = splitString1[index]
            val toCompare2 = splitString2[index]

            val comparison = if (toCompare1.isNumber() && toCompare2.isNumber()) {
                toCompare1.toInt().compareTo(toCompare2.toInt())
            } else {
                toCompare1.compareTo(toCompare2)
            }

            if (comparison != 0) {
                return comparison
            }

            index++
        }

        return splitString1.size.compareTo(splitString2.size)
    }

    private fun splitStringByNumbers(toSplit: String): List<String> {
        val result = mutableListOf<String>()
        val regex = "\\d+".toRegex()
        var lastIndex = 0

        regex.findAll(toSplit).forEach { matchResult ->
            if (matchResult.range.first > lastIndex) {
                result.add(toSplit.substring(lastIndex, matchResult.range.first))
            }

            result.add(matchResult.value)
            lastIndex = matchResult.range.last + 1
        }

        if (lastIndex < toSplit.length) {
            result.add(toSplit.substring(lastIndex))
        }

        return result
    }
}

object AnalysisQuestionSourceComparator: Comparator<AnalysisQuestionData> {
    override fun compare(data1: AnalysisQuestionData, data2: AnalysisQuestionData): Int {
        return NumberAwareStringComparator.compare(
            data1.question.source,
            data2.question.source
        )
    }
}

object AnalysisQuestionNumWrongComparator: Comparator<AnalysisQuestionData> {
    override fun compare(data1: AnalysisQuestionData, data2: AnalysisQuestionData): Int {
        val compareTo = data1.numIncorrect.compareTo(data2.numIncorrect)

        return if (compareTo != 0) {
            compareTo
        } else {
            AnalysisQuestionSourceComparator.compare(data1, data2)
        }
    }
}

object AnalysisQuestionRatioCorrectComparator: Comparator<AnalysisQuestionData> {
    override fun compare(data1: AnalysisQuestionData, data2: AnalysisQuestionData): Int {
        val compareTo = data1.ratioCorrect.compareTo(data2.ratioCorrect)

        return if (compareTo != 0) {
            compareTo
        } else {
            AnalysisQuestionSourceComparator.compare(data1, data2)
        }
    }
}