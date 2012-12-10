// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import com.nokia.symbian 1.1

Page {

    anchors.fill: parent
    Column {
        anchors.centerIn: parent

        Text {
            text: "setting page"
            anchors.horizontalCenter: parent.horizontalCenter
        }
    }

    tools: toolBarLayout
}
