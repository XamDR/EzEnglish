package drm.ezenglish.entities

import drm.ezenglish.util.template

class Quiz(private val content: String = "", private val ranges: List<SelectionRange> = emptyList()) {

    fun render(): String {
        val builder = StringBuilder(content)

        for (i in ranges.size - 1 downTo 0) {
            builder.removeRange(ranges[i].start, ranges[i].start + ranges[i].length)
            val length = ranges[i].length
            val replacement = """<input id=""q${i}""
                type =""text"" class = ""editable""
                maxlength=""$length"" 
                style =""width: ${ length * 1.162 } ch;""/>
            """.trimIndent()
            builder.insert(ranges[i].start, replacement)
        }
        return String.format(template, builder)
    }
}