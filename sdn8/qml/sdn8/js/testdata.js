.pragma library

function updateFolderView(folderid, model) {

    console.debug("updating for:" + folderid)
    var id = Math.random() * 10;
    model.clear();
    for (var i=0;i<10;i++) {
        var obj = new Object();
        obj.folderid = "" + Math.random();
        obj.name = "Name." + obj.folderid;
        obj.type= "folder"
        model.append(obj);
    }
}
