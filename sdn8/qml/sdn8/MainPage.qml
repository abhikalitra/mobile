import QtQuick 1.1
import com.nokia.symbian 1.1
import "js/skydrive.js" as SkyDrive
import "js/testdata.js" as TestData

Page {

    id: mainPage
    tools: toolBarLayout
    property string pagefolderid : "ROOT"

    ListView {
        id : skyDriverFolderView
        anchors.fill: parent
        model : ListModel { id:skyDriveFolderModel }
        delegate: folderViewDelegate
        highlight: Rectangle { color: "lightsteelblue"; radius: 5 }
        focus:true
    }

    Component {
        id: folderViewDelegate
        Item {
            width: skyDriverFolderView.width; height: 60
            Column {
                id : texts
                Text {
                    text: '<b>Name:</b> ' + name
                    color: "white"
                }
                Text {
                    text: '<b>FolderId:</b> ' + folderid
                    color: "white"
                }
            }

            Image {
                 source: privateStyle.toolBarIconPath("toolbar-next")
                 anchors.right: parent.right;
                 anchors.verticalCenter: parent.verticalCenter
            }

            MouseArea {
                id:mouseArea
                anchors.fill: texts
                onClicked: {
                    if (type === "folder") {
                        mainPage.openFolder(folderid)
                    }
                    else {
                        console.log("types other than folder not implemented!" + type);
                    }
                }
            }
        }
    }

    Timer {
        interval: 10
        running: true
        repeat: false
        onTriggered: {
            //TestData.updateFolderView(pagefolderid, skyDriveFolderModel)
            SkyDrive.updateFolderView(pagefolderid, skyDriveFolderModel);
        }
    }

    function openFolder(currentid) {
        var componentPage = Qt.createComponent("MainPage.qml");
        if (componentPage.status === Component.Ready) {
            pageStack.push(componentPage, {pagefolderid : currentid});
        }
    }
}
