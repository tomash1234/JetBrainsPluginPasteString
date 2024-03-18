package cz.tobb.stringpaste

import com.intellij.openapi.options.Configurable
import javax.swing.*

class StringPasteConfigurable : Configurable {

    private var appComponent: AppSettingsComponent? = null

    override fun createComponent(): JComponent {
        appComponent = AppSettingsComponent()
        appComponent!!.load(SettingsState.getInstance())
        return appComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = SettingsState.getInstance()
        val model = appComponent!!.getModel()
        val modified: Boolean = settings.predefinedStrings.size != model.size
        if(!modified){
            for (i in 0 until model.size){
                val element = model.getElementAt(i)
                val element2 = settings.predefinedStrings.get(i)
                if(element.name != element2.name ||
                    element.template != element2.template
                ){
                    return true
                }
            }
        }
        return modified
    }

    override fun apply() {
        val settings = SettingsState.getInstance()
        settings.predefinedStrings = ArrayList()
        for(i in 0 until appComponent!!.getModel().size){
            settings.predefinedStrings += appComponent!!.getModel().getElementAt(i)
        }
    }

    override fun disposeUIResources() {
        appComponent = null
    }

    override fun getDisplayName(): String {
        return "Test description template settings"
    }

    override fun getHelpTopic(): String? {
        // Optional: Provide a help topic ID
        return null
    }

    override fun reset() {
        val settings = SettingsState.getInstance()
        settings.predefinedStrings = ArrayList()
    }
}