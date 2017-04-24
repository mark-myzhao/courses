var std = {
    hasGettingNum: [false, false, false, false, false],   //  判断对应五个输入是否取得随机数
    isDisable: [false, false, false, false, false],       //  判断对应五个输入是否被灭活
    isInfoBarDisable: true,                               //  判断放结果的圆圈是否被灭活
    ajaxRunning: false,                                   //  判断当前是否存在Ajax请求,若有为true;否则为false
    abort: false,                                         //  判断当前的Ajax请求是否需要被忽略,是为true,否为false
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
                clickInputButton(i);
            });
        }) (i);
    }
    $("#info-bar").click(getSum);
    $("#button").mouseleave(restart);
});

function clickInputButton(i) {
    var j = 0;
    if (!std.isDisable[i]) {     //  若已灭活,无反应
        std.ajaxRunning = true;  //  发出Ajax请求
        $(".inputMessage").eq(i).addClass("waiting").text("...");
        makeChange(0, 1, 2, 3, 4, "disable");
        $.get("ajax", function (data) {
            std.ajaxRunning = false;  //  完成Ajax请求
            //  如果移出@+区域, 需要忽略此次ajax请求
            if (!std.abort) {
                $(".inputMessage").eq(i).text(data);
                std.sum += parseInt(data);
                std.hasGettingNum[i] = true;
                for (j = 0; j < 5; ++j) {
                    if (!std.hasGettingNum[j]) {
                        makeChange(j, "clickable");
                    }
                }
                std.infoBarDisable();
                changeInfo();
            }
            std.abort = false;
        });
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
    }
    $("#info-bar").addClass("disabled").text("");
    if (std.ajaxRunning) {  //  若有发出的请求,将其忽略
        std.abort = true;
        std.ajaxRunning = false;
    }
}