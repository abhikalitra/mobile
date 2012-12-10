import QtQuick 1.1
import com.nokia.symbian 1.1
import "js/skydrive.js" as SkyDrive

PageStackWindow {

    id: window
    initialPage: MainPage {}
    showStatusBar: true
    showToolBar: true
    Settings{ id: settings }

    ToolBarLayout {
        id: toolBarLayout
        ToolButton {
            flat: true
            iconSource: "toolbar-back"
            onClicked: window.pageStack.depth <= 1 ? Qt.quit() : window.pageStack.pop()
        }
        ToolButton {
            flat: true
            iconSource: "toolbar-settings"
            onClicked: window.pageStack.push(Qt.resolvedUrl("SettingsView.qml"))
        }
        ToolButton {
            flat: true
            iconSource: "toolbar-refresh"
            onClicked: SkyDrive.getFolderList();
        }
        ToolButton {
            flat: true
            iconSource: "toolbar-home"
            onClicked: SkyDrive.init(window.pageStack);
        }
    }

}
