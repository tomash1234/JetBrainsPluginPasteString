package cz.tobb.stringpaste

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.project.Project

class PasteStringGroup : ActionGroup() {

    override fun getChildren(p0: AnActionEvent?): Array<AnAction> {
        return SettingsState.getInstance().predefinedStrings.map {
            PasteStringAction(it) }.toTypedArray()
    }
}

class PasteStringAction(private val item: PredefinedItem) : AnAction(item.name) {
    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project: Project = e.getRequiredData(CommonDataKeys.PROJECT)
        val document = editor?.document
        val primaryCaret: Caret = editor!!.caretModel.primaryCaret
        WriteCommandAction.runWriteCommandAction(project, Runnable
        {
            val indentation = editor.caretModel.logicalPosition.column
            var indentedString = item.template.lines().joinToString("\n") { line ->
                " ".repeat(indentation) + line
            }
            val offset = document!!.getLineStartOffset(primaryCaret.logicalPosition.line)
            indentedString = indentedString.trimEnd()
            document.insertString(offset, indentedString)
        })
    }
}