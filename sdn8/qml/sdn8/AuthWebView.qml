// import QtQuick 1.0 // to target S60 5th Edition or Maemo 5
import QtQuick 1.1
import QtWebKit 1.0
import com.nokia.symbian 1.1

Page {

    id: authPage

    Column {
        id: webBrowser
        property string urlString : "https://login.live.com/oauth20_authorize.srf?client_id=00000000440BCC68&scope=wl.basic%20wl.skydrive&response_type=code&redirect_uri=http://www.abhikalitra.com/sdn8"
        anchors.fill: parent

        ProgressBar {
            value: authView.progress
            width: parent.width
        }

        WebView {
                id: authView
                url: webBrowser.urlString
                height: 600
                width: parent.width
                onLoadStarted: console.log("started:" + authView.url)
                onLoadFinished: console.log("finished:" + authView.url)
                onLoadFailed: console.log("failed:" + authView.url)
        }

    }

    tools: toolBarLayout
}
