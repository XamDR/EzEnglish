package drm.ezenglish.entities

data class Quiz(val content: String = "", val ranges: List<SelectionRange> = emptyList())