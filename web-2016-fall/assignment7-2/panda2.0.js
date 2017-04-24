"use strict";
var std = {
    selector: "block",
    mark: 15,
    running: false,
    steps: 0,
    second: 0,
    minute: 0,
    movement1: [14, 10, 6, 7, 6, 5, 9, 8, 12, 13, 14, 10, 11, 7, 3, 2, 1, 5, 6, 10, 11, 15],
    movement2: [14, 13, 12, 8, 0, 1, 2, 6, 5, 9, 8, 4, 5, 6, 7, 11, 10, 14, 13, 9, 5, 1, 2,
        6, 7, 3, 2, 6, 10, 14, 15],
    movement3: [11, 10, 6, 7, 3, 2, 6, 10, 9, 8, 4, 5, 1, 0, 4, 8, 12, 13, 14, 15],
    clear: function() {
        std.running = false;
        std.mark = 15;
        std.steps = 0;
        std.second = 0;
        std.minute = 0;
    }
};
$(document).ready(function() {
    buildPlayground();
    $("#start").click(getStart);
    $("#tryAgain").click(restart);
});
function getStart() {
    generator();
    timeAndSteps();
    $("#start").val("Try Again");
    if (!std.running) {
        std.running = true;
        play();
    }
}
function play() {
    var len = $(".blocks").length;
    for (var i = 0; i < len; ++i) {
        $(".blocks").eq(i).click(function() {
            if (std.running) {
                var a = parseInt(this.className.slice(16));
                if (move(a)) {
                    ++std.steps;
                    $("#stepsLast").attr("value", std.steps);
                }
            }
        });
    }
}
function move(i) {
    //  move blocks and judge if you will win at the same time~
    //  If the block you click can't move, return fales.
    //  Else return true
    var flag = false;
    var a_ = $("#block15").attr("class");
    if (i + 4 == std.mark || i - 4 == std.mark) {
        var b_ = $(".blocksPos" + i + ":eq(0)").attr("class");
        $(".blocksPos" + i + ":eq(0)").attr("class", a_);
        $("#block15").attr("class", b_);
        std.mark = i;
        flag = true;
    }
    if (i % 4 != 3) {
        if (i + 1 == std.mark) {
            b_ = $(".blocksPos" + i + ":eq(0)").attr("class");
            $(".blocksPos" + i + ":eq(0)").attr("class", a_);
            $("#block15").attr("class", b_);
            std.mark = i;
            flag = true;
        }
    }
    if (i % 4 != 0) {
        if (i - 1 == std.mark) {
            b_ = $(".blocksPos" + i + ":eq(0)").attr("class");
            $(".blocksPos" + i + ":eq(0)").attr("class", a_);
            $("#block15").attr("class", b_);
            std.mark = i;
            flag = true;
        }
    }
    if (judgeWin()) {
        std.steps++;
        var tValue = $("#clockLast").attr("value");
        var sValue = $("#stepsLast").attr("value");
        $("#stepsLast").attr("value", std.steps);
        alert(" You Win!\n In " + tValue +"\n Using " + sValue + " steps");
        stopCount();
        std.clear();
        $("#clockLast").attr("id", "clock");
        $("#stepsLast").attr("id", "steps");
        $("#start").attr("value", "Start");
        flag = false;
    }
    return flag;
}
function judgeWin() {
    for (var i = 0; i < $(".blocks").length - 1; ++i) {
        var name = $(".blocks:eq(" + i + ")").attr("class");
        var tmp = parseInt(name.slice(16));
        if (i !== tmp) return false;
    }
    return true;
}
function buildPlayground() {
    var game = document.getElementById("playground");
    var frag = document.createDocumentFragment();
    var count = 0;
    for (var i = 0; i < 4; ++i) {
        for (var j = 0; j < 4; ++j) {
            var tmp = document.createElement("button");
            var str = "block";
            str += count;
            tmp.setAttribute("type", "input");
            tmp.setAttribute("id", str);
            tmp.setAttribute("class", "blocks blocksPos" + count);
            frag.appendChild(tmp);
            count++;
        }
    }
    game.appendChild(frag);
}
function restart() {
    if (std.running) {
        std.clear();
        $("#start").val("Start");
        stopCount();
        $("#clockLast").attr("id", "clock");
        $("#stepsLast").attr("id", "steps");
        for (var i = 0; i < $(".blocks").length; ++i) {
            $(".blocks:eq(" + i + ")").attr("id", "block" + i).attr("class", "blocks blocksPos" + i);
        }
    }
}
//  generate the begining status before you enter the game
function generator() {
    var ran = Math.floor(Math.random() * 100) % 5 + 1;
    for (var j = 0; j < ran; ++j) {
        var tmp = Math.floor(Math.random() * 100) % 3;
        if (tmp == 0) {
            for (var i = 0; i < std.movement1.length; ++i) {
                move(std.movement1[i]);
            }
        }
        else if (tmp == 1) {
            for (i = 0; i < std.movement2.length; ++i) {
                move(std.movement2[i]);
            }
        }
        else {
            for (i = 0; i < std.movement3.length; ++i) {
                move(std.movement3[i]);
            }
        }
    }
}

function timeAndSteps() {
    if (!std.running) {
        $("#clock").attr("id", "clockLast");
        $("#steps").attr("value", std.steps).attr("id", "stepsLast");
        timedCount();
    } else {
        stopCount();
        std.clear();
        std.running = true;
        $("#clockLast").attr("value", "00:00");
        $("#stepsLast").attr("value", std.steps);
        timedCount();
    }
}

//  time counter
function timedCount() {
    var output = "";
    if (std.second > 59) {
        ++std.minute;
        std.second = 0;
    }
    if (std.minute < 10) {
        output = "0" + std.minute;
    } else {
        output = std.minute.toString();
    }
    output += ":";
    if (std.second < 10) {
        output += "0";
    }
    output += std.second;
    if (std.minute > 59) {
        alert("You have spent so many time on my game, I feel quite sorry. So, you win! Don't waste time anymore!");
        std.clear();
        stopCount();
        $("#clockLast").attr("id", "clock");
        $("#stepsLast").attr("id", "steps");
        $("#start").attr("value", "Start");
        return;
    }
    $("#clockLast").attr("value", output);
    std.second = std.second + 1;
    std.t = setTimeout("timedCount()", 1000);
}
function stopCount() {
    clearTimeout(std.t);
}