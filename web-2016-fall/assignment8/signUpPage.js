//  Created by zhaomingyu on 15/12/4.
var canSubmit = [false, false, false, false];
//  together with function judge()
//  judge if the data is invalid before it is posted

$(document).ready(function() {
    var i = 0;
    //$(".mes:eq(0)").blur(judgeUsername);
    for (i = 0; i < 4; ++i) {
        $(".mes:eq(" + i + ")").blur(judge).keyup(judge);
    }
});
function judge() {
    var willSubmit = true;
    var i = 0;
    if (this.name == "username") {
        canSubmit[0] = judgeUsername(this.value);
    }
    if (this.name == "id") {
        canSubmit[1] = judgeId(this.value);
    }
    if (this.name == "phone") {
        canSubmit[2] = judgePhone(this.value);
    }
    if (this.name == "email") {
        canSubmit[3] = judgeEmail(this.value);
    }
    for (i = 0; i < 4; ++i) {
        if (canSubmit[i] == false) {
            willSubmit = false;
        }
    }
    if (willSubmit) {
        $("#submit").removeAttr("disabled");
    } else {
        $("#submit").attr("disabled", "disabled");
    }
}
function judgeUsername(name) {
    var regExp = /^[a-zA-Z][a-zA-Z0-9_]{5,17}$/;
    if (!regExp.test(name)) {
        $(".warning:eq(0)").val("invalid");
        return false;
    } else {
        $(".warning:eq(0)").val("");
        return true;
    }
}
function judgeId(id) {
    var regExp = /^[^0][0-9]{7}$/;
    if (!regExp.test(id)) {
        $(".warning:eq(1)").val("invalid");
        return false;
    } else {
        $(".warning:eq(1)").val("");
        return true;
    }
}
function judgePhone(phone) {
    var regExp = /^[^0][0-9]{10}$/;
    if (!regExp.test(phone)) {
        $(".warning:eq(2)").val("invalid");
        return false;
    } else {
        $(".warning:eq(2)").val("");
        return true;
    }
}
function judgeEmail(email) {
    var regExp = /^[a-zA-Z0-9_\-]+@(([a-zA-Z_0-9\-])+\.)+[a-zA-Z]{2,4}$/;
    if (!regExp.test(email)) {
        $(".warning:eq(3)").val("invalid");
        return false;
    } else {
        $(".warning:eq(3)").val("");
        return true;
    }
}