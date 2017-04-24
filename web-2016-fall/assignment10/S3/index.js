var std = {
    xhr: [null, null, null, null, null],
    hasGettingNum: [false, false, false, false, false],   //
    isGettingNum: [false, false, false, false, false],
    isDisable: [false, false, false, false, false],       //  判断对应五个输入是否被灭活
    isInfoBarDisable: true,                               //  判断放结果的圆圈是否被灭活
    sum: 0,                                               //  存储计算结果
    infoBarDisable: function() {                          //  计算isInfoBarDisable的值
        for (var i = 0; i < 5; ++i) {
            if (!std.hasGettingNum[i])  {
                std.isInfoBarDisable = true;
                return;
            }
        }
        std.isInfoBarDisable = false;
    }
};
$(document).ready(function() {
    for (var i = 0; i < 5; ++i) {
        (function(i) {
            $(".button").eq(i).click(function () {
                clickInputButton(i, req);
            });
        }) (i);
    }
    $("#info-bar").click(getSum);
    $("#button").mouseleave(restart);
    $(".apb").click(autoClick);
});
function autoClick() {
    var flag1 = true;
    var flag2= false;
    std.sum = 0;
    for (var i = 0 ; i < 5; ++i) {
        if (std.isGettingNum[i]) {
            flag1 = false;
        }
        if (!std.hasGettingNum[i]) {
            flag2 = true;
        }
    }
    if (flag1 && flag2) {
        for (i = 4; i >= 0; --i) {
            std.isDisable[i] = false;
            (function(i) {
                setTimeout(function () {
                    std.isDisable[i] = false;
                    clickInputButton(i, req, getSum);
                }, 0);
            }) (i);
        }
    }
}
function req(i, callback) {
    std.xhr[i] = $.get("ajax" + i, function (data) {
        if (!std.abort) {
            $(".inputMessage").eq(i).text(data);
            std.sum += parseInt(data);
            std.hasGettingNum[i] = true;
            std.isGettingNum[i] = false;
            for (j = 0; j < 5; ++j) {
                if (!std.hasGettingNum[j] && !std.isGettingNum[j]) {
                    makeChange(j, "clickable");
                }
            }
            std.infoBarDisable();
            changeInfo();
            if (typeof callback == "function") {
                callback();
            }
        }
        std.abort = false;
    });
}
function clickInputButton(i, callback, callback2) {
    var j = 0;
    if (!std.isDisable[i]) {     //  若已灭活,无反应
        std.isGettingNum[i] = true;
        $(".inputMessage").eq(i).addClass("waiting").text("...");
        makeChange(0, 1, 2, 3, 4, "disable");
        callback(i, callback2);
    }
}
function getSum() {  // 求和及显示
    if (!std.isInfoBarDisable) {
        $("#info-bar").text(std.sum);
        std.sum = 0;
        std.isInfoBarDisable = true;
        changeInfo();
    }
}
function changeInfo() {  //  改变info-bar的状态:灭活/激活
    if (std.isInfoBarDisable) {
        $("#info-bar").addClass("disabled").removeClass("cli");
    } else {
        $("#info-bar").removeClass("disabled").addClass("cli");
    }
}
function makeChange() {  //  改变五个输入的状态:灭活/激活
    var i;
    if (arguments[arguments.length-1] == "disable") {
        for (i = 0; i < arguments.length - 1; ++i) {
            std.isDisable[arguments[i]] = true;
            $(".button").eq(arguments[i]).addClass("disabled").removeClass("cli");
        }
    }
    if (arguments[arguments.length-1] == "clickable") {
        for (i = 0; i < arguments.length - 1; ++i) {
            std.isDisable[arguments[i]] = false;
            $(".button").eq(arguments[i]).removeClass("disabled").addClass("cli");
        }
    }
}
function restart() {  //  重启计算器,若存在发出的Ajax请求,需要忽略该次请求
    makeChange(0, 1, 2, 3, 4, "clickable");
    std.sum = 0;
    std.isInfoBarDisable = true;
    for (var i = 0; i < 5; ++i) {
        $(".inputMessage").removeClass("waiting").text("");
        std.isDisable[i] = false;
        std.hasGettingNum[i] = false;
        std.isGettingNum[i] = false;
        if (std.xhr[i] != null) {
            std.xhr[i].abort();
            std.xhr[i] = null;
        }
    }
    $("#info-bar").addClass("disabled").text("");
}