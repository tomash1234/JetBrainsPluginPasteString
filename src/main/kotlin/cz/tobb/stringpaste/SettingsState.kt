package cz.tobb.stringpaste

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "cz.tobb.stringpaste.SettingsState",
    storages = [Storage("StringPaste.xml")]
)
class SettingsState : PersistentStateComponent<SettingsState> {

    var predefinedStrings : List<PredefinedItem> = ArrayList<PredefinedItem>()

    companion object {
        fun getInstance(): SettingsState {
            return ApplicationManager.getApplication().getService(SettingsState::class.java)
        }
    }

    override fun getState(): SettingsState {
        return this
    }

    override fun loadState(state: SettingsState) {
        XmlSerializerUtil.copyBean(state, this);
    }
}