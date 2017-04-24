/**
 * Created by zhaomingyu on 15/11/26.
 */
"use strict";
var std = {
    position: 0,
    table1: false,
    table2: false
};

$(document).ready(function() {
    var obj1 = {
        tableName: "todo"
    };
    var obj2 = {
        tableName: "staff"
    };
    for (var i = 1; i <= 3; ++i) {
        $("#todoTitle" + i).click(obj1, sortTable);
        $("#staffTitle" + i).click(obj2, sortTable)
    }
});

function sortTable(event) {
    var data = [];
    var tableName = event.data.tableName;
    var sortBy = "";
    if (tableName == "todo") {
        sortBy = this.id.slice(9);
    } else {
        sortBy = this.id.slice(10);
    }
    //alert(tableName);
    for (var j = 1; j <= 3; ++j) {
        var tmpStore = [];
        for (var i = 0; i < 3; ++i) {
            var tmp = $("#" + tableName).find("tr:eq(" + j + ") td:eq(" + i + ")").text();
            tmpStore.push(tmp);
        }
        data.push(tmpStore);
    }

    sortMain(data, sortBy, tableName);

    for (j = 1; j <= 3; ++j) {
        for (i = 0; i < 3; ++i) {
            tmp = $("#" + tableName).find("tr:eq(" + j + ") td:eq(" + i + ")").text(data[j-1][i]);
        }
    }
}

function sortMain(data, sortBy, tableName) {
    if (sortBy == std.position && std.table1) {
        std.table1 = false;
        std.position = sortBy;
        data.sort((function (a, b) {
            return a[sortBy-1] < b[sortBy-1];
        }));
        clearAll();
        if (tableName == "todo") {
            $("#" + tableName + " #todoTitle" + sortBy).addClass("todoTitle" + sortBy + "Down");
        } else {
            $("#" + tableName + " #staffTitle" + sortBy).addClass("staffTitle" + sortBy + "Down");
        }
    } else {
        std.table1 = true;
        std.position = sortBy;
        data.sort((function (a, b) {
            return a[sortBy-1] > b[sortBy-1];
        }));
        clearAll();
        if (tableName == "todo") {
            $("#" + tableName + " #todoTitle" + sortBy).addClass("todoTitle" + sortBy + "Up");
        } else {
            $("#" + tableName + " #staffTitle" + sortBy).addClass("staffTitle" + sortBy + "Up");
        }
    }
}

function clearAll() {
    for (var i = 1; i <= 3; ++i) {
        $(".todoTitle" + i + "Up").removeClass("todoTitle" + i + "Up");
        $(".todoTitle" + i + "Down").removeClass("todoTitle" + i + "Down");
        $(".staffTitle" + i + "Up").removeClass("staffTitle" + i + "Up");
        $(".staffTitle" + i + "Down").removeClass("staffTitle" + i + "Down");
    }
}