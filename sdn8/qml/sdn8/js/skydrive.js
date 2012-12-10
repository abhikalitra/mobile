.pragma library
var initialised = false
var authCode = "7b7b8090-3e2c-9f21-daf9-307dccea2d73"; //current auth code
var clientCode = "00000000440BCC68";
var clientSecret = "46X0RiktuQvBiosensIsIIjGrI5eRHWB";
var authUrl = "https://login.live.com/oauth20_token.srf";
var validToken = false;
var token;
var apiURL = "https://apis.live.net/v5.0/";

function init(pageStack) {

    if (initialised) {
        console.log("already initialised")
        return;
    }

    console.log("initialising...");
    login(pageStack);

}

function login(pageStack) {
    pageStack.push(Qt.resolvedUrl("../AuthWebView.qml"));
    console.log("pushed auth page");
    initialised  = true;
}

function getRootFolderList(callback) {
    if (!validToken) {
        getAccessToken(function() {

           var finalURL = apiURL + "me/skydrive?access_token=" + token.access_token;
           getHttpJson(finalURL, "GET", null , function(json) {
               var responseText = JSON.stringify(json);
               console.debug(responseText);

               //this is always my root folder, now get 1 level inside:)
                console.debug("getting for:" + json.id);
                getFolderListFor(json.id, callback);

           });

        });
    }
}


function getFolderListFor(folderId, callback) {

    if (!validToken) {

        getAccessToken(function() {

           var finalURL = apiURL + folderId + "/files?access_token=" + token.access_token;
           getHttpJson(finalURL, "GET", null , function(json) {                           
               var responseText = JSON.stringify(json);
               console.debug(responseText);
               callback(json);
           });

        });
    }
    else {

        var finalURL = apiURL + folderId + "/files?access_token=" + token.access_token;
        getHttpJson(finalURL, "GET", null , function(json) {
            var responseText = JSON.stringify(json);
            console.debug(responseText);
            callback(json);
        });

    }
}

function updateFolderView(folderid, skyDriveFolderModel) {

    if (folderid === "ROOT") {

        getRootFolderList(function(result) {
            updateModel(result, skyDriveFolderModel);
        });

    }
    else {
        getFolderListFor(folderid, function(result) {
             updateModel(result, skyDriveFolderModel);
        })
    }

}

function updateModel(result, skyDriveFolderModel) {
    skyDriveFolderModel.clear();
    console.debug("data length:" + result.data.length);
    if (result.data.length > 0) {
        for (var i=0; i < result.data.length; i++) {
            var modelObj = new Object();
            modelObj.folderid = result.data[i].id;
            modelObj.name = result.data[i].name;
            modelObj.type = result.data[i].type;
            skyDriveFolderModel.append(modelObj);
        }
    }
}

function getUserInfo() {

    if (!validToken) {
        getAccessToken(function() {

           var finalURL = apiURL + "me?access_token=" + token.access_token;
           getHttpJson(finalURL, "GET", null , function(json) {
               var responseText = JSON.stringify(json);
               console.debug(responseText);
           });

        });
    }

}

function getAccessToken(callback) {

    var params = "client_id=" + clientCode +
            "&redirect_uri=" + "http://www.abhikalitra.com/sdn8" +
            "&client_secret=" + clientSecret +
            "&code=" + authCode +
            "&grant_type=authorization_code";

    getHttpJson(authUrl, "POST", params, function(json) {
        var responseText = JSON.stringify(json);
        validToken  = true;
        token = json;
        console.debug(responseText);
        console.debug("token:" + token.access_token);
        if (callback) {
            callback();
        }
    });
}

function getHttpJson(request_url, type, params, callback)
{
    var request = new XMLHttpRequest();

    console.log("Sending request type...", type)
    console.log("Sending request...", request_url)
    request.open(type, request_url, true);

    request.onreadystatechange = function () {

        console.log("state:" + request.readyState);
        console.log("status:" + request.status);
        if (request.readyState == 4 && request.status == 200) {
            var json = JSON.parse(request.responseText);
            callback(json);
        }

        if (request.readyState == 4 && request.status != 200) {
            console.log("seems some issue:" + request.responseText);
        }
    }

    if (type === "POST" && params) {
        request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        console.log("setting params: "+ params);
        request.send(params);
    }
    else {
        request.send();
    }

}
