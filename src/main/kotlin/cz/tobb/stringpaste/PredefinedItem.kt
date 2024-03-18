package cz.tobb.stringpaste

class PredefinedItem(actionName: String = "", content: String = "") {
    var name : String = actionName
    var template : String = content

    override fun toString(): String {
        return name
    }
}