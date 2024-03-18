package cz.tobb.stringpaste

import com.intellij.ui.components.*
import com.intellij.ui.util.maximumWidth
import com.intellij.ui.util.preferredHeight
import com.intellij.ui.util.preferredWidth
import org.jdesktop.swingx.HorizontalLayout
import org.jdesktop.swingx.VerticalLayout
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.ListSelectionEvent


class AppSettingsComponent {

    private val panel: JPanel = JPanel(VerticalLayout(4))
    private val txName = JBTextField(50)
    private val txContent = JTextArea(15, 50)
    private var listModel: DefaultListModel<PredefinedItem> = DefaultListModel<PredefinedItem>()
    private var listView : JBList<PredefinedItem> = JBList(listModel)

    init {
        val listScrollPanel = JBScrollPane(listView)
        txContent.font = txName.font
        txContent.margin = txName.margin
        txContent.setBorder(BorderFactory.createCompoundBorder(
            txContent.border,
            BorderFactory.createEmptyBorder(1, 1, 1,1)));
        txContent.lineWrap = true
        txContent.maximumWidth = 240
        listView.selectionMode = ListSelectionModel.SINGLE_SELECTION;
        listScrollPanel.preferredHeight = 140
        panel.add(listScrollPanel)
        val buttonPanel = JPanel(HorizontalLayout())
        val butAdd = JButton("Add")
        val butDel = JButton("Delete")
        buttonPanel.add(butAdd)
        buttonPanel.add(butDel)
        panel.add(buttonPanel)

        val actionNamePanel = JPanel(HorizontalLayout(4))
        val labelName = JBLabel("Name")
        labelName.preferredWidth = 45
        actionNamePanel.add(labelName)
        actionNamePanel.add(txName)

        val actionContentPanel = JPanel(HorizontalLayout(4))
        val labelContent = JBLabel("Content")
        labelContent.preferredWidth = 45
        actionContentPanel.add(labelContent)
        actionContentPanel.add(txContent)

        panel.add(actionNamePanel)
        panel.add(actionContentPanel)

        butAdd.addActionListener {addItem()}
        butDel.addActionListener {delItem()}

        listView.addListSelectionListener { e: ListSelectionEvent? ->
            if (e != null) {
                selectItem(e)
            }
        }
        txName.document.addDocumentListener(SimpleDocumentListener{saveContent(true)})
        txContent.document.addDocumentListener(SimpleDocumentListener{saveContent(false)})
    }

    private fun saveContent(name: Boolean){
        if(listView.selectedIndex == -1){
            return
        }
        val current = listModel.get(listView.selectedIndex)
        if(name){
            current.name = txName.text
        }else{
            current.template = txContent.text
        }
    }

    fun load(appState: SettingsState) {
        this.listModel.clear()
        this.listModel.addAll(appState.predefinedStrings)
    }

    fun getModel(): ListModel<PredefinedItem> {
        return this.listModel
    }

    private fun selectItem(event: ListSelectionEvent) {
        if(event.valueIsAdjusting){
            return
        }
        if(listView.selectedIndex  == -1){
            clearStringView()
            return
        }
        displayCurrent(listModel.get(listView.selectedIndex))
    }

    private fun displayCurrent(predefinedItem: PredefinedItem){
        SwingUtilities.invokeLater {
            txName.text = predefinedItem.name
            txContent.text = predefinedItem.template
            txName.setEnabled(true)
            txContent.setEnabled(true)
        }
    }

    private fun clearStringView(){
        SwingUtilities.invokeLater {
            txName.text = ""
            txContent.text = ""
            txName.setEnabled(false)
            txContent.setEnabled(false)
            listView.clearSelection()
        }
    }

    private fun addItem(){
        SwingUtilities.invokeLater {
            val newContent = PredefinedItem("New item", "")
            listModel.addElement(newContent)
            listView.selectedIndex = listModel.size() - 1;
        }
    }

    private fun delItem(){
        SwingUtilities.invokeLater {
            if(listView.selectedIndex != -1){
                listModel.remove(listView.selectedIndex)
                listView.clearSelection()
            }
        }
    }

    fun getPanel(): JPanel {
        return panel
    }
}

class SimpleDocumentListener(runnable: Runnable): DocumentListener {

    private var runnableVal : Runnable = runnable

    override fun insertUpdate(e: DocumentEvent?) {
        runnableVal.run()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        runnableVal.run()
    }

    override fun changedUpdate(e: DocumentEvent?) {
        runnableVal.run()
    }

}